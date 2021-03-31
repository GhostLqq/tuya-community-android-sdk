# Housing management

House management mainly provides house listings and house details data

| Parameters                | Description               |
| ------------------------- | ------------------------- |
| ITuyaCommunityHomeManager | Housing List Management   |
| ITuyaCommunityHome        | Housing Object Management |


### Get housing list interface

**Interface Description**

```java
void getCommunityHomeList(ITuyaCommunityResultCallback<List<CommunityHomeBean>> callback);
```

**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| callback   | callback    |

**Sample Code**

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

### Get housing details interface

**Interface Description**

```java
void getCommunityHomeDetail(long homeId, ITuyaCommunityResultCallback<CommunityHomeBean> callback);
```

**Parameter Description**

| Parameters | Description                                                  |
| ---------- | ------------------------------------------------------------ |
| homeId     | Zhijia house id. Please refer to the relevant chapters of the house to obtain |
| callback   | callback                                                     |

**Sample Code**

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
