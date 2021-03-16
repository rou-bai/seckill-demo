package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ty
 * @since 2021-03-12
 */
public interface IOrderService extends IService<Order> {

    /*
    商品秒杀
     */
    Order seckill(User user, GoodsVo goods);

    /*
    获取订单详情
     */
    OrderDetailVo detail(Long orderId);

    /*
    获取秒杀地址
     */
    String createPath(User user, Long goodsId);

    /*
    秒杀路径确认
     */
    Boolean checkPath(String path, User user, long goodsId);
}
