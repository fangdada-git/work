package com.tuanche.directselling.utils;

import com.tuanche.directselling.dto.CacheKeyConfig;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-17 11:49
 */
public class ApiBaseCacheKeys {

    /**
     * 售后特卖活动开启后 -只缓存基础信息key
     */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITY_BASE_CACHE = new CacheKeyConfig("after:sale:activity:base:", 1000 * 60 * 60 * 12);

    /**
     * 售后特卖活动开启后 活动规则key
     */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITY_CONFIG_CACHE = new CacheKeyConfig("after:sale:activity:config:", 60 * 60 * 12);

    /**
     * 售后特卖-订单是否发过奖励的缓存
     */
    public static final CacheKeyConfig AFTER_SALE_ORDER_REWARD = new CacheKeyConfig("aftersale:order:reward:", 1000 * 10);
}
