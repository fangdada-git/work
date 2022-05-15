package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * fission_sale
 *
 * @author
 */
public class FissionAllDealerRankVo implements Serializable {

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
     * 销售数量
     */
    private Integer saleCount;

    /**
     * 经销商名称
     */
    private String dealerName;


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
     * 完成任务的任务总积分
     */
    private Integer finishTaskIntegral;

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

    public Integer getFinishTaskIntegral() {
        return finishTaskIntegral;
    }

    public void setFinishTaskIntegral(Integer finishTaskIntegral) {
        this.finishTaskIntegral = finishTaskIntegral;
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

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }
}