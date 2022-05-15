package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/4/19 15:18
 **/
public class SaleRewardReconciliationDto implements Serializable {
    private int page;
    private int limit;
    private Integer fissionId;
    private Date withdrawalBeginTime;
    private Date withdrawalEndTime;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Date getWithdrawalBeginTime() {
        return withdrawalBeginTime;
    }

    public void setWithdrawalBeginTime(Date withdrawalBeginTime) {
        this.withdrawalBeginTime = withdrawalBeginTime;
    }

    public Date getWithdrawalEndTime() {
        return withdrawalEndTime;
    }

    public void setWithdrawalEndTime(Date withdrawalEndTime) {
        this.withdrawalEndTime = withdrawalEndTime;
    }
}
