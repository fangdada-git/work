package com.tuanche.directselling.constant;

import com.tuanche.directselling.dto.CacheKeyConfig;

/**
 * @description: 公共缓存类
 * @author: gongbo
 * @date: 2020年3月4日17:27:20
*/
public class BaseCacheKeys {

    public static final CacheKeyConfig CACHE_DEMO = new CacheKeyConfig("demo:tencent:signature",2*24*60*60*1000);
    // 百城主播排行榜
    public static final CacheKeyConfig DEALER_ANCHOR_RANKING = new CacheKeyConfig("dealer:anchor:ranking:{0}",3*60*60*1000);

    // 百城车展会场 -  城市直播间数据
    public static final CacheKeyConfig ANCHOR_LIVE_CITY = new CacheKeyConfig("anchor:live:city:{0}",2*60*60*1000);

    // 百城车展会场 -  主播城市数据
    public static final CacheKeyConfig ANCHOR_CITY_LIST = new CacheKeyConfig("anchor:city:list",2*60*60*1000);

    // 所有未结束场次活动  的所有城市数据
    public static final CacheKeyConfig NOT_END_SCENE_CITY_CACHE = new CacheKeyConfig("scene:not:end:city:",24*60*60);

    // 直播会场 — 实时播报直播动态数据
    public static final CacheKeyConfig ROLL_NOTICE_DYNAMIC_PERIODS = new CacheKeyConfig("roll:notice:dynamic:periods:{0}",5*60*1000);

    // 直播会场 — 好店推荐直播数据
    public static final CacheKeyConfig REC_DEALER_LIVE_PERIODS_CITY = new CacheKeyConfig("rec:dealer:live:periods:{0}:city:{1}",5*60*1000);

    // 直播会场 — 直播列表数据接口
    public static final CacheKeyConfig LIVING_PERIODS_CITY = new CacheKeyConfig("living:periods:{0}:city:{1}",5*60*1000);

    // 长安云车展 - 正在直播数据缓存
    public static final CacheKeyConfig CHANGAN_PERIODS_LIVING_CACHE = new CacheKeyConfig("living:periods:changan",5*60*1000);

    // 长安云车展 - 直播回放数据缓存
    public static final CacheKeyConfig CHANGAN_PERIODS_PLAYBACK_CACHE = new CacheKeyConfig("live:playback:periods:changan",5*60*1000);

    // 油卡赠品-赠送商品订单缓存-12小时
    public static final CacheKeyConfig REFUELING_CARD_ORDER_CACHE = new CacheKeyConfig("refuelingcard:order:",60*60*12);

    //裂变活动开启后 裂变任务配置规则key
    public static final CacheKeyConfig FISSION_ACTIVITY_CONFIG_CACHE = new CacheKeyConfig("fission:activity:config:",60*60*12);
    //裂变活动开启后 裂变任务配置规则key-5天
    //裂变活动开启后 裂变任务配置规则key-5天
    public static final CacheKeyConfig FISSION_USER_INFO_CACHE = new CacheKeyConfig("fission:user:info:",60*60*36);
    public static final CacheKeyConfig FISSION_USER_LOCK = new CacheKeyConfig("fission:user:lock:",30);
    //裂变活动-销售缓存,KEY=裂变活动ID+wxunionid-15天
    public static final CacheKeyConfig FISSION_SALE_FISSIONID_WXUNIONID_CACHE = new CacheKeyConfig("fission:sale:fissionId:{0}:wxunionid:{1}",1000*60*60*24*15);
    //裂变活动-缓存,用户任务的完成缓存-5天
    public static final CacheKeyConfig FISSION_FISSIONID_USER_TASK_ISCOMPLETE = new CacheKeyConfig("fission:fissionId:{0}:user:{1}:task:{2}:isComplete",1000*60*60*24*5);
    //裂变活动-缓存,用户第一个非自然渠道记录-5天
    public static final CacheKeyConfig FISSION_FISSIONID_USER_FIRST_UNNATURAL_CHANNEL = new CacheKeyConfig("fission:fissionId:{0}:user:{1}:firstUnnaturalChannel",1000*60*60*24*5);

    //任务字典
    public static final CacheKeyConfig FISSION_DICT_CONFIG_CACHE = new CacheKeyConfig("fission:dict:config:",24*60*60*1000);

    /**
     * 裂变活动效果数（浏览/预约/分享）缓存key 有效期7天
     */
    public static final CacheKeyConfig FISSION_EFFECT_NUM_CACHE = new CacheKeyConfig("fission:effect:num:fid:{0}", 7 * 24 * 60 * 60 * 1000);
    /**
     * 裂变活动微信订阅消息场景值缓存key
     */
    public static final CacheKeyConfig FISSION_WX_SUBSCRIBE_SCENE_CACHE = new CacheKeyConfig("fission:wxscene:fid:{0}", 2 * 60 * 60 * 1000);

    /**
     * 裂变活动-销售收入计算锁-8分钟
     */
    public static final CacheKeyConfig FISSION_INCOME_STATISTICS_LOCK = new CacheKeyConfig("fission:income:statistics:", 8 * 60);
    /**
     * 裂变活动-销售是否存在-15天
     */
    public static final CacheKeyConfig FISSION_HAS_SALE = new CacheKeyConfig("fission:sale:hassale:", 1000 * 60 * 60 * 24 * 15);




    /** 售后特卖-活动用户缓存-5天 */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITYID_USER = new CacheKeyConfig("aftersale:activityId:{0}:user:{1}:", 1000* 60 * 60*24*5);
    /** 售后特卖-代理人缓存-31天 */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITYID_AGENT = new CacheKeyConfig("aftersale:activityId:{0}:agent:{1}:", 1000* 60 * 60*24*15);
    /** 售后特卖-代理人奖励记录缓存-5天 */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITYID_REWARDRECORD_USER_REWARDTYPE = new CacheKeyConfig("aftersale:activityId:{0}:rewardRecord:user:{1}:rewardType:{1}", 1000* 60 * 60*24*5);
    /** 售后特卖-订单奖励缓存-5天 */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITYID_ORDER_USERTYPE_REWARDTYPE = new CacheKeyConfig("aftersale:activityId:{0}:rewardRecord:orde:{1}:userType:{2}:rewardType:{3}", 1000* 60 * 60*24*5);
    /** 售后特卖-分享人的奖励记录分布式锁-未获取到锁等待10*1000毫秒 */
    public static final CacheKeyConfig AFTER_SALE_ACTIVITYID_REWARDRECORD_SHARER_LOCK = new CacheKeyConfig("aftersale:activityId:{0}:rewardRecord:sharer:{1}:lock", 1000* 10);
    /** 售后特卖-第一次余额预警-缓存时间为活动结束时间 */
    public static final CacheKeyConfig AFTER_SALE_AMOUNT_ALARM_FIRST_ACTIVITYID = new CacheKeyConfig("aftersale:amount_alarm_first:activityId:{0}", 1000 * 10);
    /** 售后特卖-分账订单查询ID */
    public static final CacheKeyConfig AFTER_SALE_SUB_ACCOUNT_ORDER_QUERY_ID = new CacheKeyConfig("aftersale:SubAccount:orderQueryId:", 1000 * 10);
    /**
     * 售后特卖 同步流失客户-5分钟
     */
    public static final CacheKeyConfig AFTER_SALE_SYNC_USER = new CacheKeyConfig("aftersale:sync:user:{0}", 5 * 60 * 1000);

    /**
     * 潮车集 初始化中奖码分布式锁
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_INIT_LOCK = new CacheKeyConfig("fashion_car:num_init_lock:{0}", 3000);

    /**
     * 潮车集 发中奖码分布式锁
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_SEND_LOCK = new CacheKeyConfig("fashion_car:num:send:lock:{0}:{1}", 3000);

    /**
     * 潮车集 已发出的中奖码
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_SENT = new CacheKeyConfig("fashion_car:num:sent:{0}:{1}");

    /**
     * 潮车集 已生成的中奖码数量
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_CREATED_COUNT = new CacheKeyConfig("fashion_car:num:created:count:{0}");

    /**
     * 潮车集 中奖码已发出的数量
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_SEND_COUNT = new CacheKeyConfig("fashion_car:num:sent:count:{0}");

    /**
     * 潮车集 中奖码LIST
     */
    public static final CacheKeyConfig FASHION_CAR_NUM_LIST = new CacheKeyConfig("fashion_car:num:list:{0}");

    /**
     * 潮车集 用户获得的中奖码
     */
    public static final CacheKeyConfig FASHION_CAR_USER_NUM_LIST = new CacheKeyConfig("fashion_car:num:user:list:{0}:{1}");

    /**
     * 潮车集 用户获得的中奖码锁
     */
    public static final CacheKeyConfig FASHION_CAR_USER_NUM_LIST_LOCK = new CacheKeyConfig("fashion_car:num:user:list:lock:{0}:{1}", 3);

    /**
     * 潮车集活动品牌列表
     */
    public static final CacheKeyConfig FASHION_CAR_BRAND_LIST_CACHE = new CacheKeyConfig("fashion_car:brand:list:", 60 * 60 * 12);

    /**
     * 半价车活动信息
     */
    public static final CacheKeyConfig FASHION_CAR_HALF_PRICE_CACHE = new CacheKeyConfig("fashion_car:halft:price:", 60 * 60 * 12);
}
