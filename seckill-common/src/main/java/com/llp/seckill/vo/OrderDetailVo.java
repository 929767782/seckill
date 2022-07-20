package com.llp.seckill.vo;


import com.llp.seckill.entiry.Order;
import lombok.Getter;
import lombok.Setter;

/**
 *  订单详情页VO
 */
@Getter
@Setter
public class OrderDetailVo {
    private GoodsVo goods;
    private Order order;
}
