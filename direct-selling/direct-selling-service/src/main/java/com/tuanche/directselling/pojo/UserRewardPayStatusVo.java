package com.tuanche.directselling.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @class: UserRewardPayStatusVo
 * @description:
 * @author: dudg
 * @create: 2020-09-29 16:39
 */
public class UserRewardPayStatusVo implements Serializable {

    public UserRewardPayStatusVo(Long tradeRecordId, List<Integer> rewardIdList) {
        this.tradeRecordId = tradeRecordId;
        this.rewardIdList = rewardIdList;
    }

    /**
     * 打款记录id
     */
    private Long tradeRecordId;
    /**
     * 打款 奖励ids
     */
    private List<Integer> rewardIdList;

    public Long getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(Long tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
    }

    public List<Integer> getRewardIdList() {
        return rewardIdList;
    }

    public void setRewardIdList(List<Integer> rewardIdList) {
        this.rewardIdList = rewardIdList;
    }
}
