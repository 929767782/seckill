package com.llp.goods.redis;

/**
 *  订单key前缀
 */
public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }
    public static OrderKey getSeckillOrderByUidGid = new OrderKey("seckill");
}
