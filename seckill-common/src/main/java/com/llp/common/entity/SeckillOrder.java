package com.llp.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *  秒杀订单信息
 */
@Getter
@Setter
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;

}
