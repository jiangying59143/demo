package com.mall.demo.common.result;

import com.mall.demo.common.constants.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ApiModel(value="响应返回对象",description="响应返回对象")
public class Result implements Serializable {

    private static final long serialVersionUID = -4762928619495260423L;

    @ApiModelProperty(value="状态码",name="code")
    private Integer code;

    @ApiModelProperty(value="返回信息",name="msg")
    private String msg;

    @ApiModelProperty(value="返回数据",name="data")
    private Object data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    public static Result error(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }


    public Map<String, Object> simple() {
        Map<String, Object> simple = new HashMap<String, Object>();
        this.data = simple;

        return simple;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}