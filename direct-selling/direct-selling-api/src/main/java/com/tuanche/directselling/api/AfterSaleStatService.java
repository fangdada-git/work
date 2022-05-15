package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.AfterSaleActivityDataBaseDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataVerifyDto;
import com.tuanche.directselling.dto.AfterSaleAgentOrderSalesStatDto;
import com.tuanche.directselling.dto.AfterSaleAgentStatDetailDto;
import com.tuanche.directselling.dto.AfterSaleAgentStatDto;
import com.tuanche.directselling.dto.AfterSaleDealerPaymentVerifyDto;
import com.tuanche.directselling.dto.AfterSaleOrderPersonRecordDto;
import com.tuanche.directselling.enums.AfterSaleAgentSortType;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.AfterSaleActivityDataVerifyVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 排行、统计
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/20 21:41
 **/
public interface AfterSaleStatService {

    /**
     * 分享统计查询
     *
     * @param activityId 活动ID
     * @param startTime
     * @param endTime
     * @param orderBy    排序字段
     * @return
     */
    List<AfterSaleAgentStatDto> selectShareStat(Integer activityId, Date startTime, Date endTime, AfterSaleAgentSortType afterSaleAgentSortType);

    /**
     * 分享统计详细查询
     *
     * @param activityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<AfterSaleAgentStatDetailDto> selectShareStatDetail(Integer activityId, Date startTime, Date endTime);

    /**
     * 分享统计详细数据生成
     *
     * @param activityId
     * @return
     */
    void createShareStatDetail(Integer activityId);

    /**
     * 代理个人业绩明细
     *
     * @return
     */
    List<AfterSaleOrderPersonRecordDto> selectSaleOrderByAgent(AfterSaleOrderPersonRecordDto personRecordDto);

    /**
     * 一天中每个代理的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCount(Date startTime, Date endTime, Integer dealerId, Integer activityId);

    /**
     * 2：电销客服 3：裸连接（公众号）的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<Integer, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCountByChannel(Date startTime, Date endTime, Integer dealerId, Integer activityId);


    /**
     * 一天中每个代理转发（转介绍）的销量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesForwardCount(Date startTime, Date endTime, Integer dealerId, Integer activityId);

    /**
     * 无代理的转介绍
     *
     * @param startTime
     * @param endTime
     * @param dealerId
     * @param activityId
     * @return
     */
    int selectAgentSalesForwardCountNonAgent(Date startTime, Date endTime, Integer dealerId, Integer activityId);

    /**
     * 浏览次数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer selectPageView(Date startTime, Date endTime, Integer dealerId, Integer activityId);

    /**
     * 浏览人数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer selectUniqueVisit(Date startTime, Date endTime, Integer dealerId, Integer activityId);

    /**
     * 活动数据对账
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    List<AfterSaleActivityDataVerifyVo> selectAfterSaleActivityDataVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);


    /**
     * 活动数据对账
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    PageResult selectAfterSaleActivityDataVerifyPage(int page, int limit, AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);


    /**
     * 活动数据对账总计
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    AfterSaleActivityDataVerifyDto selectAfterSaleActivityDataVerifySum(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);


    /**
     * 经销商打款对账
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    List<AfterSaleDealerPaymentVerifyDto> selectAfterSaleDealerPaymentVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);


    /**
     * 经销商打款对账
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    PageResult selectAfterSaleDealerPaymentVerifyPage(int page, int limit, AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);

    /**
     * 活动数据明细
     *
     * @param activityId
     * @return
     */
    AfterSaleActivityDataDetailDto selectAfterSaleActivityDataDetailDto(Integer activityId);

    /**
     * 活动场数
     *
     * @param afterSaleActivityDataBaseDto
     * @return
     */
    int selectAfterSaleActivityCount(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto);

    /**
     * 活动数据统计详情
     * @author HuangHao
     * @CreatTime 2021-09-17 17:02
     * @param activityId
     * @return com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto
     */
    AfterSaleActivityDataDetailDto getActivityDataDetailDto(Integer activityId);
}
