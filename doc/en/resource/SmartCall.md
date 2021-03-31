# Smart call elevator

The smart call module provides the URL to enter the call page, which needs to be connected to [web container](./resource/WebContainer.md#Web container access) to jump

| Class name              | Description                                      |
| ----------------------- | ------------------------------------------------ |
| ITuyaCommunitySmartCall | Tuya Community Smart elevator Control Management |

### Smart elevator Control Page

**Interface Description**

   ```java
String getSmartCallElevatorUrl(String projectId, String spaceTreeId)
   ```

**Parameter Description**

| Parameters  | Description |
| ----------- | ----------- |
| projectId   | Project ID  |
| spaceTreeId | House ID    |

**Sample Code**

```java
String url = TuyaCommunitySDK.getTuyaCommunitySmartCallInstance().getSmartCallElevatorUrl( String projectId, String spaceTreeId)
//web container
Intent intent = new Intent(mContext, WebViewActivity.class);
intent.putExtra("Uri", url);
mContext.startActivity(intent);
```