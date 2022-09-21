package io.github.zhangxh.networkmapping.entity.impl;


import io.github.zhangxh.networkmapping.entity.IRequestURL;

public class RequestURL implements IRequestURL {
    private String url;

    private boolean full;

    public String getURL() {
        return url;
    }

    public boolean isFull() {
        return full;
    }

    public RequestURL setURL(String url) {
        this.url = url;
        return this;
    }

    public RequestURL setFull(boolean full) {
        this.full = full;
        return this;
    }
}
