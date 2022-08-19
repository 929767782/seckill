package com.llp.user.rabbitmq;


import com.llp.common.entiry.User;
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
