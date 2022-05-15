package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * fission_sale
 *
 * @author
 */
public class FissionSaleRewardVo implements Serializable {
    private Integer id;

    /**
     * 裂变活动ID
     */
    private Integer fissionId;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 销售id
     */
    private Integer saleId;

    /**
     * 销售姓名
     */
    private String saleName;

    /**
     * 销售手机号
     */
    private String salePhone;

    /**
     * 实际收入金额
     */
    private BigDecimal realIncome;

    /**
     * 未提现金额
     */
    private BigDecimal waitingWithdrawal;

    /**
     * 已提现金额
     */
    private BigDecimal withdrawaled;

    /**
     * 提现状态 0：未提现 1：已提现
     */
    private Integer withdrawalState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getSalePhone() {
        return salePhone;
    }

    public void setSalePhone(String salePhone) {
        this.salePhone = salePhone;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public Integer getWithdrawalState() {
        return withdrawalState;
    }

    public void setWithdrawalState(Integer withdrawalState) {
        this.withdrawalState = withdrawalState;
    }

    public BigDecimal getWaitingWithdrawal() {
        return waitingWithdrawal;
    }

    public void setWaitingWithdrawal(BigDecimal waitingWithdrawal) {
        this.waitingWithdrawal = waitingWithdrawal;
    }

    public BigDecimal getWithdrawaled() {
        return withdrawaled;
    }

    public void setWithdrawaled(BigDecimal withdrawaled) {
        this.withdrawaled = withdrawaled;
    }
}