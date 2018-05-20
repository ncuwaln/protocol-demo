package protocol;

import java.io.UnsupportedEncodingException;

/**
 * Created by tangjiaqi on 2018/5/17.
 */
public class Message {

    public enum MessageType{
        BASE(1),
        DISCONNECT(2);


        int value;

        MessageType(int i) {
            value = i;
        }

        public int getValue(){
            return value;
        }

        public static MessageType valueOf(int i){
            MessageType[] types =  MessageType.values();
            MessageType result = null;
            for (MessageType x: types){
                if (x.getValue() == i){
                    result = x;
                    break;
                }
            }
            return result;
        }
    }

//    协议头，标记协议从什么时候开始
    private final static int HEADER = 0x7788b;
//    消息类型
    private MessageType messageType;
//    字符集的字符串长度(考虑不同编码的数据)
    private int charsetLength;
//    内容长度
    private int contentLength;
//    内容
    private byte[] charset;
//    字符集
    private byte[] content;

    public Message() {
    }

    public Message(MessageType messageType, String content, String charset) throws UnsupportedEncodingException {
        this.messageType = messageType;
        this.content = content.getBytes(charset);
        this.charset = charset.getBytes();
        this.contentLength = this.content.length;
        this.charsetLength = this.charset.length;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public int getCharsetLength() {
        return charsetLength;
    }

    public int getContentLength() {
        return contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(String content, String charset) throws UnsupportedEncodingException {
        this.content = content.getBytes(charset);
        this.charset = charset.getBytes();
        this.contentLength = this.content.length;
        this.charsetLength = this.charset.length;
    }

    public byte[] getCharset() {
        return charset;
    }

    public static int getHEADER() {
        return HEADER;
    }
}
