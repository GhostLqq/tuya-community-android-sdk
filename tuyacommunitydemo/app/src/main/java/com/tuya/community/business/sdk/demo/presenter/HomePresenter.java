package com.tuya.community.business.sdk.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.Scene;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.home.api.ITuyaCommunityHome;
import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.android.user.bean.CommunityUser;
import com.tuya.community.business.sdk.demo.activity.CommonDeviceDebugActivity;
import com.tuya.community.business.sdk.demo.activity.PushActivity;
import com.tuya.community.business.sdk.demo.activity.SceneActivity;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.IMainView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.List;

import static com.tuya.community.business.sdk.demo.activity.SceneActivity.EXTRA_HOMEID;

/**
 * create by nielev on 2020/11/12
 */
public class HomePresenter {
    private Context mContext;
    private IMainView mView;
    private CommunityHomeBean mDetailHomeBean;
    private long mHomeId;

    public HomePresenter(Context context, IMainView activity) {
        mContext = context;
        mView = activity;
    }

    public void getHomeList() {
        TuyaCommunitySDK.getCommunityHomeInstance().getCommunityHomeList(new ITuyaCommunityResultCallback<List<CommunityHomeBean>>() {
            @Override
            public void onSuccess(List<CommunityHomeBean> result) {
                mView.showCommunityHomeBean(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    public void getHomeDetail(long homeId) {
        CommunityUser user = TuyaCommunitySDK.getCommunityUserInstance().getUser();
        TuyaCommunitySDK.newCommunityHomeInstance(homeId).getCommunityHomeDetail(new ITuyaCommunityResultCallback<CommunityHomeBean>() {
            @Override
            public void onSuccess(CommunityHomeBean result) {
                mHomeId = result.getHomeId();
                mView.showDetail(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    public void gotoDevOperate(String devId) {
        Intent intent = new Intent(mContext, CommonDeviceDebugActivity.class);
        intent.putExtra(CommonDeviceDebugPresenter.INTENT_DEVID, devId);
        ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void gotoPush() {
        Intent intent = new Intent(mContext, PushActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void gotoScene() {
        if(mHomeId != 0){
            Intent intent = new Intent(mContext, SceneActivity.class);
            intent.putExtra(EXTRA_HOMEID, mHomeId);
            ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, false);
        } else {
            ToastUtil.shortToast(mContext, "请先获取家庭id和详情");
        }
    }
}
