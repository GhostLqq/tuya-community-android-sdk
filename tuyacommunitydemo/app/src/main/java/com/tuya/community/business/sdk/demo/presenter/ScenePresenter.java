package com.tuya.community.business.sdk.demo.presenter;

import android.app.Activity;

import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.scene.bean.CommunitySceneBean;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.ISceneView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.community.business.sdk.demo.activity.SceneActivity.EXTRA_HOMEID;


/**
 * create by nielev on 2020/11/18
 */
public class ScenePresenter {
    private Activity mContext;
    private long mHomeId;
    private ISceneView mView;
    public ScenePresenter(Activity context, ISceneView view) {
        mContext = context;
        mView = view;
        mHomeId = context.getIntent().getLongExtra(EXTRA_HOMEID, 0);
    }

    public void getSimpleSceneList(){
        if(mHomeId != 0){
            TuyaCommunitySDK.getCommunitySceneManager().getSceneList(mHomeId, new ITuyaCommunityResultCallback<List<CommunitySceneBean>>() {
                @Override
                public void onSuccess(List<CommunitySceneBean> result) {
                    List<CommunitySceneBean> onClickExecute = new ArrayList<>();
                    List<CommunitySceneBean> automation = new ArrayList<>();
                    for (CommunitySceneBean sceneBean : result) {
                        //如果没有condition，就是一键执行，如果包含condition，就是自动化
                        if (null == sceneBean.getConditions() || sceneBean.getConditions().isEmpty()){
                            onClickExecute.add(sceneBean);
                        } else {
                            automation.add(sceneBean);
                        }
                    }
                    mView.showSceneList(onClickExecute, automation);
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    ToastUtil.shortToast(mContext, errorMessage);
                }
            });

        }

    }

    public void executeOneClick(String sceneId) {
        //如果需要展示具体的成功的动作，先要获取场景详情数据
        TuyaCommunitySDK.getCommunitySceneManager().getSceneDetail(mHomeId, sceneId, new ITuyaCommunityResultCallback<CommunitySceneBean>() {
            @Override
            public void onSuccess(CommunitySceneBean communitySceneBean) {
                ToastUtil.shortToast(mContext, "获取详情数据成功");
            }

            @Override
            public void onError(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);
            }
        });
        //发送命令给云端
        TuyaCommunitySDK.newCommunitySceneInstance(sceneId).executeScene(new ITuyaCommunityCallback() {
            @Override
            public void onError(String code, String error) {
                ToastUtil.shortToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ToastUtil.shortToast(mContext, "执行成功");
            }
        });
    }

    public void enableAuto(String sceneId, boolean isChecked) {
        if(isChecked){
            TuyaCommunitySDK.newCommunitySceneInstance(sceneId).enableScene(new ITuyaCommunityCallback() {
                @Override
                public void onError(String code, String error) {
                    ToastUtil.shortToast(mContext, error);
                }

                @Override
                public void onSuccess() {
                    ToastUtil.shortToast(mContext, "启动自动化成功");
                }
            });
        } else {
            TuyaCommunitySDK.newCommunitySceneInstance(sceneId).disableScene(new ITuyaCommunityCallback() {
                @Override
                public void onError(String code, String error) {
                    ToastUtil.shortToast(mContext, error);
                }

                @Override
                public void onSuccess() {
                    ToastUtil.shortToast(mContext, "停用自动化成功");
                }
            });
        }


    }
}
