package com.tuya.community.business.sdk.demo.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.home.api.ITuyaCommunityHome;
import com.tuya.community.android.home.bean.CommunityHomeBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.MessageUtil;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.utils.Utils;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.common.utils.L;
import com.tuya.community.business.sdk.demo.utils.Constants;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2PFactory;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.camera.utils.AudioUtils;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.nio.ByteBuffer;

import static com.tuya.community.business.sdk.demo.presenter.CommonDeviceDebugPresenter.INTENT_DEVID;
import static com.tuya.community.business.sdk.demo.presenter.HomePresenter.PROJECT_ID;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_FROM_MQTT;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_SN;
import static com.tuya.community.business.sdk.demo.presenter.VisualSpeakPresenter.EXTRA_TARGET_ADDRESS;
import static com.tuya.community.business.sdk.demo.utils.Constants.ARG1_OPERATE_FAIL;
import static com.tuya.community.business.sdk.demo.utils.Constants.ARG1_OPERATE_SUCCESS;
import static com.tuya.community.business.sdk.demo.utils.Constants.INTENT_P2P_TYPE;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_CONNECT;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_GET_CLARITY;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_MUTE;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_SCREENSHOT;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_TALK_BACK_BEGIN;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_TALK_BACK_OVER;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_VIDEO_RECORD_BEGIN;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_VIDEO_RECORD_FAIL;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_VIDEO_RECORD_OVER;

/**
 * @author chenbj
 */
public class CameraPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CameraPanelActivity";
    private Toolbar toolbar;
    private TuyaCameraView mVideoView;
    private ImageView muteImg;
    private TextView qualityTv;
    private TextView speakTxt, recordTxt, photoTxt, replayTxt, settingTxt, cloudStorageTxt,messageCenterTxt;

    private static final int ASPECT_RATIO_WIDTH = 9;
    private static final int ASPECT_RATIO_HEIGHT = 16;
    private boolean isSpeaking = false;
    private boolean isRecording = false;
    private boolean isPlay = false;
    private int previewMute = ICameraP2P.MUTE;
    private int videoClarity = ICameraP2P.HD;

    private String picPath, videoPath;

    private int p2pType;

    private String mDevId;
    private ITuyaSmartCameraP2P mCameraP2P;

    private String mProjectId;
    private String mTargetSpaceId;
    private String mSn;
    private int mSdkProvider;
    private DeviceBean mDeviceBean;
    private boolean isFromMqtt;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECT:
                    handleConnect(msg);
                    break;
                case MSG_GET_CLARITY:
                    handleClarity(msg);
                    break;
                case MSG_MUTE:
                    handleMute(msg);
                    break;
                case MSG_SCREENSHOT:
                    handlesnapshot(msg);
                    break;
                case MSG_VIDEO_RECORD_BEGIN:
                    ToastUtil.shortToast(CameraPanelActivity.this, "record start success");
                    break;
                case MSG_VIDEO_RECORD_FAIL:
                    ToastUtil.shortToast(CameraPanelActivity.this, "record start fail");
                    break;
                case MSG_VIDEO_RECORD_OVER:
                    handleVideoRecordOver(msg);
                    break;
                case MSG_TALK_BACK_BEGIN:
                    handleStartTalk(msg);
                    break;
                case MSG_TALK_BACK_OVER:
                    handleStopTalk(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private View reject;
    private View handup;
    private View open_door;

    private void handleStopTalk(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            ToastUtil.shortToast(CameraPanelActivity.this, "stop talk success" + msg.obj);
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }

    private void handleStartTalk(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            ToastUtil.shortToast(CameraPanelActivity.this, "start talk success" + msg.obj);
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }

    private void handleVideoRecordOver(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            ToastUtil.shortToast(CameraPanelActivity.this, "record success");
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }

    private void handlesnapshot(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            ToastUtil.shortToast(CameraPanelActivity.this, "snapshot success");
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }

    private void handleMute(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            muteImg.setSelected(previewMute == ICameraP2P.MUTE);
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }


    private void handleClarity(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            qualityTv.setText(videoClarity == ICameraP2P.HD ? "HD" : "SD");
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "operation fail");
        }
    }

    private void handleConnect(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            preview();
        } else {
            ToastUtil.shortToast(CameraPanelActivity.this, "connect fail");
        }
    }

    /**
     * the lower power Doorbell device change to true
     */
    private boolean isDoorbell = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_panel);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoView = findViewById(R.id.camera_video_view);
        muteImg = findViewById(R.id.camera_mute);
        open_door = findViewById(R.id.open_door);
        qualityTv = findViewById(R.id.camera_quality);
        speakTxt = findViewById(R.id.speak_Txt);
        reject = findViewById(R.id.reject);
        handup = findViewById(R.id.handup);


        WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = width * ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar_view);
        findViewById(R.id.camera_video_view_Rl).setLayoutParams(layoutParams);

        muteImg.setSelected(true);
    }
    
    private void initData() {
        mDevId = getIntent().getStringExtra(INTENT_DEVID);
        p2pType = getIntent().getIntExtra(INTENT_P2P_TYPE, -1);

        //设备呼叫进来
        getDeviceCallData();

        initCamera();
    }

    private void getDeviceCallData() {
        mDeviceBean = TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(mDevId);
        mProjectId = getIntent().getStringExtra(PROJECT_ID);
        mTargetSpaceId = getIntent().getStringExtra(EXTRA_TARGET_ADDRESS);
        mSn = getIntent().getStringExtra(EXTRA_SN);
        isFromMqtt = getIntent().getBooleanExtra(EXTRA_FROM_MQTT, false);
        //判断从mqtt消息通知过来的项目id和房屋id是否为空，为空则取当前房屋和项目
        if((TextUtils.isEmpty(mProjectId) || TextUtils.isEmpty(mTargetSpaceId))){
            if(Utils.getHomeId() != 0){
                ITuyaCommunityHome iTuyaCommunityHome = TuyaCommunitySDK.newCommunityHomeInstance(Utils.getHomeId());
                CommunityHomeBean communityHomeBean = iTuyaCommunityHome.getCommunityHomeBean();
                mProjectId = communityHomeBean.getProjectId();
                mTargetSpaceId = communityHomeBean.getSpaceTreeId();
            }
        }
    }

    private void showNotSupportToast() {
        ToastUtil.shortToast(CameraPanelActivity.this, "device is not support!");
    }

    private void preview() {
        mCameraP2P.startPreview(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                Log.d(TAG, "start preview onSuccess");
                isPlay = true;
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                Log.d(TAG, "start preview onFailure, errCode: " + errCode);
                isPlay = false;
            }
        });
    }

    private void initListener() {
        if (mCameraP2P == null) {
            return;
        }
        open_door.setOnClickListener(this);
        muteImg.setOnClickListener(this);
        qualityTv.setOnClickListener(this);
        speakTxt.setOnClickListener(this);

        reject.setOnClickListener(this);
        handup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_door:
                openDoor();
                break;
            case R.id.camera_mute:
                muteClick();
                break;
            case R.id.camera_quality:
                setVideoClarity();
                break;
            case R.id.speak_Txt:
                speakClick();
                break;
            case R.id.reject:
                rejectCall();
                break;
            case R.id.handup:
                handUp();
                break;
            default:
                break;
        }
    }

    private void handUp() {
        stopCamera();
    }

    private void rejectCall() {
        if(TextUtils.isEmpty(mSn)) return;
        TuyaCommunitySDK.getCommunityVisualSpeakManager().visualSpeakReject(mDevId, mProjectId, mTargetSpaceId, mSn, new ITuyaCommunityCallback() {
            @Override
            public void onError(String s, String s1) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public void openDoor() {
        if(TextUtils.isEmpty(mProjectId) || TextUtils.isEmpty(mTargetSpaceId)) return;
        TuyaCommunitySDK.getCommunityVisualSpeakManager().openDoor(mDevId, mProjectId, mTargetSpaceId, new ITuyaCommunityCallback() {
            @Override
            public void onError(String s, String s1) {
            }

            @Override
            public void onSuccess() {
                ToastUtil.shortToast(CameraPanelActivity.this, "开门成功");
            }
        });
    }

    private void initCamera() {
        mCameraP2P = TuyaSmartCameraP2PFactory.createCameraP2P(p2pType, mDevId);
        mVideoView.setViewCallback(new AbsVideoViewCallback() {
            @Override
            public void onCreated(Object o) {
                super.onCreated(o);
                if (null != mCameraP2P){
                    mCameraP2P.generateCameraView(o);
                }
            }
        });
        mVideoView.createVideoView(p2pType);
        if (null == mCameraP2P) {
            showNotSupportToast();
        }
    }


    private void muteClick() {
        int mute;
        mute = previewMute == ICameraP2P.MUTE ? ICameraP2P.UNMUTE : ICameraP2P.MUTE;
        mCameraP2P.setMute(ICameraP2P.PLAYMODE.LIVE, mute, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                previewMute = Integer.valueOf(data);
                mHandler.sendMessage(MessageUtil.getMessage(MSG_MUTE, ARG1_OPERATE_SUCCESS));
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                mHandler.sendMessage(MessageUtil.getMessage(MSG_MUTE, ARG1_OPERATE_FAIL));
            }
        });
    }

    private void speakClick() {
        if (isSpeaking) {
            mCameraP2P.stopAudioTalk(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    isSpeaking = false;
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_OVER, ARG1_OPERATE_SUCCESS));
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    isSpeaking = false;
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_OVER, ARG1_OPERATE_FAIL));

                }
            });
        } else {
            if (Constants.hasRecordPermission()) {
                mCameraP2P.startAudioTalk(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isSpeaking = true;
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_BEGIN, ARG1_OPERATE_SUCCESS));
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        isSpeaking = false;
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_BEGIN, ARG1_OPERATE_FAIL));
                    }
                });
            } else {
                Constants.requestPermission(CameraPanelActivity.this, Manifest.permission.RECORD_AUDIO, Constants.EXTERNAL_AUDIO_REQ_CODE, "open_recording");
            }
        }
    }

    private void setVideoClarity() {
        mCameraP2P.setVideoClarity(videoClarity == ICameraP2P.HD ? ICameraP2P.STANDEND : ICameraP2P.HD, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                videoClarity = Integer.valueOf(data);
                mHandler.sendMessage(MessageUtil.getMessage(MSG_GET_CLARITY, ARG1_OPERATE_SUCCESS));
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                mHandler.sendMessage(MessageUtil.getMessage(MSG_GET_CLARITY, ARG1_OPERATE_FAIL));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        //must register again,or can't callback
        if (null != mCameraP2P) {
            AudioUtils.getModel(this);
            mCameraP2P.registorOnP2PCameraListener(p2pCameraListener);
            mCameraP2P.generateCameraView(mVideoView.createdView());
            if (mCameraP2P.isConnecting()) {
                mCameraP2P.startPreview(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlay = true;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        Log.d(TAG, "start preview onFailure, errCode: " + errCode);
                    }
                });
            }
            if (!mCameraP2P.isConnecting()) {
                mCameraP2P.connect(mDevId, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int i, int i1, String s) {
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_CONNECT, ARG1_OPERATE_SUCCESS));
                    }

                    @Override
                    public void onFailure(int i, int i1, int i2) {
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_CONNECT, ARG1_OPERATE_FAIL));
                    }
                });
            }
        }
    }

    private AbsP2pCameraListener p2pCameraListener = new AbsP2pCameraListener() {
        @Override
        public void onReceiveSpeakerEchoData(ByteBuffer pcm, int sampleRate) {
            if (null != mCameraP2P){
                int length = pcm.capacity();
                L.d(TAG, "receiveSpeakerEchoData pcmlength " + length + " sampleRate " + sampleRate);
                byte[] pcmData = new byte[length];
                pcm.get(pcmData, 0, length);
                mCameraP2P.sendAudioTalkData(pcmData,length);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
        stopCamera();
    }

    private void stopCamera() {
        if (isSpeaking) {
            mCameraP2P.stopAudioTalk(null);
        }
        if (isPlay) {
            mCameraP2P.stopPreview(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {

                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {

                }
            });
            isPlay = false;
        }
        if (null != mCameraP2P) {
            mCameraP2P.disconnect(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int i, int i1, String s) {

                }

                @Override
                public void onFailure(int i, int i1, int i2) {

                }
            });
        }
        AudioUtils.changeToNomal(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCameraP2P) {
            mCameraP2P.destroyP2P();
        }
    }

}
