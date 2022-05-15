package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDto;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface AfterSaleUserRewardStatisticsReadMapper {

    /**
     * 批量获取用户的id
     * @author HuangHao
     * @CreatTime 2021-08-19 10:49
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleUserRewardStatistics>
     */
    List<AfterSaleUserRewardStatistics> getStatisticsIds(@Param("list") List<AfterSaleUserRewardStatistics> list);

    List<AfterSaleUserRewardStatisticsDto> getStatisticsListByPage(AfterSaleUserRewardStatisticsDto rewardStatistics);

    HashMap<String, Object> getStatisticsCountMap(AfterSaleUserRewardStatisticsDto rewardStatistics);
}