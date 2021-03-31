# 云可视对讲

云可视对讲模块提供获取可视对讲列表和跳入云可视设备面板方法

| 类名        | 说明              |
| ----------- | ----------------- |
| ITuyaCommunityVisualSpeakManager| 涂鸦社区云可视对讲|

VisualSpeakDeviceBean 对象注释

| 字段        | 说明              |
| ----------- | ----------------- |
| deviceId | 设备id|
| authSpaceTreeId | 房屋id|
| authAddress | 设备地址|

DeviceMessageType 设备消息类型注释

| 字段        | 说明              |
| ----------- | ----------------- |
| DEVICE\_HANGUP_DRIVE | 设备主动挂断 |
| DEVICE\_HANGUP_TIMEOUT | 超时挂断type |
| DEVICE\_HANGUP_PASSIVE | 被动挂断 |
| DEVICE_ANSWER | 设备在通话中 |
| CALL\_TIMEOUT_HANGUP\_PASSIVE | 设备通话时间超过90秒，由设备发起挂断 |



## 准备工作

app如果要收到可视设备的呼叫，需要在登录后就设置好呼叫监听注册，退出app或者退出登录时则需要注销监听

### 设置可视对讲呼叫监听

**接口说明**
 
  ```java
registerVisualSpeakCallListener(OnVisualSpeakCallListener listener)	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| OnVisualSpeakCallListener   |    可视对讲监听对象          |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().registerVisualSpeakCallListener(new OnVisualSpeakCallListener() {
            @Override
            public void onDeviceCall(DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "设备呼叫"+deviceCallDataBean.getDevId());
            }

            @Override
            public void onAppOrDeviceAnswer(DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "设备已被接听");
            }

            @Override
            public void onHandup(HangupMessageType hangupMessageType, DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "设备已被挂断");
            }
        });
```

### 注销可视对讲呼叫监听

**接口说明**
 
  ```java
void unRegisterVisualSpeakCallListener(OnVisualSpeakCallListener listener);	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| OnVisualSpeakCallListener   |    可视对讲监听对象          |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().unRegisterVisualSpeakCallListener(mListener);
```


### 可视对讲设备列表

**接口说明**
 
  ```java
getVisualSpeakDeviceList(String projectId,String spaceTreeId，ITuyaCommunityResultCallback<ArrayList<VisualSpeakDeviceBean>> callback)
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| projectId   |    项目id          |
| spaceTreeId    |房屋id|
| callback    | 回调                          |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().getVisualSpeakDeviceList(String projectId,String spaceTreeId，new ITuyaCommunityResultCallback<ArrayList<VisualSpeakDeviceBean>>{
 @Override
            public void onError(String s, String s1) {

            }

            @Override
            public void onSuccess(ArrayList<VisualSpeakDeviceBean> visualTalkDeviceBeans) {

            }
});

```

### 获取通话状态

**接口说明**
 
  ```java
void getVisualSpeakDeviceStatus(String devId, String projectId, String spaceTreeId, String sn, ITuyaCommunityResultCallback<DeviceMessageType> callback)	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| devId   |    设备id          |
| projectId    |项目id|
| spaceTreeId    | 房屋Id                         |
| sn    | 通话唯一标示                       |
| callback    | 回调                      |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().getVisualSpeakDeviceStatus(mDevId, mProjectId, mSpaceId, sn, new ITuyaCommunityResultCallback<DeviceMessageType>() {
            @Override
            public void onSuccess(DeviceMessageType deviceMessageType) {
                if (null == deviceMessageType) {
                    ToastUtil.shortToast(mActivity, "error");
                } else if (DEVICE_ANSWER == deviceMessageType) {
                    ToastUtil.shortToast(mActivity, "device answered");
                } else {
                    ToastUtil.shortToast(mActivity, "device hand up");
                }
            }

            @Override
            public void onError(String s, String s1) {

            }
        });
```

### 开门

**接口说明**
 
  ```java
void openDoor(String devId, String projectId, String spaceTreeId, ITuyaCommunityCallback callback)	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| devId   |    设备id          |
| projectId    |项目id|
| spaceTreeId    | 房屋Id                         |
| callback    | 回调                      |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().openDoor(mDevId, mProjectId, mSpaceId, new ITuyaCommunityCallback() {

            @Override
            public void onError(String s, String s1) {

            }

            @Override
            public void onSuccess() {

            }
        });
```

### 拒接呼叫

**接口说明**
 
  ```java
void visualSpeakReject(String devId, String projectId, String spaceTreeId, String sn, ITuyaCommunityCallback callback)	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| devId   |    设备id          |
| projectId    |项目id|
| spaceTreeId    | 房屋Id                         |
| sn    | 通话唯一标示                       |
| callback    | 回调                      |

**示例代码**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().visualSpeakReject(mDevId, mProjectId, mSpaceId, sn, new ITuyaCommunityCallback() {
            @Override
            public void onError(String s, String s1) {
                
            }

            @Override
            public void onSuccess() {

            }
        });
```
