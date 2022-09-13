package io.github.zhangxh.networkmapping.formatter;

import  io.github.zhangxh.networkmapping.entity.impl.RequestEntity;

public interface INetworkMappingResponseFormatter {

    public Object format(String response, RequestEntity requestEntity);

}
