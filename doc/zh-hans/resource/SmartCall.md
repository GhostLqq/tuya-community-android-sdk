# 智能呼梯
智能呼梯模块提供进入呼梯页面的url,需要接入[web容器](./resource/WebContainer.md#Web容器接入)才能跳转

| 类名        | 说明              |
| ----------- | ----------------- |
| ITuyaCommunitySmartCall| 涂鸦社区智能梯控管理|

### 智能梯控页面
**接口说明**
 
  ```java
String getSmartCallElevatorUrl(String projectId, String spaceTreeId)	
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| projectId    | 项目标识                          |
| spaceTreeId |房屋标识|

**示例代码**

```java
String url = TuyaCommunitySDK.getTuyaCommunitySmartCallInstance().getSmartCallElevatorUrl( String projectId, String spaceTreeId)
//web容器
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```