package com.tuya.community.business.sdk.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.community.android.scene.bean.CommunitySceneBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.ScenePresenter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.view.ISceneView;

import java.util.List;

/**
 * create by nielev on 2020/11/18
 */
public class SceneActivity extends BaseActivity implements View.OnClickListener, ISceneView {

    private Button mBtn_scene_list;
    private TextView mTv_scene;
    private EditText mEt_scene_id;
    private Button mBtn_exe_scene;
    private ScenePresenter mPresenter;
    public static final String EXTRA_HOMEID = "homeId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        initToolbar();
        initView();
        initData();
    }


    private void initView() {
        mBtn_scene_list = findViewById(R.id.btn_scene_list);
        mBtn_scene_list.setOnClickListener(this);
        mTv_scene = findViewById(R.id.tv_scene);
        mEt_scene_id = findViewById(R.id.et_scene_id);
        mBtn_exe_scene = findViewById(R.id.btn_exe_scene);
        mBtn_exe_scene.setOnClickListener(this);
    }


    private void initData() {
        setTitle("场景");
        setDisplayHomeAsUpEnabled();
        mPresenter = new ScenePresenter(this, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_scene_list) {
            mPresenter.getSimpleSceneList();
        } else if(v.getId() == R.id.btn_exe_scene) {
            String sceneId = mEt_scene_id.getText().toString();
            if(!TextUtils.isEmpty(sceneId)){
                mPresenter.executeOneClick(sceneId);
            } else {
                ToastUtil.shortToast(this, "一键执行id不能为空");
            }

        }
    }

    @Override
    public void showSceneList(List<CommunitySceneBean> onClickExecute, List<CommunitySceneBean> automation) {
        StringBuilder s = new StringBuilder();
        s.append("一键执行数量：").append(onClickExecute.size()).append("个").append("\n");
        if(onClickExecute.size() > 0){
            s.append("第一个一键执行名称和id：").append(onClickExecute.get(0).getName()).append(",").append(onClickExecute.get(0).getId()).append("\n");
        }
        s.append("自动化数量：").append(automation.size()).append("个").append("\n");
        if(onClickExecute.size() > 0){
            s.append("第一个自动化名称和id：").append(automation.get(0).getName()).append(",").append(automation.get(0).getId()).append("\n");
        }
        mTv_scene.setText(s.toString());
    }
}
