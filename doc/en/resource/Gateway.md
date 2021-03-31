# ZigBee Gateway Control

The gateway itself is also a device. If you want to control the gateway itself, you can refer to the previous chapters.

The content of this section is to control the sub-devices under the gateway.

|  Class name  | Description                                                  |
| :----------: | ------------------------------------------------------------ |
| ITuyaGateway | The gateway class encapsulates the related operations of the Zigbee gateway, including controlling, querying sub-devices, and monitoring the status of sub-devices. |

## Initialize the gateway

**Interface Description**

Create a gateway object to control sub-devices.

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(String devId)
```

**Parameter Description**

| Parameters | Description                     |
| ---------- | ------------------------------- |
| devId      | Device id of the gateway device |

## Get the list of sub-devices

**Interface Description**

Request the server interface to obtain the sub-device list of the current gateway device

```java
void getSubDevList(ITuyaDataCallback<List<DeviceBean>> callback)
```

**Parameter Description**

The parameter of this interface is the asynchronous callback callback, and the callback content is as follows:

```java
public interface ITuyaDataCallback<List<DeviceBean>> {
  
    void onSuccess(List<DeviceBean> result);

    void onError(String errorCode, String errorMessage);
  
}
```

Will return to the device information list after success.

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).getSubDevList(new ITuyaDataCallback<List<DeviceBean>>() {
    @Override
    public void onSuccess(List<DeviceBean> list) {
    }

    @Override
    public void onError(String errorCode, String errorMessage) {

    }
});
```

## Register sub-device monitoring

**Interface Description**

Register the status of the child device to monitor, and the change of the status of the child device will be called back to the asynchronous monitor.

```java
void registerSubDevListener(ISubDevListener listener);
```

**Parameter Description**

The contents of the listener interface in the parameters are as follows:

```java
public interface ISubDevListener {

    /**
     * Notification when the dp point status of the device changes
     *
     * @param nodeId child device nodeId, the nodeId field in the child device DeviceBean
     * @param dpStr Function point data changed by sub-device
     */
    void onSubDevDpUpdate(String nodeId, String dpStr);

    /**
     * Notification when the device is removed
     */
    void onSubDevRemoved(String devId);

    /**
     * Notification when new equipment is added
     */
    void onSubDevAdded(String devId);

    /**
     * Notification when the child device is renamed
     */
    void onSubDevInfoUpdate(String devId);
    
    /**
     * Sub-device online or offline status change notification
     */
    void onSubDevStatusChanged(List<String> onlineNodeIds, List<String> offlineNodeIds);
}
```

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).registerSubDevListener(new ISubDevListener() {
    @Override
    public void onSubDevDpUpdate(String nodeId, String dpStr) {
        
    }

    @Override
    public void onSubDevRemoved(String devId) {

    }

    @Override
    public void onSubDevAdded(String devId) {

    }

    @Override
    public void onSubDevInfoUpdate(String devId) {

    }

    @Override
    public void onSubDevStatusChanged(List<String> onlines, List<String> offlines) {

    }
});
```

## Log off sub-device monitoring

**Interface Description**

When you do not need to monitor the sub-device, log off the sub-device monitoring.

```java
void unRegisterSubDevListener();
```

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).unRegisterSubDevListener();
```

## Individual control of sub-device

**Interface Description**

Send control commands to a single device

```java
void publishDps(String nodeId, String dps, IResultCallback callback)
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| nodeId     | Child device node id, obtained from the DeviceBean of the child device |
| dps        | List of function points to be controlled, in the format of json string |
| callback   | Send success or failure callback                             |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).publishDps(subDeviceBean.getNodeId(), "{\"101\": true}", new IResultCallback() {
    @Override
    public void onError(String code, String error) {

    }

    @Override
    public void onSuccess() {

    }
});
```

## Sub-device group control

**Interface Description**

Control all devices in the same group of this sub-device

```java
void multicastDps(String nodeId, String dps, IResultCallback callback)
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| nodeId     | Child device node id, obtained from the DeviceBean of the child device |
| dps        | List of function points to be controlled, in the format of json string |
| callback   | Send success or failure callback                             |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).multicastDps(subDeviceBean.getNodeId(), "{\"101\": true}", new IResultCallback() {
    @Override
    public void onError(String code, String error) {

    }
    
    @Override
    public void onSuccess() {
    
    }

});
```

## Sub-device broadcast control

**Interface Description**

Control all sub-devices under the gateway.

```
void broadcastDps(String dps, IResultCallback callback)
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| dps        | List of function points to be controlled, in the format of json string |
| callback   | Send success or failure callback                             |

**Sample Code**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).broadcastDps("{\"101\": true}", new IResultCallback() {
    @Override
    public void onError(String code, String error) {

    }
    
    @Override
    public void onSuccess() {
    
    }

});
```