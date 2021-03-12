package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.service.impl.GoodsServiceImpl;
import com.xxxx.seckill.service.impl.UserServiceImpl;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SecKillController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGoodsService goodsService;

    /*
    秒杀
     */

    @RequestMapping("/doSecKill")
    public String doSecKill(Model model, User user, @RequestParam("goodsId") long goodsId){
        if(null == user){
            return "login";
        }
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
        if(goods == null){
            //商品不存在
            model.addAttribute("errmsg", RespBeanEnum.GOODS_NOT_EXISTS.getMessage());
            return "kill_fail";
        }else if(goods.getStockCount() < 1){
            //商品库存不足
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "kill_fail";
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder =
                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId()).eq("goods_id", goodsId));
        if(seckillOrder != null){
            model.addAttribute("errmsg", RespBeanEnum.REPETE_STOCK.getMessage());
            return "kill_fail";
        }

        Order order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

}
