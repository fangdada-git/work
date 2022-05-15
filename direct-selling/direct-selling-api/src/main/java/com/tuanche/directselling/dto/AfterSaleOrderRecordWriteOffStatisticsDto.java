package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-09 10:57
 */
public class AfterSaleOrderRecordWriteOffStatisticsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户微信unionid
    private Integer saleId;
    //用户微信unionid
    private String userWxUnionId;
    //用户姓名
    private String name;
    //用户手机
    private String phone;
    //售卖推广卡数量
    int promotionCardTotal;
    //售卖套餐卡数量
    int packageCardTotal;
    //核销推广卡数量
    int promotionCardWriteOffTotal;
    //核销套餐卡数量
    int packageCardWriteOffTotal;
    //套餐核销总金额
    BigDecimal packageCardAmount;
    //核销开始时间
    String checkedDateStart;
    //核销结束时间
    String checkedDateEnd;

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPromotionCardTotal() {
        return promotionCardTotal;
    }

    public void setPromotionCardTotal(int promotionCardTotal) {
        this.promotionCardTotal = promotionCardTotal;
    }

    public int getPackageCardTotal() {
        return packageCardTotal;
    }

    public void setPackageCardTotal(int packageCardTotal) {
        this.packageCardTotal = packageCardTotal;
    }

    public BigDecimal getPackageCardAmount() {
        return packageCardAmount;
    }

    public void setPackageCardAmount(BigDecimal packageCardAmount) {
        this.packageCardAmount = packageCardAmount;
    }

    public String getCheckedDateStart() {
        return checkedDateStart;
    }

    public void setCheckedDateStart(String checkedDateStart) {
        this.checkedDateStart = checkedDateStart;
    }

    public String getCheckedDateEnd() {
        return checkedDateEnd;
    }

    public void setCheckedDateEnd(String checkedDateEnd) {
        this.checkedDateEnd = checkedDateEnd;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public int getPromotionCardWriteOffTotal() {
        return promotionCardWriteOffTotal;
    }

    public void setPromotionCardWriteOffTotal(int promotionCardWriteOffTotal) {
        this.promotionCardWriteOffTotal = promotionCardWriteOffTotal;
    }

    public int getPackageCardWriteOffTotal() {
        return packageCardWriteOffTotal;
    }

    public void setPackageCardWriteOffTotal(int packageCardWriteOffTotal) {
        this.packageCardWriteOffTotal = packageCardWriteOffTotal;
    }
}
