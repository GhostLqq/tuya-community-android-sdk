package com.tuya.community.business.sdk.demo.view;

import com.tuya.community.android.scene.bean.CommunitySceneBean;

import java.util.List;

/**
 * create by nielev on 2020/11/18
 */
public interface ISceneView {

    void showSceneList(List<CommunitySceneBean> onClickExecute, List<CommunitySceneBean> automation);
}
