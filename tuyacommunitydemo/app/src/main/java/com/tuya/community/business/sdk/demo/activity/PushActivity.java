package com.tuya.community.business.sdk.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.push.bean.CommunityPushStatusBean;
import com.tuya.community.android.push.bean.CommunityPushType;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.PushPresenter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.IPushView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;


/**
 * 推送Demo
 * create by nielev on 2020/11/17
 */
public class PushActivity extends BaseActivity implements View.OnClickListener, IPushView, CompoundButton.OnCheckedChangeListener {
    private PushPresenter mPresenter;
    private TextView mTv_push_type;
    private Switch mSwitch_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        initToolbar();
        initView();
        initData();
    }

    private void initView() {
        setTitle("推送设置");
        setDisplayHomeAsUpEnabled();
        findViewById(R.id.btn_getpushtype).setOnClickListener(this);
        mSwitch_push = findViewById(R.id.switch_push);
        mSwitch_push.setOnCheckedChangeListener(this);
        mTv_push_type = findViewById(R.id.tv_push_type);
    }

    private void initData() {
        mPresenter = new PushPresenter(this, this);

//        TuyaCommunitySDK.getCommunityPushInstance().setPushStatus(true, new ITuyaCommunityResultCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//        });
//        TuyaCommunitySDK.getCommunityPushInstance().getPushStatusByType(CommunityPushType.PUSH_ALARM, new ITuyaCommunityResultCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//        });
//        TuyaCommunitySDK.getCommunityPushInstance().setPushStatusByType(CommunityPushType.PUSH_ALARM, true, new ITuyaCommunityResultCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(String s, String s1) {
//
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_getpushtype){
            mPresenter.getPushType();
        }
    }

    @Override
    public void showStatus(String deviceId, String isPushEnable) {
        mTv_push_type.setText("设备Id:"+deviceId+"\n"+"推送状态："+ (TextUtils.equals(isPushEnable,"1")?"开启":"关闭"));
        mSwitch_push.setChecked(TextUtils.equals(isPushEnable, "1"));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mPresenter.enablePush();
        } else {
            mPresenter.disablePush();
        }
    }
}
