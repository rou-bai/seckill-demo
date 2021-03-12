package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-03-12
 */
public interface IGoodsService extends IService<Goods> {

    /*
    获取商品列表
     */
    List<GoodsVo> findGoodsVo();
}
