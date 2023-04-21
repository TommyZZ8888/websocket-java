package com.zz.websocket.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description WebSocketModel
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
@Component
public class WebSocketModel {

    @Value("${spring.redis.port}")
    public String serverPort;

    @Value("${redis.channel.msgToAll}")
    public String msgToAll;

    @Value("${redis.channel.userStatus}")
    public String userStatus;

    @Value("${redis.set.onlineUsers}")
    public String onlineUsers;
}
