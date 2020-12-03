package com.tuya.community.business.sdk.demo.utils;

/**
 *
 * create by nielev on 2020/12/1
 */
public class Utils {
    /**
     * 智家Id的临时缓存类，客户接入实现需要换成自己的缓存
     */
    public static long mCurrentHomeId = 0;
    public static void setCurrentHomeId(long homeId){
        mCurrentHomeId = homeId;
    }
    public static long getHomeId(){
        return mCurrentHomeId;
    }
}
