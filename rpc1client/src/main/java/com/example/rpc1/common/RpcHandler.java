package com.example.rpc1.common;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author XuanZi
 * @date 2021/7/2522:15
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private Map<String, Object> beanMap;


    public RpcHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object handle = handle(request);
            response.setResult(handle);

        }catch (Exception e) {
            response.setError(e);
        }

        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws InvocationTargetException {
        String className = request.getClassName();
        Object serviceBean  = beanMap.get(className);

        Class<?> aClass = serviceBean.getClass();

        FastClass fastClass = FastClass.create(aClass);
        Object invoke = fastClass.invoke(request.getMethodName(), request.getParameterTypes(), serviceBean, request.getParameters());

        return invoke;
    }

}
