package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;


public class ServerMain {

    //日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        //初始化字典工厂
        CmdHandlerFactory.init();


        //实际上是两个线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //服务器哪里的信道如何处理
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup); //bossGroup负责连接，workGroup负责业务
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),//前端是H5的，使用的协议是websocket
                        new GameMsgDecoder(), //自定义消息解码器
                        new GameMsgEncoder(), //自定义消息编码器
                        new GameMsgHandler()
                );
            }
        }); //处理客户端

        try {
            //绑定端口，捕获异常
            ChannelFuture f = b.bind(12345).sync();
            if (f.isSuccess()){
                LOGGER.info("服务员已经启动");
            }

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
