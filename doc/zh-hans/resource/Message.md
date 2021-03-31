# 消息中心

消息管理模块包含推送相关功能，主要分为告警、家庭、通知三个大类，每个大类分别支持开启或关闭。


| 类名     | 说明                               |
| -------- | ---------------------------------- |
| ITuyaCommunityPush | 推送接口类 |

## 消息推送设置

### 获取 APP 消息推送的开启状态

消息推送开关为总开关，关闭状态下无法接收到 设备告警、房屋消息、通知消息 等任何消息。

***接口说明***

```java
void getPushStatus(ITuyaCommunityResultCallback<CommunityPushStatusBean> callback);
```

**参数说明**

| 参数     | 说明                               |
| -------- | ---------------------------------- |
| callback | 回调，包括获取总开关状态成功和失败 |

**示例代码**

```java
 TuyaCommunitySDK.getCommunityPushInstance().getPushStatus(new ITuyaCommunityResultCallback<CommunityPushStatusBean>()  {
       @Override
       public void onSuccess(CommunityPushStatusBean result) {}
       @Override
       public void onError(String errorCode, String errorMessage) {}
 });
```



### 开启或者关闭APP消息推送

消息推送开关为总开关，关闭状态下无法接收到 设备告警、房屋消息、通知消息 等任何消息

**接口说明**

```java
void setPushStatus(boolean isOpen, ITuyaCommunityResultCallback <Boolean> callback);
```

**参数说明**

| 参数     | 说明                     |
| -------- | ------------------------ |
| isOpen   | 是否开启                 |
| callback | 回调，包括设置成功和失败 |

**示例代码**

```java
TuyaCommunitySDK.getCommunityPushInstance().setPushStatus(isOpen, new ITuyaCommunityResultCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```

### 根据消息类型获取或设置消息的开关状态

#### 根据消息类型获取消息的开关状态

根据消息类型获取当前类型消息的开关状态

**接口说明**

```java
void getPushStatusByType(CommunityPushType type, ITuyaCommunityResultCallback <Boolean> callback);
```

**参数说明**

| 参数     | 说明                                                       |
| -------- | ---------------------------------------------------------- |
| type     | 消息类型（0-告警消息，1-房屋消息，2-通知消息，4-营销消息） |
| callback | 回调，包括获取成功和失败                                   |

**示例代码**

```java
TuyaCommunitySDK.getCommunityPushInstance().getPushStatusByType(type, new ITuyaCommunityResultCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```



#### 根据消息类型设置消息的开关状态

根据消息类型设置当前类型消息的开关状态。

**接口说明**

```java
void setPushStatusByType(CommunityPushType type, isOpen, ITuyaCommunityResultCallback <Boolean> callback);
```

**参数说明**

| 参数     | 说明                                                       |
| -------- | ---------------------------------------------------------- |
| type     | 消息类型（0-告警消息，1-房屋消息，2-通知消息，4-营销消息） |
| isOpen   | 是否开启                                                   |
| callback | 回调，包括设置成功和失败                                   |

**示例代码**

```java
TuyaCommunitySDK.getCommunityPushInstance().setPushStatusByType(type, isOpen, new ITuyaCommunityResultCallback<Boolean>()  {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```
