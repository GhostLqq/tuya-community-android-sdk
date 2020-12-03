package com.tuya.community.business.sdk.demo.view;

import com.tuya.community.android.visualspeak.bean.VisualSpeakDeviceBean;

import java.util.ArrayList;

/**
 * create by nielev on 2020/11/28
 */
public interface IVisualSpeakView {
    void showVisualSpeakDevices(ArrayList<VisualSpeakDeviceBean> visualSpeakDeviceBeans);
}
