package com.llp.order.redis;

/**
 * 商品key前缀
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getGoodsStock = new GoodsKey(0, "gs");
    public static GoodsKey getUploadGoods = new GoodsKey(0, "ug");
    public static GoodsKey getUploadGoods(int expire){
        return new GoodsKey(expire, "ug");
    }
    public static GoodsKey getGoodsStock(int expire){
        return new GoodsKey(expire, "gs");
    }
}
