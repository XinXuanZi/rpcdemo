package com.example.rpc1.server;

import com.example.rpc1.common.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XuanZi
 * @date 2021/7/2517:56
 */
@Configuration
public class RpcApplication implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    private Map<String, Object> map = new HashMap<>();

    @Value("${server.address}")
    private String serverAddress;

    @Resource
    private ServiceRegistry serviceRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object value : beansWithAnnotation.values()) {
            String name = value.getClass().getAnnotation(RpcService.class).value().getName();
            map.put(name, value);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 服务注册
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup workerGroup  = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new RpcEncoder(RpcRequest.class))
                            .addLast(new RpcDecoder(RpcResponse.class))
                            .addLast(new RpcHandler(map));
                }
            }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] split = serverAddress.split(":");

            Integer port = Integer.parseInt(split[1]);
            ChannelFuture sync = bootstrap.bind(split[0], port).sync();


            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress);
            }
            sync.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}
