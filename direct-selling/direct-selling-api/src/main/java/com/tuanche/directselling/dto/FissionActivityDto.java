package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionActivityData;
import com.tuanche.directselling.model.FissionActivityExtend;
import com.tuanche.directselling.model.FissionAwardRule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author lvsen
 * @Description 裂变活动实体dto
 * @Date 2020/9/23
 **/
public class FissionActivityDto extends FissionActivity implements Serializable {

    private static final long serialVersionUID = -8163429985691874972L;
    /**
     * 裂变活动氛围数据
     */
    private FissionActivityData fissionActivityData;

    /**
     * 裂变活动扩展（数据类型（1城市 2商品 3经销商））
     */
    private List<FissionActivityExtend> fissionActivityExtend;

    /**
     * 裂变奖励规则
     */
    private List<FissionAwardRule> awardRuleList;

    /**
     * 活动状态 1 进行中 0 未开始 2 已结束
     */
    private Integer activityStatus;

    /**
     * 瓜分奖金销售数
     */
    private Integer divideUpPrizeSaleNum;

    /**
     * C端总奖励金
     */
    private BigDecimal totalReward;

    /**
     * 开始时间（String）
     */

    private String startTimeStr;
    /**
     * 结束时间（String）
     */
    private String endTimeStr;
    /**
     * 详情图1
     */
    private String detailPicUrl1;
    /**
     * 详情图2
     */
    private String detailPicUrl2;
    /**
     * 详情图3
     */
    private String detailPicUrl3;

    public FissionActivityData getFissionActivityData() {
        return fissionActivityData;
    }

    public void setFissionActivityData(FissionActivityData fissionActivityData) {
        this.fissionActivityData = fissionActivityData;
    }

    public List<FissionActivityExtend> getFissionActivityExtend() {
        return fissionActivityExtend;
    }

    public void setFissionActivityExtend(List<FissionActivityExtend> fissionActivityExtend) {
        this.fissionActivityExtend = fissionActivityExtend;
    }

    public List<FissionAwardRule> getAwardRuleList() {
        return awardRuleList;
    }

    public void setAwardRuleList(List<FissionAwardRule> awardRuleList) {
        this.awardRuleList = awardRuleList;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getDivideUpPrizeSaleNum() {
        return divideUpPrizeSaleNum;
    }

    public void setDivideUpPrizeSaleNum(Integer divideUpPrizeSaleNum) {
        this.divideUpPrizeSaleNum = divideUpPrizeSaleNum;
    }

    public BigDecimal getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(BigDecimal totalReward) {
        this.totalReward = totalReward;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getDetailPicUrl1() {
        return detailPicUrl1;
    }

    public void setDetailPicUrl1(String detailPicUrl1) {
        this.detailPicUrl1 = detailPicUrl1;
    }

    public String getDetailPicUrl2() {
        return detailPicUrl2;
    }

    public void setDetailPicUrl2(String detailPicUrl2) {
        this.detailPicUrl2 = detailPicUrl2;
    }

    public String getDetailPicUrl3() {
        return detailPicUrl3;
    }

    public void setDetailPicUrl3(String detailPicUrl3) {
        this.detailPicUrl3 = detailPicUrl3;
    }
}
