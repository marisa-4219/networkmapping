package io.github.zhangxh.networkmapping.entity.impl;

import io.github.zhangxh.networkmapping.entity.IHost;

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
}
