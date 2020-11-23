package com.tuya.community.business.sdk.demo.view;

import com.tuya.community.android.home.bean.CommunityHomeBean;

import java.util.List;

/**
 * create by nielev on 2020/11/12
 */
public interface IMainView {
    void showCommunityHomeBean(List<CommunityHomeBean> result);

    void showDetail(CommunityHomeBean result);
}
