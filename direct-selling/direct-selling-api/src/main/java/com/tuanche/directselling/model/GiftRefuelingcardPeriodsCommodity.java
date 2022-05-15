package com.tuanche.directselling.model;

import java.io.Serializable;

public class GiftRefuelingcardPeriodsCommodity implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 赠送油卡场次表ID
     */
    private Integer rcPeriodsId;

    /**
     * 商品类型
     */
    private String commodityType;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * gift_refuelingcard_periods_commodity
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

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }
}