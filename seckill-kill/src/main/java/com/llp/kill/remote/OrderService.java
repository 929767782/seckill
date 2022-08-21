package com.llp.kill.remote;

import com.llp.common.entity.SeckillOrder;

public interface OrderService {

    SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId);
}
