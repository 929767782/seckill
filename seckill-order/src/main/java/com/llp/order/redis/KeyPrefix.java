package com.llp.order.redis;

/**
 *  redis的key前缀
 */
public interface KeyPrefix {

    /**
     * 有效期
     * @return
     */
    int expireSeconds();

    /**
     * 前缀
     * @return
     */
    String getPrefix();
}
