package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @GetMapping("/toList")
    public String toList(Model model, @CookieValue("userTicket") String ticket,
                         HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isBlank(ticket)){
            return "login";
        }
//        User user = (User)session.getAttribute("ticket");
        User user = userService.getUserByCookie(ticket, request, response);
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
