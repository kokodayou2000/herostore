package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * 创建统一的接口
 * TCmd是一个泛型，这个泛型要求继承自GeneratedMessageV3
 * @param <TCmd>
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理指令
     *
     * @param ctx
     * @param cmd
     */
    void handler(ChannelHandlerContext ctx, TCmd cmd);

}
