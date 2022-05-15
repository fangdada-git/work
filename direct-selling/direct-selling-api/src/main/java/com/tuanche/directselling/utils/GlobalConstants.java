package com.tuanche.directselling.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 公共常量类
 * @author: gongbo
 * @date: 2020年3月4日17:04:57
 */
public class GlobalConstants {

    private static final BigDecimal hundred = new BigDecimal("100");
    /**
     * 场次活动相关常量
     **/
    public static class Scene {

        /** 场次活动状态：0未开始 */
        public static Integer SCENE_STATE_0 = 0;
        /** 场次活动状态：1进行中 */
        public static Integer SCENE_STATE_1 = 1;
        /** 场次活动状态：2已结束 */
        public static Integer SCENE_STATE_2 = 2;
        /** 场次活动状态 Map */
        public static Map<Integer, String> SCENE_STATE_MAP = new HashMap<Integer, String>();
        static {
            SCENE_STATE_MAP.put(SCENE_STATE_0, "未开始");
            SCENE_STATE_MAP.put(SCENE_STATE_1, "进行中");
            SCENE_STATE_MAP.put(SCENE_STATE_2, "已结束");
        }
    }

    /**
     * 通用常量
     **/
    public static class Common {
        /** kafka直播通知业务类型： 0更新导播 */
        public static Integer KAFKA_LIVE_NOTICE_TYPE_0 = 0;
        /** kafka直播通知业务类型： 1删除机位 */
        public static Integer KAFKA_LIVE_NOTICE_TYPE_1 = 1;
        /** kafka直播通知业务类型： 2删除节目 */
        public static Integer KAFKA_LIVE_NOTICE_TYPE_2 = 2;
        /** kafka直播通知业务类型 Map */
        public static Map<Integer, String>  KAFKA_LIVE_NOTICE_TYPE_MAP = new HashMap<Integer, String>();

        static {
            KAFKA_LIVE_NOTICE_TYPE_MAP.put(KAFKA_LIVE_NOTICE_TYPE_0, "更新导播");
            KAFKA_LIVE_NOTICE_TYPE_MAP.put(KAFKA_LIVE_NOTICE_TYPE_1, "删除机位");
            KAFKA_LIVE_NOTICE_TYPE_MAP.put(KAFKA_LIVE_NOTICE_TYPE_2, "删除节目");
        }
    }

    //百城主播排行榜
    public static class dealer_anchor_ranking {
        //销冠主播
        public static final Integer anchor_ranking_type1 = 1;
        //人气主播
        public static final Integer anchor_ranking_type2 = 2;
        //活力主播
        public static final Integer anchor_ranking_type3 = 3;
    }

    public enum DEALER_ANCHOR {
        //主播类型
        anchor_type1(1,"经销商主播"),
        anchor_type2(2,"团车主播"),
        anchor_type3(3,"企业账号主播");

        private int code;
        private String desc;

        private DEALER_ANCHOR(int code,String desc){
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){
            return code;
        }
        public String getDesc(){
            return desc;
        }
    }

    /** 油卡状态1：未赠送 */
    public final static Integer REFUELING_CARD_STATE_1 = 1;
    /** 油卡状态2：已赠送 */
    public final static Integer REFUELING_CARD_STATE_2 = 2;
    /** 油卡状态2：已赠送 */
    public final static Integer DELETE_FLAG_TRUE = 2;

    /**
     * 油卡赠送经销商订单渠道
     * @author HuangHao
     * @CreatTime 2020-05-19 10:22
     * @return
     */
    public enum RefuelingCardOrderChannel {
        //主播类型
        ORDER_CHANNEL_TMALL("tmall","淘宝"),
        ORDER_CHANNEL_YOUZAN("youzan","有赞");

        private String code;
        private String text;

        private RefuelingCardOrderChannel(String code,String text){
            this.code = code;
            this.text = text;
        }
        public String getCode(){
            return code;
        }
        public static String getText(String code) {
            if(code==null){
                return null;
            }
            for (RefuelingCardOrderChannel t : RefuelingCardOrderChannel.values()) {
                if (code.equals(t.getCode())) {
                    return t.text;
                }
            }
            return null;
        }
    }

    public static class fission_sales_order {
        /* 裂变-销售订单 订单状态 1：待支付  2：已支付 */
        public static final Integer STATUS_NON_PAYMENT = 1;
        /* 裂变-销售订单 订单状态 1：待支付  2：已支付 */
        public static final Integer STATUS_PAID = 2;
        /* 删除状态 0：未删除 1：已删除  */
        public static final Integer DELETE_FALG_0 = 0;
        /* 删除状态 0：未删除 1：已删除  */
        public static final Integer DELETE_FALG_1 = 1;
        /* 订单类型  1：销售对赌金  */
        public static final Integer TYPE_FISSION = 1;
    }

    /**
     * @description: 交易类型：1 支付对赌金 2 支付商品 3 商品退款 4 销售提现 5 C端用户奖励
     * @author: dudg
     * @date: 2020/9/29 14:03
    */
    public enum FissionTradeType {
        ORDER_PAY(1,"支付对赌金"),
        GOODS_PAY(2,"商品支付"),
        GOODS_REFUND(3,"商品退款"),
        SALE_WITHDRAWAL(4,"销售提现"),
        USER_REWARD(5,"C端用户奖励");

        private Integer code;
        private String text;

        FissionTradeType(Integer code,String text){
            this.code = code;
            this.text = text;
        }
        public Integer getCode(){
            return code;
        }
        public String getText() {
            return getText();
        }
    }
    
    public static class FissionTrade {
        /* 裂变-交易记录 交易状态 0 交易中 1 成功  2 失败 */
        public static final Integer TRADE_STATUS_PAY_ING = 0;
        /* 裂变-交易记录 交易状态 0 交易中 1 成功  2 失败 */
        public static final Integer TRADE_STATUS_PAY_SUCCESS = 1;
        /* 裂变-交易记录 交易状态 0 交易中 1 成功  2 失败 */
        public static final Integer TRADE_STATUS_PAY_FAILED = 2;
    }
    public static class FissionChannel {
        /** 裂变活动渠道名称 Map */
        public  static Map<Integer, String> FISSION_CHANNEL_MAP = new HashMap<>();
        static {
            FISSION_CHANNEL_MAP.put(1, "裸链接");
            FISSION_CHANNEL_MAP.put(2, "销售渠道");
            FISSION_CHANNEL_MAP.put(3, "用户运营渠道");
            FISSION_CHANNEL_MAP.put(4, "团车app");
        }
    }
    public static class FissionOrderSource {
        /** 裂变活动订单来源 Map */
        public static Map<String, String> FISSION_ORDER_SOURCE_MAP = new HashMap<>();
    }

    /* 裂变-扩展类型 1 城市 */
    public final static Byte FISSION_EXTEND_TYPE1 = 1;
    /* 裂变-扩展类型 2 商品 */
    public final static Byte FISSION_EXTEND_TYPE2 = 2;
    /* 裂变-扩展类型 3 经销商 */
    public final static Byte FISSION_EXTEND_TYPE3 = 3;
    /* 裂变-扩展关联状态 是 */
    public final static Byte FISSION_EXTEND_REL_YES = 1;
    /* 裂变-扩展关联状态 否 */
    public final static Byte FISSION_EXTEND_REL_NO = 2;
    /* 裂变-奖励规则类型 B端 */
    public final static Integer FISSION_AWARD_RULE_TYPE_B = 1;
    /* 裂变-奖励规则类型 C端 */
    public final static Integer FISSION_AWARD_RULE_TYPE_C = 2;
    /* 裂变-奖励任务类型code 浏览 */
    public final static String FISSION_AWARD_RULE_TASK_CODE1 = "1";
    /* 裂变-奖励任务类型code 预约直播 */
    public final static String FISSION_AWARD_RULE_TASK_CODE2 = "2";
    /* 裂变-奖励任务类型code 购买活动页 */
    public final static String FISSION_AWARD_RULE_TASK_CODE3 = "3";
    /* 裂变-奖励任务类型code 观看直播 */
    public final static String FISSION_AWARD_RULE_TASK_CODE4 = "4";
    /* 裂变-奖励任务类型code 购买直播商品 */
    public final static String FISSION_AWARD_RULE_TASK_CODE5 = "5";


    /** 裂变-销售收入计算类型-1：预计收入计算 */
    public final static Integer FISSION_SALE_INCOME_TYPE_1 = 1;
    /** 裂变-销售收入计算类型-2：实际收入计算 */
    public final static Integer FISSION_SALE_INCOME_TYPE_2 = 2;
    /** 裂变活动状态-0：未开始 */
    public final static Integer FISSION_ACTIVITY_STATUS_0 = 0;
    /** 裂变活动状态-1：进行中 */
    public final static Integer FISSION_ACTIVITY_STATUS_1 = 1;
    /** 裂变活动状态-2：已结束 */
    public final static Integer FISSION_ACTIVITY_STATUS_2 = 2;
    /** 裂变开启状态-0：未开启 */
    public final static Short FISSION_ON_STATUS_0 = 0;
    /** 裂变开启状态-1：已开启 */
    public final static Short FISSION_ON_STATUS_1 = 1;

    /**
     * 裂变活动效果数类型
     */
    public enum FissionEffectTypeEnum {
        /**
         * 浏览数
         */
        BROWSE_NUM(1, "browse"),
        /**
         * 预约数
         */
        SUBSCRIBE_NUM(1, "subscribe"),
        /**
         * 分享数
         */
        SHARE_NUM(1, "share");

        private Integer code;
        private String key;

        FissionEffectTypeEnum(Integer code, String key) {
            this.code = code;
            this.key = key;
        }

        public Integer getCode() {
            return code;
        }

        public String getKey() {
            return this.key;
        }
    }

    /**
     * 微信订阅消息链接
     */
    public static String WEIXIN_SUBSCRIBE_MSG_URL = "https://mp.weixin.qq.com/mp/subscribemsg?action=get_confirm&appid={APPID}&scene={SCENE}&template_id={TEMPLATEID}&redirect_url={REDIRECTURL}&reserved={RESERVED}#wechat_redirect";

    public static String dateToString(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String underlineStitching(Integer v1,String v2){
        return v1+"_"+v2;
    }

    /**
     * 手机号脱敏/隐藏中间4位
     * @author HuangHao
     * @CreatTime 2021-08-26 16:45
     * @param phone
     * @return java.lang.String
     */
    public static String desensitizationPhone(String phone){
        if(phone!=null && phone.length()>7){
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return phone;
    }

    /**
     * 百分转换
     * @return java.math.BigDecimal
     */
    public static BigDecimal percentConversion(Integer divisor,Integer dividend){
        if(divisor==null || dividend==null || dividend==0){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(String.valueOf(divisor.floatValue() / dividend.floatValue())).multiply(hundred).setScale(2, RoundingMode.HALF_UP);
    }
    public static String StringFormat(Integer length, Object val){
        if (ObjectUtils.isEmpty(val) || length==null || length<=val.toString().length()) {
            return ObjectUtils.isEmpty(val) ? "": val.toString();
        }
        return String.format("%0"+length+"d", val);
    }
}
