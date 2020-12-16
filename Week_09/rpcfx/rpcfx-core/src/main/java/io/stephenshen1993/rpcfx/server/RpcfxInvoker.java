package io.stephenshen1993.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.stephenshen1993.rpcfx.api.RpcfxRequest;
import io.stephenshen1993.rpcfx.api.RpcfxResolver;
import io.stephenshen1993.rpcfx.api.RpcfxResponse;
import io.stephenshen1993.rpcfx.exception.RpcfxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RpcfxInvoker(RpcfxResolver resolver){
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        try {
            // 作业1：改成泛型和反射
            Class<?> clazz = Class.forName(serviceClass);
            Object service = resolver.resolve(clazz);

            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch ( IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            RpcfxException rpcfxException = new RpcfxException("RpcfxInvoker#invoke", e);
            logger.error("RpcfxInvoker#invoke", rpcfxException);
            response.setStatus(false);
            response.setRpcfxException(rpcfxException);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
