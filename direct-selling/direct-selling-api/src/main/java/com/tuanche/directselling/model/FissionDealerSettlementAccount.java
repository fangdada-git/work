package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FissionDealerSettlementAccount implements Serializable {
    /**
     * 
     */
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
     * 供应商名
     */
    private String dealerName;

    /**
     * 账号类型 1：对公账户 2：个人账户
     */
    private Integer accountType;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行地址
     */
    private String bankAddress;

    /**
     * 银行卡号
     */
    private String bankCardNumber;

    /**
     * 银行卡持有人姓名
     */
    private String bankCardName;

    /**
     * 银行卡持有人电话
     */
    private String bankCardPhone;

    /**
     * 公司税号
     */
    private String dutyParagraph;

    /**
     * 是否已打款 0：否 1：是
     */
    private Boolean paymentStatus;
    /**
     * 是否允许填写私人账户 0：否 1：是
     */
    private Boolean personalAccount;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 更新人
     */
    private String updateName;

    /**
     * fission_dealer_settlement_account
     */
    private static final long serialVersionUID = 1L;

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

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getPersonalAccount() {
        return personalAccount;
    }

    public void setPersonalAccount(Boolean personalAccount) {
        this.personalAccount = personalAccount;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }
}