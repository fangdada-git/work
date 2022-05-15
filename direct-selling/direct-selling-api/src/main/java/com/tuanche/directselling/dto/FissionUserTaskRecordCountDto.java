package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionUserTaskRecord;

import java.io.Serializable;

public class FissionUserTaskRecordCountDto extends FissionUserTaskRecord implements Serializable {
    /**
     * 数量
     */
    private Integer count;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }
}
