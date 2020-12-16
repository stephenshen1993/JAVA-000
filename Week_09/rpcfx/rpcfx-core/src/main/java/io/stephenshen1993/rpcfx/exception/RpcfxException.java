package io.stephenshen1993.rpcfx.exception;

/**
 * @ClassName : RpcfxException  //类名
 * @Description : 自定义RPC异常  //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-17 00:56  //时间
 */
public class RpcfxException extends RuntimeException {

    public RpcfxException() {
        super();
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcfxException(Throwable cause) {
        super(cause);
    }
}