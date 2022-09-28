package io.github.zhangxh.networkmapping.handler;

import io.github.zhangxh.networkmapping.entity.RequestEntity;

public abstract class IGlobalResponseHandler {

    private RequestEntity requestEntity;

    public void setRequestEntity(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public RequestEntity getRequestEntity() {
        return requestEntity;
    }

    public Object response(Object response) {
        return response;
    }


    public void exception(Throwable e) throws Throwable {
        throw e;
    }

}
