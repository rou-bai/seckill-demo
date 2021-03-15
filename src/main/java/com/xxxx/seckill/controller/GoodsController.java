package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
商品
优化前： 1000 * 10  吞吐量QPS:2077
缓存QPS:3040
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;  //这个可以用来手动渲染页面

    @GetMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,
                         User user,
                         HttpServletResponse response,
                         HttpServletRequest request){
        //从redis中获取页面，如果不为空，则直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isBlank(html)){
            return html;
        }

        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        //如果redis内页面为空，则需要手动渲染页面，并存入redis
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if(!StringUtils.isBlank(html)){
            //存入redis
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }

        return html;
//        return "goods_list";
    }

    @GetMapping(value = "/detail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String detail2(@PathVariable("goodsId") long goodsId,
                         User user,
                         Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        //数据存redis缓存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if(!StringUtils.isBlank(html)){
            return html;
        }

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

        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if(!StringUtils.isBlank(html)){
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;
//        return "goods_detail";
    }


    @GetMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public RespBean detail(@PathVariable("goodsId") long goodsId, User user){
        //数据通过ajax和前端对接
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);

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

        //构造object
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(seckillStatus);
        return RespBean.success(detailVo);
//        return "goods_detail";
    }


}
