package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
全局异常处理类
 */
@RestControllerAdvice  //这个表示返回的都是json
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            //是否属于全局异常
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException){
            //是否属于validation组件或相关自定义验证的错误
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数验证异常：" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }

        return RespBean.error(RespBeanEnum.ERROR);
    }

}
