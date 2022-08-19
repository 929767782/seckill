package com.llp.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *  商品信息
 */
@Getter
@Setter
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;

}
