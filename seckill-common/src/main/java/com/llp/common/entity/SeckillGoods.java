package com.llp.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *  秒杀商品信息
 */
@Getter
@Setter
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
