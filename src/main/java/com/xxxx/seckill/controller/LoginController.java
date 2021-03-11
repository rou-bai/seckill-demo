package com.xxxx.seckill.controller;

import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private IUserService UserService;
    //跳转登陆页面
    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    //登陆功能
    @PostMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo){
//        log.info("{}", loginVo);  打印传入参数
        return UserService.doLogin(loginVo);
    }
}
