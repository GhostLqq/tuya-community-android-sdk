# Web container access

If you need Tuya community H5 related services, you must access the community web container

| Class name              | Description              |
| ----------------------- | ------------------------ |
| ITuyaCommunityContainer | Tuya Community Container |

## Ready to work

Add the dependencies downloaded in the integration preparation to the build.gradle file.

```groovy
defaultConfig {
    ndk {
        abiFilters "armeabi-v7a", "arm64-v8a"
    }
 }
 packagingOptions {
        pickFirst'lib/*/libc++_shared.so'
        pickFirst'lib/*/libgnustl_shared.so'
        pickFirst'lib/*/libjnimain.so'
  }
    
dependencies {
    implementation'com.alibaba:fastjson:1.1.67.android'
    implementation'com.squareup.okhttp3:okhttp-urlconnection:3.14.9'
  To
     //**** web container access *****//
    implementation'com.facebook.fresco:fresco:2.2.0'
    implementation'com.tuya.smart:tuyacommunity-webcontainer:1.0.0-rc.5'
    //**** web container access *****//
   
}
```

### Configuration file

Put the *module_app.json* and *x_platform_config.json* of [Demo中](https://github.com/TuyaInc/tuya_community_android_sdk) into the assets directory of the project

### Initialization

Need to perform container-related initialization in Application

```java
 
 //Fresco initialization
 Fresco.initialize(this);
 
 //Community SDK initialization
 ...
 //
 
 // web container initialization
TuyaWrapper.init(this, new RouteEventListener() {
    @Override
    public void onFaild(int errorCode, UrlBuilder urlBuilder) {
        // Route does not implement callback
        Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
    }
}, new ServiceEventListener() {
    @Override
    public void onFaild(String serviceName) {
        // The service does not implement the callback
        Log.e("service not implement", serviceName);
    }
});
```


## Instructions

### Jump to web container page


**Sample Code**

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