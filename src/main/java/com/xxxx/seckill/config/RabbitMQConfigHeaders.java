package com.xxxx.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/*
MQ配置类：headers广播模式
 */
@Configuration
public class RabbitMQConfigHeaders {
    private static final String QUEUE01 = "queue_header01";
    private static final String QUEUE02 = "queue_header02";
    private static final String EXCHANGE = "headersExchange";  //交换机

    @Bean
    public Queue queue01Headers(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02Headers(){
        return new Queue(QUEUE02);
    }

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(EXCHANGE);
    }

    @Bean
    public Binding binding01Headers(){
        Map<String, Object> map = new HashMap<>();
        map.put("color", "red");
        map.put("speed", "low");
        //通过键值对去绑定,whereAny表示匹配上其中一个即可
        return BindingBuilder.bind(queue01Headers()).to(headersExchange()).whereAny(map).match();
    }

    @Bean
    public Binding binding02Headers(){
        Map<String, Object> map = new HashMap<>();
        map.put("color", "red");
        map.put("speed", "fast");
        //通过键值对去绑定,whereAll表示匹配上全部才可以
        return BindingBuilder.bind(queue02Headers()).to(headersExchange()).whereAll(map).match();
    }
}
