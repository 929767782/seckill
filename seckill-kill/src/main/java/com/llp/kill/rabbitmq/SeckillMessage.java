package com.llp.kill.rabbitmq;


import com.llp.common.entity.User;
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
