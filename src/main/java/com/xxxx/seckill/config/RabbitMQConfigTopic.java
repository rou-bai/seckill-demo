package com.xxxx.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
topic模式
 */
@Configuration
public class RabbitMQConfigTopic {
    private static final String QUEUE01 = "queue_topic01";
    private static final String QUEUE02 = "queue_topic02";
    private static final String EXCHANGE = "topicExchange";  //交换机

    //通配符匹配时，#表示匹配多个单词，*表示匹配一个单词，单词之间用.分隔;有*时还必须有一个匹配
    private static final String ROUTINGKEY01 = "#.queue.#";  //路由键
    private static final String ROUTINGKEY02 = "*.queue.#";  //路由键

    @Bean
    public Queue queue01Topic(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02Topic(){
        return new Queue(QUEUE02);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding01Topic(){
        //队列绑定交换机并且指定路由Key
        return BindingBuilder.bind(queue01Topic()).to(topicExchange()).with(ROUTINGKEY01);
    }

    @Bean
    public Binding binding02Topic(){
        //队列绑定交换机并且指定路由Key
        return BindingBuilder.bind(queue02Topic()).to(topicExchange()).with(ROUTINGKEY02);
    }
}
