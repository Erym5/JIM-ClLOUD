package cn.tojintao.netty;

import cn.tojintao.common.ChatTypeEnum;
import cn.tojintao.common.MsgActionEnum;
import cn.tojintao.model.entity.Message;
import cn.tojintao.model.protocol.ChatMsg;
import cn.tojintao.model.protocol.DataContent;
import cn.tojintao.service.MsgService;
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

/**
 * @author cjt
 * @date 2022/5/4 21:22
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

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
        if (action.equals(MsgActionEnum.CHAT.type)) { //聊天行为
            Integer senderId = chatMsg.getSenderId();
            Integer receiverId = chatMsg.getReceiverId();
            String message = chatMsg.getMessage();
            Integer type = chatMsg.getType();
            RedisService redisService = SpringUtil.getBean(RedisService.class);
            if (redisService.isBan(senderId)) return; //禁言
            MsgService msgService = SpringUtil.getBean(MsgService.class);
            if (type.equals(ChatTypeEnum.GROUP.getType())) {
                log.info("senderId:" + senderId + ",sendGroupMessage:" + message);
                msgService.sendGroupMessage(senderId, receiverId, message);
            } else if (type.equals(ChatTypeEnum.PERSONAL.getType())) {
                log.info("senderId:" + senderId + ",sendMessage:" + message);
                msgService.sendMessage(senderId, receiverId, message);
            }
        } else if (action.equals(MsgActionEnum.KEEPALIVE.type)) {
            log.info("收到来自channel为【" + channel + "】的心跳包");
        }
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
