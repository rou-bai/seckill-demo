package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.http.HttpSession;

/*
商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @GetMapping("/toList")
    public String toList(HttpSession session, Model model, @CookieValue("userTicket") String ticket){
        if(StringUtils.isBlank(ticket)){
            return "login";
        }
        User user = (User) session.getAttribute(ticket);
        if(null == user){
            return "login";
        }
        model.addAttribute("user", user);
        return "goods_list";
    }

    @GetMapping("/goodsList")
    public String goodsList(){
        return "goods_list";
    }

}
