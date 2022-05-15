package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/4/19 15:18
 **/
public class UserRewardReconciliationDto implements Serializable {
    private int page;
    private int limit;
    private Integer fissionId;
    private Date payBeginTime;
    private Date payEndTime;

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

    public Date getPayBeginTime() {
        return payBeginTime;
    }

    public void setPayBeginTime(Date payBeginTime) {
        this.payBeginTime = payBeginTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
        this.payEndTime = payEndTime;
    }
}
