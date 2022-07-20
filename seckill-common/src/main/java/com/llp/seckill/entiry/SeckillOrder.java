package com.llp.seckill.entiry;

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
