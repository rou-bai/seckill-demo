package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.RespBean;
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
    /*
    用户信息（测试）
     */
    @RequestMapping("/info")
    public RespBean info(User user){
        return RespBean.success(user);
    }

}
