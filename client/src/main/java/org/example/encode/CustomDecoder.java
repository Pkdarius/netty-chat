package org.example.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import org.example.model.Message;
import org.example.util.SerializeUtil;

import java.util.List;

public class CustomDecoder extends ByteToMessageDecoder {

    public CustomDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 5) {
            return;
        }
        byteBuf.markReaderIndex();

        short firstByte = byteBuf.readUnsignedByte();
        short secondByte = byteBuf.readUnsignedByte();

        if (firstByte != 'S' || secondByte != 'T') {
            byteBuf.resetReaderIndex();
            throw new CorruptedFrameException("Gói tin không hợp lệ");
        }

        int packetType = byteBuf.readUnsignedByte();
        if (packetType < 0x01 || packetType > 0x03) {
            return;
        }
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] decoded = new byte[dataLength];
        byteBuf.readBytes(decoded);
        list.add(SerializeUtil.deserialize(decoded));
    }
}
