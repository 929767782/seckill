package com.llp.common.vo;

import com.llp.common.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 *  商品详情页VO
 */
@Getter
@Setter
public class GoodsDetailVo {
    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private User user;

}
