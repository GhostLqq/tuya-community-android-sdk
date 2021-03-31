# Smart scene

Smart scenes are divided into "one-key execution scenes" and "automation scenes", hereinafter referred to as "one-key execution" and "automation" respectively.

One-click execution is a user-added action, which is triggered manually; automation is a condition set by the user, and the set action is automatically executed when the condition is triggered.

Tuya Cloud allows users to set weather or equipment conditions according to actual life scenarios, and when the conditions are met, let one or more devices perform corresponding tasks.

| Scene Management | Description |
| ------------------------------------------------- - | ----------------------------------------------- ------------- |
| TuyaCommunitySDK.newCommunitySceneInstance(sceneId) | Provides the execution of a single scene, which needs to be initialized with the scene id. The scene id refers to the `id` field of the `CommunitySceneBean`, which can be obtained from the [Get Scene List Interface](#GetSceneList) Obtained from the returned result. |
| TuyaCommunitySDK.getCommunitySceneManager() | Currently, it mainly provides scene list data acquisition. |



Before using the smart scene-related interfaces, you need to understand the two concepts of scene conditions and scene tasks.

## Scene conditions

The scene conditions correspond to the `CommunitySceneCondition` class, and Tuya Cloud supports the following condition types:

-Meteorological conditions: including temperature, humidity, weather, PM2.5, air quality, sunset and sunrise, users can select the current city when selecting the weather conditions.
-Equipment condition: The user can pre-select the functional state of a device. When the device reaches this state, the task in the current scene will be triggered, but the same device cannot be used as a condition and task at the same time to avoid operation conflicts.
-Timing condition: It means that the scheduled task can be executed according to the specified time.

## Scenario task

A scene task is to let one or more devices perform a certain operation when the scene meets the set weather or equipment conditions, corresponding to the `CommunitySceneTask` class. Or turn off or turn on an automation.

The main attributes of CommunitySceneTask are defined as follows

|Field|Type|Description|
| ------ | ------ | ----------- |
| id | Sting | Action id
| actionExecutor | String | Action type. The enumeration is as follows: <br>ruleTrigger: trigger scenario<br>ruleEnable: enable scenario<br>ruleDisable: disable scenario<br>appPushTrigger: push message<br>mobileVoiceSend: phone service<br>smsSend: short message service<br>deviceGroupDpIssue ：Execute group<br>irIssue: execute infrared device<br>dpIssue: execute ordinary device<br>delay: delay<br>irIssueVii: execute infrared device (execution parameter is real infrared control code)<br>toggle: execute Toggle switch action<br>dpStep: execute step action
| entityId | String | Device id
| entityName | String | Device name
| actionDisplayNew | Map&lt;String, List&lt;String&gt;&gt; | Action display information
| executorProperty | Map&lt;String, Object&gt; | Action execution information
| extraProperty | Map&lt;String, Object&gt; | Action extra information

## Intelligent scene management

### <span id="GetSceneList">Scene list function</span>


**Interface Description**


Get the scene list data of simple data. The scenario and automation are returned together, and the conditions and automation are distinguished by whether the conditions field is empty.

```java
void getSceneList(long homeId,ITuyaCommunityResultCallback<List<CommunitySceneBean>> callback)
```

**Parameter Description**

| Parameters | Description     |
| ---------- | --------------- |
| homeId     | Zhijia house id |
| callback   | callback        |


Among them, the main attributes of `CommunitySceneBean` are defined as follows

|Field|Type|Description|
| ------ | ------ | ----------- |
| id |Sting| Scene id
| name |String| Scene name
| conditions | List&lt;SceneCondition&gt; | List of scene conditions
| actions | List&lt;SceneTask&gt; | Scene task list
| matchType | int | The type that satisfies the condition, satisfies any condition as 1, and satisfies all conditions as 2
| enable | boolean | Whether to enable automation



**Sample Code**

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

### <span id="GetSceneDetail">Get scene detail data</span>



**Interface Description**


Get detailed data of a single scene, including details of conditions and actions

```java
void getSceneDetail(long homeId,String sceneId, ITuyaCommunityResultCallback<CommunitySceneBean> callback)
```

**Parameter Description**

| Parameters | Description     |
| ---------- | --------------- |
| homeId     | Zhijia house id |
| sceneId    | scene id        |
| callback   | callback        |




**Sample Code**

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

## Single scene management

### Execute one-click execution

**Interface Description**

Note: This method only sends instructions to the cloud for execution scenarios. Whether the specific device is executed successfully or not, you need to monitor the dp point changes of the device through TuyaCommunitySDK.getCommunityDeviceManageInstance().newDeviceInstance(devId).registerDevListener().

```java
void executeScene(ITuyaCommunityCallback callback)
```

**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| callback   | callback    |


**Sample Code**

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

### Turn on and off the automation scene (only the automation scene can be turned on and disabled)

**Interface Description**

Used to turn on or off the automation scene

```java
void enableScene(String sceneId, final ITuyaCommunityCallback callback);

void disableScene(String sceneId, final ITuyaCommunityCallback callback);
```


**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| sceneId    | scene id    |
| callback   | callback    |

**Sample Code**

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
