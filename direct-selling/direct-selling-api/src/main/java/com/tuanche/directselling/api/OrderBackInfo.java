package com.tuanche.directselling.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderBackInfo implements Serializable {
    private static final long serialVersionUID = -3026971788119333066L;

    private Integer id;
    /** 订单号 */
    private String bizId;
    /** 描述信息 业务内容 */
    private String content;
    /** 业务编码 */
    private String code;
    /** 业务编码 */
    private Byte payType;
    /** 业务编码 */
    private String codeKey;
    /** 回调地址 */
    private String notifyUrl;
    /** 回调地址 */
    private String returnUrl;
    /** 我也不知道是啥 */
    private String productCodeAccount;



    /** 产品编码 */
    private String productCode;
    /** 金额 两位小数 */
    private BigDecimal amount;
    /** 序列号、流水号 */
    private String serialNo;
    /** 第三方流水号 */
    private String resId;
    /** 支付时间 */
    private Date bizTime;
    /** 订单号 */
    private String paySign;
    /** 状态 */
    private String status;
    /** 状态描述 */
    private String statusDesc;
    /** 交易参考号*/
    private String referNo;

    private String tranDate; //交易日期

    private String partner; //商户号

    private String refundBusiness; //退款业务流水号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getProductCodeAccount() {
        return productCodeAccount;
    }

    public void setProductCodeAccount(String productCodeAccount) {
        this.productCodeAccount = productCodeAccount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getRefundBusiness() {
        return refundBusiness;
    }

    public void setRefundBusiness(String refundBusiness) {
        this.refundBusiness = refundBusiness;
    }
}
