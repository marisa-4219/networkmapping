package io.github.zhangxh.networkmapping.entity.request.impl;


import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IPath;

public class Path implements IPath {
    private String url;

    private boolean overwriteHost;

    public String getURL() {
        return url;
    }

    public boolean isOverwriteHost() {
        return overwriteHost;
    }

    public Path setURL(String url) {
        this.url = url;
        return this;
    }

    public Path setOverwriteHost(boolean overwriteHost) {
        this.overwriteHost = overwriteHost;
        return this;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
