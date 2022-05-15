package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionUser;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @class: FissionUserRewardRankDto
 * @description: 用户奖励排行实体
 * @author: zhangyixin
 * @create: 2020-09-29 18:01
 */
public class FissionUserRewardRankDto extends FissionUser implements Serializable {

    /**
     * 总奖励金
     */
    private BigDecimal rewardAmount;

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
