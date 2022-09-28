package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IHost;

public class Host implements IHost {

    private String host;

    public Host(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    public Host setHost(String host) {
        this.host = host;
        return this;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
