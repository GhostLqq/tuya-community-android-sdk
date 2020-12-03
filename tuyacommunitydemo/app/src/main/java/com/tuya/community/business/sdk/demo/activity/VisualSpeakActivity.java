package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.community.android.visualspeak.bean.VisualSpeakDeviceBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter;
import com.tuya.community.business.sdk.demo.view.IVisualSpeakView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;

/**
 * 可视对接功能页面
 * create by nielev on 2020/11/28
 */
public class VisualSpeakActivity extends BaseActivity implements View.OnClickListener, IVisualSpeakView {

    private View mBtn_visual_speak_devices;
    private VisualSpeakPresenter mPresenter;
    private TextView mDevices;
    private Button mBtn_register_listener;
    private EditText mEt_devId;
    private View mBtn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_speak);
        initToolbar();
        initView();
        initData();
    }

    private void initView() {
        mBtn_visual_speak_devices = findViewById(R.id.btn_visual_speak_devices);
        mBtn_visual_speak_devices.setOnClickListener(this);
        mDevices = findViewById(R.id.devices);
        mBtn_register_listener = findViewById(R.id.btn_register_listener);

        mBtn_register_listener.setOnClickListener(this);
        mEt_devId = findViewById(R.id.et_devId);
        mBtn_enter = findViewById(R.id.btn_enter);
        mBtn_enter.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new VisualSpeakPresenter(this, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_visual_speak_devices) {
            mPresenter.getVisualDevices();
        } else if(v.getId() == R.id.btn_register_listener) {
            mPresenter.registerListener();
        } else if(v.getId() == R.id.btn_enter) {
            String devId = mEt_devId.getText().toString();
            mPresenter.enterDev(devId);
        }
    }

    @Override
    public void showVisualSpeakDevices(ArrayList<VisualSpeakDeviceBean> visualSpeakDeviceBeans) {
        StringBuilder text = new StringBuilder();
        for (VisualSpeakDeviceBean visualSpeakDeviceBean : visualSpeakDeviceBeans){
            String deviceId = visualSpeakDeviceBean.getDeviceId();
            DeviceBean deviceBean = TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(deviceId);

            if(null != deviceBean) {
                text.append("设备名称:").append(deviceBean.getName()).append("=设备Id:").append(deviceId).append("\n");
            }
        }
        mDevices.setText(text.toString());
    }
}
