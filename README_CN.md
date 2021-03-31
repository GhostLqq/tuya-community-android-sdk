# 涂鸦社区 SDK



## 功能概述

Android Community SDK 是一套涂鸦智能针对社区领域而提供的 Android 5.0 及以上版本的 SDK。Android 开发者可以基于 SDK 快速的实现 APP 开发，以实现社区场景下的多种功能。

SDK 主要包括以下功能：

* 用户管理    
* 房屋管理  
* 云可视对讲      
* 智家设备
* 智家联动



## 集成 SDK

### 配置 build.gradle 

build.gradle 文件里添加集成准备中下载的 dependencies 依赖库。

```groovy
defaultConfig {
    ndk {
        abiFilters "armeabi-v7a", "arm64-v8a"
    }
 }
dependencies {
    implementation 'com.alibaba:fastjson:1.1.67.android'
    implementation 'com.squareup.okhttp3:okhttp-urlconnection:3.14.9'
  	
    // Tuya CommunitySDK 最新稳定版：（前期以aar形式给出）
    implementation(name:'tuyacommunity-sdk-0.0.1', ext:'aar')
    
    //**** Camera 所需工具类，未接入web容器时需要引入 *****//
    implementation 'com.tuya.smart:tuyasmart-base-utils:3.18.0r143-rc.9'
}
```

在根目录的 build.gradle 文件中增加 jcenter() 仓库

```groovy
repositories {
    jcenter()
}
```

### [开发者文档使用指南](./doc/zh-hans/SUMMARY.md)  

### [发布日志](./note/release_note.md)    

### 技术支持

- Tuya IoT 开发者平台: <https://developer.tuya.com/en/>
- Tuya 开发者帮助中心: <https://support.tuya.com/en/help>
- Tuya 工单系统: <https://service.console.tuya.com/>