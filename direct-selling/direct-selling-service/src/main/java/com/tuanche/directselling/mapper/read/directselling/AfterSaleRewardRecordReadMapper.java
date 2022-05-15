package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.AfterSaleRewardRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-09 17:39
 */
public interface AfterSaleRewardRecordReadMapper {

    /**
     * 获取正在进行中的且是已开启状态的活动已发放的奖励
     * @author HuangHao
     * @CreatTime 2021-08-09 17:42
     * @param
     * @return com.tuanche.directselling.dto.AfterSaleRewardRecordAmountAlarmDto
     */
    List<AfterSaleRewardRecordAmountAlarmDto> amountAlarm();

    AfterSaleRewardRecordAmountAlarmDto getamountAlarmByTime(@Param("startTime")Date startTime, @Param("endTime")Date endTime);

    /**
     * 用户奖励统计
     * @author HuangHao
     * @CreatTime 2021-08-18 11:27
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleUserRewardStatistics>
     */
    List<AfterSaleUserRewardStatistics> userRewardStatistics(@Param("activityIds") List<Integer> activityIds);

    /**
     * 按活动统计礼品券发放总数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> giftCertificatesIssuedMap(@Param("activityIds") List<Integer> activityIds);
    /**
     * 获取奖励总额
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getRewardTotalMap(@Param("activityIds") List<Integer> activityIds);
    /**
     * 获取获得奖励的分享数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getShareTotalMap(@Param("list") List<AfterSaleUserRewardStatistics> list);

    /**
     * 获取用户的奖励明细
     * @author HuangHao
     * @CreatTime 2021-08-23 15:14
     * @param afterSaleRewardRecord
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDetailDto>
     */
    List<AfterSaleUserRewardStatisticsDetailDto> getUserRewardDetailList(AfterSaleRewardRecord afterSaleRewardRecord);


    /**
     * 获取转账失败的支付类列表
     * @author HuangHao
     * @CreatTime 2021-08-20 14:56
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRewardRecordFailDto>
     */
    List<AfterSaleRewardRecordFailDto> getFailRewardRecordList(AfterSaleRewardRecord afterSaleRewardRecord);

    /**
     * @description 轮播奖励列表（一次随机查20条）
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRewardSlideshowDto>
     * @date 2021/10/26 11:49
     * @author lvsen
     */
    List<AfterSaleRewardSlideshowDto> selectSlideshowReward(AfterSaleRewardRecord afterSaleRewardRecord);
}
