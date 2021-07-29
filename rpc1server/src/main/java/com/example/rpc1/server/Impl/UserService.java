package com.example.rpc1.server.Impl;

import com.example.rpc1.common.RpcService;
import com.example.rpc1.server.IUserService;

/**
 * @author XuanZi
 * @date 2021/7/2517:55
 */
@RpcService(IUserService.class)
public class UserService implements IUserService {
    @Override
    public String getName(Long id) {
        return "XUANZI";
    }
}
