# 智能场景

智能场景分为「一键执行场景」和「自动化场景」，下文分别简称为「一键执行」和「自动化」。

一键执行是用户添加动作，手动触发；自动化是由用户设定条件，当条件触发后自动执行设定的动作。

涂鸦云支持用户根据实际生活场景，通过设置气象或设备条件，当条件满足时，让一个或多个设备执行相应的任务。

| 场景管理 | 说明 |
| -------------- | ---------- |
|  TuyaCommunitySDK.newCommunitySceneInstance(sceneId)   |   提供了单个场景的执行操作，需要使用场景 id  进行初始化，场景 id 指的是 `CommunitySceneBean` 的 `id` 字段，可以从[获取场景列表接口](#GetSceneList)的返回结果中获取。 |
|TuyaCommunitySDK.getCommunitySceneManager()| 目前主要提供了场景列表数据获取。|



在使用智能场景相关的接口之前，需要首先了解场景条件和场景任务这两个概念。
## 场景条件

场景条件对应 `CommunitySceneCondition` 类，涂鸦云支持以下条件类型：

- 气象条件：包括温度、湿度、天气、PM2.5、空气质量、日落日出，用户选择气象条件时，可以选择当前城市。
- 设备条件：指用户可预先选择一个设备的功能状态，当该设备达到该状态时，会触发当前场景里的任务，但同一设备不能同时作为条件和任务，避免操作冲突。
- 定时条件：指可以按照指定的时间去执行预定的任务。

## 场景任务

场景任务是指当该场景满足已经设定的气象或设备条件时，让一个或多个设备执行某种操作，对应 `CommunitySceneTask` 类。或者关闭、开启一个自动化。

CommunitySceneTask的主要属性定义如下

|字段|类型| 描述 |
| ------ | ------ | ----------- |
| id | Sting | 动作 id 
| actionExecutor | String | 动作类型。枚举如下：<br>ruleTrigger：触发场景<br>ruleEnable：启用场景<br>ruleDisable：禁用场景<br>appPushTrigger：推送消息<br>mobileVoiceSend：电话服务<br>smsSend：短信服务<br>deviceGroupDpIssue：执行群组<br>irIssue：执行红外设备<br>dpIssue：执行普通设备<br>delay：延时<br>irIssueVii：执行红外设备（执行参数为真实红外控制码）<br>toggle：执行切换开关动作<br>dpStep：执行步进动作
| entityId | String | 设备 id
| entityName | String | 设备名称
| actionDisplayNew | Map&lt;String, List&lt;String&gt;&gt; | 动作展示信息
| executorProperty | Map&lt;String, Object&gt; | 动作执行信息
| extraProperty | Map&lt;String, Object&gt; | 动作额外信息

## 智能场景管理
### <span id="GetSceneList">场景列表功能</span>


**接口说明**


获取简单数据的场景列表数据。场景和自动化一起返回，通过条件 conditions 字段是否为空来区分场景和自动化。

```java
void getSceneList(long homeId,ITuyaCommunityResultCallback<List<CommunitySceneBean>> callback)
```
**参数说明**

| 参数 | 说明 |
| ----------- | ----------------------------------------------- |
| homeId | 智家房屋 id                           |                                   |
| callback    | 回调 |


其中，`CommunitySceneBean`的主要属性定义如下

|字段|类型| 描述 |
| ------ | ------ | ----------- |
| id |Sting| 场景 id 
| name |String| 场景名称 
| conditions | List&lt;SceneCondition&gt; | 场景条件列表
| actions | List&lt;SceneTask&gt; | 场景任务列表
| matchType | int | 满足条件的类型，满足任意条件为1，满足所有条件为2
| enable | boolean | 自动化是否启用



**示例代码**

```java
TuyaCommunitySDK.getCommunitySceneManager().getSceneList(long homeId, ITuyaCommunityResultCallback<List<CommunitySceneBean>>() {
    @Override
    public void onSuccess(List<CommunitySceneBean> result) {
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
```

### <span id="GetSceneDetail">获取场景详情数据</span>



**接口说明**


获取单个场景的详细数据，包含条件和动作的详细内容

```java
void getSceneDetail(long homeId,String sceneId, ITuyaCommunityResultCallback<CommunitySceneBean> callback)
```
**参数说明**

| 参数 | 说明 |
| ----------- | ----------------------------------------------- |
| homeId | 智家房屋 id                           |      
| sceneId | 场景id                           |                              
| callback    | 回调 |




**示例代码**

```java

TuyaCommunitySDK.getCommunitySceneManager().getSceneDetail(homeId, sceneId, new ITuyaCommunityResultCallback<CommunitySceneBean>() {
                @Override
                public void onSuccess(CommunitySceneBean communitySceneBean) {

                }

                @Override
                public void onError(String s, String s1) {

                }
            });
```            
 
## 单场景管理

### 执行一键执行

**接口说明**

注：这个方法只管发送指令到云端执行场景，具体设备执行成功与否，需要通过TuyaCommunitySDK.getCommunityDeviceManageInstance().newDeviceInstance(devId).registerDevListener() 监听设备的 dp 点变化。

```java
void executeScene(ITuyaCommunityCallback callback)
```
**参数说明**

|参数|说明|
| ------ | ----- |
| callback | 回调 |


**示例代码**

```java
String sceneId = sceneBean.getId();  
TuyaCommunitySDK.newCommunitySceneInstance(sceneId).executeScene(new ITuyaCommunityCallback() {
    @Override
    public void onSuccess() {
        Log.d(TAG, "Excute Scene Success");
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
```
### 开启关闭自动化场景（只有自动化场景才可以开启和失效）

**接口说明**

用于开启或关闭自动化场景

```java
void enableScene(String sceneId, final ITuyaCommunityCallback callback);

void disableScene(String sceneId, final ITuyaCommunityCallback callback);
```


**参数说明**

|参数|说明|
| ------ | ----- |
| sceneId |场景 id |
| callback | 回调 |

**示例代码**

```java
String sceneId = sceneBean.getId();  
TuyaCommunitySDK.newCommunitySceneInstance(sceneId).enableScene(sceneId,new 
ITuyaCommunityCallback() {
    @Override
    public void onSuccess() {
        Log.d(TAG, "enable Scene Success");
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});

TuyaCommunitySDK.newCommunitySceneInstance(sceneId).disableScene(sceneId,new 
ITuyaCommunityCallback() {
    @Override
    public void onSuccess() {
        Log.d(TAG, "disable Scene Success");
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
```
