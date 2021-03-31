# ZigBee 网关控制

网关本身也是个设备，如果要控制网关本身，可以参考之前的章节。

本节内容是控制网关下的子设备。

|   类名    |       说明       |
| :-------: | :-------------- |
| ITuyaGateway | 网关类封装了 Zigbee 网关的相关操作，包括控制、查询子设备，监听子设备状态等。 |

## 初始化网关

**接口说明**

创建网关对象，用来控制子设备。

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(String devId)
```

**参数说明**

| 参数  | 说明              |
| ----- | ----------------- |
| devId | 网关设备的设备 id |

## 获取子设备列表

**接口说明**

请求服务端接口，获取当前网关设备的子设备列表

```java
void getSubDevList(ITuyaDataCallback<List<DeviceBean>> callback)
```

**参数说明**

该接口的参数为异步回调的 callback，callback 内容如下：

```java
public interface ITuyaDataCallback<List<DeviceBean>> {
  
    void onSuccess(List<DeviceBean> result);

    void onError(String errorCode, String errorMessage);
  
}
```

将在成功后返回设备信息列表。

**示例代码**

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

## 注册子设备监听

**接口说明**

注册子设备状态监听，子设备状态的变更将回调到异步监听器中。

```java
void registerSubDevListener(ISubDevListener listener);
```
**参数说明**

参数中的监听器接口内容如下：

```java

public interface ISubDevListener {

    /**
     * 当设备dp点状态变更时的通知
     *
     * @param nodeId 子设备 nodeId，子设备 DeviceBean 中的 nodeId 字段
     * @param dpStr 子设备变更的功能点数据
     */
    void onSubDevDpUpdate(String nodeId, String dpStr);

    /**
     * 设备移除时的通知
     */
    void onSubDevRemoved(String devId);

    /**
     * 新增设备时的通知
     */
    void onSubDevAdded(String devId);

    /**
     * 子设备重命名时的通知
     */
    void onSubDevInfoUpdate(String devId);
    
    /**
     * 子设备在线 or 离线状态变更通知
     */
    void onSubDevStatusChanged(List<String> onlineNodeIds, List<String> offlineNodeIds);
}
```

**示例代码**

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

## 注销子设备监听

**接口说明**

不需要监听子设备时，注销子设备监听。

```java
void unRegisterSubDevListener();
```

**示例代码**

```java
TuyaCommunitySDK.getCommunityDeviceManageInstance().newGatewayInstance(devId).unRegisterSubDevListener();
```

## 子设备单个控制

**接口说明**

向单个设备发送控制指令

```java
void publishDps(String nodeId, String dps, IResultCallback callback)
```

**参数说明**

| 参数     | 说明                                        |
| -------- | ------------------------------------------- |
| nodeId   | 子设备节点 id，从子设备的 DeviceBean 中获取 |
| dps      | 要控制的功能点列表，格式为 json 字符串      |
| callback | 发送成功或失败的回调                        |

**示例代码**

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

## 子设备群组控制

**接口说明**

控制该子设备同一个群组下的所有设备

```java
void multicastDps(String nodeId, String dps, IResultCallback callback)
```

**参数说明**

| 参数     | 说明                                 |
| -------- | ------------------------------------ |
| nodeId  | 子设备节点 id，从子设备的 DeviceBean 中获取                          |
| dps      | 要控制的功能点列表，格式为 json 字符串 |
| callback | 发送成功或失败的回调                 |

**示例代码**

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

## 子设备广播控制

**接口说明**

控制网关下所有子设备。

```
void broadcastDps(String dps, IResultCallback callback)
```

**参数说明**

| 参数     | 说明                                   |
| -------- | -------------------------------------- |
| dps      | 要控制的功能点列表，格式为 json 字符串 |
| callback | 发送成功或失败的回调                   |

**示例代码**

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

