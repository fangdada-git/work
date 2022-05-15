package com.tuanche.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentSalesSumStatVo implements Serializable {
    /**
     * 浏览次数
     */
    private Integer pageViewCount;

    /**
     * 浏览人数
     */
    private Integer uniqueVisitorCount;

    /**
     * 成交占比
     */
    private String deal;

    /**
     * 员工销量占比
     */
    private String staffSales;

    /**
     * 转介绍销量占比
     */
    private String forwardSales;

    /**
     * 电销占比
     */
    private String telSales;

    /**
     * 其他占比
     */
    private String otherSales;

    private List<AfterSaleAgentSalesStatVo> salesStatVoList;

    public Integer getPageViewCount() {
        return pageViewCount;
    }

    public void setPageViewCount(Integer pageViewCount) {
        this.pageViewCount = pageViewCount;
    }

    public Integer getUniqueVisitorCount() {
        return uniqueVisitorCount;
    }

    public void setUniqueVisitorCount(Integer uniqueVisitorCount) {
        this.uniqueVisitorCount = uniqueVisitorCount;
    }

    public List<AfterSaleAgentSalesStatVo> getSalesStatVoList() {
        return salesStatVoList;
    }

    public void setSalesStatVoList(List<AfterSaleAgentSalesStatVo> salesStatVoList) {
        this.salesStatVoList = salesStatVoList;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getStaffSales() {
        return staffSales;
    }

    public void setStaffSales(String staffSales) {
        this.staffSales = staffSales;
    }

    public String getForwardSales() {
        return forwardSales;
    }

    public void setForwardSales(String forwardSales) {
        this.forwardSales = forwardSales;
    }

    public String getTelSales() {
        return telSales;
    }

    public void setTelSales(String telSales) {
        this.telSales = telSales;
    }

    public String getOtherSales() {
        return otherSales;
    }

    public void setOtherSales(String otherSales) {
        this.otherSales = otherSales;
    }
}
