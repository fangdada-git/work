package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDto;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface AfterSaleUserRewardStatisticsService {

    void userRewardStatistics(List<Integer> activityIds);

    Map<String, Integer> getStatisticsIdMap(List<AfterSaleUserRewardStatistics> list);

    PageResult<AfterSaleUserRewardStatisticsDto> getStatisticsListByPage(PageResult<AfterSaleUserRewardStatisticsDto> pageResult, AfterSaleUserRewardStatisticsDto rewardStatistics);

    Map<String, Object> getStatisticsCountMap(AfterSaleUserRewardStatisticsDto rewardStatistics);
}
