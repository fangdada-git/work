package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionUserReward;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @class: FissionUserRewardDto
 * @description: 用户奖励实体
 * @author: dudg
 * @create: 2020-09-29 18:01
 */
public class FissionUserRewardDto extends FissionUserReward implements Serializable {

    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 总奖励金
     */
    private BigDecimal totalAmount;

    /**
     * 是否分页
     */
    private boolean openPage;

    /**
     * 打款记录ID
     */
    private String tradeRecordIds;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isOpenPage() {
        return openPage;
    }

    public void setOpenPage(boolean openPage) {
        this.openPage = openPage;
    }

    public String getTradeRecordIds() {
        return tradeRecordIds;
    }

    public void setTradeRecordIds(String tradeRecordIds) {
        this.tradeRecordIds = tradeRecordIds;
    }
}
