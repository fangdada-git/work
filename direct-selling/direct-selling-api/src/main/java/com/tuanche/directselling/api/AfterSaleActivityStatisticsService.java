package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-13 16:46
 */
public interface AfterSaleActivityStatisticsService {

    /**
     * 根据活动ID获取渠道统计信息
     * @author HuangHao
     * @CreatTime 2021-09-13 16:33
     * @param activityId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityChannelStatistics>
     */
    List<AfterSaleActivityChannelStatistics> getListByActivityId(Integer activityId);
}
