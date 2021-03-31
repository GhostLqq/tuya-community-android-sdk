# Firmware upgrade

Firmware upgrades are mainly used to enhance device capabilities and fix bugs;

There are two types of firmware upgrades: "device upgrade" and "MCU upgrade";

## Initialize firmware information

**Interface Description**

Wi-Fi, Zigbee gateway, camera and other equipment:

```java
ITuyaOta newOTAInstance(String devId);
```

**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| devId      | device id   |

**Example code**

```java
ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance("devId");
```

## Initialize the firmware information of the sub-device

NOTE: It is supported since SDK 2.9.1;

**Interface Description**

Sub-device:

```java
ITuyaOta newOTAInstance(String meshId, String devId, String nodeId);
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| meshId     | Zigbee gateway id                                            |
| devId      | Zigbee sub-device id                                         |
| nodeId     | Zigbee sub-device mac address (obtained from the DeviceBean of the sub-device) |

**Example code**

```java
ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance("meshId", "devId", "nodeId");
```

## Get firmware upgrade information

**Interface Description**

```java
iTuyaOta.getOtaInfo(new IGetOtaInfoCallback({
@Override
void onSuccess(List<UpgradeInfoBean> list) {
To
}
@Override
void onFailure(String code, String error) {
To
}
});
```

**Parameter Description**

| Field           | Type   | Description                                                  |
| --------------- | ------ | ------------------------------------------------------------ |
| upgradeStatus   | int    | Upgrade status, 0: No new version 1: New version 2: Upgrading |
| version         | String | Latest version                                               |
| currentVersion  | String | Current version                                              |
| timeout         | int    | Timeout time, unit: seconds                                  |
| upgradeType     | int    | 0: App reminder to upgrade 2: App mandatory upgrade 3: Detect upgrade |
| type            | int    | 0: Wi-Fi device 1: Bluetooth device 2: GPRS device 3: zigbee device 9: MCU |
| typeDesc        | String | Module description                                           |
| lastUpgradeTime | long   | Last upgrade time, unit: milliseconds                        |

## Add firmware upgrade monitor

**Interface Description**

```java
iTuyaOta.setOtaListener(new IOtaListener() {
        @Override
        public void onSuccess(int otaType) {

        }

        @Override
        public void onFailure(int otaType, String code, String error) {

        }

        @Override
        public void onFailureWithText(int otaType, String code,
                                      OTAErrorMessageBean messageBean) {

        }

        @Override
        public void onProgress(int otaType, int progress) {

        }

        @Override
        public void onTimeout(int otaType) {

        }
});
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| otaType    | Upgraded device type, same as the type field in UpgradeInfoBean; |

## Start upgrading

**Interface Description**

Call this method to start the upgrade, and the registered OTA monitor after the call will return the upgrade status so that the developer can build the UI;

```java
iTuyaOta.startOta();
```

## Destroy

**Interface Description**

Destroy and reclaim memory after leaving the upgrade page;

```java
iTuyaOta.onDestory();
```

## Complete sample code

```java
public void start() {
    String devId = "your_devId";
    final ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance(devId);
    iTuyaOta.getOtaInfo(new IGetOtaInfoCallback() {
        @Override
        public void onSuccess(List<UpgradeInfoBean> upgradeInfoBeans) {
            int type = getUpgradeType(upgradeInfoBeans);
            if (type> 0) {
                startUpgrade(iTuyaOta, type);
            }
        }

        @Override
        public void onFailure(String code, String error) {
            Log.i(TAG, "Get OTA info failed,errorCode=" + code
                    + ",errorMessage=" + error);
        }
    });
}

private int getUpgradeType(List<UpgradeInfoBean> upgradeInfoBeans) {
    int type = -1;
    for (UpgradeInfoBean bean: upgradeInfoBeans) {
        if (bean.upgradeType == 2) {
            return -1;
        } else if (bean.upgradeType == 1 && type == -1) {
            type = bean.getType();
        }
    }
    return type;
}

private void startUpgrade(final ITuyaOta iTuyaOta, int type) {
    final int messageWhat = 10001;
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "upgrade timeout");
        }
    };
    iTuyaOta.setOtaListener(new IOtaListener() {
        @Override
        public void onSuccess(int otaType) {
            handler.removeMessages(messageWhat);
            iTuyaOta.onDestroy();
            Log.i(TAG, "upgrade success");
        }

        @Override
        public void onFailure(int otaType, String code, String error) {
            handler.removeMessages(messageWhat);
            iTuyaOta.onDestroy();
            Log.i(TAG, "upgrade failure, errorCode = "+ code
                    + ",errorMessage = "+ error);
        }

        @Override
        public void onFailureWithText(int otaType, String code,
                                      OTAErrorMessageBean messageBean) {
            handler.removeMessages(messageWhat);
            iTuyaOta.onDestroy();
            Log.i(TAG, "upgrade failure, errorCode = "+ code
                    + ",errorMessage = "+ messageBean.text);
        }

        @Override
        public void onProgress(int otaType, int progress) {
            Log.i(TAG, "upgrade progress = "+ progress);
        }

        @Override
        public void onTimeout(int otaType) {
            handler.removeMessages(messageWhat);
            iTuyaOta.onDestroy();
            Log.i(TAG, "upgrade timeout");
        }
    });
    handler.sendEmptyMessageDelayed(messageWhat, 60 * 1000);
    iTuyaOta.startOta();
}
```