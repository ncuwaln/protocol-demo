package codce;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import protocol.Message;

/**
 * Created by tangjiaqi on 2018/5/17.
 */
public class MyProtocolEncoder extends MessageToByteEncoder<Message> {

    public MyProtocolEncoder(){

    }


    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getHEADER());
        out.writeInt(msg.getMessageType().getValue());
        out.writeInt(msg.getCharsetLength());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getCharset());
        out.writeBytes(msg.getContent());
    }
}
