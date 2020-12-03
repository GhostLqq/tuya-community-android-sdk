package com.tuya.community.business.sdk.demo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.community.android.user.api.ILoginListener;
import com.tuya.community.business.sdk.demo.activity.QrcodeLoginActivity;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.service.RouteEventListener;
import com.tuya.smart.api.service.ServiceEventListener;
import com.tuya.smart.wrapper.api.TuyaWrapper;
//import com.tuya.smart.api.router.UrlBuilder;
//import com.tuya.smart.api.service.RouteEventListener;
//import com.tuya.smart.api.service.ServiceEventListener;
//import com.tuya.smart.wrapper.api.TuyaWrapper;

/**
 * create by nielev on 2020/11/11
 */
public class TuyaApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 请不要修改初始化顺序
        Fresco.initialize(this);

        // SDK 初始化
        TuyaCommunitySDK.setDebugMode(true);
        TuyaCommunitySDK.init(this);
        TuyaCommunitySDK.getCommunityUserInstance().setOnNeedLoginListener(new ILoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, QrcodeLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // web容器初始化
        TuyaWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                // 路由未实现回调
                Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                // 服务未实现回调
                Log.e("service not implement", serviceName);
            }
        });
    }

}
