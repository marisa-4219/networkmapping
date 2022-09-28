package io.github.zhangxh.networkmapping.entity.response.impl;

import io.github.zhangxh.networkmapping.entity.response.IResult;


public class Result<T> implements IResult<T> {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private String msg;

    private T data;

    private Long timestamp;


    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
