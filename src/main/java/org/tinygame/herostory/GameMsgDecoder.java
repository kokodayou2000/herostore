package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

//解码
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

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




        //拿到消息体之后
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);


        //消息的父类
        GeneratedMessageV3 messageV3 = null;


        switch (msgCode){
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE: //如果读取到的是消息0
                messageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody); //
                break;
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                messageV3 = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                messageV3 = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;


        }

        if (null != messageV3){
            //ctx是一个信道，将消息解码之后，重新触发ChannelRead函数
            ctx.fireChannelRead(messageV3);
        }




    }
}
