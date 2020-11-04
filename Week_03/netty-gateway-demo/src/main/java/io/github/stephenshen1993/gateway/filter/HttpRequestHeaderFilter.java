package io.github.stephenshen1993.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @ClassName : HttpRequestHeaderFilter  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-11-05 01:29  //时间
 */
public class HttpRequestHeaderFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("nio", "shenxiangjun");
    }
}
