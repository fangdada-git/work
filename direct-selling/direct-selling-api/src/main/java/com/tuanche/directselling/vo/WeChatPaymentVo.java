package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @class: WeChatPaymentVo
 * @description: 微信付款参数实体
 * @author: dudg
 * @create: 2020-09-24 18:05
 */
public class WeChatPaymentVo implements Serializable {

    /**
     * 交易类型：1 支付对赌金 2 支付商品 3 商品退款 4  销售提现 5  支付C端用户佣金
     */
    private Integer tradeType;
    /**
     * wxopenid
     */
    private String openId;
    /**
     * 订单号（数字/字母）
     */
    private String order_no;
    /**
     * 付款金额（单位：分） 最少30分
     */
    private int amount;
    /**
     * 付款备注
     */
    private String desc;
    /**
     * 公众号APPID
     */
    private String appId;

    /**
     * 打款金额
     */
    private BigDecimal originAmount;

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getOriginAmount() {
        return originAmount;
    }

    public void setOriginAmount(BigDecimal originAmount) {
        this.originAmount = originAmount;
    }
}
