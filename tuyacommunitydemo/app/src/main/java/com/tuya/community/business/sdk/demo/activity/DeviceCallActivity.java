package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;
import android.view.View;

import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.DeviceCallPresenter;
import com.tuya.community.business.sdk.demo.view.IDeviceCallView;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;

/**
 * 设备呼叫页面
 * create by nielev on 2020/11/30
 */
public class DeviceCallActivity extends BaseActivity implements View.OnClickListener, IDeviceCallView, TuyaCameraView.CreateVideoViewCallback {
    private DeviceCallPresenter mPresenter;
    private boolean isInitMedia = false;
    private TuyaCameraView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_call);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.btn_open_door).setOnClickListener(this);
        findViewById(R.id.btn_accept).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);
        mVideoView = findViewById(R.id.camera_video_view);
    }

    private void initData() {
        mPresenter = new DeviceCallPresenter(this, this);
        mVideoView.setCameraViewCallback(this);
        mVideoView.createVideoView(mPresenter.getSdkProvider());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        doRetry();
    }

    public void doRetry() {
        if (mPresenter.inOnline()) {
            if (mPresenter.isConnect()) {
                mPresenter.startPlay();
            } else {
                mPresenter.requestCameraInfo();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isInitMedia) {
            isInitMedia = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open_door) {
            mPresenter.openDoor();
        } else if (v.getId() == R.id.btn_accept) {
            mPresenter.accept();
        } else if (v.getId() == R.id.btn_reject) {
            mPresenter.reject();
        }
    }

    @Override
    public void onCreated(Object o) {

    }

    @Override
    public void videoViewClick() {

    }

    @Override
    public void startCameraMove(int i) {

    }

    @Override
    public void onActionUP() {

    }

    @Override
    public void showCameraPlay(boolean isAccept) {
        if(isAccept) {

        } else {

        }
    }
}
