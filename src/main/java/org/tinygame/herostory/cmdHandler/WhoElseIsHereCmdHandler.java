package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {
    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {
        //创建一个谁在场的结果消息对象
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        //遍历每一个登录用户的对象
        for(User currUser: UserManager.listUser()){
            if (null == currUser){
                continue;
            }
            //通过userInfoBuilder 设置用户id和角色
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currUser.userId);
            userInfoBuilder.setHeroAvatar(currUser.heroAvatar);
            //添加到resultBuilder中（结果对象）
            resultBuilder.addUserInfo(userInfoBuilder);
        }
        //构建最终的结果
        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        //写入
        ctx.writeAndFlush(newResult);
    }

}
