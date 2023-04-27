package com.zz.websocket.redis;

import com.zz.websocket.domain.ChatMessage;
import com.zz.websocket.domain.WebSocketModel;
import com.zz.websocket.service.ChatService;
import com.zz.websocket.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description redis订阅频道处理类
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerHandle.class);

    @Autowired
    private WebSocketModel webSocketModel;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatService chatService;


    /**
     * 收到监听消息
     *
     * @param message the incoming Redis message
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;

        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);

        } catch (SerializationException e) {
            LOGGER.error(e.getMessage());
            return;
        }

        try {
            if (Objects.equals(topic, webSocketModel.msgToAll)) {
                ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
                //发送消息给所有在线cid
                if (chatMessage != null) {
                    chatService.sendMsg(chatMessage);
                }
            } else if (webSocketModel.userStatus.equals(topic)) {
                ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
                if (chatMessage != null) {
                    chatService.alertUserStatus(chatMessage);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("no further operations with this topic");
        }
    }
}
