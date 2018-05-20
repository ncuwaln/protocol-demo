package codce;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.Message;
import protocol.Message.MessageType;

import java.util.List;

/**
 * Created by tangjiaqi on 2018/5/17.
 */
public class MyProtocolDecoder extends ByteToMessageDecoder {
    public MyProtocolDecoder(){
    }

//    协议头+消息类型+charsetLength+contentLength的长度
    private final static int BASE_LENGTH = 16;

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("start decode");
        int beginIndex = in.readerIndex();
        int messageTypeValue = 0, charsetLength = 0, contentLength = 0;
        boolean flag = false;
        while (in.readableBytes() > BASE_LENGTH){
            beginIndex = in.readerIndex();
            int tmp = in.readInt();
//            协议开始
            if (tmp == Message.getHEADER()){
                messageTypeValue = in.readInt();
                charsetLength = in.readInt();
                contentLength = in.readInt();
                flag = true;
                break;
            }
        }
        if (!flag){
            return;
        }else {
//            剩余可读数据小于所需，返回等待下次数据的到来
            if (in.readableBytes() < (charsetLength + contentLength)){
//                记住将读索引归位
                in.readerIndex(beginIndex);
                return;
            }else {
                byte[] charset = new byte[charsetLength];
                byte[] content = new byte[contentLength];
                in.readBytes(charset);
                in.readBytes(content);
                String charsetStr = new String(charset);
                String contentStr = new String(content, charsetStr);
                Message message = new Message(MessageType.valueOf(messageTypeValue), contentStr, charsetStr);
                out.add(message);
            }
        }
        System.out.println("end decode");
    }
}
