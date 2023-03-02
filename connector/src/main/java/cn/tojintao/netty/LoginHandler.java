package cn.tojintao.netty;

import cn.tojintao.common.MsgActionEnum;
import cn.tojintao.concurrent.CallbackTask;
import cn.tojintao.concurrent.CallbackTaskScheduler;
import cn.tojintao.model.protocol.ChatMsg;
import cn.tojintao.model.protocol.DataContent;
import cn.tojintao.service.RedisService;
import cn.tojintao.util.SpringUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class LoginHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理所有客户端(Client)的管道组ChannelGroup
     * 一个客户端(Client) 对应一个 Channel 通道~
     */
    public static ChannelGroup userClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端所传输的消息
        String content = msg.text();
        Channel channel = ctx.channel();
        DataContent dataContent = JSON.parseObject(content, DataContent.class);
        Integer action = dataContent.getAction();
        ChatMsg chatMsg = dataContent.getChatMsg();
        //根据消息类型执行不同操作
        if (action.equals(MsgActionEnum.CONNECT.type)) { //初始化连接行为
            loginProcess(ctx, channel, chatMsg, log);
        }

        //异步任务，处理登录的逻辑
        CallbackTaskScheduler.add(new CallbackTask<Boolean>()
        {
            @Override
            public Boolean execute() throws Exception
            {
                return loginProcess(ctx, channel, chatMsg, log);

            }

            //异步任务返回
            @Override
            public void onBack(Boolean r)
            {
                if (r)
                {
//                    ctx.pipeline().remove(LoginRequestHandler.this);
                    log.info("登录成功:");

//                    ctx.pipeline().addAfter("login", "chat",   chatRedirectHandler);
                    ctx.pipeline().remove("login");
                } else
                {
                    ctx.channel().close();
                    log.info("登录失败:" );

                }

            }
            //异步任务异常

            @Override
            public void onException(Throwable t)
            {
                t.printStackTrace();
                log.info("登录失败:" );
                ctx.channel().close();
            }
        });

    }


    static boolean loginProcess(ChannelHandlerContext ctx, Channel channel, ChatMsg chatMsg, Logger log) {
        Integer userId = chatMsg.getSenderId();
        UserChannelRelation.put(userId, channel);   //本地新增连接
        UserChannelRelation.put(ctx.channel(), userId);
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        redisService.online(userId);    //用户上线，更新用户所在netty节点
        log.info("用户id:" + userId + ", channel:" + UserChannelRelation.getChannel(userId));
        return true;
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接：channel id 为：" + ctx.channel().id().asLongText());
        userClients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接：channel id 为：" + ctx.channel().id().asLongText());
        //移除Redis状态信息
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        redisService.offline(UserChannelRelation.getUserByChannel(ctx.channel()));
        userClients.remove(ctx.channel());
        UserChannelRelation.offline(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug(cause.getMessage());
        //移除Redis状态信息
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        redisService.offline(UserChannelRelation.getUserByChannel(ctx.channel()));
        ctx.channel().close();
        userClients.remove(ctx.channel());
        UserChannelRelation.offline(ctx.channel());
    }
}
