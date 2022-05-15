package com.tuanche.directselling.api;

import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDetailDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.enums.AfterSaleRewardTypeEnums;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleRewardRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-25 10:05
 */
public interface AfterSaleRewardRecordService {

    /**
     * 售后特卖奖励
     * @author HuangHao
     * @CreatTime 2021-07-25 10:05
     * @param order
     * @return com.tuanche.commons.util.ResultDto
     */
    ResultDto reward(AfterSaleOrderRecord order);

    /**
     * 根据ID更新
     * @author HuangHao
     * @CreatTime 2021-08-02 16:12
     * @param afterSaleRewardRecord
     * @return int
     */
    int updateById(AfterSaleRewardRecord afterSaleRewardRecord);

    /**
     * 更新奖励状态
     * @author HuangHao
     * @CreatTime 2021-08-02 16:26
     * @param activityId    活动ID
     * @param wxUnionId     用户微信unionid
     * @param rewardType    奖励类型
     * @param payStatus     状态
     * @return int
     */
    int updatePayStatus(Integer activityId, String wxUnionId, AfterSaleRewardTypeEnums rewardType, AfterSaleConstants.RewardPayStatus payStatus);

    /**
     * 用户奖励统计
     * @author HuangHao
     * @CreatTime 2021-08-18 11:27
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleUserRewardStatistics>
     */
    List<AfterSaleUserRewardStatistics> userRewardStatistics(List<Integer> activityIds);

    /**
     * 按活动统计礼品券发放总数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> giftCertificatesIssuedMap(List<Integer> list);

    /**
     * 获取奖励总额
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getRewardTotalMap(List<Integer> list);
    /**
     * 获取获得奖励的分享数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getShareTotalMap(List<AfterSaleUserRewardStatistics> list);
    /**
     * 获取用户的奖励明细-分页
     * @author HuangHao
     * @CreatTime 2021-08-23 15:14
     * @param afterSaleRewardRecord
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDetailDto>
     */
    PageResult<AfterSaleUserRewardStatisticsDetailDto> getUserRewardDetailListByPage(PageResult<AfterSaleUserRewardStatisticsDetailDto> pageResult, Integer activityId, String userWxUnionId);

    /**
     * 补发失败了的奖励
     * @author HuangHao
     * @CreatTime 2021-08-20 14:56
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRewardRecordFailDto>
     */
    void reissueReward(Integer activityId);
    /**
     * 获取用户类型  0：备案用户 1：流失用户 2：普通用户 3：车商代理人
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     * @param afterSaleKeepOnRecord
     * @return int
     */
    Integer getUserType(AfterSaleOrderRecord order);

    void getamountAlarmByTime (String params);
}
