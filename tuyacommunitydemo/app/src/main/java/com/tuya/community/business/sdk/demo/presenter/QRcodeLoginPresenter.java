package com.tuya.community.business.sdk.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.zxing.WriterException;
import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.user.bean.CommunityUser;
import com.tuya.community.business.sdk.demo.activity.MainActivity;
import com.tuya.community.business.sdk.demo.utils.QRUtils;
import com.tuya.community.business.sdk.demo.view.IQRcodeLoginView;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

/**
 * create by nielev on 2020/11/11
 */
public class QRcodeLoginPresenter implements Handler.Callback {
    private static final String TAG = "QRcodeLoginPresenter";
    private static final int GET_TOKEN_SUCCESS = 0x1;
    private static final int GET_LOOP_TOKEN_SUCCESS = 0x2;
    private static final int LOOP_WHAT = 101;
    private Context mActivity;
    private IQRcodeLoginView mView;
    private String mToken;
    private Handler mHandler;
    private boolean isLooping = false;
    public QRcodeLoginPresenter(Context activity, IQRcodeLoginView view) {
        mActivity = activity;
        mView = view;
        mHandler = new Handler(mActivity.getMainLooper(), this);
    }

    private void tokenUserGet(String token) {
        TuyaCommunitySDK.getCommunityUserInstance().phoneQRTokenLogin("", 1, "", new ITuyaCommunityCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
        TuyaCommunitySDK.getCommunityUserInstance().deviceQRCodeLogin("86", token, new ITuyaCommunityResultCallback<CommunityUser>() {
            @Override
            public void onSuccess(CommunityUser result) {
                mHandler.sendEmptyMessage(GET_LOOP_TOKEN_SUCCESS);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                if (errorCode.equals("USER_QR_LOGIN_TOKEN_EXPIRE")){
                    // token 过期 重新刷新token
                    getToken();
                }

                startLoop();
            }
        });
    }

    public void getToken() {
        TuyaCommunitySDK.getCommunityUserInstance().getQRCodeToken("86", new ITuyaCommunityResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mToken = result;
                mHandler.sendEmptyMessage(GET_TOKEN_SUCCESS);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoHome() {
        mActivity.startActivity(new Intent(mActivity, MainActivity.class));
        ((Activity)mActivity).finish();
    }

    public void startLoop() {
        isLooping = true;
        mHandler.sendEmptyMessageDelayed(LOOP_WHAT, 2000);
    }

    public void stopLoop() {
        isLooping = false;
        mHandler.removeMessages(LOOP_WHAT);
    }

    public void onDestroy() {
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case GET_TOKEN_SUCCESS:
                String scheme = "tuyaSmart--qrLogin?token=" + mToken;
                try {
                    Bitmap bitmap = QRUtils.createQRCode(scheme, 1080);
                    mView.refreshQRCode(bitmap);
                    //开始轮询了
                    if (!isLooping){
                        startLoop();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case GET_LOOP_TOKEN_SUCCESS:
                stopLoop();
                gotoHome();
                break;

            case LOOP_WHAT:
                tokenUserGet(mToken);
                break;
            default:
                break;
        }
        return false;
    }

    public void checkLogin() {
        if(TuyaCommunitySDK.getCommunityUserInstance().isLogin()){
            stopLoop();
            gotoHome();
        }
    }
}
