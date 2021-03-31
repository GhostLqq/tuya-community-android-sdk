## Get the history report of function points


To obtain the latest historical report log of the function point, only the last 7 days can be obtained for the time being, call [Universal Interface](./CommonInterface.md).


**Interface Description**

| Interface name               | Version | Description                           |
| ---------------------------- | ------- | ------------------------------------- |
| tuya.m.smart.operate.all.log | 1.0     | Get function point history report log |

**Business parameters**

| Name      | Type    | Description                                                  | Whether the parameter is required |
| --------- | ------- | ------------------------------------------------------------ | --------------------------------- |
| devId     | String  | Device ID                                                    | true                              |
| dpIds     | String  | The dp points to be queried, separated by commas, for example @“1，2” | true                              |
| offset    | Integer | Page offset                                                  | true                              |
| limit     | Integer | Page size, the number of data on a page                      | true                              |
| startTime | String  | The time reported by the device, the query start time (in milliseconds), optional parameters | false                             |
| endTime   | String  | The time reported by the device, the query deadline (in milliseconds), optional parameters | false                             |
| sortType  | String  | Sort by time: ASC, DESC, default DESC                        | false                             |

##### Request example

```
{
  "devId": "05200020b4e62d16ce8b",
  "dpIds": "1,2",
  "offset": 0,
  "limit": 10,
  "startTime": "1542800401000",
  "endTime": "1542886801000",
  "sortType": "DESC"
}
```

##### Response example

```
{
  "result": {
    "total": 11055,
    "dps": [{
      "timeStamp": 1542829972,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:52",
      "value": "311"
    }, {
      "timeStamp": 1542829970,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:50",
      "value": "323"
    }, {
      "timeStamp": 1542829966,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:46",
      "value": "230"
    }, {
      "timeStamp": 1542829964,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:44",
      "value": "231"
    }, {
      "timeStamp": 1542829960,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:40",
      "value": "307"
    }, {
      "timeStamp": 1542829958,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:38",
      "value": "320"
    }, {
      "timeStamp": 1542829954,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:34",
      "value": "229"
    }, {
      "timeStamp": 1542829950,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:30",
      "value": "325"
    }, {
      "timeStamp": 1542829948,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:28",
      "value": "292"
    }, {
      "timeStamp": 1542829942,
      "dpId": 5,
      "timeStr": "2018-11-21 20:52:22",
      "value": "231"
    } ],
    "hasNext": true
  },
  "t": 1542959314632,
  "success": true,
  "status": "ok"
}
```

**Code Example**

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