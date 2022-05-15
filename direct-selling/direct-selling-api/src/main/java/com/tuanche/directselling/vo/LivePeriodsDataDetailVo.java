package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author lvsen
 * @description 直卖场次经销商7天线索详情
 * @date 2020/7/6 16:54
 */
public class LivePeriodsDataDetailVo implements Serializable {

    /**
     * 日期描述（含总计）
     */
    private String dayDesc;
    /**
     * 今日新增线索量（含总计）
     */
    private int addClueCount;
    /**
     * 今日分发线索量（含总计）
     */
    private int disClueCount;
    /**
     * 今日新增车型商品线索量（含总计）
     */
    private int orderClueCount;

    public String getDayDesc() {
        return dayDesc;
    }

    public void setDayDesc(String dayDesc) {
        this.dayDesc = dayDesc;
    }

    public int getAddClueCount() {
        return addClueCount;
    }

    public void setAddClueCount(int addClueCount) {
        this.addClueCount = addClueCount;
    }

    public int getDisClueCount() {
        return disClueCount;
    }

    public void setDisClueCount(int disClueCount) {
        this.disClueCount = disClueCount;
    }

    public int getOrderClueCount() {
        return orderClueCount;
    }

    public void setOrderClueCount(int orderClueCount) {
        this.orderClueCount = orderClueCount;
    }
}
