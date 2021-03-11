package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
全局异常
 */
@Data
@NoArgsConstructor  //空参构造
@AllArgsConstructor //全参构造
//继承runtimeexception
public class GlobalException extends RuntimeException{
    private RespBeanEnum respBeanEnum;

}
