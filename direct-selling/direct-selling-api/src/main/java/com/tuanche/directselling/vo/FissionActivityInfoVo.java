package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 活动详情-活动中的信息
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 9:49
 **/
public class FissionActivityInfoVo implements Serializable {

    /**
     * 奖金池总金额
     */
    private BigDecimal prizePool;

    /**
     * 销售获得的总积分
     */
    private Integer integral;

    /**
     * 预计收入
     */
    private BigDecimal estimatedIncome;

    /**
     * 实际收入
     */
    private BigDecimal realIncome;

    /**
     * 是否完成所有任务  0：否 1：是
     */
    private Integer isFinishAllTask;
    /**
     * 个人成绩
     */
    private List<FissionActivityPersonalIntegralVo> personalIntegral;

    /**
     * 个人的任务目标情况
     */
    private List<FissionActivityPersonalGoalVo> personalGoal;

    /**
     * 销售成绩榜单
     */
    private List<FissionActivityRankVo> saleRank;

    /**
     * 经销商成绩榜单
     */
    private List<FissionActivityRankVo> dealerRank;

    /**
     * 店内销售排行
     */
    private List<FissionActivityRankVo> dealerSaleRank;

    /**
     * 实际收入是否生成
     */
    private boolean isRealIncomeComplete;

    /**
     * 财务审核状态 0：未审核 1：财务已审核 2:  财务已打款
     */
    private Integer financialAuditStatus;

    public BigDecimal getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(BigDecimal prizePool) {
        this.prizePool = prizePool;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public BigDecimal getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(BigDecimal estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public List<FissionActivityRankVo> getSaleRank() {
        return saleRank;
    }

    public void setSaleRank(List<FissionActivityRankVo> saleRank) {
        this.saleRank = saleRank;
    }

    public List<FissionActivityRankVo> getDealerRank() {
        return dealerRank;
    }

    public void setDealerRank(List<FissionActivityRankVo> dealerRank) {
        this.dealerRank = dealerRank;
    }

    public List<FissionActivityPersonalIntegralVo> getPersonalIntegral() {
        return personalIntegral;
    }

    public void setPersonalIntegral(List<FissionActivityPersonalIntegralVo> personalIntegral) {
        this.personalIntegral = personalIntegral;
    }

    public List<FissionActivityRankVo> getDealerSaleRank() {
        return dealerSaleRank;
    }

    public void setDealerSaleRank(List<FissionActivityRankVo> dealerSaleRank) {
        this.dealerSaleRank = dealerSaleRank;
    }

    public List<FissionActivityPersonalGoalVo> getPersonalGoal() {
        return personalGoal;
    }

    public void setPersonalGoal(List<FissionActivityPersonalGoalVo> personalGoal) {
        this.personalGoal = personalGoal;
    }


    public Integer getIsFinishAllTask() {
        return isFinishAllTask;
    }

    public void setIsFinishAllTask(Integer isFinishAllTask) {
        this.isFinishAllTask = isFinishAllTask;
    }

    public boolean isRealIncomeComplete() {
        return isRealIncomeComplete;
    }

    public void setRealIncomeComplete(boolean realIncomeComplete) {
        isRealIncomeComplete = realIncomeComplete;
    }

    public Integer getFinancialAuditStatus() {
        return financialAuditStatus;
    }

    public void setFinancialAuditStatus(Integer financialAuditStatus) {
        this.financialAuditStatus = financialAuditStatus;
    }
}
