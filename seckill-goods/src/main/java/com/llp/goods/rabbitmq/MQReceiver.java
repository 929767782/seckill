package com.llp.goods.rabbitmq;


import com.llp.common.entiry.User;
import com.llp.common.redis.RedisService;
import com.llp.common.service.GoodsService;
import com.llp.common.service.SeckillService;
import com.llp.common.vo.GoodsVo;
import com.llp.goods.remote.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 *  接收消息
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);


    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RabbitListener(queues=MQConfig.SECKILL_QUEUE)
    public void seckillListener(String message){
        log.info("receive message:"+message);
        SeckillMessage m = RedisService.stringToBean(message, SeckillMessage.class);
        User user = m.getUser();
        long goodsId = m.getGoodsId();

        //这步可有可无，就是再判断一下库存有没有
        GoodsVo goodsVo = goodsService.getUploadGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            return;
        }

        //减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goodsVo);
    }

    @RabbitHandler
    public void orderCloseListener(String message, Message msg, Channel channel) throws IOException {
        log.info("receive message:"+message);
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        OrderMessage m = RedisService.stringToBean(message, OrderMessage.class);
        long orderId = m.getOrderId();
        try {
            orderService.closeOrder(orderId);
            channel.basicAck(deliveryTag,false);
        } catch (Exception e){
            channel.basicReject(deliveryTag,true);
        }

    }


    @RabbitHandler
    public void stockReleaseListener(String message, Message msg, Channel channel) throws IOException {
        StockReleaseMessage m = RedisService.stringToBean(message, StockReleaseMessage.class);
        try {
            goodsService.releaseStock(m);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(msg.getMessageProperties().getDeliveryTag(),true);
        }

    }

}
