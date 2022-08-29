package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.cmdHandler.*;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;


//pipeLine实际上要执行的操作
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //事件感知,并且添加事件到channel数组中
        super.channelActive(ctx);
        BroadCaster.addChannel(ctx.channel());
    }

    //离线
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        super.handlerRemoved(ctx);

        BroadCaster.removeChannel(ctx.channel());

        //先拿到用户id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId){
            return;
        }

        //如果不为空,将集合中的id删除掉
        UserManager.removeUserById(userId);


        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);


        GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
        BroadCaster.broadcast(newResult);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("客户端收到消息, msgClazz -> "+msg.getClass().getName()+", msg = "+msg);

        //通过字典的模式读取handler
        ICmdHandler<?extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());

        if (null != cmdHandler){
            //cmdHandler.handler(ctx, msg);
            //cast是一个欺骗编译器的小技巧
            cmdHandler.handler(ctx, cast(msg));
        }
    }

    /**
     * 转型消息对象
     *
     * @param msg
     * @return
     * @param <TCmd>
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if (msg == null){
            return null;
        } else{
            return (TCmd) msg;
        }

    }



}
