package com.example.rpc1.client;

import com.example.rpc1.common.RpcRequest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * auth:XuanZi
 * time:2021/7/29 17:41
 */
public class RpcBeanFactory implements FactoryBean {

    private Class aClass;


    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {

        return null;
    }

    private Object create() {


        
        return Proxy.newProxyInstance(
                aClass.getClassLoader(),
                aClass.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());



                        return null;
                    }
                }
        );
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
