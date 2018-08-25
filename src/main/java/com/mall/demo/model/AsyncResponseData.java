package com.mall.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import java.util.Map;

public final class AsyncResponseData {
    public static final int RESPONSE_STATUS_OK = 200;
    public static final int RESPONSE_STATUS_CLIENT_ERROR = 404;
    public static final int RESPONSE_STATUS_REQUEST_DENIED = 401;
    public static final int RESPONSE_STATUS_INTERNAL_ERROR = 500;
    public static final int RESPONSE_STATUS_REDIRECT = 302;
    private static final String ONE_MSG_KEY = "oneErrorMsg";
    private static final String ONE_DATA_KEY = "oneSimpleData";
    private static final String REDIRECT_URL_KEY = "redirectUrl";

    private AsyncResponseData() {
    }

    public static final AsyncResponseData.ResultData get404Response(String errMsg) {
        return new AsyncResponseData.ResultData(404, errMsg);
    }

    public static final AsyncResponseData.ResultData get404Response(Map<String, String> errMsgs) {
        return new AsyncResponseData.ResultData(404, errMsgs);
    }

    public static final AsyncResponseData.ResultData get401Response(String errMsg) {
        return new AsyncResponseData.ResultData(401, errMsg);
    }

    public static final AsyncResponseData.ResultData get500Response(String errMsg) {
        return new AsyncResponseData.ResultData(500, errMsg);
    }

    public static final AsyncResponseData.ResultData get302Response(String redirectUrl) {
        AsyncResponseData.ResultData data = new AsyncResponseData.ResultData(302);
        data.appendData("redirectUrl", redirectUrl);
        return data;
    }

    public static final AsyncResponseData.ResultData get200Response() {
        return new AsyncResponseData.ResultData(200);
    }

    public static class ResultData {
        private int status;
        private Map<String, Object> data;
        private Map<String, String> messages;

        private ResultData(int code) {
            this.status = 200;
        }

        private ResultData(int code, String message) {
            this.status = code;
            this.messages = new HashMap();
            this.messages.put("oneErrorMsg", message);
        }

        private ResultData(int code, Map<String, String> messages) {
            this.status = code;
            this.messages = messages;
        }

        @JsonIgnore
        public boolean isOk() {
            return this.status == 200;
        }

        public int getStatus() {
            return this.status;
        }

        @JsonInclude(Include.NON_EMPTY)
        public Object getData() {
            if (null == this.data) {
                return null;
            } else if (this.data.containsKey("oneSimpleData")) {
                return this.data.get("oneSimpleData");
            } else if (this.data.containsKey("redirectUrl")) {
                Map<String, Object> map = new HashMap(this.data);
                map.remove("redirectUrl");
                return map;
            } else {
                return this.data;
            }
        }

        @JsonInclude(Include.NON_NULL)
        public Object getRedirect() {
            return null == this.data ? null : this.data.get("redirectUrl");
        }

        public void setData(Object data) {
            if (null == this.data) {
                this.data = new HashMap();
            }

            this.data.put("oneSimpleData", data);
        }

        public void setStatus(int status) {
            this.status = status;
        }

//        @JsonInclude(Include.NON_NULL)
//        @JsonSerialize(
//                using = InnerMessageCodeConvertor.class
//        )
        public Object getMessage() {
            if (null == this.messages) {
                return null;
            } else {
                return this.messages.containsKey("oneErrorMsg") ? this.messages.get("oneErrorMsg") : this.messages;
            }
        }

        public void appendData(String key, Object data) {
            if (null == this.data) {
                this.data = new HashMap();
            }

            this.data.put(key, data);
        }

        public void appendMessage(String key, String msg) {
            if (200 != this.status) {
                if (null == this.messages) {
                    this.messages = new HashMap();
                }

                this.messages.put(key, msg);
            }
        }

        public void setMessages(Map<String, String> messages) {
            this.messages = messages;
        }

        public AsyncResponseData.ResultData as404() {
            return this.switchStatus(404);
        }

        public AsyncResponseData.ResultData as401() {
            return this.switchStatus(401);
        }

        public AsyncResponseData.ResultData as500() {
            return this.switchStatus(500);
        }

        public AsyncResponseData.ResultData as302(String redirectUrl) {
            AsyncResponseData.ResultData data = this.switchStatus(302);
            if (302 == data.status) {
                data.appendData("redirectUrl", redirectUrl);
            }

            return data;
        }

        public AsyncResponseData.ResultData as404(String msg) {
            return this.switchStatus(404).putOneMessage(msg);
        }

        public AsyncResponseData.ResultData as401(String msg) {
            return this.switchStatus(401).putOneMessage(msg);
        }

        public AsyncResponseData.ResultData as500(String msg) {
            return this.switchStatus(500).putOneMessage(msg);
        }

        private AsyncResponseData.ResultData switchStatus(int status) {
            if (status == this.status) {
                return this;
            } else if (200 != this.status) {
                this.messages = new HashMap();
                this.messages.put("oneErrorMsg", "不能将" + this.status + "的消息转换为" + status);
                this.status = 500;
                return this;
            } else {
                this.status = status;
                return this;
            }
        }

        private AsyncResponseData.ResultData putOneMessage(String msg) {
            if (null == this.messages) {
                this.messages = new HashMap();
            }

            if (!this.messages.containsKey("oneErrorMsg")) {
                this.messages.put("oneErrorMsg", msg);
            }

            return this;
        }
    }
}
