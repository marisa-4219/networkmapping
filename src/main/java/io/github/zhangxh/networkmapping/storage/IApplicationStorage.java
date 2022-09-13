package io.github.zhangxh.networkmapping.storage;

import java.util.Date;

public interface IApplicationStorage {

    public String getToken();


    public void setToken(String token, Date expiresTime);

}
