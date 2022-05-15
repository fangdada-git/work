package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveSceneDealerBrand;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: LiveSceneDealerBrandDto
 * @Description: 场次活动经销商品牌Dto
 * @Author: GongBo
 * @Date: 2020/4/15 15:12
 * @Version 1.0
 **/
public class LiveSceneDealerBrandDto extends LiveSceneDealerBrand implements Serializable {
    /**
     * 获取品牌集合数据
     **/
    private String brandMapList;

    /**
     * 保量目标
     **/
    private Integer ensureSize;
    //合同经销商名称
    private String contractDealerName;

    public String getBrandMapList() {
        return brandMapList;
    }

    public void setBrandMapList(String brandMapList) {
        this.brandMapList = brandMapList;
    }

    public Integer getEnsureSize() {
        return ensureSize;
    }

    public void setEnsureSize(Integer ensureSize) {
        this.ensureSize = ensureSize;
    }

    public String getContractDealerName() {
        return contractDealerName;
    }

    public void setContractDealerName(String contractDealerName) {
        this.contractDealerName = contractDealerName;
    }
}
