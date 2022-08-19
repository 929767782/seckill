package com.llp.order.rabbitmq;

import com.llp.common.entiry.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单消息体
 */
@Getter
@Setter
public class OrderMessage {
    private User user;
    private long goodsId;
    private long orderId;
}
