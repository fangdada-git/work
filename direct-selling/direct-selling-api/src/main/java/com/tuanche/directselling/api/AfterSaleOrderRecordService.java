package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.model.AfterSaleActivityStatistics;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.AfterSaleActivityVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AfterSaleOrderRecordService {

    AfterSaleOrderRecord getById(Integer id);

    List<AfterSaleOrderRecord> getAfterSaleOrderRecordByOrderCodes (List<String> OrderCodes);

    Integer addAfterSaleOrderRecord (AfterSaleOrderRecord afterSaleOrderRecord);

    Integer updateAfterSaleOrderRecord (AfterSaleOrderRecord afterSaleOrderRecord);
    Integer updateByOrderCode (AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 获取非备案的裂变总人数
     * @author HuangHao
     * @CreatTime 2021-07-22 18:24
     * @param afterSaleOrderRecord
     * @return int
     */
    int getNonFilingFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取裂变的人数
     * @author HuangHao
     * @CreatTime 2021-10-21 16:45
     * @param afterSaleOrderRecord
     * @return int
     */
    int getFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取非备案的裂变信息列表
     * @author HuangHao
     * @CreatTime 2021-07-28 22:47
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getNonFilingFissionList(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取裂变人的裂变列表
     * @author HuangHao
     * @CreatTime 2021-10-22 17:40
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getFissionList(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取购买人的单个订单信息
     *
     * @param activityId
     * @param shareWxUnionId
     * @param orderType
     * @return com.tuanche.directselling.model.AfterSaleOrderRecord
     * @author HuangHao
     * @CreatTime 2021-07-25 10:58
     */
    AfterSaleOrderRecord getBuyerOrder(Integer activityId, String userWxUnionId, Integer orderType);

    void updateAfterSaleOrderPaySuccess(AfterSaleOrderRecord record);

    PageResult<AfterSaleOrderRecordDto> getAfterSaleOrderRecordListByPage(int page, int limit, AfterSaleOrderRecordDto afterSaleOrderRecord);

    List<AfterSaleOrderRecordDto> getAfterSaleOrderRecordDtoList(AfterSaleOrderRecordDto afterSaleOrderRecord);

    List<AfterSaleOrderRecord> getAfterSaleOrderRecordList(AfterSaleOrderRecordDto afterSaleOrderRecord);

    AfterSaleOrderRecord getAfterSaleOrderChangeInfo(AfterSaleOrderRecordDto afterSaleOrderRecord);

    AfterSaleOrderRecord getUserBuyFlag(AfterSaleOrderRecord query);

    /**
     * @param :
     * @return :
     * @description : 备案用户自动退款
     * @author : fxq
     * @date : 2021/7/29 20:07
     */
    void keepOnRecordUserRefund (Integer activityId, Integer dealerId);

    PageResult<AfterSaleOrderRecord> getAfterSaleBuyListByPage(PageResult<AfterSaleOrderRecord> pageResult, AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 根据order_code修改车牌
     *
     * @param orderCodes
     * @param licencePlate
     * @param dealerId
     * @return
     */
    int updateLicencePlateByOrderCode(List<String> orderCodes, String licencePlate);

    /**
     * 根据order_code验证车牌是否重复
     *
     * @param orderCodes
     * @param licencePlate
     * @return
     */
    int validatePlateByOrderCode(List<String> orderCodes, String licencePlate);

    /**
     * 核销统计
     *
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.vo.AfterSaleOrderRecordWriteOffStatisticsVo>
     * @author HuangHao
     * @CreatTime 2021-08-09 11:09
     */
    List<AfterSaleOrderRecordWriteOffStatisticsDto> writeOffStatistics(AfterSaleOrderRecordDto orderRecord);

    List<AfterSaleOrderRecord> getUserBuyCheck(AfterSaleOrderRecord record);

    /**
     * 获取用户邀请人数-map
     * @author HuangHao
     * @CreatTime 2021-08-18 15:07
     * @param list
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordShareStatDto>
     */
    Map<String,AfterSaleOrderRecordShareStatDto> getShareTotalMap(List<AfterSaleUserRewardStatistics> list);

    /**
     * 获取用户身份 1：代理人 2：CC代理人 3：普通用户 4：备案用户
     * @author HuangHao
     * @CreatTime 2021-08-18 16:51
     * @param list
     * @return Map<String,AfterSaleOrderRecordUserTypeDto>
     */
    Map<String, ResultMapDto> getUserTypeMap(List<AfterSaleUserRewardStatistics> list);

    /**
     * 活动数据统计
     * @author HuangHao
     * @CreatTime 2021-08-20 11:00
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    List<AfterSaleActivityStatistics> activityStatistics(List<Integer> activityIds);

    /**
     * 活动渠道数据统计
     * @author HuangHao
     * @CreatTime 2021-9-13 11:33:27
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    List<AfterSaleActivityChannelStatistics> activityChannelStatistics(List<Integer> activityIds);
    /**
     * 获取代理人已经核销了的礼品券
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getAgentWriteOffGgiftCertificatesMap(List<AfterSaleUserRewardStatistics> list);
    /**
     * 获取分享人已经核销了的礼品券
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String,ResultMapDto> getShareWriteOffGgiftCertificatesMap(List<AfterSaleUserRewardStatistics> list);

    /**
      * @description : 订单统计
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/8/25 11:15
      */
    HashMap<String, Object> getOrderStatistics (AfterSaleOrderRecordDto recordDto);

    /**
     * 获取订单的分享人和代理人信息列表
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    List<AfterSaleOrderRecordDto> getDealerOrderSharerAndAgentList(AfterSaleOrderRecordDto afterSaleOrderRecordDto);

    /**
     * 获取经销订单列表（带分享人和代理人信息）-分页
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    PageResult<AfterSaleOrderRecordDto> getDealerOrderSharerAndAgentListByPage(PageResult<AfterSaleOrderRecordDto> pageResult, AfterSaleOrderRecordDto afterSaleOrderRecordDto);
    /**
     * 获取订单列表（带分享人和代理人信息）-分页
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    PageResult<AfterSaleOrderRecordDto> getOrderSharerAndAgentListByPage(PageResult<AfterSaleOrderRecordDto> pageResult, AfterSaleOrderRecordDto afterSaleOrderRecordDto);

    /**
     * @description 活动红包榜
     * @param [pageResult, afterSaleOrderRecord]
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.model.AfterSaleOrderRecord>
     * @date 2021/10/27 11:08
     * @author lvsen
     */
    PageResult<AfterSaleRedPacketListDto> getAfterSaleRedPackListByPage(PageResult<AfterSaleRedPacketListDto> pageResult, AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 获取用户红包列表
     * @return
     */
    PageResult<AfterSaleRedPacketDto> getSaleRedPacketsByUser(PageResult<AfterSaleRedPacketDto> pageResult, AfterSaleActivityVo activityVo);

    /**
     * @description : 手动核销
     * @author : fxq
     * @param : checkoutType 核销类型 1:订单类卡券核销  2：券码类卡券核销
     * @param : orderCode 订单编号
     * @param : dealerId 经销商id
     * @param : salesId 销售id
     * @param : type 订单类卡券核销时，是否需要客户领券 null,0：不需要  其他需要
     * @param : userCouponId 用户卡券id
     * @return :
     * @date : 2021/11/15 15:25
     */
    String checkoutCouponByOrderCode (Integer checkoutType, String orderCode,Integer dealerId, Integer salesId, Integer type, Integer userCouponId);

    PageResult<AfterSaleOrderRecordTotalByCarDto> getAfterSaleOrderTotalByCar(PageResult<AfterSaleOrderRecordTotalByCarDto> pageResult, AfterSaleOrderRecordTotalByCarDto orderRecordTotalByCarDto);

    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-12-20 17:27
     * @param list
     * @return int
     */
    int updateBatchByIds(List<AfterSaleOrderRecord> list);
}
