package com.example.rpc1.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author XuanZi
 * @date 2021/7/2522:11
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> aClass;

    public RpcEncoder(Class<?> genericClass) {
        this.aClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (aClass.isInstance(o)) {
            byte[] data = SerializationUtil.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
