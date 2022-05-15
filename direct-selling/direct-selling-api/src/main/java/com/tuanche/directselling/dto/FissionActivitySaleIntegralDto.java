package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivity;

import java.io.Serializable;

/**
 * 裂变活动和销售信息
 * @author ZhangYiXin
 * @date 2020/9/23 18:03
 * @version 1.0
 **/
public class FissionActivitySaleIntegralDto extends FissionActivity implements Serializable {

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 活动ID
     */
    private Integer fissionId;

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }
}
