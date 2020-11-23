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
}
