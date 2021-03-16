package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;

/**
 * <p>
 * 秒杀关联表 服务类
 * </p>
 *
 * @author ty
 * @since 2021-03-12
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    /*
    获取秒杀结果
     */

    Long getResult(User user, Long goodsId);
}
