package com.zz.websocket.redis;

import com.zz.websocket.domain.WebSocketModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @Description RedisListenerBean
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */

@Component
public class RedisListenerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerBean.class);

    @Autowired
    private WebSocketModel webSocketModel;


    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把相应的消息监听器和消息订阅处理器绑定
     * 该消息监听器通过反射技术调用消息订阅处理器的一些方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听msgToAll
        container.addMessageListener(listenerAdapter, new PatternTopic(webSocketModel.msgToAll));
        container.addMessageListener(listenerAdapter, new PatternTopic(webSocketModel.userStatus));
        LOGGER.info("subscribed redis channel: " + webSocketModel.msgToAll);
        return container;
    }
}
