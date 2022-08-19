package com.llp.kill.redis;

/**
 *  手机验证码前缀
 */
public class CodeKey extends BasePrefix{
    public static final int TOKEN_EXPIRE = 60*10;//10分钟

    /**
     * 防止被外面实例化
     */
    private CodeKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 需要缓存的字段
     */
    public static CodeKey code = new CodeKey(TOKEN_EXPIRE,"code");
}
