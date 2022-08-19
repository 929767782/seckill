package com.llp.kill.rabbitmq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockReleaseMessage {
    private long goodsId;
    private long orderId;
}
