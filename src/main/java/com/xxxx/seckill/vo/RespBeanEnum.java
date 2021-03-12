package com.xxxx.seckill.vo;
/*
公共返回对象枚举
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "成功"),
    ERROR(500, "服务端异常"),
    //登陆模块
    LOGIN_ERROR(500210, "用户名或者密码错误"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
     BIND_ERROR(500212, "参数校验异常"),
    //秒杀模块
    EMPTY_STOCK(500500, "库存不足"),
    REPETE_STOCK(500501, "该商品每人限购一件"),
    GOODS_NOT_EXISTS(500502, "商品不存在");
    private final Integer code;
    private final String message;
}
