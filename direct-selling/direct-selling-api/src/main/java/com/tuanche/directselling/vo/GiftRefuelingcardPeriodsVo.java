package com.tuanche.directselling.vo;

import com.tuanche.directselling.model.GiftRefuelingcardPeriods;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-27 16:03
 */
public class GiftRefuelingcardPeriodsVo extends GiftRefuelingcardPeriods{

    /**
     * 当前页数，默认第一页
     */
    private int page = 1;

    /**
     * 查询条数，默认10条
     */
    private int limit = 10;

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
}
