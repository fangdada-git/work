package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AfterSaleActivityStatisticsReadMapper {

    List<AfterSaleActivityDataVerifyDto> getByActivityId(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);
    List<AfterSaleActivityDataVerifyDto> selectAfterSaleActivityDataVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);

    AfterSaleActivityDataVerifyDto selectAfterSaleActivityDataVerifySum(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);

    List<AfterSaleDealerPaymentVerifyDto> selectAfterSaleDealerPaymentVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);

    AfterSaleActivityDataDetailDto selectAfterSaleActivityDataDetailDto(@Param("activityId") Integer activityId);

    /**
     * 获取活动统计的ID
     * @author HuangHao
     * @CreatTime 2021-08-23 18:31
     * @param activityIds
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getActivityStatisticIdMap(@Param("activityIds") List<Integer> activityIds);


    int selectAfterSaleActivityCount(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);
}