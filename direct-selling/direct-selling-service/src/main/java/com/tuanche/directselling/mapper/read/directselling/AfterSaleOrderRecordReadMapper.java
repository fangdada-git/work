package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleAgentOrderSalesStatDto;
import com.tuanche.directselling.dto.AfterSaleAgentOrderSalesStatPeriodDto;
import com.tuanche.directselling.dto.AfterSaleOrderPersonRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordShareStatDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordTotalByCarDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordWriteOffStatisticsDto;
import com.tuanche.directselling.dto.AfterSaleRedPacketDto;
import com.tuanche.directselling.dto.AfterSaleRedPacketListDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.model.AfterSaleActivityStatistics;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AfterSaleOrderRecordReadMapper extends MyBatisBaseDao<AfterSaleOrderRecord, Integer> {

    List<AfterSaleOrderRecord> getAfterSaleOrderRecordList(AfterSaleOrderRecordDto afterSaleOrderRecord);

    AfterSaleOrderRecord getAfterSaleOrderChangeInfo(AfterSaleOrderRecordDto afterSaleOrderRecord);

    Integer selectLatestIdByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 一天中每个代理的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);

    /**
     * 2：电销客服 3：裸连接（公众号）的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @MapKey("channel")
    Map<Integer, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCountByChannel(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);


    /**
     * 一天中每个代理转发（转介绍）的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesForwardCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);

    int selectAgentSalesForwardCountNonAgent(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);

    List<AfterSaleOrderRecord> getAfterSaleOrderRecordByOrderCodes(@Param("orderCodes") List<String> orderCodes);

    /**
     * 代理个人业绩明细
     *
     * @return
     */
    List<AfterSaleOrderPersonRecordDto> selectSaleOrderByAgent(AfterSaleOrderPersonRecordDto personRecordDto);

    List<AfterSaleOrderRecord> getAfterSaleOrderRecordListByPage(AfterSaleOrderRecordDto afterSaleOrderRecord);

    List<AfterSaleOrderRecord> keepOnRecordUserRefund(@Param("activityId") Integer activityId, @Param("dealerId")Integer dealerId);

    List<AfterSaleOrderRecord> getAfterSaleBuyListByPage(AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 核销统计
     *
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.vo.AfterSaleOrderRecordWriteOffStatisticsVo>
     * @author HuangHao
     * @CreatTime 2021-08-09 11:09
     */
    List<AfterSaleOrderRecordWriteOffStatisticsDto> writeOffStatistics(AfterSaleOrderRecordDto orderRecord);

    List<AfterSaleOrderRecord> getUserBuyCheck(AfterSaleOrderRecord query);

    /**
     * 获取用户邀请人数
     * @author HuangHao
     * @CreatTime 2021-08-18 15:07
     * @param list
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordShareStatDto>
     */
    List<AfterSaleOrderRecordShareStatDto> getShareTotal(@Param("list") List<AfterSaleUserRewardStatistics> list);


    /**
     * 获取用户身份 1：代理人 2：CC代理人 3：普通用户 4：备案用户
     * @author HuangHao
     * @CreatTime 2021-08-18 16:51
     * @param list
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordUserTypeDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getUserType(@Param("list") List<AfterSaleUserRewardStatistics> list);


    /**
     * 活动数据统计
     * @author HuangHao
     * @CreatTime 2021-08-20 11:00
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    List<AfterSaleActivityStatistics> activityStatistics(@Param("activityIds") List<Integer> activityIds);

    /**
     * 活动渠道数据统计
     * @author HuangHao
     * @CreatTime 2021-09-10 17:40
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    List<AfterSaleActivityChannelStatistics> activityChannelStatistics(@Param("activityIds") List<Integer> activityIds);

    /**
     * 获取代理人已经核销了的礼品券
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String,ResultMapDto> getAgentWriteOffGgiftCertificatesMap(@Param("list") List<AfterSaleUserRewardStatistics> list);

    /**
     * 获取分享人已经核销了的礼品券
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String,ResultMapDto> getShareWriteOffGgiftCertificatesMap(@Param("list") List<AfterSaleUserRewardStatistics> list);

    HashMap<String,Object> getOrderStatistics(AfterSaleOrderRecordDto recordDto);

    /**
     * 获取未核销的普通用户和已完成任务的备案用户
     * @author HuangHao
     * @CreatTime 2021-09-13 18:31
     * @param
     * @return java.util.List<java.lang.String>
     */
    List<AfterSaleOrderRecord> getNotWrittenOffUserOpenIdList(@Param("activityIds")List<Integer> activityIds);

    /**
     * 获取未核销且未完成裂变任务的备案用户
     * @author HuangHao
     * @CreatTime 2021-09-17 11:35
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getUnfinishedTaskUserList(@Param("activityIds")Set<Integer> activityIds);

    /**
     * 获取订单的分享人和代理人信息裂变
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    List<AfterSaleOrderRecordDto> getOrderSharerAndAgentList(AfterSaleOrderRecordDto afterSaleOrderRecordDto);

    /**
     * @description 获得红包榜
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRedPacketListDto>
     * @date 2021/10/26 16:00
     * @author lvsen
     */
    List<AfterSaleRedPacketListDto> selectAfterRedPacketListByPage(AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * @description 获得红包榜总金额
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRedPacketListDto>
     * @date 2021/10/26 16:00
     * @author lvsen
     */
    Integer selectAfterSaleListTotal(AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * @description 获得用户红包列表
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRedPacketListDto>
     * @date 2021/10/26 16:00
     * @author lvsen
     */
    List<AfterSaleRedPacketDto> selectAfterRedPacketsByUser(AfterSaleActivityVo activityVo);

    /**
     * @description 获得邀请人
     * @return int
     * @date 2021/11/13 15:18
     * @author lvsen
     */
    int selectAfterShareCountByUser(@Param("activityVo") AfterSaleActivityVo activityVo);

    /**
     * 用户红包总金额
     *
     * @param activityVo
     * @return
     */
    BigDecimal selectAfterRedPacketsUserTotal(AfterSaleActivityVo activityVo);


    /**
     * 代理售出套餐卡数
     *
     * @param startTime
     * @param endTime
     * @param activityId
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesPackageCardCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    /**
     * 转介绍
     *
     * @param startTime
     * @param endTime
     * @param activityId
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesForwardPackageCardCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    /**
     * 售卡数
     *
     * @param startTime
     * @param endTime
     * @param activityId
     * @return
     */
    @MapKey("dateTime")
    Map<String, AfterSaleAgentOrderSalesStatPeriodDto> selectPeriodAgentSalesPackageCardCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    /**
     * 总售卡数
     *
     * @param startTime
     * @param activityId
     * @return
     */
    int selectAgentSalesPackageCardTotal(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    List<AfterSaleOrderRecordTotalByCarDto> getAfterSaleOrderTotalToCarByPage(AfterSaleOrderRecordTotalByCarDto orderRecordTotalByCarDto);

    /**
     * 根据手机号或车牌号查是否有记录
     * @param activityId
     * @param userPhone
     * @param licencePlate
     * @return
     */
    int selectAfterSaleAgentOrderCount(@Param("activityId") Integer activityId, @Param("userPhone") String userPhone, @Param("licencePlate") String licencePlate);

    /**
     * 获取未分账的订单
     * @author HuangHao
     * @CreatTime 2021-12-13 17:24
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getUndistributeAccountOrderList(AfterSaleOrderRecordDto afterSaleOrderRecordDto);
}