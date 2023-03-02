package cn.tojintao.distributed;

import cn.tojintao.bean.Notification;
import cn.tojintao.model.entity.ImNode;
import cn.tojintao.netty.HeartBeatHandler;
import cn.tojintao.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Slf4j
@Data
public class PeerSender
{

    private  int reConnectCount=0;
    private Channel channel;

    private ImNode rmNode;
    /**
     * 唯一标记
     */
    private boolean connectFlag = false;
    private boolean online = false;

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) ->
    {
        log.info("分布式连接已经断开……{}", rmNode.toString());
        channel = null;
        connectFlag = false;
    };

    private GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) ->
    {
        final EventLoop eventLoop = f.channel().eventLoop();
        if (!f.isSuccess() && ++reConnectCount<3)
        {
            log.info("连接失败! 在10s之后准备尝试第{}次重连!",reConnectCount);
            eventLoop.schedule(() -> PeerSender.this.doConnect(), 10, TimeUnit.SECONDS);

            connectFlag = false;
        } else
        {
            connectFlag = true;

            log.info(new Date() + "分布式节点连接成功:{}", rmNode.toString());

            channel = f.channel();
            channel.closeFuture().addListener(closeListener);

            /**
             * 发送链接成功的通知
             */
            Notification<ImNode> notification = new Notification<>(ImWorker.getInst().getLocalNodeInfo());
            notification.setType(Notification.CONNECT_FINISHED);
            String json = JsonUtil.pojoToJson(notification);
            writeAndFlush(json);
        }
    };


    private Bootstrap b;
    private EventLoopGroup g;

    public PeerSender(ImNode n)
    {
        this.rmNode = n;

        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
         * 都是AbstractBootstrap的子类。
         **/

        b = new Bootstrap();
        /**
         * 通过nio方式来接收连接和处理连接
         */

        g = new NioEventLoopGroup();


    }

    /**
     * 重连
     */
    public void doConnect()
    {

        // 服务器ip地址
        String host = rmNode.getHost();
        // 服务器端口
        int port = rmNode.getPort();

        try
        {
            if (b != null && b.group() == null)
            {
                b.group(g);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                b.remoteAddress(host, port);

                // 设置通道初始化
                b.handler(
                        new ChannelInitializer<SocketChannel>()
                        {
                            private static final int READ_IDLE_TIME_OUT = 3600; // 读超时
                            private static final int WRITE_IDLE_TIME_OUT = 0;// 写超时
                            private static final int ALL_IDLE_TIME_OUT = 0; // 所有超时
                            public void initChannel(SocketChannel ch)
                            {
                                ch.pipeline().addLast(new HttpServerCodec());
                                //http是以块方式写，添加ChunkedWriteHandler处理器
                                ch.pipeline().addLast(new ChunkedWriteHandler());
                                //将HTTP消息的多个部分合成一条完整的HTTP消息
                                ch.pipeline().addLast(new HttpObjectAggregator(65535));
                                // WebSocket数据压缩
                                ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                                //对于websocket来说，它的数据都是以frame帧形式传输的，不同的数据类型对应的frame也不同
                                //WebSocket协议处理器，负责WebSocket的握手处理以及协议升级（通过状态码101）
                                ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 10 * 1024));
                                //当连接在60秒内没有接收到消息时，就会触发一个IdleStateEvent事件，
                                //此事件被HeartbeatHandler的userEventTriggered方法处理到
                                ch.pipeline().addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
                                ch.pipeline().addLast(new HeartBeatHandler());
                            }
                        }
                );
                log.info(new Date() + "开始连接分布式节点:{}", rmNode.toString());

                ChannelFuture f = b.connect();
                f.addListener(connectedListener);


            } else if (b.group() != null)
            {
                log.info(new Date() + "再一次开始连接分布式节点", rmNode.toString());
                ChannelFuture f = b.connect();
                f.addListener(connectedListener);
            }
        } catch (Exception e)
        {
            log.info("客户端连接失败!" + e.getMessage());
        }

    }

    public void stopConnecting()
    {
        g.shutdownGracefully();
        connectFlag = false;
    }

    public void writeAndFlush(Object pkg)
    {
        if (connectFlag == false)
        {
            log.error("分布式节点未连接:", rmNode.toString());
            return;
        }
        channel.writeAndFlush(pkg);
    }

    public boolean getConnectFlag() {
        return this.connectFlag;
    }

}