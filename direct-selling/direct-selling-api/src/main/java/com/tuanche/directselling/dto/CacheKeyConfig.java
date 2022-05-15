package com.tuanche.directselling.dto;

import java.text.MessageFormat;

/**
 * @class: CacheKeyConfig
 * @description: 缓存key配置类
 * @author: gongbo
 * @create: 2020年3月4日17:03:01
 */
public class CacheKeyConfig {
    /**
     * 缓存key
     */
    private final String key;
    /**
     * 单位毫秒
     */
    private final int expire;
    /**
     * 默认过期时间
     */
    private final int DEFAULT_TTL_SIGN = 0;


    public CacheKeyConfig(String key, int expire) {
        this.key = key;
        this.expire = expire;
    }

    public CacheKeyConfig(String key) {
        this.key = key;
        this.expire = DEFAULT_TTL_SIGN;
    }

    public String getKey() {
        return this.key;
    }


    public int getExpire() {
        return expire;
    }


    public String setParamValue(Object... part){
        if(part == null){
            return null;
        }
        return MessageFormat.format(this.key, part);
    }

}
