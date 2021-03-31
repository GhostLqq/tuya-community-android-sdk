
## 获取功能点历史上报⽇志


获取功能点最近的历史上报⽇志，暂时只能获取近7天的，调用[万能接口](./CommonInterface.md)。


**接口说明**

| 接口名称 | 版本 | 描述 |
| ------------ | ------------- | ------------ |
| tuya.m.smart.operate.all.log | 1.0 | 获取功能点历史上报日志 |

**业务参数**

| 名称 | 类型 | 描述 | 参数是否必选 |
| ------------ | ------------- | ------------ | ------------ |
| devId | String | 设备 ID | true|
| dpIds | String | 要查询的 dp 点，按逗号分隔，例如 @“1，2” | true|
| offset | Integer | 分页偏移量 | true|
| limit | Integer | 分页大小，一页的数据条数 | true|
| startTime | String | 设备上报的时间，查询起始时间(单位为毫秒)，非必须参数 | false|
| endTime | String | 设备上报的时间，查询截止时间(单位为毫秒)，非必须参数 | false|
| sortType | String | 按时间排序方式：ASC、DESC，默认 DESC | false|

##### 请求示例
```
{
  "devId" : "05200020b4e62d16ce8b",
  "dpIds" : "1,2",
  "offset" : 0,
  "limit" : 10,
  "startTime" : "1542800401000",
  "endTime" : "1542886801000",
  "sortType" : "DESC"
}
```
##### 响应示例
```
{
  "result" : {
    "total" : 11055,
    "dps" : [ {
      "timeStamp" : 1542829972,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:52",
      "value" : "311"
    }, {
      "timeStamp" : 1542829970,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:50",
      "value" : "323"
    }, {
      "timeStamp" : 1542829966,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:46",
      "value" : "230"
    }, {
      "timeStamp" : 1542829964,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:44",
      "value" : "231"
    }, {
      "timeStamp" : 1542829960,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:40",
      "value" : "307"
    }, {
      "timeStamp" : 1542829958,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:38",
      "value" : "320"
    }, {
      "timeStamp" : 1542829954,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:34",
      "value" : "229"
    }, {
      "timeStamp" : 1542829950,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:30",
      "value" : "325"
    }, {
      "timeStamp" : 1542829948,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:28",
      "value" : "292"
    }, {
      "timeStamp" : 1542829942,
      "dpId" : 5,
      "timeStr" : "2018-11-21 20:52:22",
      "value" : "231"
    } ],
    "hasNext" : true
  },
  "t" : 1542959314632,
  "success" : true,
  "status" : "ok"
}
```

**代码示例**

```java
Map<String, Object> map = new HashMap<>();
map.put("devId", "your_devId");
map.put("dpIds", "your_device_dpId_str"); //"1,2"
//map.put("..","..");

TuyaCommunitySDK.getRequestInstance().requestWithApiName("tuya.m.smart.operate.all.log", "1.0", map, JSONObject.class, new ITuyaDataCallback<JSONObject>() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onError(String s, String s1) {

    }
});
```