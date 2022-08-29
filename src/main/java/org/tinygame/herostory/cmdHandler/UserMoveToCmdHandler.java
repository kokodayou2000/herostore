package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.BroadCaster;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        //拿到的可能是一个null值，所以要使用对象类型的Integer
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (null == userId){
            return;
        }

        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);

        //对应的坐标位置存放到了cmd里面了

        GameMsgProtocol.UserMoveToCmd cmd = msg;

        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());


        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        BroadCaster.broadcast(newResult);
    }
}
