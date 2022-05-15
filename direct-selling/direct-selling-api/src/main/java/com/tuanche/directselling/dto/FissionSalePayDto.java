package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionSale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @class: FissionSalePayDto
 * @description: 裂变销售打款&提现实体
 * @author: dudg
 * @create: 2020-09-27 11:31
 */
public class FissionSalePayDto extends FissionSale implements Serializable {

    /**
     * 裂变活动名称
     */
    private String fissionName;
    /**
     * 经销商名称
     */
    private String dealerName;
    /**
     * 销售手机号
     */
    private String salePhone;

    /**
     * 审核类型：1 审核无误 2 确认打款  3 冻结  4 解冻
     */
    private Integer auditType;
    /**
     * (冻结/解冻)销售ids
     */
    private List<Integer> saleIdList;

    private List<Integer> idList;

    public String getFissionName() {
        return fissionName;
    }

    public void setFissionName(String fissionName) {
        this.fissionName = fissionName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSalePhone() {
        return salePhone;
    }

    public void setSalePhone(String salePhone) {
        this.salePhone = salePhone;
    }

    public Integer getAuditType() {
        return auditType;
    }

    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }

    public List<Integer> getSaleIdList() {
        return saleIdList;
    }

    public void setSaleIdList(List<Integer> saleIdList) {
        this.saleIdList = saleIdList;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }
}
