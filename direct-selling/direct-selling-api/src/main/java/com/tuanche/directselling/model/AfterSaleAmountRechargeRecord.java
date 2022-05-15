package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AfterSaleAmountRechargeRecord implements Serializable {
    private Integer id;

    ////累计充值金额
    private BigDecimal accruingAmounts;

    //充值金额
    private BigDecimal rechargeAmount;

    private Integer createBy;

    private Date createDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public BigDecimal getAccruingAmounts() {
        return accruingAmounts;
    }

    public void setAccruingAmounts(BigDecimal accruingAmounts) {
        this.accruingAmounts = accruingAmounts;
    }
}