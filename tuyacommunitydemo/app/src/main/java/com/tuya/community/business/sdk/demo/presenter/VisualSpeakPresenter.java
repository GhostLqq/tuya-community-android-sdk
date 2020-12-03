package com.tuya.community.business.sdk.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.visualspeak.bean.DeviceCallDataBean;
import com.tuya.community.android.visualspeak.bean.DeviceMessageType;
import com.tuya.community.android.visualspeak.bean.VisualSpeakDeviceBean;
import com.tuya.community.android.visualspeak.listener.OnVisualSpeakCallListener;
//import com.tuya.community.business.sdk.demo.activity.DeviceCallActivity;
import com.tuya.community.business.sdk.demo.activity.CameraPanelActivity;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.IVisualSpeakView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Map;

import static com.tuya.community.business.sdk.demo.presenter.CommonDeviceDebugPresenter.INTENT_DEVID;
import static com.tuya.community.business.sdk.demo.presenter.HomePresenter.PROJECT_ID;
import static com.tuya.community.business.sdk.demo.presenter.HomePresenter.SPACETREE_ID;
import static com.tuya.community.business.sdk.demo.utils.Constants.INTENT_P2P_TYPE;

/**
 * create by nielev on 2020/11/28
 */
public class VisualSpeakPresenter {

    private Activity mActivity;
    private IVisualSpeakView mView;
    private final String mProjectId;
    private final String mSpaceId;
    public static final String EXTRA_CAMERA_DEV_ID = "extra_camera_uuid";
    public static final String EXTRA_SN = "extra_sn";
    public static final String EXTRA_TARGET_ADDRESS = "extra_target_address";
    public static final String EXTRA_FROM_MQTT = "extra_from_mqtt";
    private OnVisualSpeakCallListener mListener = new OnVisualSpeakCallListener() {
        @Override
        public void onDeviceCall(DeviceCallDataBean deviceCallDataBean) {
            ToastUtil.shortToast(mActivity, "设备呼叫"+deviceCallDataBean.getDevId());
            deviceCall(deviceCallDataBean);
        }

        @Override
        public void onAppOrDeviceAnswer(DeviceCallDataBean deviceCallDataBean) {
            ToastUtil.shortToast(mActivity, "设备已被接听");
        }

        @Override
        public void onHandup(DeviceMessageType deviceMessageType, DeviceCallDataBean deviceCallDataBean) {
            ToastUtil.shortToast(mActivity, "设备已被挂断");
        }
    };

    /**
     * 设备呼叫处理
     * @param deviceCallDataBean
     */
    private void deviceCall(DeviceCallDataBean deviceCallDataBean) {
        //唤醒屏幕
        wakeUpScreen();
        //跳入呼叫页面
        Intent intent = new Intent(mActivity, CameraPanelActivity.class);
        intent.putExtra(INTENT_DEVID, deviceCallDataBean.getDevId());
        intent.putExtra(EXTRA_SN, deviceCallDataBean.getSn());
        intent.putExtra(PROJECT_ID, mProjectId);
        intent.putExtra(EXTRA_TARGET_ADDRESS, deviceCallDataBean.getTargetAddress());
        intent.putExtra(EXTRA_FROM_MQTT, deviceCallDataBean.isFromMqtt());
        ActivityUtils.startActivity(mActivity,  intent, -1, false);
    }

    private void wakeUpScreen() {
        PowerManager pm;
        PowerManager.WakeLock wl = null;
        //获取电源管理器对象
        pm=(PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()){
            return;
        }
        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        if (pm != null) {
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        }
        //点亮屏幕
        wl.acquire(6*1000L);
    }

    public VisualSpeakPresenter(Activity activity, IVisualSpeakView view) {
        mActivity = activity;
        mView = view;
        mProjectId = mActivity.getIntent().getStringExtra(PROJECT_ID);
        mSpaceId = mActivity.getIntent().getStringExtra(SPACETREE_ID);

    }

    public void getVisualDevices() {
        TuyaCommunitySDK.getCommunityVisualSpeakManager().getVisualSpeakDeviceList(mProjectId, mSpaceId, new ITuyaCommunityResultCallback<ArrayList<VisualSpeakDeviceBean>>() {
            @Override
            public void onSuccess(ArrayList<VisualSpeakDeviceBean> visualSpeakDeviceBeans) {
                mView.showVisualSpeakDevices(visualSpeakDeviceBeans);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                ToastUtil.shortToast(mActivity, errorMsg);
            }
        });

//        TuyaCommunitySDK.getCommunityVisualSpeakManager().getVisualSpeakDeviceStatus(mDevId, mProjectId, mSpaceId, sn, new ITuyaCommunityResultCallback<DeviceMessageType>() {
//            @Override
//            public void onSuccess(DeviceMessageType deviceMessageType) {
//                if (null == deviceMessageType) {
//                    ToastUtil.shortToast(mActivity, "error");
//                } else if (DEVICE_ANSWER == deviceMessageType) {
//                    ToastUtil.shortToast(mActivity, "设备已被接听");
//                } else {
//                    ToastUtil.shortToast(mActivity, "设备已挂断");
//                }
//            }
//
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//        });
//
//        TuyaCommunitySDK.getCommunityVisualSpeakManager().openDoor(mDevId, mProjectId, mSpaceId, new ITuyaCommunityCallback() {
//
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//
//            @Override
//            public void onSuccess() {
//
//            }
//        });
//        TuyaCommunitySDK.getCommunityVisualSpeakManager().visualSpeakReject(mDevId, mProjectId, mSpaceId, sn, new ITuyaCommunityCallback() {
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//
//            @Override
//            public void onSuccess() {
//
//            }
//        });
    }

    public void registerListener() {
        TuyaCommunitySDK.getCommunityVisualSpeakManager().registerVisualSpeakCallListener(mListener);
        ToastUtil.shortToast(mActivity, "监听已经注册");
    }

    public void enterDev(String devId) {
        Intent intent = new Intent(mActivity, CameraPanelActivity.class);
        intent.putExtra(INTENT_DEVID, devId);
        DeviceBean devBean = TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(devId);
        intent.putExtra(CommonDeviceDebugPresenter.INTENT_DEVID, devBean.getDevId());
        Map<String, Object> map = devBean.getSkills();
        int p2pType = -1;
        if (map == null || map.size() == 0) {
            p2pType = -1;
        } else {
            p2pType = (Integer) (map.get("p2pType"));
        }
        intent.putExtra(INTENT_P2P_TYPE, p2pType);
        ActivityUtils.startActivity(mActivity, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
