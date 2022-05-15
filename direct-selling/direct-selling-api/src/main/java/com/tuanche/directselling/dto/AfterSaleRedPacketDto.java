package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lvsen
 * @Description 红包列表
 * @Date 2021/10/27
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleRedPacketDto implements Serializable {
    private Integer id;
    /**
     * 被邀请人
     */
    private String licencePlate;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 到账时间
     */
    private Date wxPayTime;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 到账时间
     */
    private Long wxPayTimeMils;
    /**
     * 支付时间
     */
    private Long payTimeMils;
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 奖励类型（分享红包 2：购买红包 3：礼品券 4：邀请额外奖励'）
     */
    private Integer rewardType;

    private BigDecimal quantity;

    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 失败原因toast
     */
    private String reasonToast;

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getWxPayTime() {
        return wxPayTime;
    }

    public void setWxPayTime(Date wxPayTime) {
        this.wxPayTime = wxPayTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getWxPayTimeMils() {
        return wxPayTimeMils;
    }

    public void setWxPayTimeMils(Long wxPayTimeMils) {
        this.wxPayTimeMils = wxPayTimeMils;
    }

    public Long getPayTimeMils() {
        return payTimeMils;
    }

    public void setPayTimeMils(Long payTimeMils) {
        this.payTimeMils = payTimeMils;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getReasonToast() {
        return reasonToast;
    }

    public void setReasonToast(String reasonToast) {
        this.reasonToast = reasonToast;
    }
}
