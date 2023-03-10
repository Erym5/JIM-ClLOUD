package cn.tojintao.netty;

import cn.tojintao.concurrent.FutureTaskScheduler;
import cn.tojintao.distributed.ImWorker;
import cn.tojintao.distributed.WorkerRouter;
import cn.tojintao.service.RedisService;
import cn.tojintao.util.SpringUtil;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.tojintao.constant.ServerConstants.*;

/**
 * @author cjt
 * @date 2022/5/4 20:56
 */
public class NettyServer {


    private static NettyServer nettyServer = new NettyServer();

    public static NettyServer getInstance() {
        return nettyServer;
    }

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public static void main(String[] args) {
        new NettyServer().run();
    }

    public void run() {
        final NettyServer nettyServer = new NettyServer();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer());
            // changed
            String IP = InetAddress.getLocalHost().getHostName();
            InetSocketAddress socketAddress = new InetSocketAddress(IP, PORT);
            ChannelFuture channelFuture = serverBootstrap.bind(socketAddress).sync();
            channelFuture.syncUninterruptibly();
            // ???????????????????????????
            ImWorker.getInst().setLocalNode(IP, PORT);

            // ????????????????????????????????????
            FutureTaskScheduler.add(() ->
            {
                /**
                 * ??????nacos??????
                 */
                ImWorker.getInst().init();

                /**
                 * ??????nacos???????????????????????????????????????
                 */
                WorkerRouter.getInst().init();
            });

            channel = channelFuture.channel();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    nettyServer.stop();
                }
            });

            channelFuture.channel().closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * ???????????????????????????
     */
    public void stop() {
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        Set<String> userIdSet = UserChannelRelation.userChannel.keySet().stream()
                .map(String::valueOf).collect(Collectors.toSet());
        redisService.nettyStop(userIdSet);  //??????Redis?????????netty????????????????????????
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    class ChatServerInitializer extends ChannelInitializer<Channel> {
        private static final int READ_IDLE_TIME_OUT = 3600; // ?????????
        private static final int WRITE_IDLE_TIME_OUT = 0;// ?????????
        private static final int ALL_IDLE_TIME_OUT = 0; // ????????????
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //websocket??????http?????????????????????http????????????
            pipeline.addLast(new HttpServerCodec());
            //http???????????????????????????ChunkedWriteHandler?????????
            pipeline.addLast(new ChunkedWriteHandler());
            //???HTTP??????????????????????????????????????????HTTP??????
            pipeline.addLast(new HttpObjectAggregator(65535));
            // WebSocket????????????
            pipeline.addLast(new WebSocketServerCompressionHandler());
            //??????websocket??????????????????????????????frame???????????????????????????????????????????????????frame?????????
            //WebSocket????????????????????????WebSocket???????????????????????????????????????????????????101???
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 10 * 1024));
            //????????????60???????????????????????????????????????????????????IdleStateEvent?????????
            //????????????HeartbeatHandler???userEventTriggered???????????????
            pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
            pipeline.addLast(new HeartBeatHandler());
            //?????????Handler?????????????????????
            pipeline.addLast("login", new LoginHandler());
            pipeline.addLast("chat", new ChatHandler());
        }
    }

}
