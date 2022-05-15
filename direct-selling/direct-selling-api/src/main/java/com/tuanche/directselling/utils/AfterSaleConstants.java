package com.tuanche.directselling.utils;

public class AfterSaleConstants {

    //卡券类型
    public enum CouponType {
        EXCHANGE(1, "兑换券"),
        GIFT(2, "礼品券"),
        DEDUCTION(3, "抵用券");
        private Integer code;
        private String key;
        CouponType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //卡券发放类型
    public enum CouponGiveOutType {
        USER_EXCHANGE(1, "用户发放兑换券"),
        USER_GIFT(2, "用户发放礼品券"),
        USER_DEDUCTION(3, "用户发放抵用券");
//        SHARE_GIFT(4, "分享人发放礼品券")
        private Integer code;
        private String key;
        CouponGiveOutType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //卡券状态
    public enum CouponStatus {
        GRANT_NON(0, "待发放"),
        USE_NON(1, "未使用"),
        USE(2, "已使用"),
        INVALID(3, "未生效"),
        HAVE_EXPIRED(4, "已过期");
        private Integer code;
        private String key;
        CouponStatus(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}

    }

    //订单类型
    public enum OrderType {
        PROMOTION_CARD(1, "推广卡"),
        SYNTHESIZE_CARD(2, "套餐卡");
        private Integer code;
        private String key;

        OrderType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }

        public Integer getCode() {
            return code;
        }

        public String getKey() {
            return this.key;
        }
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (OrderType t : OrderType.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }

    //订单状态
    public enum OrderStatus {
        TRADE_CLOSED(0, "已取消"),
        UNPAID(1, "待支付"),
        //瞬间状态 暂时未用到
        PAY_ING(2, "支付中"),
        PAID(3, "待核销"),
        CHECKOUT(4, "已发券"),//订单的已核销状态改为”已发券“，财务端同步处理
        APPLY_REFUND(5, "申请退款"),
        //瞬间状态 暂时未用到
        REFUND_ING(6, "退款中"),
        REFUND_SUCCESS_HAND(7, "已退款（人工）"),
        REFUND_SUCCESS_TIMING(13, "已退款（备案）"),
        REFUND_SUCCESS_FINISH(14, "已退款（全部）"),
        WAIT_BUYER_CONFIRM_GOODS(8, "等待买家确认收货"),
        WAIT_SELLER_SEND_GOODS(9, "等待买家确认收货"),
        GRANT_COUPON_NON(11, "待发券"),
        ARRIVE_SHOP(12, "已进店"),
        NOT_EFFECTIVE(99, "未生效");//备案用户购买后未完成裂变要求的订单
        private Integer code;
        private String key;
        OrderStatus(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (OrderStatus t : OrderStatus.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }

    //订单状态
    public enum ActivityCoupon {
        //数据类型（1卡券 2套餐）
        TYPE_PROMOTION_CARD(1, "推广卡"),
        TYPE_SYNTHESIZE_CARD(2, "套餐卡"),
        //瞬间状态 暂时未用到
        REL_STATUS_YES(1, "已关联"),
        REL_STATUS_NO(2, "取消关联");
        private Integer code;
        private String key;
        ActivityCoupon(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //推送类型
    public enum PushType {
        //数据类型（1卡券 2套餐）
        TYPE1(1, "推广卡推送"),
        TYPE2(2, "自己购买礼品卡推送"),
        TYPE3(3, "推荐朋友购买成功礼品卡推送"),
        TYPE4(4, "退款推送");
        private int code;
        private String key;
        PushType(int code, String key) {
            this.code = code;
            this.key = key;
        }
        public int getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //奖励支付状态
    public enum RewardPayStatus {
        //数据类型（1卡券 2套餐）
        PayStatus0(0, "未支付/未发放"),
        PayStatus1(1, "支付成功/发放成功"),
        PayStatus2(2, "支付失败/发放失败"),
        PayStatus3(3, "已退款/奖励已收回"),
        PayStatus4(4, "未生效");
        private int code;
        private String key;
        RewardPayStatus(int code, String key) {
            this.code = code;
            this.key = key;
        }
        public int getCode() {return code;}
        public String getKey() {return this.key;}
    }

    //
    public enum OrderUser {
        //数据类型（1卡券 2套餐）
        KEEP_ON_RECODE_USER_N(0, "非备案用户"),
        KEEP_ON_RECODE_USER_Y(1, "备案用户"),
        FINISH_FISSION_TASK_N(0, "未完成裂变任务"),
        FINISH_FISSION_TASK_Y(1, "已完成裂变任务");
        private int code;
        private String key;
        OrderUser(int code, String key) {
            this.code = code;
            this.key = key;
        }
        public int getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //活动开启状态
    public enum ActivityOnState {
        //数据类型（1卡券 2套餐）
        ONSTATE_0(0, "未开启"),
        ONSTATE_1(1, "已开启");
        private int code;
        private String key;
        ActivityOnState(int code, String key) {
            this.code = code;
            this.key = key;
        }
        public int getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //活动开启状态
    public enum UserType {
        USER_TYPE_0(0, "备案用户"),
        USER_TYPE_1(1, "流失用户"),
        USER_TYPE_2(2, "普通用户"),
        USER_TYPE_3(3, "代理人");
        private Integer code;
        private String key;
        UserType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (UserType t : UserType.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }
    //代理人类型
    public enum AgentType {
        TYPE_0(0, "代理人"),
        TYPE_1(1, "团车电销");
        private Integer code;
        private String key;
        AgentType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
    }
    //渠道
    public enum ChannelType {
        TYPE_1(1, "代理人"),
        TYPE_2(2, "团车电销"),
        TYPE_3(3, "团车运营");
        private Integer code;
        private String key;
        ChannelType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (ChannelType t : ChannelType.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }
    //业务类型（活动类型）
    public enum BusinessType {
        TYPE_1(1, "经销商"),
        TYPE_2(2, "汽修厂");
        private Integer code;
        private String key;
        BusinessType(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (BusinessType t : BusinessType.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }
    //礼品券状态
    public enum GiftCertificatesStstus {
        TYPE_0(0, "未获得"),
        TYPE_1(1, "待核销"),
        TYPE_2(2, "已核销");
        private Integer code;
        private String key;
        GiftCertificatesStstus(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (GiftCertificatesStstus t : GiftCertificatesStstus.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }

    /**
     * 活动分账状态
     */
    public enum SubAccountType {
        NO_OPEN(0, "未开启"),
        OPENED(1, "开启"),
        VERIFY(2, "已验证");
        private Integer code;
        private String key;

        SubAccountType(Integer code, String key) {
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
     * 账号验证状态
     */
    public enum SubAccountVerifyState {
        STATE_000("000", "未验证"),
        STATE_100("100", "支付成功"),
        STATE_200("200", "支付失败"),
        STATE_110("110", "分账成功"),
        STATE_120("120", "分账失败"),
        STATE_111("111", "退款成功，验证通过"),
        STATE_112("112", "分账成功，退款失败"),
        STATE_101("101", "未分账，退款成功"),
        STATE_102("102", "未分账，退款失败");
        private String code;
        private String value;

        SubAccountVerifyState(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return this.value;
        }
    }
    //错误码
    public enum ErrCode {
        SUCCESS("SUCCESS", "SUCCESS"),
        TC_ERROR("TC_ERROR", "团车系统错误"),
        TC_FAIL("TC_FAIL", "失败"),
        TC_INEFFECTIVE("TC_INEFFECTIVE", "未生效"),
        //以下都是微信错误码（err_code字段）
        SYSTEMERROR("SYSTEMERROR", "系统繁忙，请稍后再试。"),
        V2_ACCOUNT_SIMPLE_BAN("V2_ACCOUNT_SIMPLE_BAN", "微信未实名认证，不能收款"),
        NOTENOUGH("NOTENOUGH", "余额不足"),
        SEND_MONEY_LIMIT("NOTENOUGH", "已达到今日商户付款额度上限"),
        RECEIVED_MONEY_LIMIT("RECEIVED_MONEY_LIMIT", "已达到今日付款给此用户额度上限"),
        SENDNUM_LIMIT("SENDNUM_LIMIT", "该用户今日付款次数超过限制"),
        AMOUNT_LIMIT("AMOUNT_LIMIT", "金额超限"),
        NO_AUTH("NO_AUTH", "微信账户状态异常，已被限制收款");
        private String code;
        private String desc;
        ErrCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode() {return code;}
        public String getDesc() {return this.desc;}
        public static String getDesc(String code) {
            if(code==null){
                return null;
            }
            for (ErrCode t : ErrCode.values()) {
                if (code.equals(t.getCode())) {
                    return t.desc;
                }
            }
            return null;
        }
    }
    //订单分账状态
    public enum OrderSubAccountStatus {
        STATUS_1(1, "不分账"),
        STATUS_2(2, "待分账"),
        STATUS_3(3, "分账中"),
        STATUS_4(4, "已分账"),
        STATUS_5(5, "分账失败"),
        STATUS_6(6, "分账退回中"),
        STATUS_7(7, "分账已退回"),
        STATUS_8(8, "分账退回失败");
        private Integer code;
        private String key;
        OrderSubAccountStatus(Integer code, String key) {
            this.code = code;
            this.key = key;
        }
        public Integer getCode() {return code;}
        public String getKey() {return this.key;}
        public static String getKey(Integer code) {
            if(code==null){
                return null;
            }
            for (OrderSubAccountStatus t : OrderSubAccountStatus.values()) {
                if (code.equals(t.getCode())) {
                    return t.key;
                }
            }
            return null;
        }
    }


}
