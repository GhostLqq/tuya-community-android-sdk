package com.tuya.community.business.sdk.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tuya.community.android.user.api.ILoginListener;
import com.tuya.community.business.sdk.demo.activity.QrcodeLoginActivity;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

/**
 * create by nielev on 2020/11/11
 */
public class TuyaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaCommunitySDK.init(this);
        TuyaCommunitySDK.getCommunityUserInstance().setOnNeedLoginListener(new ILoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                context.startActivity(new Intent(context, QrcodeLoginActivity.class));
            }
        });
        TuyaCommunitySDK.setDebugMode(true);
    }

}
