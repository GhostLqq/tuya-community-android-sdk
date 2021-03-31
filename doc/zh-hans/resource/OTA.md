# 固件升级

固件升级主要用于增强设备能力以及修复 bug；

固件升级分为“设备升级”和“MCU 升级”两种；

## 初始化固件信息

**接口说明**

Wi-Fi，Zigbee 网关，摄像头等设备：

```java
ITuyaOta newOTAInstance(String devId);
```

**参数说明**

| 参数  | 说明    |
| ----- | ------- |
| devId | 设备 id |

**实例代码**

```java
ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance("devId");
```

## 初始化子设备固件信息

NOTE：从SDK 2.9.1开始支持；

**接口说明**

子设备：

```java
ITuyaOta newOTAInstance(String meshId, String devId, String nodeId);
```

**参数说明**

| 参数   | 说明                                                 |
| ------ | ---------------------------------------------------- |
| meshId | Zigbee 网关 id                                       |
| devId  | Zigbee 子设备 id                                     |
| nodeId | Zigbee 子设备 mac 地址（从子设备的 DeviceBean 获取） |

**实例代码**

```java
ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance("meshId", "devId", "nodeId");
```

## 获取固件升级信息

**接口说明**

```java
iTuyaOta.getOtaInfo(new IGetOtaInfoCallback({
	@Override
	void onSuccess(List<UpgradeInfoBean> list) {
	
	}
	@Override
	void onFailure(String code, String error) {
	
	}
});
```

**参数说明**

| 字段            | 类型   | 描述                                                      |
| --------------- | ------ | --------------------------------------------------------- |
| upgradeStatus   | int    | 升级状态，0 :无新版本 1 :有新版本 2 :在升级中             |
| version         | String | 最新版本                                                  |
| currentVersion  | String | 当前版本                                                  |
| timeout         | int    | 超时时间，单位：秒                                        |
| upgradeType     | int    | 0 :app 提醒升级  2: app 强制升级  3: 检测升级             |
| type            | int    | 0: Wi-Fi 设备 1:蓝牙设备 2:GPRS 设备 3:zigbee 设备 9: MCU |
| typeDesc        | String | 模块描述                                                  |
| lastUpgradeTime | long   | 上次升级时间，单位：毫秒                                  |

## 添加固件升级监听

**接口说明**

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

**参数说明**

| 参数    | 描述                                                |
| ------- | --------------------------------------------------- |
| otaType | 升级的设备类型，同 UpgradeInfoBean 中的 type 字段； |

## 开始升级

**接口说明**

调用该方法开始升级,调用后注册的 OTA 监听会把升级状态返回回来，以便开发者构建 UI；

```java
iTuyaOta.startOta();
```

## 销毁

**接口说明**

离开升级页面后要销毁，回收内存；

```java
iTuyaOta.onDestory();
```

## 完整示例代码

```java
public void start() {
    String devId = "your_devId";
    final ITuyaOta iTuyaOta = TuyaCommunitySDK.getCommunityDeviceManageInstance().newOTAInstance(devId);
    iTuyaOta.getOtaInfo(new IGetOtaInfoCallback() {
        @Override
        public void onSuccess(List<UpgradeInfoBean> upgradeInfoBeans) {
            int type = getUpgradeType(upgradeInfoBeans);
            if (type > 0) {
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
    for (UpgradeInfoBean bean : upgradeInfoBeans) {
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
            Log.i(TAG, "upgrade failure, errorCode = " + code
                    + ",errorMessage = " + error);
        }

        @Override
        public void onFailureWithText(int otaType, String code,
                                      OTAErrorMessageBean messageBean) {
            handler.removeMessages(messageWhat);
            iTuyaOta.onDestroy();
            Log.i(TAG, "upgrade failure, errorCode = " + code
                    + ",errorMessage = " + messageBean.text);
        }

        @Override
        public void onProgress(int otaType, int progress) {
            Log.i(TAG, "upgrade progress = " + progress);
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

