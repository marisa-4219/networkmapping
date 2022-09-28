package io.github.zhangxh.networkmapping.formatter;

import io.github.zhangxh.networkmapping.entity.RequestEntity;
import org.springframework.http.HttpHeaders;

public interface INetworkMappingResponseFormatter {

    public Object format(byte[] response, RequestEntity requestEntity, HttpHeaders responseHeaders);

}
