package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
MQ配置类：direct直连模式
 */
@Configuration
public class RabbitMQConfigDirect {
    private static final String QUEUE01 = "queue_direct01";
    private static final String QUEUE02 = "queue_direct02";
    private static final String EXCHANGE = "directExchange";  //交换机
    private static final String ROUTINGKEY01 = "queue.red";  //路由键
    private static final String ROUTINGKEY02 = "queue.green";  //路由键

    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding01(){
        //队列绑定交换机并且指定路由Key
        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
    }

    @Bean
    public Binding binding02(){
        //队列绑定交换机并且指定路由Key
        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
    }
}
