# Property

 The property module provides access to the announcement list and return to the property details URL. Returning all the announcement lists is a web interface. You need to get the URL first, and then call the method of [web container](./resource/WebContainer.md#Web container access). Show.

| Class name             | Description                        |
| ---------------------- | ---------------------------------- |
| ITuyaCommunityProperty | Tuya Community Property Management |

## Property announcement list page

 **Interface Description**

  ```java
String getAnnounceListWebUrl(String projectId)
  ```

**Parameter Description**

| Parameters | Description |
| ---------- | ----------- |
| projectId  | Project ID  |

**Sample Code**

```java
String url = TuyaCommunitySDK.getTuyaCommunityPropertyInstance().getAnnounceListWebUrl(projectId);
//Web container
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```

## Property details page

**Interface Description**

  ```java
String getAnnouceDetailWebUrl(String announcementId, String projectId)
  ```

**Parameter Description**

| Parameters     | Description              |
| -------------- | ------------------------ |
| announcementId | Property announcement ID |
| projectId      | Project ID               |

**Sample Code**

```java
String url = TuyaCommunitySDK.getTuyaCommunityPropertyInstance.getAnnouceDetailWebUrl(announcementId,projectId)
//Web container
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```

## Get the list of the last three property announcements

 **Interface Description**

  ```java
void getRecentAnnounceList(String projectId,String spaceTreeId,callback)
  ```

**Parameter Description**

| Parameters  | Description |
| ----------- | ----------- |
| projectId   | Project ID  |
| spaceTreeId | House ID    |
| callback    | callback    |

**Sample Code**

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