# Message Center

The message management module contains push-related functions, which are mainly divided into three categories: alarm, family, and notification. Each category can be turned on or off.


| Class name         | Description          |
| ------------------ | -------------------- |
| ITuyaCommunityPush | Push interface class |

## Message push settings

### Get the open status of APP message push

The message push switch is the master switch. When it is turned off, you cannot receive any messages such as device alarms, house messages, and notification messages.

***Interface Description***

```java
void getPushStatus(ITuyaCommunityResultCallback<CommunityPushStatusBean> callback);
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| callback   | Callback, including the success and failure of obtaining the status of the master switch |

**Sample Code**

```java
 TuyaCommunitySDK.getCommunityPushInstance().getPushStatus(new ITuyaCommunityResultCallback<CommunityPushStatusBean>() {
       @Override
       public void onSuccess(CommunityPushStatusBean result) {}
       @Override
       public void onError(String errorCode, String errorMessage) {}
 });
```



### Turn on or off APP message push

The message push switch is the main switch. When it is turned off, you cannot receive any messages such as device alarms, house messages, notification messages, etc.

**Interface Description**

```java
void setPushStatus(boolean isOpen, ITuyaCommunityResultCallback <Boolean> callback);
```

**Parameter Description**

| Parameters | Description                                     |
| ---------- | ----------------------------------------------- |
| isOpen     | Whether to open                                 |
| callback   | Callback, including setting success and failure |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityPushInstance().setPushStatus(isOpen, new ITuyaCommunityResultCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```

### Get or set the switch status of the message according to the message type

#### Get the switch status of the message according to the message type

Get the switch status of the current type of message according to the message type

**Interface Description**

```java
void getPushStatusByType(CommunityPushType type, ITuyaCommunityResultCallback <Boolean> callback);
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| type       | Message type (0-alarm message, 1-house message, 2-notification message, 4-marketing message) |
| callback   | Callback, including acquisition success and failure          |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityPushInstance().getPushStatusByType(type, new ITuyaCommunityResultCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```



#### Set the switch status of the message according to the message type

Set the switch state of the current type of message according to the message type.

**Interface Description**

```java
void setPushStatusByType(CommunityPushType type, isOpen, ITuyaCommunityResultCallback <Boolean> callback);
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| type       | Message type (0-alarm message, 1-house message, 2-notification message, 4-marketing message) |
| isOpen     | Whether to open                                              |
| callback   | Callback, including setting success and failure              |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityPushInstance().setPushStatusByType(type, isOpen, new ITuyaCommunityResultCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {}
      @Override
      public void onError(String errorCode, String errorMessage) {}
});
```