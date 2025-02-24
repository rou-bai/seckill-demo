package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
秒杀mq配置：使用topic主机模式
 */
@Configuration
public class RabbitMQSeckillConfig {
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queueSeckill(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchangeSeckill(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingSeckill(){
        return BindingBuilder.bind(queueSeckill()).to(topicExchangeSeckill()).with("seckill.#");
    }
}
