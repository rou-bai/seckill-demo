package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/*
商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @GetMapping("/toList")
    public String toList(Model model, User user){
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goods_list";
    }

    @GetMapping("/detail/{goodsId}")
    public String detail(@PathVariable("goodsId") long goodsId, User user,
                         Model model){
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        model.addAttribute("goods", goodsVo);

        //秒杀状态判断
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();
        int seckillStatus = 0;
        long remainSeconds = 0;

        if(now.before(startDate)){
            //秒杀未开始
            remainSeconds = (startDate.getTime() - now.getTime()) / 1000;
        }else if(now.after(endDate)){
            //秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀进行时
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}
