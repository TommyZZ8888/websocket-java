package com.zz.websocket.listener;

import com.zz.websocket.domain.ChatMessage;
import com.zz.websocket.domain.WebSocketModel;
import com.zz.websocket.enums.MessageType;
import com.zz.websocket.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description WebSocketEventListener
 * @Author 张卫刚
 * @Date Created on 2023/4/20
 */

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WebSocketModel webSocketModel;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        InetAddress localhost;

        try {
            localhost = Inet4Address.getLocalHost();
            LOGGER.info("Received a new web socket connection");
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            LOGGER.info("User Disconnected : " + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);
            try {
                redisTemplate.opsForSet().remove(webSocketModel.onlineUsers, username);
                redisTemplate.convertAndSend(webSocketModel.userStatus, JsonUtil.parseObjToJson(chatMessage));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
