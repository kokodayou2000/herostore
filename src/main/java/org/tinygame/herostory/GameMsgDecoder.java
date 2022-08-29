package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

//解码
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof BinaryWebSocketFrame)){
            return ;
        }

        //o是BinaryWebSocketFrame类型的
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        //转化byteBuf
        ByteBuf byteBuf = frame.content();




        byteBuf.readShort(); //读取消息长度
        int msgCode = byteBuf.readShort(); //读取消息编号


        //通过一个工厂类，根据msgCode得到MessageBuilder
        Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);

        if (null == msgBuilder){
            LOGGER.info("无法识别的消息：msgCode = {}",msgCode);
            return;
        }


        //拿到消息体之后
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        msgBuilder.clear();

        //通过合并的方式读取消息
        msgBuilder.mergeFrom(msgBody);

        //构建消息
        Message newMessage = msgBuilder.build();

        if (null != newMessage){
            //ctx是一个信道，将消息解码之后，重新触发ChannelRead函数
            ctx.fireChannelRead(newMessage);
        }




    }
}
