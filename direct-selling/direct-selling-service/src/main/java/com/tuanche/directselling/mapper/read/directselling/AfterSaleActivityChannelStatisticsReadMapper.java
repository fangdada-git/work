package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-10 17:27
 */
public interface AfterSaleActivityChannelStatisticsReadMapper {

    /**
     * 根据活动ID获取渠道统计信息
     * @author HuangHao
     * @CreatTime 2021-09-13 16:33
     * @param activityId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityChannelStatistics>
     */
    List<AfterSaleActivityChannelStatistics> getListByActivityId(@Param("activityId") Integer activityId);
    /**
     * 获取活动统计的ID
     * @author HuangHao
     * @CreatTime 2021-08-23 18:31
     * @param activityIds
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getActivityChanelStatisticIdMap(@Param("list") List<AfterSaleActivityChannelStatistics> list);
}
