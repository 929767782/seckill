package com.llp.common.utils;

import java.util.UUID;

/**
 * 唯一id生成类
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
