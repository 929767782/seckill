package com.llp.seckill.service;


import com.llp.seckill.entiry.Order;
import com.llp.seckill.entiry.SeckillOrder;
import com.llp.seckill.entiry.User;
import com.llp.seckill.redis.GoodsKey;
import com.llp.seckill.redis.RedisService;
import com.llp.seckill.redis.SeckillKey;
import com.llp.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *  秒杀服务类
 */
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    //保证这三个操作，减库存 下订单 写入秒杀订单是一个事务
    @Transactional
    public Order seckill(User user, GoodsVo goods){
        //减库存
        boolean success = goodsService.reduceStock(goods);
        if (success){
            //下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getSeckillResult(long userId, long goodsId){
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){
            return order.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    public void uploadSeckillSkuLatest3Days(){
        List<GoodsVo> goodsVos = goodsService.listGoodsVoBetweenTime();
        for(GoodsVo good:goodsVos){
            //生成商品随机码，防止恶意攻击
            String token = UUID.randomUUID().toString().replace("-", "");
            good.setRandomCode(token);
            long expire = good.getEndDate().getTime() - System.currentTimeMillis();
            expire = expire/1000;
            //商品上架
            //key为商品id，value为商品vo
            redisService.set(GoodsKey.getUploadGoods((int)expire),good.getId().toString(),good);
            //key为商品随机码，value为商品库存
            redisService.set(GoodsKey.getGoodsStock((int)expire),token,good.getStockCount());
        }

    }


    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }
}
