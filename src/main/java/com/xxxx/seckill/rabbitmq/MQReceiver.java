package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.pojo.SeckillMessage;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void fanout01Receive(Object msg){
//        log.info("queue_fanout01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void fanout02Receive(Object msg){
//        log.info("queue_fanout02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void direct01Receive(Object msg){
//        log.info("queue01接收消息：" + msg);
//    }

//    @RabbitListener(queues = "queue_direct02")
//    public void direct02Receive(Object msg){
//        log.info("queue02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void topic01Receive(Object msg){
//        log.info("queue01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void topic02Receive(Object msg){
//        log.info("queue02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void headers01Receive(Message msg){
//        log.info("queue01接收Message对象：" + msg);
//        log.info("queue01接收消息:" + new String(msg.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void headers02Receive(Message msg){
//        log.info("queue02接收Message对象：" + msg);
//        log.info("queue02接收消息:" + new String(msg.getBody()));
//    }
    /*
    秒杀下单操作
     */
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void headers01Receive(String msg){
        log.info("秒杀接收的消息对象：" + msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Long goodsId = message.getGoodsId();
        User user = message.getUser();

        //判断库存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        if(goodsVo.getStockCount() < 1){
            //redis内存储判断商品是否为空库存的标志，用来在用户查询秒杀结果时候用
            valueOperations.set("isStockEmpty:" + goodsId, 0);
            return;
        }

        //通过redis判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:" + user.getId() + goodsId);
        if(seckillOrder != null){
            return ;
        }

        //下单
        orderService.seckill(user, goodsVo);

    }
}
