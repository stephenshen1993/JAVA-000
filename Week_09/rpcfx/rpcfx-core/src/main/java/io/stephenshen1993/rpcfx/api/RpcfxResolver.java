package io.stephenshen1993.rpcfx.api;

public interface RpcfxResolver {

    Object resolve(String serviceClass);

    Object resolve(Class<?> clazz);
}
