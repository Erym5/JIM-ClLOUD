package cn.tojintao.util;

import cn.tojintao.model.protocol.ChatMsg;
import cn.tojintao.model.protocol.DataContent;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class DataUtil {
    public ChatMsg getData(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String content = msg.text();
        Channel channel = ctx.channel();
        DataContent dataContent = JSON.parseObject(content, DataContent.class);
        Integer action = dataContent.getAction();
        ChatMsg chatMsg = dataContent.getChatMsg();
        return chatMsg;
    }
}
