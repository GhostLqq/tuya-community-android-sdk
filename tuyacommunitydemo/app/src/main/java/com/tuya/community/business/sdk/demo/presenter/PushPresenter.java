package com.tuya.community.business.sdk.demo.presenter;

import android.content.Context;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.push.bean.CommunityPushStatusBean;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.IPushView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

/**
 * create by nielev on 2020/11/20
 */
public class PushPresenter {
    private Context mContext;
    private IPushView mView;
    public PushPresenter(Context context, IPushView view){
        mContext = context;
        mView = view;
    }

    public void getPushType(){
        TuyaCommunitySDK.getCommunityPushInstance().getPushStatus(new ITuyaCommunityResultCallback<CommunityPushStatusBean>() {
            @Override
            public void onSuccess(CommunityPushStatusBean communityPushStatusBean) {
                mView.showStatus(communityPushStatusBean.getDeviceId(), communityPushStatusBean.getIsPushEnable());
            }

            @Override
            public void onError(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);
            }
        });
    }

    public void enablePush() {
        TuyaCommunitySDK.getCommunityPushInstance().setPushStatus(true, new ITuyaCommunityResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.shortToast(mContext, "设置开启推送成功");
                }
            }

            @Override
            public void onError(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);
            }
        });
    }

    public void disablePush() {
        TuyaCommunitySDK.getCommunityPushInstance().setPushStatus(false, new ITuyaCommunityResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.shortToast(mContext, "设置关闭推送成功");
                }
            }

            @Override
            public void onError(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);
            }
        });
    }
}
