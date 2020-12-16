package io.stephenshen1993.rpcfx.api;

import io.stephenshen1993.rpcfx.exception.RpcfxException;

public class RpcfxResponse {

    private Object result;

    private boolean status;

    private RpcfxException rpcfxException;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RpcfxException getRpcfxException() {
        return rpcfxException;
    }

    public void setRpcfxException(RpcfxException rpcfxException) {
        this.rpcfxException = rpcfxException;
    }
}
