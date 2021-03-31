# 物业
 物业模块提供了获取公告列表和返回物业详情URL,返回全部的公告列表是Web界面，需要先获取url，再调用[web容器](./resource/WebContainer.md#Web容器接入)的方法进行展示。
  
| 类名        | 说明              |
| ----------- | ----------------- |
| ITuyaCommunityProperty| 涂鸦社区物业管理|

## 物业公告列表页面

 **接口说明**

  ```java
String getAnnounceListWebUrl(String projectId)
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| projectId   |   项目标识            |                         |

**示例代码**

```java
String url = TuyaCommunitySDK.getTuyaCommunityPropertyInstance().getAnnounceListWebUrl(projectId);
//Web容器
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```

## 物业详情页面

**接口说明**

  ```java
String getAnnouceDetailWebUrl(String announcementId, String projectId)
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| announcementId   | 物业公告标识            |
| projectId      | 项目标识         |

**示例代码**

```java
String url = TuyaCommunitySDK.getTuyaCommunityPropertyInstance.getAnnouceDetailWebUrl(announcementId,projectId)
//Web容器
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```

## 获取最近三条物业公告列表

 **接口说明**

  ```java
void getRecentAnnounceList(String projectId,String spaceTreeId,callback)
  ```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| projectId   |   项目标识            |
| spaceTreeId      |房屋标识         |
| callback    | 回调                            |

**示例代码**

```java
TuyaCommunitySDK.getCommunityPropertyInstance().getRecentAnnounceList(projectId, spaceTreeId, new ITuyaCommunityResultCallback<ArrayList<CommAnnounceResponseBean>>() {
            @Override
            public void onSuccess(ArrayList<CommAnnounceResponseBean> commAnnounceResponseBeans) {
              

            }

            @Override
            public void onError(String s, String s1) {
                mView.showFailed(s1);
            }
        });
```