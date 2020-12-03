package com.tuya.community.business.sdk.demo.bean;

import android.text.TextUtils;

import com.tuya.community.android.property.bean.AnnounceLeve;

public class CommTransBean {
    public String announcementId;
    public String announcementTitle;
    public String level;
    public String type;
    public String releaseTime;

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public String getLevelStr(){
        if (TextUtils.equals("1",level)){
            return AnnounceLeve.NORMAL.getLeve();
        }

        if (TextUtils.equals("2",level)){
            return AnnounceLeve.URGENT.getLeve();
        }
        return "";
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    @Override
    public String toString() {
        return "CommTransBean{" +
                "announcementId='" + announcementId + '\'' +
                ", announcementTitle='" + announcementTitle + '\'' +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                '}';
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

}
