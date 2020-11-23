package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.HomePresenter;
import com.tuya.community.business.sdk.demo.utils.DialogUtil;
import com.tuya.community.business.sdk.demo.view.IMainView;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, IMainView {

    private View mBtn_home_list;
    private View mBtn_home_detail;
    private HomePresenter mPresenter;
    private TextView mTv_home_list;
    private EditText mEt_homeId;
    private TextView mTv_home_detail;
    private EditText mEt_devId;
    private Button mBtn_dev_oprate;
    private Button mBtn_push;
    private Button mBtn_scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        mBtn_home_list = findViewById(R.id.btn_home_list);
        mTv_home_list = findViewById(R.id.tv_home_list);
        mEt_homeId = findViewById(R.id.et_homeId);
        mBtn_home_list.setOnClickListener(this);
        mBtn_home_detail = findViewById(R.id.btn_home_detail);
        mBtn_home_detail.setOnClickListener(this);
        mTv_home_detail = findViewById(R.id.tv_detail);
        mEt_devId = findViewById(R.id.et_devId);
        mBtn_dev_oprate = findViewById(R.id.btn_dev_oprate);
        mBtn_dev_oprate.setOnClickListener(this);
        mBtn_push = findViewById(R.id.btn_push);
        mBtn_push.setOnClickListener(this);
        mBtn_scene = findViewById(R.id.btn_scene);
        mBtn_scene.setOnClickListener(this);

    }

    private void initData() {
        mPresenter = new HomePresenter(this, this);
        //获取一个默认家庭
        mPresenter.getHomeList();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_home_list) {
            mPresenter.getHomeList();
        } else if (v.getId() == R.id.btn_home_detail) {
            if(!TextUtils.isEmpty(mEt_homeId.getText().toString())){
                long homeId = Long.parseLong(mEt_homeId.getText().toString());
                mPresenter.getHomeDetail(homeId);
            }
        } else if (v.getId() == R.id.btn_dev_oprate) {
            if(!TextUtils.isEmpty(mEt_devId.getText().toString())){
                String devId = mEt_devId.getText().toString();
                mPresenter.gotoDevOperate(devId);
            }
        } else if (v.getId() == R.id.btn_push) {
            mPresenter.gotoPush();
        } else if(v.getId() == R.id.btn_scene) {
            mPresenter.gotoScene();
        }
    }

    @Override
    public void showCommunityHomeBean(List<CommunityHomeBean> result) {
        StringBuilder builder = new StringBuilder();
        for (CommunityHomeBean communityHomeBean : result){
            builder.append("房屋地址：").append(communityHomeBean.getHouseAddress()).append("---").append("房屋数据标示：").append(communityHomeBean.getHomeId()).append("\n");
        }
        mTv_home_list.setText(builder.toString());


    }

    @Override
    public void showDetail(CommunityHomeBean result) {
        StringBuilder builder = new StringBuilder();
        builder.append("所属房屋:").append(result.getCommunityName()+"").append("\n").append("拥有设备：").append("\n");
        if(null != result.getDeviceList() && !result.getDeviceList().isEmpty()){
            for (DeviceBean deviceBean:
             result.getDeviceList()) {
                builder.append("设备名称：").append(deviceBean.getName()).append("-设备id：").append(deviceBean.getDevId()).append("\n");
            }
        } else {
            builder.append("0个");
        }

        if(null != mTv_home_detail) mTv_home_detail.setText(builder.toString());
    }
}