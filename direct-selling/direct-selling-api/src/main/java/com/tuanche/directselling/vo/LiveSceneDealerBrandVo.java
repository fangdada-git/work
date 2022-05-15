package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author gongbo
 * @Description 团车直卖-场次活动经销商品牌vo
 * @Date 2020年3月11日15:37:50
 **/
public class LiveSceneDealerBrandVo implements Serializable {

    /**
     * 场次活动ID
     **/
    private Integer sceneId;

    /**
     * 品牌ID
     **/
    private Integer brandId;

    /**
     * 经销商ID
     **/
    private Integer dealerId;

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }
}
