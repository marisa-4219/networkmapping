package io.github.zhangxh.networkmapping.entity;

public interface IResult<T> {


    public Integer getCode();


    public String getMessage();


    public T getData();


}
