package com.tuya.community.business.sdk.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.android.property.bean.CommAnnounceResponseBean;
import com.tuya.community.business.sdk.demo.activity.CommonDeviceDebugActivity;
import com.tuya.community.business.sdk.demo.activity.PushActivity;
import com.tuya.community.business.sdk.demo.activity.SceneActivity;
import com.tuya.community.business.sdk.demo.activity.VisualSpeakActivity;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.utils.Utils;
import com.tuya.community.business.sdk.demo.view.IMainView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.jsbridge.base.webview.WebViewActivity;

import java.util.ArrayList;
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
    public static final String PROJECT_ID = "project_id";
    public static final String SPACETREE_ID = "spaceTree_Id";
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
        TuyaCommunitySDK.newCommunityHomeInstance(homeId).getCommunityHomeDetail(new ITuyaCommunityResultCallback<CommunityHomeBean>() {
            @Override
            public void onSuccess(CommunityHomeBean result) {
                mHomeId = result.getHomeId();
                mView.showDetail(result);
                Utils.setCurrentHomeId(result.getHomeId());
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
    public void getPropertyList(String projectId,String rooid){
        TuyaCommunitySDK.getCommunityPropertyInstance().getRecentAnnounceList(projectId, rooid, new ITuyaCommunityResultCallback<ArrayList<CommAnnounceResponseBean>>() {
            @Override
            public void onSuccess(ArrayList<CommAnnounceResponseBean> commAnnounceResponseBeans) {
               mView.showPropertyList(commAnnounceResponseBeans);


            }

            @Override
            public void onError(String s, String s1) {
                mView.showFailed(s1);
            }
        });
    }
    public String getPropertyDetailUrl(String announcementId, String communityId){
       return TuyaCommunitySDK.getCommunityPropertyInstance().getAnnouceDetailWebUrl(announcementId,communityId);
    }
    public void openUrl(String url){
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("Uri", url);
        mContext.startActivity(intent);
    }

    public String getSmartCallUrl(String projectId,String spaceTreeId){
       return TuyaCommunitySDK.getCommunitySmartCallInstance().getSmartCallElevatorUrl(projectId, spaceTreeId);
    }

    public void gotoVisualSpeak(String projectId, String spaceTreeId) {
        Intent intent = new Intent(mContext, VisualSpeakActivity.class);
        intent.putExtra(PROJECT_ID, projectId);
        intent.putExtra(SPACETREE_ID, spaceTreeId);
        ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
