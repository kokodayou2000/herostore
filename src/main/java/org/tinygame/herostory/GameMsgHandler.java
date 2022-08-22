package org.tinygame.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

//pipeLine实际上要执行的操作
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    //事件广播
    /**
     * 客户端信道数组，一定要有static，否则无法实现群发
     */
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //用户字典
    private static final Map<Integer,User> _userMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //事件感知,并且添加事件到channel数组中
        super.channelActive(ctx);
        _channelGroup.add(ctx.channel());
    }

    //离线
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        super.handlerRemoved(ctx);

        _channelGroup.remove(ctx.channel());

        //先拿到用户id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId){
            return;
        }

        //如果不为空,将集合中的id删除掉
        _userMap.remove(userId);


        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);


        GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
        _channelGroup.writeAndFlush(newResult);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到消息, msgClazz -> "+msg.getClass().getName()+", msg = "+msg);

        if (msg instanceof GameMsgProtocol.UserEntryCmd){
            //从指令对象中获取用户id和形象
            GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd) msg;
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
            _userMap.put(newUser.userId, newUser);

            _userMap.forEach((a,b)->{
                System.out.println("userId = "+a);
                System.out.println("HeroAvatar = "+b.heroAvatar);
            });

            //将用户Id附着到Channel
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            //构建结果，并发送
            GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);
            
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            //创建一个谁在场的结果消息对象
            GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
            //遍历每一个登录用户的对象
            for(User currUser:_userMap.values()){
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

        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            //拿到的可能是一个null值，所以要使用对象类型的Integer
            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

            if (null == userId){
                return;
            }

            GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
            resultBuilder.setMoveUserId(userId);

            //对应的坐标位置存放到了cmd里面了

            GameMsgProtocol.UserMoveToCmd cmd = (GameMsgProtocol.UserMoveToCmd)msg;

            resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
            resultBuilder.setMoveToPosY(cmd.getMoveToPosY());


            GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);
        }

    }
}
