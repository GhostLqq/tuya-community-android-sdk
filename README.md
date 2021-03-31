# Tuya Community SDK

[English](./README.md) | [中文文档](./README_CN.md)

## Functional Overview

Android Community SDK is a set of SDK for Android 5.0 and above provided by Tuya Smart for the community field. Android developers can quickly implement APP development based on the SDK to achieve multiple functions in community scenarios.

The SDK mainly includes the following functions:

* Account system
* Housing Management System
* Video intercom for smart access control
* Smart devices in the home
* Linkage between smart devices




## Integrate SDK

### Configure build.gradle

Add the dependencies downloaded in the integration preparation to the build.gradle file.

```groovy
defaultConfig {
    ndk {
        abiFilters "armeabi-v7a", "arm64-v8a"
    }
 }
dependencies {
    implementation 'com.alibaba:fastjson:1.1.67.android'
    implementation 'com.squareup.okhttp3:okhttp-urlconnection:3.14.9'
  	
    //The latest version of Community Sdk:（In the previous period, it is given in aar form）
    implementation(name:'tuyacommunity-sdk-0.0.1', ext:'aar')
    
    //**** Camera tools needed to be imported when not connected to the web container *****//
    implementation 'com.tuya.smart:tuyasmart-base-utils:3.18.0r143-rc.9'
}
```

Add the jcenter() warehouse to the build.gradle file in the root directory

```groovy
repositories {
    jcenter()
}
```


### [Documentation](./doc/en/SUMMARY.md)   

### [Release Notes](./note/release_note_en.md)    

### Technical Support

* Tuya IoT Developer Platform: <https://developer.tuya.com/en/>
* Tuya Developer Help Center: <https://support.tuya.com/en/help>
* Tuya Ticket System: <https://service.console.tuya.com/>