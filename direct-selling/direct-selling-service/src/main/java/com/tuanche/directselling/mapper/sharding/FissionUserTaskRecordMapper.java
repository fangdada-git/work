package com.tuanche.directselling.mapper.sharding;

import com.tuanche.directselling.dto.FissionStatisticIntDto;
import com.tuanche.directselling.dto.FissionUserRewardRankDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordCountDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordDetailDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordStatDto;
import com.tuanche.directselling.model.FissionUserTaskRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FissionUserTaskRecordMapper {

    /**
     * 活动中所有销售各项数据统计
     *
     * @param fissionId
     * @param saleWxOpenIds
     * @return
     */
    List<FissionUserTaskRecordCountDto> selectUserTaskRecordByFissionIdAndSaleWxUnionIds(@Param("fissionId") Integer fissionId, @Param("saleWxOpenIds") List<String> saleWxOpenIds);
    /**
     * 活动中所有经销商各项数据统计
     *
     * @param fissionId
     * @param dealerIdIds
     * @return
     */
    List<FissionUserTaskRecordCountDto> selectUserTaskRecordByFissionIdAndDealerIdIds(@Param("fissionId") Integer fissionId, @Param("dealerIdIds") List<Integer> dealerIdIds);

    /**
     * 分享结果的数据明细
     *
     * @param fissionId
     * @param taskId
     * @return
     */
    List<FissionUserTaskRecordDetailDto> selectFissionUserTaskRecordDetailDto(@Param("fissionId") Integer fissionId, @Param("saleWxUnionId") String saleWxUnionId, @Param("taskId") Integer taskId);


    /**
     * 预约直播or观看直播人数
     *
     * @param fissionId
     * @param taskId
     * @return
     */
    Integer selectSubscribeOrLiveUserCount(@Param("fissionId") Integer fissionId, @Param("taskId") Integer taskId);

    /**
     * 预约直播并观看直播人数
     *
     * @param fissionId
     * @return
     */
    Integer selectSubscribeLiveUserCount(@Param("fissionId") Integer fissionId);

    /**
     * 预约直播or观看直播记录UserWxUnionIds
     *
     * @param fissionId
     * @param taskId
     * @param userWxUnionIds 用户的WxUnionId
     * @return
     */
    List<String> selectSubscribeOrLiveUserWxUnionIds(@Param("fissionId") Integer fissionId, @Param("taskId") Integer taskId, @Param("userWxUnionIds") List<String> userWxUnionIds);


    /**
     * 分享结果的数据明细
     *
     * @param fissionId
     * @param taskId
     * @return
     */
    List<FissionUserTaskRecordDetailDto> selectFissionUserTaskRecordDetailDtoWithSaleName(@Param("fissionId") Integer fissionId, @Param("taskId") Integer taskId, @Param("saleName") String saleName, @Param("dealerIds") List<Integer> dealerIds, @Param("isEffective") Integer isEffective);

    /**
     * 是否有观看直播记录
     *
     * @param fissionId
     * @param userWxUnionId
     * @return
     */
    Integer selectWatchLiveCount(@Param("fissionId") Integer fissionId, @Param("userWxUnionId") String userWxUnionId);

    /**
     * 查询已开启状态的任务
     *
     * @param taskRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-02-20 17:21
     */
    Integer hasActivityBeginTask(FissionUserTaskRecord taskRecord);

    /**
     * C端用户任务记录统计
     *
     * @param fissionId
     * @param shareWxOpenIds 分享人IDS
     * @return
     */
    List<FissionUserTaskRecordStatDto> selectUserTaskRecordStat(@Param("fissionId") Integer fissionId, @Param("channelId") Integer channelId, @Param("shareWxUnionIds") List<String> shareWxUnionIds);

    /**
     * 用户奖金排名
     *
     * @param fissionId
     * @return
     */
    List<FissionUserRewardRankDto> selectFissionUserRewardRank(@Param("fissionId") Integer fissionId, @Param("channelId") Integer channelId);

    /**
     * 新增用户任务记录
     *
     * @param fissionUserEffectiveTask
     * @return int
     * @author HuangHao
     * @CreatTime 2020-09-23 14:31
     */
    int insertFissionUserTaskRecord(FissionUserTaskRecord taskRecord);


    /**
     * 预约用户进入直播间总人数
     *
     * @param fissionId
     * @return
     */
    FissionStatisticIntDto selectSubscribeLiveUserCountStatistic(@Param("fissionId") Integer fissionId, @Param("saleWxOpenId") String saleWxOpenId, @Param("shareWxOpenId") String shareWxOpenId, @Param("userWxOpenId") String userWxOpenId, @Param("channel") Integer channel);

    List<FissionUserTaskRecordStatDto> selectDistinctUserTaskRecordStat(@Param("fissionId") Integer fissionId, @Param("channelId") Integer channelId, @Param("shareWxUnionIds") List<String> shareWxUnionIds);

    /**
     * 数据统计通用方法
     *
     * @param distinctType  排重类型
     * @param fissionId     活动id
     * @param taskId        任务id
     * @param saleWxOpenId  销售wxid是否为null
     * @param shareWxOpenId 分享者wxid是否为null
     * @param userWxOpenId  用户wxid是否为null
     * @param source        来源
     * @param channel       渠道
     * @return
     */
    FissionStatisticIntDto selectStatisticData(@Param("distinctType") String distinctType, @Param("fissionId") Integer fissionId, @Param("taskId") Integer taskId, @Param("saleWxOpenId") String saleWxOpenId, @Param("shareWxOpenId") String shareWxOpenId, @Param("userWxOpenId") String userWxOpenId, @Param("source") Integer source, @Param("channel") Integer channel);

    /**
     * 获取第一个非自然渠道记录
     * @author HuangHao
     * @CreatTime 2021-04-08 16:38
     * @param FissionUserTaskRecord
     * @return com.tuanche.directselling.model.FissionUserTaskRecord
     */
    FissionUserTaskRecord getFirstUnnaturalChannel(FissionUserTaskRecord FissionUserTaskRecord);
}
