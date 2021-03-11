package com.xxxx.seckill.controller;

/*
测试
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/index")
    public String index(){
        return "demo";
    }
}
