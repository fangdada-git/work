package com.tuanche.directselling.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 经销商打款对账dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleDealerPaymentVerifyDto extends AfterSaleActivityDataBaseDto {
    /**
     * 财务-抬头类型：0对公账号 1;私人账号
     */
    private Integer accountType;

    /**
     * 财务-银行账号
     */
    private String cardNumber;

    /**
     * 财务-开户行
     */
    private String cardBank;

    /**
     * 财务-开户行地址
     */
    private String cardBankAddress;

    /**
     * 分账比率
     */
    private BigDecimal subAccountRatio;

    /**
     * 售出套餐卡金额
     */
    private BigDecimal packageCardAmount;

    /**
     * 应打款金额
     */
    private BigDecimal payableAmount;

    /**
     * 实际打款金额
     */
    private BigDecimal actualAmount;

    /**
     * 打款时间
     */
    private Date paymentTime;

    /**
     * 打款备注
     */
    private String paymentRemark;

    /**
     * 流程编号
     */
    private String paymentFlowNum;

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getCardBankAddress() {
        return cardBankAddress;
    }

    public void setCardBankAddress(String cardBankAddress) {
        this.cardBankAddress = cardBankAddress;
    }

    public BigDecimal getSubAccountRatio() {
        return subAccountRatio;
    }

    public void setSubAccountRatio(BigDecimal subAccountRatio) {
        this.subAccountRatio = subAccountRatio;
    }

    public BigDecimal getPackageCardAmount() {
        return packageCardAmount;
    }

    public void setPackageCardAmount(BigDecimal packageCardAmount) {
        this.packageCardAmount = packageCardAmount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public String getPaymentFlowNum() {
        return paymentFlowNum;
    }

    public void setPaymentFlowNum(String paymentFlowNum) {
        this.paymentFlowNum = paymentFlowNum;
    }
}
