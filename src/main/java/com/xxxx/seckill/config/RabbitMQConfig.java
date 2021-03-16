package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final String FANOUTQUEUE1 = "queue_fanout01";
    private static final String FANOUTQUEUE2 = "queue_fanout02";
    private static final String FANOUTEXCHANGE = "fanoutExchange";

    @Bean
    public Queue queue(){
        return new Queue("queue", true);
    }

    @Bean
    public Queue queueFanout01(){
        return new Queue(FANOUTQUEUE1);
    }

    @Bean
    public Queue queueFanout02(){
        return new Queue(FANOUTQUEUE2);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUTEXCHANGE);
    }

    @Bean
    public Binding bingFanout01(){
        //绑定到交换机,fanout广播模式
        return BindingBuilder.bind(queueFanout01()).to(fanoutExchange());
    }

    @Bean
    public Binding bingFanout02(){
        //绑定到交换机,fanout广播模式
        return BindingBuilder.bind(queueFanout02()).to(fanoutExchange());
    }
}
