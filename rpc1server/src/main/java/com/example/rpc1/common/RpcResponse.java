package com.example.rpc1.common;

import lombok.Data;

/**
 * @author XuanZi
 * @date 2021/7/2518:42
 */
@Data
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;
}
