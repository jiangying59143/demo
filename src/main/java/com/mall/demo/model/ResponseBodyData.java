package com.mall.demo.model;

import java.io.Serializable;
import java.util.Map;

public class ResponseBodyData implements Serializable {
    private Object data;

    private String message;

    public ResponseBodyData (){}

    public ResponseBodyData(Map<String, Object> data, String message) {
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
