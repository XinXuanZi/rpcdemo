package com.example.rpc1.common;

import lombok.Data;

/**
 * @author XuanZi
 * @date 2021/7/2518:42
 */
@Data
public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
