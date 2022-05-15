package com.tuanche.directselling.vo;

import com.tuanche.directselling.model.GiftRefuelingcardGiftrecord;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-12 18:14
 */
public class GiftRefuelingcardGiftrecordVo extends GiftRefuelingcardGiftrecord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页数，默认第一页
     */
    private int page = 1;

    /**
     * 查询条数，默认10条
     */
    private int limit = 10;
    //城市ID
    private Integer cityId;



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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }


}
