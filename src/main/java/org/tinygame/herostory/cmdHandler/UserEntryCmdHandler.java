package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.BroadCaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {
    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        //从指令对象中获取用户id和形象
        GameMsgProtocol.UserEntryCmd cmd = msg;
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        //result的构造器，放入id和角色
        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        //构建用户对象
        User newUser = new User();
        newUser.userId = userId;
        newUser.heroAvatar = heroAvatar;
        //将用户加入字典
        UserManager.addUser(newUser);

        UserManager.listUser().forEach((o)->{
            System.out.println("Hero Id = "+o.userId);
            System.out.println("Hero Avatar = "+o.heroAvatar);
        });

        //将用户Id附着到Channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        //构建结果，并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        BroadCaster.broadcast(newResult);
    }
}
