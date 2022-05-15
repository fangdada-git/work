package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * 油卡使用统计dto
 * @author：HuangHao
 * @CreatTime 2020-06-01 18:05
 */
public class GiftRefuelingcardUsedStatisticsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    //油卡总数
    private Integer total;
    //未使用数
    private Integer unUsed;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUnUsed() {
        return unUsed;
    }

    public void setUnUsed(Integer unUsed) {
        this.unUsed = unUsed;
    }
}
