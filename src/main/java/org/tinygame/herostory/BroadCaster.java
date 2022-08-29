package org.tinygame.herostory;


import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

//负责广播
public final class BroadCaster {

    /**
     * 客户端信道数组，一定要有static，否则无法实现群发
     */

    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 私有化类的默认构造器
     */
    private BroadCaster(){}

    /**
     * 添加信道
     * @param channel
     */

    public static void addChannel(Channel channel){
        _channelGroup.add(channel);
    }

    /**
     * 移除信道
     * @param channel
     */
    public static void removeChannel(Channel channel){
        _channelGroup.remove(channel);
    }


    /**
     * 广播消息
     * @param msg
     */
    public static void broadcast(Object msg){
        //defence
        if (null == msg){
            return;
        }
        //广播
        _channelGroup.writeAndFlush(msg);

    }


}
