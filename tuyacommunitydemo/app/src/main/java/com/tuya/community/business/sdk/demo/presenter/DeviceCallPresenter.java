package com.tuya.community.business.sdk.demo.presenter;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.home.api.ITuyaCommunityHome;
import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.business.sdk.demo.bean.Mqtt308ParamsBean;
import com.tuya.community.business.sdk.demo.utils.MessageUtil;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.utils.Utils;
import com.tuya.community.business.sdk.demo.view.IDeviceCallView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.network.Business;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OnP2PCameraListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.ipccamerasdk.bean.CameraInfoBean;
import com.tuya.smart.camera.ipccamerasdk.business.CameraBusiness;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuya.smart.camera.ipccamerasdk.utils.CameraConstant;
import com.tuya.smart.camera.ipccamerasdk.utils.MqttServiceUtils;
import com.tuya.smart.camera.middleware.p2p.CameraStrategy;
import com.tuya.smart.camera.utils.StateServiceUtil;
import com.tuya.smart.camera.utils.permission.PermissionChecker;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.nio.ByteBuffer;

import static com.tuya.community.business.sdk.demo.presenter.HomePresenter.PROJECT_ID;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_CAMERA_DEV_ID;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_FROM_MQTT;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_SN;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_TARGET_ADDRESS;

/**
 * create by nielev on 2020/12/1
 */
public class DeviceCallPresenter implements OnP2PCameraListener {

    private boolean isFromMqtt;
    private Activity mAc;
    private IDeviceCallView mView;
    private String mDevId;
    private String mProjectId;
    private String mTargetSpaceId;
    private String mSn;
    private int mSdkProvider;
    private ICameraP2P mTuyaSmartCamera;
    private int mCallMode;
    private CameraBusiness mCameraBusiness;

    /**
     * has the doorbell been answered？
     */
    private boolean isAccept;
    private DeviceBean mDeviceBean;
    private String clientTraceId;
    private boolean isPlaying;


    public boolean isAccept() {
        return isAccept;
    }

    public DeviceCallPresenter(Activity context, IDeviceCallView view){
        mAc = context;
        mView = view;
        mDevId = mAc.getIntent().getStringExtra(EXTRA_CAMERA_DEV_ID);
        mDeviceBean = TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(mDevId);
        mProjectId = mAc.getIntent().getStringExtra(PROJECT_ID);
        mTargetSpaceId = mAc.getIntent().getStringExtra(EXTRA_TARGET_ADDRESS);
        mSn = mAc.getIntent().getStringExtra(EXTRA_SN);
        isFromMqtt = mAc.getIntent().getBooleanExtra(EXTRA_FROM_MQTT, false);
        //判断从mqtt消息通知过来的项目id和房屋id是否为空，为空则取当前房屋和项目
        if(isFromMqtt && (TextUtils.isEmpty(mProjectId) || TextUtils.isEmpty(mTargetSpaceId))){
            if(Utils.getHomeId() != 0){
                ITuyaCommunityHome iTuyaCommunityHome = TuyaCommunitySDK.newCommunityHomeInstance(Utils.getHomeId());
                CommunityHomeBean communityHomeBean = iTuyaCommunityHome.getCommunityHomeBean();
                mProjectId = communityHomeBean.getProjectId();
                mTargetSpaceId = communityHomeBean.getSpaceTreeId();
            }
        }
        initCameraSdk();
    }

    private void initCameraSdk() {
        DeviceBean deviceBean = TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(mDevId);
        if(deviceBean == null) {
            ToastUtil.shortToast(mAc, "未找到设备");
            return;
        }
        mSdkProvider = CameraConstant.SDK_PROVIDER_V2;
        try {
            mTuyaSmartCamera = (ICameraP2P) CameraStrategy.getCamera(mSdkProvider).getConstructor().newInstance();
            mCallMode = mTuyaSmartCamera.getMute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCameraBusiness = new CameraBusiness();
    }

    public void openDoor() {
        TuyaCommunitySDK.getCommunityVisualSpeakManager().openDoor(mDevId, mProjectId, mTargetSpaceId, new ITuyaCommunityCallback() {
            @Override
            public void onError(String s, String s1) {
                ToastUtil.shortToast(mAc, s1);
            }

            @Override
            public void onSuccess() {
                ToastUtil.shortToast(mAc, "开门成功");
            }
        });
    }

    /**
     * 接听
     */
    public void accept() {
        if (PermissionChecker.hasRecordPermission()) {
            startTalk();
        } else {
//            PermissionChecker.requestPermission(mAc, Manifest.permission.RECORD_AUDIO, PermissionChecker.EXTERNAL_AUDIO_REQ_CODE, "Please enable the recording permission in System Application Settings");
        }
    }

    /**
     * 开始对讲
     */
    private void startTalk() {
        if (null != mTuyaSmartCamera) {
            mTuyaSmartCamera.startAudioTalk(new OperationDelegateCallBack() {

                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
//                    isTalking = true;
                    handleSpeak(true);
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
//                    isTalking = false;
                    handleSpeak(false);
                }
            });
        }
    }

    private void handleSpeak(boolean b) {
        if (b) {
            if (!isAccept) {
                setMqtt308("accept");
                sendHeartBeat();
            }
            startMute();
            isAccept = true;
        } else {
            ToastUtil.showToast(mAc, "Connection failed, please try again.");
        }
    }

    private void setMqtt308(String event) {

        Mqtt308ParamsBean paramsBean = new Mqtt308ParamsBean();
        paramsBean.setType("ac_doorbell");
        Mqtt308ParamsBean.EventBean eventBean = new Mqtt308ParamsBean.EventBean();
        eventBean.setDevId(mDevId);
        eventBean.setEvent(event);
        paramsBean.setData(eventBean);
        String signal = JSON.toJSONString(paramsBean);
        MqttServiceUtils.sendMQTT308Message(mDevId, signal);
    }

    private Handler beatHandler = new Handler();
    private void sendHeartBeat() {
        beatHandler.post(new Runnable() {
            @Override
            public void run() {
                setMqtt308("heartbeat");
                beatHandler.postDelayed(this, 10000);
            }
        });
    }

    public void reject() {
    }


    public int getSdkProvider() {
        return mSdkProvider;
    }

    public void startMute() {
        if (null != mTuyaSmartCamera && mTuyaSmartCamera.getMute(null) != ICameraP2P.UNMUTE) {
            setCallMute(ICameraP2P.UNMUTE);
        }
    }
    public void stopMute() {
        if (null != mTuyaSmartCamera && mCallMode != mTuyaSmartCamera.getMute(null)) {
            if (mTuyaSmartCamera.getMute(null) != ICameraP2P.MUTE) {
                setCallMute(ICameraP2P.MUTE);
            }
        }
    }

    private void setCallMute(int muteMode) {
        if (null != mTuyaSmartCamera) {
            mTuyaSmartCamera.setMute(null, muteMode, new OperationDelegateCallBack() {

                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    int muteValue = Integer.parseInt(data);
                    ToastUtil.shortToast(mAc, "视频声音设置成功");
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    ToastUtil.shortToast(mAc, "视频声音设置失败");
                }
            });
        }
    }

    public void onResume() {
        if (null != mTuyaSmartCamera) {
            mTuyaSmartCamera.registorOnP2PCameraListener(this);
        }
    }

    @Override
    public void receiveFrameDataForMediaCodec(int i, byte[] bytes, int i1, int i2, byte[] bytes1, boolean b, int i3) {

    }

    @Override
    public void onReceiveFrameYUVData(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer1, ByteBuffer byteBuffer2, int i1, int i2, int i3, int i4, long l, long l1, long l2, Object o) {

    }

    @Override
    public void onSessionStatusChanged(Object o, int i, int i1) {
//        mHandler.sendMessage(MessageUtil.getMessage(MSG_CONNECT, ARG1_OPERATE_FAIL, "connect failure"));
    }

    @Override
    public void onReceiveAudioBufferData(int i, int i1, int i2, long l, long l1, long l2) {

    }

    @Override
    public void onReceiveSpeakerEchoData(ByteBuffer byteBuffer, int i) {

    }

    public boolean inOnline() {
        return null != mDeviceBean ? mDeviceBean.getIsOnline() : false;
    }

    public boolean isConnect() {
        if (null != mTuyaSmartCamera) {
            return mTuyaSmartCamera.isConnecting();
        }
        return false;
    }

    public void startPlay() {
        if (null != mTuyaSmartCamera) {
            mTuyaSmartCamera.startPreview(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    isPlaying = true;
                    mView.showCameraPlay(isAccept);
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    ToastUtil.shortToast(mAc, "获取video失败");
                }
            });
        }
    }

    public void stopPlay() {
        if (null != mTuyaSmartCamera) {
            mTuyaSmartCamera.stopPreview(new OperationDelegateCallBack() {

                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    isPlaying = false;
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {

                }
            });
        }
    }

    public void requestCameraInfo() {
        if (null != mCameraBusiness) {
            clientTraceId = StateServiceUtil.getClientTraceId(mDeviceBean.getDevId());
            StateServiceUtil.sendFullLinkStartLog(mDeviceBean.getDevId(), clientTraceId);
            mCameraBusiness.requestCameraInfo(mDeviceBean.getDevId(), clientTraceId, new Business.ResultListener<CameraInfoBean>() {

                @Override
                public void onFailure(BusinessResponse businessResponse, CameraInfoBean cameraInfoBean, String s) {
                    ToastUtil.shortToast(mAc, "请求摄像机信息失败");
                }

                @Override
                public void onSuccess(BusinessResponse businessResponse, CameraInfoBean cameraInfoBean, String s) {
                    if (cameraInfoBean != null && cameraInfoBean.getP2pConfig() != null) {
//                        if (null != cameraInfoBean.getP2pId()) {
//                            mP2pId = cameraInfoBean.getP2pId().split(",")[0];
//                        }
//                        mP2p3Id = cameraInfoBean.getId();
//                        p2pType = cameraInfoBean.getP2pSpecifiedType();
//                        if (null != TuyaHomeSdk.getUserInstance().getUser()) {
//                            mlocalId = TuyaHomeSdk.getUserInstance().getUser().getUid();
//                        }
//                        mInitString = cameraInfoBean.getP2pConfig().getInitStr();
//                        mP2pKey = cameraInfoBean.getP2pConfig().getP2pKey();
//                        if (null != cameraInfoBean.getP2pConfig().getIces()) {
//                            token = cameraInfoBean.getP2pConfig().getIces().toString();
//                        }
//                        mInitString += ":" + mP2pKey;
//                        if (null != mDeviceBean) {
//                            mLocalKey = mDeviceBean.getLocalKey();
//                        }
//                        mPwd = cameraInfoBean.getPassword();
//                        mHandler.sendMessage(MessageUtil.getMessage(MSG_GET_CAMERAINFO, ARG1_OPERATE_SUCCESS));
//                    } else {
//                        mHandler.sendMessage(MessageUtil.getMessage(MSG_GET_CAMERAINFO, ARG1_OPERATE_FAIL));
                    }
                }
            });
        }
    }
}
