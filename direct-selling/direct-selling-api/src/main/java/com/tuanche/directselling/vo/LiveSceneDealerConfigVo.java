package com.tuanche.directselling.vo;

import com.tuanche.directselling.model.LiveSceneDealerConfig;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-29 15:36
 */
public class LiveSceneDealerConfigVo extends LiveSceneDealerConfig {

    //城市ID
    private Integer cityId;

    /**
     * 当前页数，默认第一页
     */
    private int page = 1;

    /**
     * 查询条数，默认10条
     */
    private int limit = 10;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

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
