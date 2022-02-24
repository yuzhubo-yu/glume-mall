package com.glume.glumemall.order.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ 配置
 * 容器中的 Binding, Queue, Exchange 都会自动创建（RabbitMQ 没有的情况）
 * @author tuoyingtao
 * @create 2022-02-23 15:16
 */
@Configuration
public class MyMQConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMQConfig.class);

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }

    @Bean
    public Queue orderDelayQueue() {
        Map<String,Object> arguments = new HashMap<>();
        // 设置死信路由
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        // 设置路由键
        arguments.put("x-dead-letter-routing-key","order.release.order");
        // 过期时间
        arguments.put("x-message-ttl",60000);
        return new Queue("order.delay.queue", true, false, false,arguments);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        Binding binding = new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",null);
        return binding;
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        Binding binding = new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",null);
        return binding;
    }

    /**
     * 绑定订单释放直接通知库存进行释放
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        Binding binding = new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",null);
        return binding;
    }
}
