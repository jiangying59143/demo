package com.mall.demo.model;

import java.util.Map;

public class ResponseBodyData {
    private Map<String, Object> data;

    private String message;

    public ResponseBodyData (){}

    public ResponseBodyData(Map<String, Object> data, String message) {
        this.data = data;
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
