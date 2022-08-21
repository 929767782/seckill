package com.llp.order.service;

import com.llp.common.entity.Order;
import com.llp.common.entity.SeckillOrder;
import com.llp.common.entity.User;
import com.llp.common.vo.GoodsVo;
import com.llp.order.mapper.OrderMapper;
import com.llp.order.rabbitmq.MQSender;
import com.llp.order.rabbitmq.StockReleaseMessage;
import com.llp.order.redis.OrderKey;
import com.llp.order.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *  订单服务类
 */
@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId, SeckillOrder.class);
    }

    public SeckillOrder getSeckillOrderByOrderId(long orderId) {
        return orderMapper.getSeckillOrderByOrderId(orderId);
    }

    public Order getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }

    /**
     * 因为要同时分别在订单详情表和秒杀订单表都新增一条数据，所以要保证两个操作是一个事务
     */
    @Transactional
    public Order createOrder(User user, GoodsVo goods) {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());
        orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + goods.getId(), seckillOrder);

        return order;
    }


    @Transactional
    public void closeOrder(long orderId) {

        Order order = orderMapper.getOrderById(orderId);
        if(order.getStatus()==0){
            orderMapper.updateStatus(orderId);
            StockReleaseMessage srm = new StockReleaseMessage();
            srm.setGoodsId(order.getGoodsId());
            srm.setOrderId(orderId);
            sender.sendStockReleaseMessage(srm);
        }
    }




}
