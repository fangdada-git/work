package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class GiftRefuelingcardPeriodsGiftNum implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 赠送油卡场次表ID
     */
    private Integer rcPeriodsId;

    /**
     * 商品金额
     */
    private BigDecimal commodityPrice;

    /**
     * 赠送油卡数量
     */
    private Integer giftNum;

    /**
     * gift_refuelingcard_periods_gift_num
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRcPeriodsId() {
        return rcPeriodsId;
    }

    public void setRcPeriodsId(Integer rcPeriodsId) {
        this.rcPeriodsId = rcPeriodsId;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }
}