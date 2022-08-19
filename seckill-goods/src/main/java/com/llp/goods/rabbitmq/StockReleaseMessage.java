package com.llp.goods.rabbitmq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockReleaseMessage {
    private long goodsId;
    private long orderId;
}
