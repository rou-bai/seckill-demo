package com.xxxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_fanout01")
    public void fanout01Receive(Object msg){
        log.info("queue_fanout01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void fanout02Receive(Object msg){
        log.info("queue_fanout02接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct01")
    public void direct01Receive(Object msg){
        log.info("queue01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void direct02Receive(Object msg){
        log.info("queue02接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_topic01")
    public void topic01Receive(Object msg){
        log.info("queue01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_topic02")
    public void topic02Receive(Object msg){
        log.info("queue02接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_header01")
    public void headers01Receive(Message msg){
        log.info("queue01接收Message对象：" + msg);
        log.info("queue01接收消息:" + new String(msg.getBody()));
    }

    @RabbitListener(queues = "queue_header02")
    public void headers02Receive(Message msg){
        log.info("queue02接收Message对象：" + msg);
        log.info("queue02接收消息:" + new String(msg.getBody()));
    }
}
