package com.llp.order.rabbitmq;


import com.llp.order.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 秒杀消息体
 */
@Getter
@Setter
public class SeckillMessage {

    private User user;
    private long goodsId;
}
