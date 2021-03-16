package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillMessage;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    /*
    秒杀
    优化前： 1000 * 10  吞吐量QPS:2876
    缓存QPS：1458
     */

    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, @RequestParam("goodsId") long goodsId){
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
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
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

    @RequestMapping(value = "/doSecKill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(User user, @RequestParam("goodsId") long goodsId) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购, 更改为通过redis去取，由uid+goodsid确定同一用户不能抢购两单
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:" + user.getId() + goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPETE_STOCK);
        }

        //内存标记，防止redis多余访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //库存预减
        //decrement:递减，原子型
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if(stock < 0){
            //当为-1时让他为0，更好看
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //rabbitmq发送消息对象
        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(message));
        return RespBean.success(0);

    }

    /*
    系统初始化，把商品库存数量加载到redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });

    }
}
