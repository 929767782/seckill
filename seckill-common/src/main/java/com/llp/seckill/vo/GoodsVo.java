package com.llp.seckill.vo;


import com.llp.seckill.entiry.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *  商品列表VO
 */
@Getter
@Setter
public class GoodsVo extends Goods {
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    //当前商品秒杀的随机码
    private String randomCode;
}

