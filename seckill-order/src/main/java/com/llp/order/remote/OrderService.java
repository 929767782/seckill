package com.llp.order.remote;

import com.llp.common.entity.SeckillOrder;

public interface OrderService {
    public SeckillOrder getSeckillOrderByOrderId(long orderId);
}
