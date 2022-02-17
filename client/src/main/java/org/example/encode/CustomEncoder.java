package org.example.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.example.dto.Request;
import org.example.util.SerializeUtil;

import java.io.IOException;
import java.io.Serializable;

public class CustomEncoder extends MessageToByteEncoder<Serializable> {

    public CustomEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Serializable msg, ByteBuf out) throws IOException {
        if (!(msg instanceof Request)) {
            return;
        }

        out.writeByte((byte) 'S');
        out.writeByte((byte) 'T');

        byte packetType;
        switch (((Request) msg).getType()) {
            case LOGIN -> packetType = 0x01;
            case CHAT ->  packetType = 0x02;
            case GET_MESSAGE ->  packetType = 0x03;
            default -> packetType = 0x04;
        }

        out.writeByte(packetType);

        byte[] data = SerializeUtil.serialize(msg);
        int dataLength = data.length;
        out.writeInt(dataLength);

        out.writeBytes(data);
    }
}
