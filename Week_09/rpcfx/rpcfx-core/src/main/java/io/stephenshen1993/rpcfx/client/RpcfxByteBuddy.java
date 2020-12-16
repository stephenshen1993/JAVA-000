package io.stephenshen1993.rpcfx.client;

import com.alibaba.fastjson.parser.ParserConfig;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

/**
 * @ClassName : RpcfxByteBuddy  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-17 00:48  //时间
 */
public class RpcfxByteBuddy {
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {
        try {
            return (T) new ByteBuddy()
                    .subclass(Object.class)
                    .implement(serviceClass)
                    .intercept(InvocationHandlerAdapter.of(new Rpcfx.RpcfxInvocationHandler(serviceClass, url)))
                    .make()
                    .load(RpcfxByteBuddy.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T xstreamCreate(Class<T> serviceClass, String url) {
        try {
            return (T) new ByteBuddy()
                    .subclass(Object.class)
                    .implement(serviceClass)
                    .intercept(InvocationHandlerAdapter.of(new RpcfxXstreamInvocationHandler(serviceClass, url)))
                    .make()
                    .load(RpcfxByteBuddy.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
