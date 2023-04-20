package com.zz.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Description WebSocketConfig
 * @Author 张卫刚
 * @Date Created on 2023/4/20
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * 注册websocket端点，客户端将使用它连接到我们的websocket服务器
     * withSockJS（）是用来为不支持websocket浏览器的后备选项，使用了sockJS
     *  STOMP代表简单文本导向的消息传递协议
     *  WebSocket只是一种通信协议。它没有定义诸如以下内容：如何仅向订阅特定主题的用户发送消息，或者如何向特定用户发送消息。
     *  我们需要STOMP来实现这些功能
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }


    /**
     * 配置了一个消息代理，用于将消息由一个客户端路由到另一个客户端
     * 以“/app”开头的消息应该路由到消息处理方法
     * 以“/topic”开头的消息应该路由到消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}

