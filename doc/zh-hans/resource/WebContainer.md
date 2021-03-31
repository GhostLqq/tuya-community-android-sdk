# Web容器接入

如果需要涂鸦社区H5相关业务，就必须接入社区Web容器

| 类名        | 说明              |
| ----------- | ----------------- |
| ITuyaCommunityContainer| 涂鸦社区容器类|

## 准备工作

build.gradle 文件里添加集成准备中下载的 dependencies 依赖库。

```groovy
defaultConfig {
    ndk {
        abiFilters "armeabi-v7a", "arm64-v8a"
    }
 }
 packagingOptions {
        pickFirst 'lib/*/libc++_shared.so'
        pickFirst 'lib/*/libgnustl_shared.so'
        pickFirst 'lib/*/libjnimain.so'
  }
    
dependencies {
    implementation 'com.alibaba:fastjson:1.1.67.android'
    implementation 'com.squareup.okhttp3:okhttp-urlconnection:3.14.9'
  	
     //**** web容器接入 *****//
    implementation 'com.facebook.fresco:fresco:2.2.0'
    implementation 'com.tuya.smart:tuyacommunity-webcontainer:1.0.0-rc.5'
    //**** web容器接入 *****//
   
}
```
### 配置文件

将 [Demo中](https://github.com/TuyaInc/tuya_community_android_sdk)的 *module_app.json* 和 *x_platform_config.json* 放入项目的assets目录下
### 初始化

需要在Application进行容器相关的初始化

```java
 
 //Fresco初始化
 Fresco.initialize(this);
 
 //社区 SDK初始化
 ... 
 //
 
 // web容器初始化
TuyaWrapper.init(this, new RouteEventListener() {
    @Override
    public void onFaild(int errorCode, UrlBuilder urlBuilder) {
        // 路由未实现回调
        Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
    }
}, new ServiceEventListener() {
    @Override
    public void onFaild(String serviceName) {
        // 服务未实现回调
        Log.e("service not implement", serviceName);
    }
});
```


## 使用方法

### 跳转Web容器页面


**示例代码**

Activity

```java
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);

```
Fragment

```java
WebViewFragment fragment = new WebViewFragment();
Bundle args = new Bundle();
args.putString("Uri", url);
args.putBoolean("enableLeftArea", true);
fragment.setArguments(args);
getSupportFragmentManager().beginTransaction()
        .add(R.id.web_content, fragment, WebViewFragment.class.getSimpleName())
        .commit();
```