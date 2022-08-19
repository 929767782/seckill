package com.llp.user.remote;

import com.llp.common.entity.SeckillOrder;

public interface OrderService {
    public SeckillOrder getSeckillOrderByOrderId(long orderId);
}
