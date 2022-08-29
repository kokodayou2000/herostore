package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//实现编码器，使用户能互相看到对方
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (null == msg ||
                !(msg instanceof GeneratedMessageV3)){
            //如果msg是空的，或者msg不是V3消息类型的
            super.write(ctx,msg,promise);
            return;
        }

        int msgCode = -1;
        //通过对象的类型，得到对应的msgCode
        msgCode = GameMsgRecognizer.getMessageCodyByMsgClazz(msg.getClass());



        if (msgCode <= -1 ){
            LOGGER.error("无法识别消息：msgClazz =  {}",msg.getClass().getName());
            return;
        }

        byte[] byteArray = ((GeneratedMessageV3) msg).toByteArray();

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeShort((short)0);
        byteBuf.writeShort((short)msgCode);
        byteBuf.writeBytes(byteArray);


        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);

        super.write(ctx,frame,promise);

    }



}
