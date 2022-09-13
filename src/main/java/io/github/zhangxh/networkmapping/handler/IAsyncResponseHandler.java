package io.github.zhangxh.networkmapping.handler;

public interface IAsyncResponseHandler<T> {

    public void success(T response);

    default void error(Throwable e) { e.printStackTrace();}

}
