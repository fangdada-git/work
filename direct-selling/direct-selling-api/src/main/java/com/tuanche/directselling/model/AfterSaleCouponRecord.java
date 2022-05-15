package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleCouponRecord implements Serializable {
    private Integer id;
    //券码id
    private Integer couponId;
    //用户券id
    private Integer userCouponId;
    //券码
    private String couponCode;
    //订单编号
    private String orderCode;
    //活动id
    private Integer activityId;
    //经销商id
    private Integer dealerId;
    private Integer cbId;
    private Integer csId;
    //券码状态 0:待发放 1：未使用 2：已使用 3:未生效 4：已过期
    private Integer couponStatus;
    private List<Integer> couponStatusList;
    //卡券类型  1：兑换券 2：礼品券 3：抵扣券
    private Integer couponType;
    private List<Integer> couponTypeList;
    //用户微信unionid
    private String userWxUnionId;
    //核销销售id
    private Integer checkedSalesId;
    //核销经销商id
    private Integer checkedDealerId;
    //核销时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkedDate;
    //是否删除 0未删除 1 删除
    private Byte deleteFlag;

    //兑换日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date changeStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date changeEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = StringUtil.isEmpty(couponCode) ? null : couponCode.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getCouponStatus() {
        if (this.changeEndDate!=null) {
            Date now = new Date();
            if (now.after(this.changeEndDate)) {
                return AfterSaleConstants.CouponStatus.HAVE_EXPIRED.getCode();
            }
        }
        return couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = StringUtil.isEmpty(userWxUnionId) ? null : userWxUnionId.trim();
    }

    public Integer getCheckedSalesId() {
        return checkedSalesId;
    }

    public void setCheckedSalesId(Integer checkedSalesId) {
        this.checkedSalesId = checkedSalesId;
    }

    public Integer getCheckedDealerId() {
        return checkedDealerId;
    }

    public void setCheckedDealerId(Integer checkedDealerId) {
        this.checkedDealerId = checkedDealerId;
    }

    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Integer userCouponId) {
        this.userCouponId = userCouponId;
    }

    public List<Integer> getCouponTypeList() {
        return couponTypeList;
    }

    public void setCouponTypeList(List<Integer> couponTypeList) {
        this.couponTypeList = couponTypeList;
    }

    public Date getChangeStartDate() {
        return changeStartDate;
    }

    public void setChangeStartDate(Date changeStartDate) {
        this.changeStartDate = changeStartDate;
    }

    public Date getChangeEndDate() {
        return changeEndDate;
    }

    public void setChangeEndDate(Date changeEndDate) {
        this.changeEndDate = changeEndDate;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public List<Integer> getCouponStatusList() {
        return couponStatusList;
    }

    public void setCouponStatusList(List<Integer> couponStatusList) {
        this.couponStatusList = couponStatusList;
    }
}