package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ty
 * @since 2021-03-11
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MQSender mqSender;
    /*
    用户信息（测试）
     */
    @RequestMapping("/info")
    public RespBean info(User user){
        return RespBean.success(user);
    }

    /*
    测试mq发送接收消息
     */
    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("我好烦");
    }

    /*
    测试mq发送接收消息，fanout广播模式
     */
    @RequestMapping("/fanoutMQ")
    @ResponseBody
    public void fanoutMq(){
        mqSender.fanoutSend("广播模式");
    }

    /*
    测试mq发送接收消息，direct直连模式
     */
    @RequestMapping("/directMQ/queue01")
    @ResponseBody
    public void directMQQueue01(){
        mqSender.directSend01("direct模式:red");
    }

    @RequestMapping("/directMQ/queue02")
    @ResponseBody
    public void directMQQueue02(){
        mqSender.directSend02("direct模式:green");
    }

}
