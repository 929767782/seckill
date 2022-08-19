package com.llp.common.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *   订单信息
 */
@Getter
@Setter
public class Order {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;//0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成，6已取消
    private Date createDate;
    private Date payDate;
}
