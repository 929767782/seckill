package com.llp.goods.mapper;

import com.llp.goods.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 *   商品相关的Mapper
 */
@Mapper
public interface  GoodsMapper {

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price from sk_goods_seckill sg left join sk_goods g on sg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price from sk_goods_seckill sg left join sk_goods g on sg.goods_id = g.id where sg.start_date between #{startTime} and #{endTime}")
    public List<GoodsVo> listGoodsVoBetweenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price  from sk_goods_seckill sg left join sk_goods g  on sg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    //stock_count > 0
    @Update("update sk_goods_seckill set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(@Param("goodsId") long goodsId);



}