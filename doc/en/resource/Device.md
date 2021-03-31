# Smart device

Tuya community provides a wealth of interfaces for developers to achieve device information acquisition and management capabilities (removal, etc.). The device-related return data is notified to the receiver in the form of asynchronous messages. The ITuyaDevice class provides the device status notification capability. By registering a callback function, developers can easily obtain notifications of device data acceptance, device removal, device online and offline, and mobile phone network changes. It also provides an interface for issuing control commands and upgrading device firmware


| Class name           | Description                    |
| -------------------- | ------------------------------ |
| ITuyaCommunityDevice | Device Object Management Class |
| ITuyaDevice          | Device control operation class |
| DeviceBean           | Device Data Model Class        |

**DeviceBean Data Model**


| Field         | Type    | Description                                                  |
| :------------ | :------ | ------------------------------------------------------------ |
| devId         | String  | Device unique identification id                              |
| name          | String  | Device name                                                  |
| iconUrl       | String  | Icon address                                                 |
| getIsOnline   | Boolean | Is the device online (LAN or cloud online)                   |
| schema        | String  | Type information of device control data points               |
| productId     | String  | Product ID, the same product ID, the Schema information is consistent |
| supportGroup  | Boolean | Whether the device supports groups, if not, please go to the open platform to enable this function |
| time          | Long    | Device activation time                                       |
| pv            | String  | Gateway protocol version                                     |
| bv            | String  | Gateway general firmware version                             |
| schemaMap     | Map     | Schema cache data                                            |
| dps           | Map     | Current data information of the device. key is dpId and value is value |
| isShare       | boolean | Is it a sharing device                                       |
| virtual       | boolean | Is it a virtual device                                       |
| lon, lat      | String  | Used to mark the latitude and longitude information, the user needs to call TuyaSdk.setLatAndLong to set the latitude and longitude information before using the SDK |
| isLocalOnline | boolean | Device LAN online status                                     |
| nodeId        | String  | Used for gateways and sub-devices. It is an attribute of sub-devices and identifies its short address ID. The nodeIds under a gateway are all unique |
| timezoneId    | String  | Device time zone                                             |
| category      | String  | Device Type                                                  |
| meshId        | String  | Used for devices of gateway and sub-device types. It is an attribute of sub-devices and identifies its gateway ID |
| isZigBeeWifi  | boolean | Is it a ZigBee gateway device                                |
| hasZigBee     | boolean | hasZigBee                                                    |


**Precautions**

If you need to use latitude and longitude for device control, you need to call the method to set the latitude and longitude before network distribution:

```java
TuyaSdk.setLatAndLong(String latitude, String longitude)
```

## Device initialization

### Initialize house data


The device control must first initialize the data, call the following method to obtain the device information under the house, and it is sufficient to initialize once every time the APP is alive. In addition, the house needs to be initialized:

```java
TuyaCommunitySDK.newCommunityHomeInstance(homeId).getCommunityHomeDetail(new ITuyaCommunityResultCallback<CommunityHomeBean>()() {
    @Override
    public void onSuccess(CommunityHomeBean homeBean) {
    To
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }
});
```

The onSuccess method of this interface will return `CommunityHomeBean`, and then call `getDeviceList` of `CommunityHomeBean` to get the device list:

```java
List<DeviceBean> deviceList = homeBean.getDeviceList();
```

### Initialize device control

**Interface Description**

Initialize the device control class according to the device id

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newDeviceInstance(String devId);
```

**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| devId      | device id   |

**Sample Code**

```java
ITuyaDevice mDevice = TuyaCommunitySDK.getCommunityDeviceManageInstance().newDeviceInstance(deviceBean.getDevId());
```

## Device monitoring

### Register device monitoring

**Interface Description**

ITuyaDevice provides monitoring of device-related information (dp data, device name, device online status, and device removal), which will be synchronized here in real time.

```java
ITuyaDevice.registerDevListener(IDevListener listener)
```

**Parameter Description**

| Parameters | Description              |
| ---------- | ------------------------ |
| listener   | Device status monitoring |

The interface of `IDevListener` is as follows:

```java
public interface IDevListener {

    /**
     * dp data update
     *
     * @param devId device id
     * @param dpStr The function point of the device change, it is a json string, the data format: {"101": true}
     */
    void onDpUpdate(String devId, String dpStr);

    /**
     * Device removal callback
     *
     * @param devId device id
     */
    void onRemoved(String devId);

    /**
     * Device offline callback
     *
     * @param devId device id
     * @param online is online, online is true
     */
    void onStatusChanged(String devId, boolean online);

    /**
     * Callback when the network status changes
     *
     * @param devId device id
     * @param status Whether the network status is available, can be true
     */
    void onNetworkStatusChanged(String devId, boolean status);

    /**
     * Device information update callback
     *
     * @param devId device id
     */
    void onDevInfoUpdate(String devId);

}
```

Among them, the description of equipment function points can be found in the section "[Device Function Point Description](#Device Function Point Description)" in the document.

**Sample Code**


```java
mDevice.registerDevListener(new IDevListener() {
    @Override
    public void onDpUpdate(String devId, String dpStr) {

    }
    @Override
    public void onRemoved(String devId) {

    }
    @Override
    public void onStatusChanged(String devId, boolean online) {

    }
    @Override
    public void onNetworkStatusChanged(String devId, boolean status) {

    }
    @Override
    public void onDevInfoUpdate(String devId) {

    }
});
```

### Cancel device monitoring

When you do not need to monitor the device, log off the device monitor.

**Interface Description**

```java
ITuyaDevice.unRegisterDevListener()
```

**Sample Code**


```java
mDevice.unRegisterDevListener();
```

## Device control

The device control interface function is to send function points to the device to change the device state or function.

**Interface Description**

Device control supports three channel control, local area network control, cloud control, and automatic mode (if the local area network is online, use the local area network control first, and the local area network is not online, use the cloud control)

* LAN mode

  ```java
  ITuyaDevice.publishDps(dps, TYDevicePublishModeEnum.TYDevicePublishModeLocal, callback);
  ```

* Cloud control

  ```java
  ITuyaDevice.publishDps(dps, TYDevicePublishModeEnum.TYDevicePublishModeInternet, callback);
  ```

* Automatic control

  ```java
  ITuyaDevice.publishDps(dps, TYDevicePublishModeEnum.TYDevicePublishModeAuto, callback);
  ```

  or

  ```java
  ITuyaDevice.publishDps(dps, callback);
  ```

Recommend to use `ITuyaDevice.publishDps(dps, callback)`

**Parameter Description**

| Parameters      | Description                                                  |
| --------------- | ------------------------------------------------------------ |
| dps             | data points, device function points, the format is json string |
| publishModeEnum | Device control mode                                          |
| callback        | The callback of whether the control command is sent successfully or not |

**Sample Code**

Assuming that the function point of the device for turning on the light is 101, then the control code for turning on the light is as follows:

```java
mDevice.publishDps("{\"101\": true}", new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Toast.makeText(mContext, "Failed to turn on the light", Toast.LENGTH_SHORT).show();
    }
To
    @Override
    public void onSuccess() {
        Toast.makeText(mContext, "Turn on the light successfully", Toast.LENGTH_SHORT).show();
    }
});
```


> **[warning] Precautions**
>
> * Successful command issuance does not mean that the device is actually successfully operated, it just means that the command is sent successfully. If the operation is successful, the dp data information will be reported and returned through the `IDevListener onDpUpdate` interface.
>
> * The command string is converted into a json string in the data format of `Map<String,Object>` (key-value pair of dpId and dpValue).
>
> * The command command can send multiple dp data at one time.
>
> 

## Device function point description

The dps attribute of the DeviceBean class defines the state of the device, which is called data point (dp point) or function point. Each `key` in the `dps` dictionary corresponds to the `dpId` of a function point, and `dpValue` is the value of the function point. For the definition of respective product function points, please refer to the product functions of [Tuya Developer Platform](https://iot.tuya.com/index).
For function points, please refer to [Function Point Related Concepts](https://docs.tuya.com/zh/iot/configure-in-platform/function-definition/custom-functions#%E5%8A%9F%E8%83% BD%E7%82%B9%E7%9B%B8%E5%85%B3%E6%A6%82%E5%BF%B5)

**Command format**

Send control commands in the following format:
{"(dpId)":"(dpValue)"}

**Function point example**

The development platform can see an interface like a product
![Function Points](./images/ios_dp_sample.jpeg)

According to the function point definition of the product in the background, the sample code is as follows:

```java
//Example of boolean function point setting dpId to 101 Function: switch on
dps = {"101": true};

//Example for setting dpId to 102 string type function point Function: Set RGB color to ff5500
dps = {"102": "ff5500"};

//Example of setting dpId to 103 enumerated function points Function: Set the gear to 2 gears
dps = {"103": "2"};

//Example of numerical function points with dpId set to 104 Use: set temperature to 20°
dps = {"104": 20};

//Set the transparent transmission type (byte array) function point example with dpId as 105 Function: transparent transmission infrared data is 1122
dps = {"105": "1122"};

//Multiple functions are combined and sent
dps = {"101": true, "102": "ff5500"};

mDevice.publishDps(dps, new IResultCallback() {
        @Override
        public void onError(String code, String error) {
        //Error code 11001
        //There are several situations:
        //1. The type is incorrect, for example, string format is sent as boolean data.
        //2. The read-only type dp data cannot be sent. Refer to SchemaBean getMode "ro" is a read-only type.
        //3. The data format sent in raw format is not a hexadecimal string.
        }
        @Override
        public void onSuccess() {
        }
    });
```

> **[warning] Precautions**
>
> * The sending of control commands requires special attention to the data type.
>
> For example, if the data type of the function point is value (value), the control command should be `{"104": 25}` instead of `{"104": "25"}`
>
> * The byte array transmitted by the transparent transmission type is in hexadecimal string format and must be an even number
> For example, the correct format is: `{"105": "0110"}` instead of `{"105": "110"}`


## Device information query

**Interface Description**

Query a single dp data.

This interface is not a synchronous interface, and the queried data will be called back through the `IDevListener.onDpUpdate()` interface.

```java
mDevice.getDp(String dpId, IResultCallback callback);
```

**Sample Code**

```java
mDevice.getDp(dpId, new IResultCallback() {
    @Override
    public void onError(String code, String error) {

    }

    @Override
    public void onSuccess() {

    }
});
```


> **[warning] Precautions**
>
> This interface is mainly for those dp points where data is not reported actively, such as countdown information query. Regular query dp data value can be obtained through getDps() in DeviceBean.

## Modify device name

**Interface Description**

Rename the device and support multi-device synchronization.

```java
//Rename
mDevice.renameDevice(String name,IResultCallback callback);
```

**Sample Code**

```java
mDevice.renameDevice("device name", new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        //Failed to rename
    }
    @Override
    public void onSuccess() {
        //Renamed successfully
    }
});
```

After success, `IDevListener.onDevInfoUpdate()` will be notified.

Call the following method to get the latest data, and then refresh the device information.

```java
TuyaCommunitySDK.getCommunityDataManager().getDeviceBean(String devId);
```

## Remove device

**Interface Description**

Used to remove a device from the user device list

```java
void removeDevice(IResultCallback callback);
```

**Sample Code**

```java
mDevice.removeDevice(new IResultCallback() {
    @Override
    public void onError(String errorCode, String errorMsg) {
    }

    @Override
    public void onSuccess() {
    }
});
```

## Reset

**Interface Description**

It is used to reset the device and restore it to the factory state. After the device is restored to the factory settings, it will re-enter the network ready state (quick connection mode), and the related data of the device will be cleared.

```java
void resetFactory(IResultCallback callback);
```

**Sample Code**

```java
mDevice.resetFactory(new IResultCallback() {
    @Override
    public void onError(String errorCode, String errorMsg) {
    }

    @Override
    public void onSuccess() {
    }
});
```

## Query Wi-Fi signal strength

Get the current Wi-Fi signal strength of the device

**Interface Description**

```java
void requestWifiSignal(WifiSignalListener listener);
```

**Sample Code**

```java
mDevice.requestWifiSignal(new WifiSignalListener() {
     
     @Override
     public void onSignalValueFind(String signal) {
      
     }
     
     @Override
     public void onError(String errorCode, String errorMsg) {

     }
 });;
```

## Recycling equipment resources

**Interface Description**

When the application or activity is closed, you can call this interface to reclaim the resources occupied by the device.

```java
void onDestroy()
```

**Sample Code**

```java
mDevice.onDestroy();
```