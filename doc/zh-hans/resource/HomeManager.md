# 房屋管理

房屋管理主要提供房屋列表和房屋内详情数据



| 参数        | 说明                            |
| ----------- | ------------------------------- |
| ITuyaCommunityHomeManager    | 房屋列表管理类         | 
| ITuyaCommunityHome    | 房屋对象管理类                            |


### 获取房屋列表接口

**接口说明**

```java
void getCommunityHomeList(ITuyaCommunityResultCallback<List<CommunityHomeBean>> callback);
```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| callback    | 回调                            |

**示例代码**

```java
TuyaCommunitySDK.getHomeInstance().getCommunityHomeList(new ITuyaCommunityResultCallback<List<CommunityHomeBean>>() {
            @Override
            public void onSuccess(List<CommunityHomeBean> result) {
                mView.showCommunityHomeBean(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
```
### 获取房屋详情接口

**接口说明**

```java
void getCommunityHomeDetail(long homeId, ITuyaCommunityResultCallback<CommunityHomeBean> callback);
```

**参数说明**

| 参数        | 说明                            |
| ----------- | ------------------------------- |
| homeId      | 智家房屋 id。请参考房屋相关章节获取 |
| callback    | 回调                            |

**示例代码**

```java
TuyaCommunitySDK.getHomeInstance().getCommunityHomeDetail(homeId, new ITuyaCommunityResultCallback<CommunityHomeBean>() {
            @Override
            public void onSuccess(CommunityHomeBean result) {
                mView.showDetail(result);
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
```
