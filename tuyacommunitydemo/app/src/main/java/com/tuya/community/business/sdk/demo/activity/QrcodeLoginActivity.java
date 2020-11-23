package com.tuya.community.business.sdk.demo.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.presenter.QRcodeLoginPresenter;
import com.tuya.community.business.sdk.demo.view.IQRcodeLoginView;

/**
 * create by nielev on 2020/11/11
 */
public class QrcodeLoginActivity extends AppCompatActivity implements IQRcodeLoginView, View.OnClickListener {
    private static final String TAG = "QRCodeLoginActivity";
    private ImageView mIvQRCode;
    private TextView mTvQrAppInfo;
    private Context mContext;
    private QRcodeLoginPresenter mPresenter;
    private ImageView mBtnExt;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_login);
        mContext = this;

        initViews();

        mPresenter = new QRcodeLoginPresenter(mContext, this);
        //检查是否已经登录
        mPresenter.checkLogin();
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.stopLoop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getToken();
        }
    }

    private void initViews() {
        mIvQRCode = findViewById(R.id.iv_qrcode);
        mTvQrAppInfo = findViewById(R.id.id_tv_qr_app_info);

        mBtnExt = findViewById(R.id.btn_back);
        mBtnExt.setOnClickListener(this);

    }


    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void refreshQRCode(Bitmap qr) {
        mIvQRCode.setImageBitmap(qr);
    }

    public void loginSuccess() {

    }

//    @Override
//    public void setTestText(String testText) {
//        tvQrAppInfo.setText(testText);
//    }
//
//
//    @Override
//    public void setTestText1(String token) {
//        token = token;
//    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if (i == R.id.iv_qrcode) {
//            if (presenter != null) {
//                //屏蔽点击图片刷新。使用错误码，过期自动刷新
//                presenter.getToken();
//            }
//        } else
//        if (i == R.id.id_tv_qr_app_info) {
//            UrlRouter.execute(new UrlBuilder(context, ACTIVITY_LOGIN_QRLOGIN).putString("token", token));
//        }


        if (i == R.id.btn_back) {
            finish();
        }
    }

    protected boolean isUseCustomTheme() {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
