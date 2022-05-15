package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleCouponDto extends AfterSaleCouponRecord implements Comparable<AfterSaleCouponDto> {

    //卡券名称
    private String couponName;
    //车牌
    private String licencePlate;

    private String cbLogo;
    private Integer cbId;
    private Integer goodsId;

    private String dealerName;
    private String shortName;
    private String dealerAddress;
    private String dealerTel;

    private String checkedSaleName;

    //1:订单类卡券详情  2：券码类卡券详情
    private Integer type;

    //核销链接
    private String checkedUrl;
    //订单核销码
    private String checkoutCode;
    private String agentWxUnionId;
    private String shareWxUnionId;

    private String userPhone;
    //订单价格
    private BigDecimal orderMoney;

    private BigDecimal couponMoney;

    private List<Integer> couponIds;

    private Integer sortType = 10;

    private List<Integer> activityIds;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getCbLogo() {
        return cbLogo;
    }

    public void setCbLogo(String cbLogo) {
        this.cbLogo = cbLogo;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerTel() {
        return dealerTel;
    }

    public void setDealerTel(String dealerTel) {
        this.dealerTel = dealerTel;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCheckedUrl() {
        return checkedUrl;
    }

    public void setCheckedUrl(String checkedUrl) {
        this.checkedUrl = checkedUrl;
    }

    public String getCheckoutCode() {
        return checkoutCode;
    }

    public void setCheckoutCode(String checkoutCode) {
        this.checkoutCode = checkoutCode;
    }

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<Integer> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Integer> couponIds) {
        this.couponIds = couponIds;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSortType() {
        if (this.getCouponStatus()!=null) {
            if (this.getCouponStatus().equals(AfterSaleConstants.CouponStatus.INVALID.getCode())) {
                sortType = 1;
            } else if (this.getCouponStatus().equals(AfterSaleConstants.CouponStatus.HAVE_EXPIRED.getCode())) {
                sortType = 6;
            } else if (this.getCouponStatus().equals(AfterSaleConstants.CouponStatus.USE_NON.getCode())) {
                if (this.getCouponType()!=null) {
                    if (this.getCouponType().equals(AfterSaleConstants.CouponType.EXCHANGE.getCode())) {
                        sortType = 2;
                    } else if (this.getCouponType().equals(AfterSaleConstants.CouponType.GIFT.getCode())) {
                        sortType = 3;
                    }else if (this.getCouponType().equals(AfterSaleConstants.CouponType.DEDUCTION.getCode())) {
                        sortType = 4;
                    } else {
                        sortType = 5;
                    }
                }
            }
        }
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    @Override
    public int compareTo(AfterSaleCouponDto o) {
        if (o.getSortType().compareTo(this.getSortType()) < 0) {
            return 1;
        } else if (o.getSortType().compareTo(this.getSortType()) == 0) {
            if (this.getChangeStartDate() != null && o.getChangeStartDate() != null) {
                return this.getChangeStartDate().compareTo(o.getChangeStartDate());
            }
        }
        return -1;
    }

    public List<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Integer> activityIds) {
        this.activityIds = activityIds;
    }

    public BigDecimal getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(BigDecimal couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getCheckedSaleName() {
        return checkedSaleName;
    }

    public void setCheckedSaleName(String checkedSaleName) {
        this.checkedSaleName = checkedSaleName;
    }
}
