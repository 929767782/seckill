package com.llp.seckill.rabbitmq;


import com.llp.seckill.entiry.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息体
 */
@Getter
@Setter
public class SeckillMessage {

    private User user;
    private long goodsId;
}
