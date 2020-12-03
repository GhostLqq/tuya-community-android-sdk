package com.tuya.community.business.sdk.demo.view;

import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.android.property.bean.CommAnnounceResponseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * create by nielev on 2020/11/12
 */
public interface IMainView {
    void showCommunityHomeBean(List<CommunityHomeBean> result);

    void showDetail(CommunityHomeBean result);

    void showPropertyList(ArrayList<CommAnnounceResponseBean> commAnnounceResponseBeans);

    void showFailed(String error);

}
