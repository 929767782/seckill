package com.llp.goods.service;

import com.llp.common.entity.SeckillOrder;
import com.llp.goods.mapper.GoodsMapper;
import com.llp.goods.rabbitmq.StockReleaseMessage;
import com.llp.goods.redis.GoodsKey;
import com.llp.goods.redis.RedisService;
import com.llp.goods.remote.OrderService;
import com.llp.goods.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *   商品服务类
 */
@Service
public class GoodsService {


    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    /**
     * 查询商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    /**
     * 查询商品列表
     *
     * @return
     */
    public List<GoodsVo> listUploadGoodsVo() {
        return redisService.getRange(GoodsKey.getUploadGoods,GoodsVo.class);
    }


    /**
     * 查询指定时间内的商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVoBetweenTime() {
        return goodsMapper.listGoodsVoBetweenTime(getStartTime(),getEndTime());
    }

    /**
     * 根据id查询指定商品
     *
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 根据id查询指定上架商品
     *
     * @return
     */
    public GoodsVo getUploadGoodsVoByGoodsId(long goodsId) {
        return redisService.get(GoodsKey.getUploadGoods,""+goodsId, GoodsVo.class);
    }

    /**
     * 减少库存，每次减一
     *
     * @return
     */
    public boolean reduceStock(GoodsVo goods) {
        int ret = goodsMapper.reduceStock(goods.getId());
        return ret > 0;
    }

    /**
     * 释放库存，每次加一
     *
     * @return
     */
    public boolean releaseStock(StockReleaseMessage msg) {
        long orderId = msg.getOrderId();
        SeckillOrder seckillOrder = orderService.getSeckillOrderByOrderId(orderId);
        //不是秒杀订单的取消，则释放商品表库存
        if(seckillOrder==null){
            //TODO
        }else{//是秒杀订单的取消，则释放秒杀商品表库存
            //TODO
        }

        return true;
    }


    //当前天数的 00:00:00
    private Date getStartTime() {
        LocalDate now = LocalDate.now();
        LocalDateTime time = now.atTime(LocalTime.MIN);
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    //当前天数+2 23:59:59..
    private Date getEndTime() {
        LocalDate now = LocalDate.now();
        LocalDateTime time = now.plusDays(2).atTime(LocalTime.MAX);
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}