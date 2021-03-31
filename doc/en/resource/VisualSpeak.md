# Visual Speak

The visual speak module provides methods for obtaining the visual intercom list and jumping into the cloud visual device panel

| Class name                       | Description                          |
| -------------------------------- | ------------------------------------ |
| ITuyaCommunityVisualSpeakManager | Tuya Community Cloud Visual Intercom |

VisualSpeakDeviceBean object annotation

| Field           | Description    |
| --------------- | -------------- |
| deviceId        | device id      |
| authSpaceTreeId | House id       |
| authAddress     | Device Address |

DeviceMessageType Device message type annotation

| Field                         | Description                                                  |
| ----------------------------- | ------------------------------------------------------------ |
| DEVICE\_HANGUP_DRIVE          | The device hangs up actively                                 |
| DEVICE\_HANGUP_TIMEOUT        | Timeout hangup type                                          |
| DEVICE\_HANGUP_PASSIVE        | Passive Hang Up                                              |
| DEVICE_ANSWER                 | The device is in a call                                      |
| CALL\_TIMEOUT_HANGUP\_PASSIVE | The device call time exceeds 90 seconds, the device initiates a hangup |



## Ready to work

If the app wants to receive a call from a visual device, you need to set up the call monitor registration after logging in, and you need to log out of the monitor when you log out of the app or log out

### Set up video intercom call monitoring

**Interface Description**

  ```java
registerVisualSpeakCallListener(OnVisualSpeakCallListener listener)
  ```

**Parameter Description**

| Parameters                | Description                    |
| ------------------------- | ------------------------------ |
| OnVisualSpeakCallListener | Visual Intercom Monitor Object |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().registerVisualSpeakCallListener(new OnVisualSpeakCallListener() {
            @Override
            public void onDeviceCall(DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "device call"+deviceCallDataBean.getDevId());
            }

            @Override
            public void onAppOrDeviceAnswer(DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "The device has been answered");
            }

            @Override
            public void onHandup(HangupMessageType hangupMessageType, DeviceCallDataBean deviceCallDataBean) {
                ToastUtil.shortToast(mActivity, "The device has been hung up");
            }
        });
```

### Log out of video intercom call monitoring

**Interface Description**

  ```java
void unRegisterVisualSpeakCallListener(OnVisualSpeakCallListener listener);
  ```

**Parameter Description**

| Parameters                | Description                    |
| ------------------------- | ------------------------------ |
| OnVisualSpeakCallListener | Visual Intercom Monitor Object |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().unRegisterVisualSpeakCallListener(mListener);
```

### Visual intercom equipment list

**Interface Description**

  ```java
getVisualSpeakDeviceList(String projectId,String spaceTreeId,ITuyaCommunityResultCallback<ArrayList<VisualSpeakDeviceBean>> callback)
  ```

**Parameter Description**

| Parameters  | Description |
| ----------- | ----------- |
| projectId   | Project id  |
| spaceTreeId | House id    |
| callback    | callback    |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityVisualSpeakManager().getVisualSpeakDeviceList(String projectId,String spaceTreeId, new ITuyaCommunityResultCallback<ArrayList<VisualSpeakDeviceBean>>{
 @Override
            public void onError(String s, String s1) {

            }

            @Override
            public void onSuccess(ArrayList<VisualSpeakDeviceBean> visualTalkDeviceBeans) {

            }
});

```

### Get call status

**Interface Description**

  ```java
void getVisualSpeakDeviceStatus(String devId, String projectId, String spaceTreeId, String sn, ITuyaCommunityResultCallback<DeviceMessageType> callback)
  ```

**Parameter Description**

| Parameters  | Description            |
| ----------- | ---------------------- |
| devId       | device id              |
| projectId   | Project id             |
| spaceTreeId | House Id               |
| sn          | Unique call identifier |
| callback    | callback               |

**Sample Code**

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

### Open the door

**Interface Description**

  ```java
void openDoor(String devId, String projectId, String spaceTreeId, ITuyaCommunityCallback callback)
  ```

**Parameter Description**

| Parameters  | Description |
| ----------- | ----------- |
| devId       | device id   |
| projectId   | Project id  |
| spaceTreeId | House Id    |
| callback    | callback    |

**Sample Code**

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

### Reject call

**Interface Description**

  ```java
void visualSpeakReject(String devId, String projectId, String spaceTreeId, String sn, ITuyaCommunityCallback callback)
  ```

**Parameter Description**

| Parameters  | Description            |
| ----------- | ---------------------- |
| devId       | device id              |
| projectId   | Project id             |
| spaceTreeId | House Id               |
| sn          | Unique call identifier |
| callback    | callback               |

**Sample Code**

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
