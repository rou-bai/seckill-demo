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
    //登陆模块5002x
    LOGIN_ERROR(500210, "用户名或者密码错误"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常"),
    MOBILE_NOT_EXISTS(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAILED(500214, "修改密码失败"),
    SESSION_ERROR(500215, "用户不存在"),
    //秒杀模块5005x
    EMPTY_STOCK(500500, "库存不足"),
    REPETE_STOCK(500501, "该商品每人限购一件"),
    GOODS_NOT_EXISTS(500502, "商品不存在"),
    //订单模块5004x
    ORDER_NOT_EXISTS(500400, "订单不存在");

    private final Integer code;
    private final String message;
}
