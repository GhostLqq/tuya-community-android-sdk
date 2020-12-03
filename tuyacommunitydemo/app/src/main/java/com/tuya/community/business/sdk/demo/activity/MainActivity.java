package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.android.property.bean.CommAnnounceResponseBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.HomePresenter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.IMainView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, IMainView {

    private View mBtn_home_list;
    private View mBtn_home_detail;
    private HomePresenter mPresenter;
    private TextView mTv_home_list;
    private EditText mEt_homeId;
    private TextView mTv_home_detail;
    private EditText mEt_devId;
    private EditText mEt_projectId;
    private EditText mEt_spaceTreeId;
    private Button mBtn_dev_oprate;
    private Button mBtn_push;
    private Button mBtn_scene;
    private Button mBtn_property_list;
    private Button mBtn_property_url;
    private Button mBtn_smartcall;
    private Button mBtn_visual_speak;
    private TextView mTv_Property_list;
    private EditText mEt_AnnouncementId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initData();
    }

    @Override
    public boolean needLogin() {
        return true;
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
        mBtn_property_list = findViewById(R.id.btn_property_list);
        mBtn_property_list.setOnClickListener(this);
        mBtn_property_url = findViewById(R.id.btn_property_url);
        mBtn_property_url.setOnClickListener(this);
        mBtn_smartcall = findViewById(R.id.btn_smartcall);
        mBtn_smartcall.setOnClickListener(this);
        mBtn_visual_speak = findViewById(R.id.btn_visualspeak);
        mEt_projectId = findViewById(R.id.et_projectId);
        mEt_spaceTreeId = findViewById(R.id.et_spaceTreeId);
        mTv_Property_list = findViewById(R.id.tv_property_list);
        mEt_AnnouncementId = findViewById(R.id.et_announcementId);
        mBtn_visual_speak.setOnClickListener(this);
        mEt_projectId = findViewById(R.id.et_projectId);
        mEt_spaceTreeId = findViewById(R.id.et_spaceTreeId);

    }

    private void initData() {
        mPresenter = new HomePresenter(this, this);
        //获取一个默认家庭
        mPresenter.getHomeList();
        setDisplayHomeAsUpEnabled(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuyaCommunitySDK.getCommunityUserInstance().logout(new ITuyaCommunityCallback() {
                    @Override
                    public void onError(String s, String s1) {

                    }

                    @Override
                    public void onSuccess() {
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_home_list) {
            mPresenter.getHomeList();
        } else if (v.getId() == R.id.btn_home_detail) {
            if (!TextUtils.isEmpty(mEt_homeId.getText().toString())) {
                long homeId = Long.parseLong(mEt_homeId.getText().toString());
                mPresenter.getHomeDetail(homeId);
            }
        } else if (v.getId() == R.id.btn_dev_oprate) {
            if (!TextUtils.isEmpty(mEt_devId.getText().toString())) {
                String devId = mEt_devId.getText().toString();
                mPresenter.gotoDevOperate(devId);
            }
        } else if (v.getId() == R.id.btn_push) {
            mPresenter.gotoPush();
        } else if (v.getId() == R.id.btn_scene) {
            mPresenter.gotoScene();
        } else if (v.getId() == R.id.btn_property_list) {
            if (!TextUtils.isEmpty(mEt_spaceTreeId.getText()) && !TextUtils.isEmpty(mEt_projectId.getText()))
                mPresenter.getPropertyList(mEt_projectId.getText().toString(), mEt_spaceTreeId.getText().toString());
        } else if (v.getId() == R.id.btn_property_url) {
            if (!TextUtils.isEmpty(mEt_AnnouncementId.getText().toString()) && !TextUtils.isEmpty(mEt_projectId.getText().toString())) {
                String url = mPresenter.getPropertyDetailUrl(mEt_AnnouncementId.getText().toString(), mEt_projectId.getText().toString());
                mPresenter.openUrl(url);
            }
        } else if (v.getId() == R.id.btn_visualspeak) {
            String projectId = mEt_projectId.getText().toString();
            String spaceTreeId = mEt_spaceTreeId.getText().toString();
            if (!TextUtils.isEmpty(projectId) && !TextUtils.isEmpty(spaceTreeId)) {
                mPresenter.gotoVisualSpeak(projectId, spaceTreeId);
            }
        } else if (v.getId() == R.id.btn_smartcall) {
            String projectId = mEt_projectId.getText().toString();
            String spaceTreeId = mEt_spaceTreeId.getText().toString();
            if (!TextUtils.isEmpty(projectId) && !TextUtils.isEmpty(spaceTreeId)) {
                String smartCallUrl = mPresenter.getSmartCallUrl(projectId, spaceTreeId);
                mPresenter.openUrl(smartCallUrl);
            }

        }

    }

    @Override
    public void showCommunityHomeBean(List<CommunityHomeBean> result) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (CommunityHomeBean communityHomeBean : result) {
            if (i == 0) mEt_homeId.setText(communityHomeBean.getHomeId() + "");
            builder.append("房屋地址：").append(communityHomeBean.getHouseAddress()).append("---").append("房屋数据标示：").append(communityHomeBean.getHomeId()).append("\n");
            i++;
        }
        mTv_home_list.setText(builder.toString());


    }

    @Override
    public void showDetail(CommunityHomeBean result) {
        StringBuilder builder = new StringBuilder();
        builder.append("所属房屋:").append(result.getCommunityName() + "").append("\n").append("拥有设备：").append("\n");
        if (null != result.getDeviceList() && !result.getDeviceList().isEmpty()) {
            for (DeviceBean deviceBean :
                    result.getDeviceList()) {
                builder.append("设备名称：").append(deviceBean.getName()).append("-设备id：").append(deviceBean.getDevId()).append("\n");
            }
        } else {
            builder.append("0个").append("\n");
        }
        builder.append(" 项目id：").append(result.getProjectId()).append("\n");
        builder.append(" 房屋id(SpaceTreeId)：").append(result.getSpaceTreeId()).append("\n");
        mEt_projectId.setText(result.getProjectId());
        mEt_spaceTreeId.setText(result.getSpaceTreeId());
        if (null != mTv_home_detail) mTv_home_detail.setText(builder.toString());
    }

    @Override
    public void showPropertyList(ArrayList<CommAnnounceResponseBean> commAnnounceResponseBeans) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < commAnnounceResponseBeans.size(); i++) {
            stringBuilder.append("AnnouncementId: ").append(commAnnounceResponseBeans.get(i).getAnnouncementId()).append("\n").append("公告AnnouncementTitle :").append(commAnnounceResponseBeans.get(i).getAnnouncementTitle()).append("\n");
            mEt_AnnouncementId.setText(commAnnounceResponseBeans.get(i).getAnnouncementId());
        }
        mTv_Property_list.setText(stringBuilder.toString());
    }

    @Override
    public void showFailed(String error) {
        ToastUtil.shortToast(this, error);
    }
}