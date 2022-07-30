package com.llp.seckill.service;


import com.llp.seckill.mapper.GoodsMapper;
import com.llp.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *   商品服务类
 */
@Service
public class GoodsService {


    @Autowired
    GoodsMapper goodsMapper;

    /**
     * 查询商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    /**
     * 根据id查询指定商品
     *
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 减少库存，每次减一
     *
     * @return
     */
    public boolean reduceStock(GoodsVo goods) {
        int ret = goodsMapper.reduceStockByVersion(goods.getId());
        return ret > 0;
    }
}
