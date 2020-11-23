package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;

import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.sdk.android.TuyaCommunitySDK;


/**
 * 推送Demo类
 * create by nielev on 2020/11/17
 */
public class PushActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        initToolbar();
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        TuyaCommunitySDK.getCommunityPushInstance().registerDevice("", "", new ITuyaCommunityCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
}
