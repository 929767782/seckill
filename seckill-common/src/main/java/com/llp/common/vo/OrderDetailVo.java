package com.llp.common.vo;

import com.llp.common.entity.Order;
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
