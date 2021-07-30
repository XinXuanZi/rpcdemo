package com.example.rpc1.client;

import com.example.rpc1.common.RpcDecoder;
import com.example.rpc1.common.RpcEncoder;
import com.example.rpc1.common.RpcRequest;
import com.example.rpc1.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author XuanZi
 * @date 2021/7/2621:48
 */
public class RpcClient  extends SimpleChannelInboundHandler<RpcRequest> {
    private String host;
    private String port;

    public RpcClient(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public void send(RpcRequest request) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    });

        } catch (Exception e) {

        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {

    }
}
