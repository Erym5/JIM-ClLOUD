package cn.tojintao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * @author cjt
 * @date 2022/6/12 11:09
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${netty.port}")
    String port;
    public String connectorUrl;
    {
        try {
            connectorUrl = InetAddress.getLocalHost().getHostName() + ":" + port;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String RELATION_KEY = "user:connector:relation";
    private static final String ONLINE_USER = "online_user";
    private static final String BAN_USER = "ban_user:";

    public void online(Integer userId) {
        redisTemplate.opsForHash().put(RELATION_KEY, String.valueOf(userId), connectorUrl);
        redisTemplate.opsForSet().add(ONLINE_USER, String.valueOf(userId));
    }

    public void offline(Integer userId) {
        redisTemplate.opsForHash().delete(RELATION_KEY, String.valueOf(userId));
        redisTemplate.opsForSet().remove(ONLINE_USER, String.valueOf(userId));
    }

    public String getConnectorUrl(Integer userId) {
        return (String) redisTemplate.opsForHash().get(RELATION_KEY, String.valueOf(userId));
    }

    public void nettyStop(Set<String> userIdSet) {
        for (String userId : userIdSet) {
            redisTemplate.opsForHash().delete(RELATION_KEY, userId);
            redisTemplate.opsForSet().remove(ONLINE_USER, userId);
        }
    }

    public boolean isBan(Integer userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BAN_USER + userId));
    }
}
