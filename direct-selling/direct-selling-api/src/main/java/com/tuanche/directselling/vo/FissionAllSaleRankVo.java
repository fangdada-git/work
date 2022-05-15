package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * fission_sale
 *
 * @author
 */
public class FissionAllSaleRankVo implements Serializable {

    private Integer rank;

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
     * 预计收入金额
     */
    private BigDecimal estimatedIncome;

    /**
     * 任务总积分
     */
    private Integer taskIntegral;

    /**
     * 是否完成任务目标 0：否 1：是
     */
    private Boolean whetherCompleteTask;
    /**
     * 浏览页面数
     */
    private Integer pageView;

    /**
     * 预约直播数
     */
    private Integer subscribeLive;

    /**
     * 活动页面商品购买数
     */
    private Integer activityPageProduct;

    /**
     * 观看直播数
     */
    private Integer liveView;

    /**
     * 直播间商品购买商品数
     */
    private Integer livePageProduct;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public BigDecimal getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(BigDecimal estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public Integer getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(Integer taskIntegral) {
        this.taskIntegral = taskIntegral;
    }

    public Boolean getWhetherCompleteTask() {
        return whetherCompleteTask;
    }

    public void setWhetherCompleteTask(Boolean whetherCompleteTask) {
        this.whetherCompleteTask = whetherCompleteTask;
    }

    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }

    public Integer getSubscribeLive() {
        return subscribeLive;
    }

    public void setSubscribeLive(Integer subscribeLive) {
        this.subscribeLive = subscribeLive;
    }

    public Integer getActivityPageProduct() {
        return activityPageProduct;
    }

    public void setActivityPageProduct(Integer activityPageProduct) {
        this.activityPageProduct = activityPageProduct;
    }

    public Integer getLiveView() {
        return liveView;
    }

    public void setLiveView(Integer liveView) {
        this.liveView = liveView;
    }

    public Integer getLivePageProduct() {
        return livePageProduct;
    }

    public void setLivePageProduct(Integer livePageProduct) {
        this.livePageProduct = livePageProduct;
    }
}