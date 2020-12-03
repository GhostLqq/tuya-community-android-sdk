package com.tuya.community.business.sdk.demo.bean;

/**
 * @author chenbj
 */
public class Mqtt308ParamsBean {
    private String type;
    private EventBean data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventBean getData() {
        return data;
    }

    public void setData(EventBean data) {
        this.data = data;
    }

    public static class EventBean {
        private String event;
        private String devId;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }
    }
}
