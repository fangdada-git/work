package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleActivityAccount;

import java.util.List;
import java.util.Set;

/**
 * @author：HuangHao
 * @CreatTime 2021-12-21 10:23
 */
public class AfterSaleActivityAccountDto extends AfterSaleActivityAccount {

    private Set<Integer> activityIds;

    public Set<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(Set<Integer> activityIds) {
        this.activityIds = activityIds;
    }
}
