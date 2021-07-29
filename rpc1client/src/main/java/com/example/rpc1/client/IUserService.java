package com.example.rpc1.client;

import com.example.rpc1.common.RpcService;

/**
 * @author XuanZi
 * @date 2021/7/2517:50
 */
@RpcClient
public interface IUserService {
    /**
     * huoqu
     * @param id
     * @return
     */
    String getName(Long id);
}
