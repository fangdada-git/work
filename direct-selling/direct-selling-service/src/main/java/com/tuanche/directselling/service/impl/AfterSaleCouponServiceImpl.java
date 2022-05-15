package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.certificate.api.api.ICouponService;
import com.tuanche.certificate.api.api.IUserCertificateService;
import com.tuanche.certificate.api.dto.input.CouponParamDto;
import com.tuanche.certificate.api.dto.input.SingleCouponDto;
import com.tuanche.certificate.api.dto.output.CertificateBatchDto;
import com.tuanche.certificate.api.dto.output.PageBeanDto;
import com.tuanche.certificate.api.dto.output.UserCertificateDto;
import com.tuanche.certificate.api.exception.SendCertificateException;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleCouponService;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.enums.AfterSaleRewardTypeEnums;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityCouponReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityPackageReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleCouponRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleCouponRecordWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.AfterSaleConstants.CouponStatus;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AfterSaleCouponServiceImpl implements AfterSaleCouponService {

    @Autowired
    private AfterSaleCouponRecordReadMapper afterSaleCouponRecordReadMapper;
    @Autowired
    private AfterSaleCouponRecordWriteMapper afterSaleCouponRecordWriteMapper;
    @Reference
    private ICouponService iCouponService;
    @Reference
    private IUserCertificateService iUserCertificateService;
    @Autowired
    private AfterSaleActivityServiceImpl afterSaleActivityServiceImpl;
    @Autowired
    private AfterSaleActivityCouponReadMapper afterSaleActivityCouponReadMapper;
    @Autowired
    private AfterSaleOrderRecordServiceImpl afterSaleOrderRecordServiceImpl;
    @Autowired
    private AfterSaleRewardRecordServiceImpl afterSaleRewardRecordServiceImpl;
    @Autowired
    private AfterSaleActivityPackageReadMapper afterSaleActivityPackageReadMapper;
    @Autowired
    private AfterSaleActivityReadMapper afterSaleActivityReadMapper;
    @Autowired
    private AfterSaleActivityCouponServiceImpl afterSaleActivityCouponServiceImpl;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private UserBaseService userBaseService;

    @Override
    public List<AfterSaleCouponDto> getAfterSaleCouponList (AfterSaleCouponRecord saleCouponRecord) {
        List<AfterSaleCouponDto> couponList =afterSaleCouponRecordReadMapper.getAfterSaleCouponList(saleCouponRecord);
        if (CollectionUtils.isNotEmpty(couponList)) {
            //卡券信息
            List<Integer> userCouponIds = couponList.stream().map(AfterSaleCouponRecord::getUserCouponId).collect(Collectors.toList());
            Map<Integer,UserCertificateDto> map = getUserCouponMap(userCouponIds);
            couponList.forEach(v->{
                if (map.get(v.getUserCouponId())!=null) {
                    UserCertificateDto userCertificateDto = map.get(v.getUserCouponId());
                    v.setCouponName(userCertificateDto.getCertName());
                    v.setChangeStartDate(userCertificateDto.getUseStartTime());
                    v.setChangeEndDate(userCertificateDto.getUseEndTime());
                    v.setCouponMoney(userCertificateDto.getCertMoney());
                }
            });
        }
        return couponList;
    }

    @Override
    public PageResult<AfterSaleCouponDto> getAfterSaleCouponListByPage(PageResult<AfterSaleCouponDto> pageResult, AfterSaleCouponRecord afterSaleCouponRecord) {
        Page pageHelp = PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleCouponDto> list = getAfterSaleCouponList(afterSaleCouponRecord);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> saleIds = list.stream().map(AfterSaleCouponDto::getCheckedSalesId).filter(v->v!=null).collect(Collectors.toList());
            Map<Integer, CsUser> userMap = userBaseService.getCsUserByIds(saleIds);
            if (userMap!=null && userMap.size()>0) {
                list.forEach(v->{
                    if (userMap.get(v.getCheckedSalesId())!=null) v.setCheckedSaleName(userMap.get(v.getCheckedSalesId()).getUname());
                });
            }
        }
        PageInfo<AfterSaleCouponDto> pageInfo = new PageInfo<>(pageHelp.getResult());
        PageResult<AfterSaleCouponDto> result = new PageResult<>();
        result.setCount(pageInfo.getTotal());
        result.setCode("0");
        result.setMsg("");
        result.setData(list);
        return result;
    }

    @Override
    public AfterSaleCouponDto getAfterSaleCouponByUserCouponId (Integer userCouponId, String userWxUnionId) {
        AfterSaleCouponDto couponRecord = afterSaleCouponRecordReadMapper.getAfterSaleCouponByUserCouponId(userCouponId, userWxUnionId);
        if (couponRecord!=null && couponRecord.getCouponId()!=null) {
            Map<Integer, UserCertificateDto> map = getUserCouponMap(Arrays.asList(couponRecord.getUserCouponId()));
            if (map.get(couponRecord.getUserCouponId())!=null) {
                UserCertificateDto couponBatchDto = map.get(couponRecord.getUserCouponId());
                couponRecord.setCouponName(couponBatchDto.getCertName());
                couponRecord.setChangeStartDate(couponBatchDto.getUseStartTime());
                couponRecord.setChangeEndDate(couponBatchDto.getUseEndTime());
            }
        }
        return couponRecord;
    }
    @Override
    public Integer addAfterSaleCoupon (AfterSaleCouponRecord saleCouponRecord) {
        afterSaleCouponRecordWriteMapper.insertSelective(saleCouponRecord);
        return saleCouponRecord.getId();
    }

    @Override
    public void addAfterSaleCouponList (List<AfterSaleCouponRecord> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            afterSaleCouponRecordWriteMapper.addAfterSaleCouponList(list);
        }
    }

    @Override
    public Integer updateAfterSaleCoupon (AfterSaleCouponRecord saleCouponRecord) {
        return afterSaleCouponRecordWriteMapper.updateByPrimaryKeySelective(saleCouponRecord);
    }
    @Override
    public Integer updateAfterSaleCouponByUserCouponId (AfterSaleCouponRecord saleCouponRecord) {
        return afterSaleCouponRecordWriteMapper.updateAfterSaleCouponByUserCouponId(saleCouponRecord);
    }

    public Map<Integer,UserCertificateDto> getUserCouponMap (List<Integer> userCouponIds) {
        Map<Integer,UserCertificateDto> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userCouponIds)) {
            List<UserCertificateDto> userCertificateDtos = iUserCertificateService.queryCertificateByUseIds(userCouponIds);
            if (userCertificateDtos !=null && CollectionUtils.isNotEmpty(userCertificateDtos)) {
                userCertificateDtos.forEach(v->{
                    map.put(v.getId(), v);
                });
            }
        }
        return map;
    }

    /**
      * @description : 发放卡券
      * @author : fxq
      * @param : orderCode 订单编码
      * @param : couponGiveOutType 卡券发放类型  枚举 AfterSaleConstants.CouponGiveOutType
      * @return :
      * @date : 2021/7/22 22:01
      */
    @Override
    public ResultDto giveOutCoupon (String orderCode, AfterSaleConstants.CouponGiveOutType couponGiveOutType) {
        ResultDto resultDto = new ResultDto();
        String msg = "";
        if (!StringUtil.isEmpty(orderCode) && couponGiveOutType!=null) {
            CommonLogUtil.addInfo("发放卡券", "start：", orderCode+"__"+couponGiveOutType);
            List<AfterSaleOrderRecord> recordList = afterSaleOrderRecordServiceImpl.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
            if (CollectionUtils.isNotEmpty(recordList)) {   
                AfterSaleOrderRecord record = recordList.get(0);
                try {
                    List<Integer> couponIds = new ArrayList<>();
                    Map<Integer, Integer> couponISendCountMap = new HashMap<>();
                    //发放礼品券
                    if (couponGiveOutType.equals(AfterSaleConstants.CouponGiveOutType.USER_GIFT)) {
                        AfterSaleActivityDto activity = afterSaleActivityServiceImpl.getAfterSaleActivityDtoById(record.getActivityId());
                        if (activity.getGiftCouponId()!=null) {
                            couponIds.add(activity.getGiftCouponId());
                            couponISendCountMap.put(activity.getGiftCouponId(), 1);
                        }
                    //用户发放兑换券  ??????????????
//                    } else if (couponGiveOutType.equals(AfterSaleConstants.CouponGiveOutType.USER_EXCHANGE)) {
//                        //推广卡
//                        if (record.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode())) {
//
//                            //套餐卡
//                        } else if (record.getOrderType().equals(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode())) {
//
//                        }
                        //用户发放抵用券
                    } else if (couponGiveOutType.equals(AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION)) {
                        List<AfterSaleActivityCoupon> activityCouponList = getActivityCouponList(record);
                        if (CollectionUtils.isNotEmpty(activityCouponList)) {
                            activityCouponList.forEach(v->{
                                if (v.getCouponId()!=null) {
                                    couponIds.add(v.getCouponId());
                                    if (couponISendCountMap.get(v.getCouponId())==null) {
                                        couponISendCountMap.put(v.getCouponId(), v.getCouponCount());
                                    } else {
                                        couponISendCountMap.put(v.getCouponId(), couponISendCountMap.get(v.getCouponId())+v.getCouponCount());
                                    }
                                }
                            });
                        }
                    }
                    if (CollectionUtils.isNotEmpty(couponIds)) {
                        //发放的卡券列表
                        CouponParamDto paramDto = new CouponParamDto();
                        paramDto.setSingleIds(couponIds);
                        paramDto.setPageSize(couponIds.size());
                        paramDto.setPageIndex(1);
                        PageBeanDto<SingleCouponDto> pageBeanDto = iCouponService.getCertificateBatchList(paramDto);
                        if (pageBeanDto!=null && CollectionUtils.isNotEmpty(pageBeanDto.getItems())) {
                            if (couponIds.size()!=pageBeanDto.getItems().size()) {
                                CommonLogUtil.addInfo("发放卡券", "卡券列表和卡券id不一致", JSON.toJSONString(couponIds)+"--"+JSON.toJSONString(pageBeanDto.getItems()));
                            }
                            List<AfterSaleCouponRecord> couponRecordList = new ArrayList<>();
                            for (SingleCouponDto v : pageBeanDto.getItems()) {
                                try {
                                    for (int i=0; i<couponISendCountMap.get(v.getId()); i++) {
                                        CertificateBatchDto batchDto = iUserCertificateService.sendSingleCertificate(record.getUserId(), Long.valueOf(record.getUserPhone()), v.getUuid(), 3,"1", null);
                                        CommonLogUtil.addInfo("发放卡券", "卡券系统单张卡券发放结果", batchDto==null? "":JSON.toJSONString(batchDto));
                                        if (batchDto!=null && batchDto.getUserCertId()!=null) {
                                            AfterSaleCouponRecord saleCouponRecord = new AfterSaleCouponDto();
                                            saleCouponRecord.setUserCouponId(batchDto.getUserCertId());
                                            saleCouponRecord.setCouponId(v.getId());
                                            saleCouponRecord.setCouponCode(v.getCode());
                                            saleCouponRecord.setActivityId(record.getActivityId());
                                            saleCouponRecord.setDealerId(record.getDealerId());
                                            saleCouponRecord.setCbId(record.getCbId());
                                            saleCouponRecord.setCsId(record.getCsId());
                                            saleCouponRecord.setOrderCode(record.getOrderCode());
                                            saleCouponRecord.setUserWxUnionId(record.getUserWxUnionId());
                                            saleCouponRecord.setCouponType(couponGiveOutType.equals(AfterSaleConstants.CouponGiveOutType.USER_GIFT)
                                                    ? AfterSaleConstants.CouponType.GIFT.getCode()
                                                    :AfterSaleConstants.CouponType.DEDUCTION.getCode());
                                            saleCouponRecord.setCouponStatus(couponGiveOutType.equals(AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION)
                                                    && record.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode())
                                                    ?AfterSaleConstants.CouponStatus.GRANT_NON.getCode()
                                                    :AfterSaleConstants.CouponStatus.USE_NON.getCode());
                                            couponRecordList.add(saleCouponRecord);
                                        } else {
                                            msg += ("单张卡券发放失败:couponId="+v.getId()+",");
                                            CommonLogUtil.addInfo("发放卡券", "单张卡券发放失败", JSON.toJSONString(v)+"--"+JSON.toJSONString(record) +"--"+JSON.toJSONString(batchDto));
                                        }
                                    }
                                } catch (SendCertificateException e) {
                                    msg += ("单张卡券发放error"+e.getMessage()+":couponId="+v.getId()+",");
                                    CommonLogUtil.addError("发放卡券，单张卡券发放error", orderCode+"__"+couponGiveOutType, e);
                                }
                            }
                            if (CollectionUtils.isNotEmpty(couponRecordList)) {
                                afterSaleCouponRecordWriteMapper.addAfterSaleCouponList(couponRecordList);
                            } else {
                                msg += "发放卡券失败,";
                                CommonLogUtil.addInfo("发放卡券", "失败：", orderCode+"__"+couponGiveOutType);
                            }
                        } else {
                            msg += "卡券异常:"+JSON.toJSONString(couponIds)+",";
                            CommonLogUtil.addInfo("发放卡券", "卡券异常：", JSON.toJSONString(couponIds));
                        }
                    } else {
                        msg += "卡券id为空,";
                        CommonLogUtil.addInfo("发放卡券", "卡券id为空：", orderCode+"__"+couponGiveOutType);
                    }
                } catch (Exception e) {
                    msg += "发放卡券error,";
                    CommonLogUtil.addError("发放卡券error", orderCode+"__"+couponGiveOutType, e);
                }
            } else {
                msg += "订单记录异常,";
                CommonLogUtil.addInfo("发放卡券", "订单记录异常：", orderCode+"__"+couponGiveOutType);
            }
        } else {
            msg += "参数错误,";
            CommonLogUtil.addInfo("发放卡券", "参数错误：", orderCode+"__"+couponGiveOutType);
        }
        if (!StringUtil.isEmpty(msg)) {
            resultDto.setMsg(msg);
            resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
        } else {
            resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        }
        CommonLogUtil.addInfo("发放卡券", "end：", orderCode+"__"+couponGiveOutType+"--"+msg);
        return resultDto;
    }

    private List<AfterSaleActivityCoupon> getActivityCouponList (AfterSaleOrderRecord record) {
        List<AfterSaleActivityCoupon> activityCouponList = new ArrayList<>();
                AfterSaleActivityCoupon activityCoupon = new AfterSaleActivityCoupon();
        activityCoupon.setActivityId(record.getActivityId());
        if (record.getOrderType().equals(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode())) {
            //活动关联的套餐商品
            AfterSaleActivityPackage activityPackage = new AfterSaleActivityPackage();
            activityPackage.setActivityId(record.getActivityId());
            activityPackage.setGoodsId(record.getGoodsId());
            List<AfterSaleActivityPackageDto> packageList = afterSaleActivityPackageReadMapper.getAfterSaleActivityPackageList(activityPackage);
            if (CollectionUtils.isNotEmpty(packageList)) {
                activityCoupon.setPackageId(packageList.get(0).getId());
                activityCoupon.setType(AfterSaleConstants.ActivityCoupon.TYPE_SYNTHESIZE_CARD.getCode());
            } else {
                return activityCouponList;
            }
        } else {
            activityCoupon.setType(AfterSaleConstants.ActivityCoupon.TYPE_PROMOTION_CARD.getCode());
        }
        activityCoupon.setRelStatus(AfterSaleConstants.ActivityCoupon.REL_STATUS_YES.getCode());
        activityCouponList = afterSaleActivityCouponReadMapper.selectCouponList(activityCoupon);
        return activityCouponList;
    }

    @Override
    public void delCoupon(String orderCode, List<Integer> couponTypeList) {
        if (!StringUtil.isEmpty(orderCode) && CollectionUtils.isNotEmpty(couponTypeList)) {
            List<AfterSaleOrderRecord> afterSaleOrderRecordByOrderCodes = afterSaleOrderRecordServiceImpl.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
            if (CollectionUtils.isNotEmpty(afterSaleOrderRecordByOrderCodes)) {
                AfterSaleOrderRecord orderRecord = afterSaleOrderRecordByOrderCodes.get(0);
                //删除奖励记录
                for (Integer v : couponTypeList) {
                    if (AfterSaleConstants.CouponType.GIFT.getCode().equals(v)) {
                        afterSaleRewardRecordServiceImpl.updatePayStatus(orderRecord.getActivityId(), orderRecord.getAgentWxUnionId(), AfterSaleRewardTypeEnums.REWARD_TYPE3, AfterSaleConstants.RewardPayStatus.PayStatus3);
                        break;
                    }
                }
                afterSaleCouponRecordWriteMapper.delCoupon(orderRecord.getUserWxUnionId(), orderCode, couponTypeList);
            }
        }
    }

    /**
      * @description : 扫码领券
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/8/5 11:01
      */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public ResultDto scanGetCoupon(String orderCodes, AfterSaleConstants.CouponType couponType, AfterSaleConstants.CouponStatus couponStatus) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        String msg = "";
        if (StringUtil.isEmpty(orderCodes) || couponType == null || couponStatus == null) {
            resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
            resultDto.setMsg(StatusCodeEnum.PARAM_IS_INVALID.getText());
        } else {
            if (couponStatus.equals(AfterSaleConstants.CouponStatus.USE_NON)) {
                for (String orderCode : orderCodes.split(",")) {
                    List<AfterSaleCouponDto> afterSaleCouponByOrderCode = afterSaleCouponRecordReadMapper.getAfterSaleCouponByOrderCode(orderCode, couponType.getCode());
                    if (afterSaleCouponByOrderCode.isEmpty()) {
                        msg += orderCode + "：无卡券<br>";
                        continue;
//                        resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
//                        resultDto.setMsg(StatusCodeEnum.PARAM_IS_INVALID.getText());
//                        return resultDto;
                    }
                    List<Integer> ids = new ArrayList<>();
                    for (AfterSaleCouponDto afterSaleCouponDto : afterSaleCouponByOrderCode) {
//                        if (!CouponStatus.GRANT_NON.getCode().equals(afterSaleCouponDto.getCouponStatus())) {
//                            resultDto.setCode(StatusCodeEnum.COUPON_ISSUED.getCode());
//                            resultDto.setMsg(StatusCodeEnum.COUPON_ISSUED.getText());
//                            return resultDto;
//                        }
                        if (CouponStatus.GRANT_NON.getCode().equals(afterSaleCouponDto.getCouponStatus())) ids.add(afterSaleCouponDto.getId());
                    }
//                    afterSaleCouponRecordWriteMapper.updateByOrderCode(orderCode, couponType.getCode(), couponStatus.getCode());
                    if (CollectionUtils.isNotEmpty(ids)) {
                        afterSaleCouponRecordWriteMapper.updateStatusByIds(couponStatus.getCode(), ids);
                        AfterSaleOrderRecord orderRecord = new AfterSaleOrderRecord();
                        orderRecord.setOrderCode(orderCode);
                        orderRecord.setOrderStatus(AfterSaleConstants.OrderStatus.CHECKOUT.getCode());
                        afterSaleOrderRecordServiceImpl.updateByOrderCode(orderRecord);
                    } else {
                        msg += orderCode + "：卡券已发放<br>";
                    }
                }
            } else {
                resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                resultDto.setMsg("卡券变更状态不是未使用");
            }
        }
        if (msg!="") resultDto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
        return resultDto;
    }


    @Override
    public List<AfterSaleCouponDto> getAfterSaleCouponWrittenOffList(AfterSaleCouponDto afterSaleCouponDto) {
        CommonLogUtil.addInfo("getAfterSaleCouponWrittenOffList", "销售查看待发卡券订单start：", JSON.toJSONString(afterSaleCouponDto));
        AfterSaleActivityDto afterSaleActivityDto = new AfterSaleActivityDto();
        afterSaleActivityDto.setDealerId(afterSaleCouponDto.getCheckedDealerId());
        afterSaleActivityDto.setOnState(1);
        List<AfterSaleActivity> afterSaleActivities = afterSaleActivityReadMapper.selectActivityList(afterSaleActivityDto);
        List<Integer> activityIds = new ArrayList<>();
        for (AfterSaleActivity activity : afterSaleActivities) {
            activityIds.add(activity.getId());
        }
        afterSaleCouponDto.setActivityIds(activityIds);
        List<AfterSaleCouponDto> list = afterSaleCouponRecordReadMapper.getAfterSaleCouponWrittenOffList(afterSaleCouponDto);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> goodsId = list.stream().map(AfterSaleCouponDto::getGoodsId).collect(Collectors.toList());
            CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
            queryRequestDto.setCommodityIds(goodsId);
            queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.COMMON);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            BaseResponseDto<PageableDto<CommodityResponseDto>> commodityList = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if (commodityList != null && commodityList.getData() != null && CollectionUtils.isNotEmpty(commodityList.getData().getList())) {
                Map<Integer, CommodityResponseDto> map = new HashMap<>();
                commodityList.getData().getList().forEach(v->{
                    map.put(v.getCommodityId(),v);
                });
                list.forEach(v->{
                    v.setCouponName(map.get(v.getGoodsId()).getCommodityName());
                });
            }
        }
        CommonLogUtil.addInfo("getAfterSaleCouponWrittenOffList", "销售查看待发卡券订单end：", JSON.toJSONString(list));
        return list;
    }
    /**
     * 获取核销礼品券数量
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54
     * @param
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> getWriteOffGiftVoucherMap(List<Integer> activityIds){
        if(CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleCouponRecordReadMapper.getWriteOffGiftVoucherMap(activityIds);
    }
    /**
     * 获取已经核销了礼品券的用户
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> getWriteOffGgiftCertificatesUserMap(List<AfterSaleUserRewardStatistics> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleCouponRecordReadMapper.getWriteOffGgiftCertificatesUserMap(list);
    }

    /**
      * @description : 补发卡券
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/24 11:13
      */
    @Override
    public ResultDto supplyAgainCoupon (Integer activityId, String orderCode, Integer activityStart) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
        CommonLogUtil.addInfo("补发卡券", "补发卡券start", activityId+"--"+orderCode +"--"+activityStart);
        try {
            List<AfterSaleOrderCouponInfoDto> AfterSaleOrderCouponInfoList = afterSaleCouponRecordReadMapper.supplyAgainCoupon(activityId, orderCode, activityStart);
            if (CollectionUtils.isNotEmpty(AfterSaleOrderCouponInfoList)) {
                //需要补发的订单
                List<String> orderCodeList = new ArrayList<>();
                //查询 活动、商品、卡券维度，发放的数量
                List<AfterSaleOrderCouponInfoDto> couponCountList = new ArrayList<>();
                AfterSaleOrderCouponInfoList.forEach(v->{
                    orderCodeList.add(v.getOrderCode());
                    AfterSaleOrderCouponInfoDto dto = new AfterSaleOrderCouponInfoDto();
                    dto.setActivityId(v.getActivityId());
                    dto.setGoodsId(v.getGoodsId());
                    couponCountList.add(dto);
                });
                //订单下各个卡券已经发放的数量map
                Map<String, List<AfterSaleOrderCouponInfoDto>> orderCouponCountMap = new HashMap<>();
                //订单、卡券，已发数量
                List<AfterSaleOrderCouponInfoDto> orderCouponCountList = afterSaleCouponRecordReadMapper.getOrderCouponCount(orderCodeList);
                if (CollectionUtils.isNotEmpty(orderCouponCountList)) {
                    orderCouponCountList.forEach(v->{
                        List<AfterSaleOrderCouponInfoDto> list = null;
                        if (orderCouponCountMap.get(v.getOrderCode())==null) {
                            list = new ArrayList<>();
                        } else {
                            list = orderCouponCountMap.get(v.getOrderCode());
                        }
                        list.add(v);
                        orderCouponCountMap.put(v.getOrderCode(), list);
                    });
                } else {
                    resultDto.setMsg("订单、卡券，已发数量 null");
                    CommonLogUtil.addInfo("补发卡券", "订单、卡券，已发数量 null", activityId+"--"+orderCode +"--"+activityStart);
                }
                //活动、商品、卡券维度 应发数量map
                Map<String, Map<Integer, Integer>> activityGoodsCouponCountMap = new HashMap<>();
                //活动、商品、卡券维度 应发数量
                List<AfterSaleOrderCouponInfoDto> activityGoodsCouponCountList = afterSaleCouponRecordReadMapper.getActivityGoodsCouponCount(couponCountList);
                if (CollectionUtils.isNotEmpty(activityGoodsCouponCountList)) {
                    activityGoodsCouponCountList.forEach(v->{
                        String key = v.getActivityId()+":"+v.getGoodsId();
                        Map<Integer, Integer> map = null;
                        if (activityGoodsCouponCountMap.get(key)==null) {
                            map = new HashMap<>();
                            map.put(v.getCouponId(), v.getCouponCount());
                        } else {
                            map = activityGoodsCouponCountMap.get(key);
                            map.put(v.getCouponId(), v.getCouponCount());
                        }
                        activityGoodsCouponCountMap.put(key, map);
                    });
                } else {
                    resultDto.setMsg("活动、商品、卡券维度 应发数量 null");
                    CommonLogUtil.addInfo("补发卡券", "活动、商品、卡券维度 应发数量 null", activityId+"--"+orderCode +"--"+activityStart);
                }
                //订单、卡券 需要补发的数量list
                List<AfterSaleOrderCouponInfoDto> supplyAgainList= new ArrayList<>();
                if (orderCouponCountMap!=null && orderCouponCountMap.size()>0) {
                    for (Map.Entry<String, List<AfterSaleOrderCouponInfoDto>> entry :orderCouponCountMap.entrySet()) {
                        List<AfterSaleOrderCouponInfoDto> val = entry.getValue();
                        String key = val.get(0).getActivityId()+":"+val.get(0).getGoodsId();
                        if (activityGoodsCouponCountMap.get(key)!=null) {
                            Map<Integer, Integer> map = activityGoodsCouponCountMap.get(key);
                            val.forEach(v->{
                                if (map.get(v.getCouponId())!=null && map.get(v.getCouponId())>v.getCouponCount()) {
                                    AfterSaleOrderCouponInfoDto dto = v;
                                    dto.setCouponCount(map.get(v.getCouponId())-v.getCouponCount());
                                    supplyAgainList.add(dto);
                                }
                                map.remove(v.getCouponId());
                            });
                            if (map!=null && map.size()>0) {
                                for (Map.Entry<Integer, Integer> couponEntry :map.entrySet()) {
                                    AfterSaleOrderCouponInfoDto dto = val.get(0);
                                    dto.setCouponId(couponEntry.getKey());
                                    dto.setCouponCount(couponEntry.getValue());
                                    supplyAgainList.add(dto);
                                    map.remove(couponEntry.getKey());
                                }
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(supplyAgainList)) {
                    List<Integer> couponIds = supplyAgainList.stream().map(AfterSaleOrderCouponInfoDto::getCouponId).collect(Collectors.toList());
                    //发放的卡券列表
                    CouponParamDto paramDto = new CouponParamDto();
                    paramDto.setSingleIds(couponIds);
                    PageBeanDto<SingleCouponDto> pageBeanDto = iCouponService.getCertificateBatchList(paramDto);
                    if (pageBeanDto!=null && CollectionUtils.isNotEmpty(pageBeanDto.getItems())) {
                        Map<Integer, SingleCouponDto> couponMap = new HashMap<>();
                        pageBeanDto.getItems().forEach(v->{
                            couponMap.put(v.getId(), v);
                        });
                        List<AfterSaleCouponRecord> couponRecordList = new ArrayList<>();
                        for (AfterSaleOrderCouponInfoDto orderCouponDto : supplyAgainList) {
                            if (orderCouponDto.getCouponCount() !=null && orderCouponDto.getCouponId()!=null) {
                                SingleCouponDto couponDto = couponMap.get(orderCouponDto.getCouponId());
                                if (couponDto!=null) {
                                    for (int i=0; i<orderCouponDto.getCouponCount(); i++) {
                                        CertificateBatchDto batchDto = iUserCertificateService.sendSingleCertificate(orderCouponDto.getUserId(), Long.valueOf(orderCouponDto.getUserPhone()), couponDto.getUuid(), 3,"1", null);
                                        if (batchDto!=null && batchDto.getUserCertId()!=null) {
                                            AfterSaleCouponRecord saleCouponRecord = new AfterSaleCouponDto();
                                            saleCouponRecord.setUserCouponId(batchDto.getUserCertId());
                                            saleCouponRecord.setCouponId(couponDto.getId());
                                            saleCouponRecord.setCouponCode(couponDto.getCode());
                                            saleCouponRecord.setActivityId(orderCouponDto.getActivityId());
                                            saleCouponRecord.setDealerId(orderCouponDto.getDealerId());
                                            saleCouponRecord.setCbId(orderCouponDto.getCbId());
                                            saleCouponRecord.setCsId(orderCouponDto.getCsId());
                                            saleCouponRecord.setOrderCode(orderCouponDto.getOrderCode());
                                            saleCouponRecord.setUserWxUnionId(orderCouponDto.getUserWxUnionId());
                                            saleCouponRecord.setCouponType(AfterSaleConstants.CouponType.DEDUCTION.getCode());
                                            saleCouponRecord.setCouponStatus(AfterSaleConstants.CouponStatus.USE_NON.getCode());
                                            couponRecordList.add(saleCouponRecord);
                                        } else {
                                            resultDto.setMsg("单张卡券发放失败");
                                            CommonLogUtil.addInfo("补发卡券", "单张卡券发放失败", JSON.toJSONString(orderCouponDto)+"--"+JSON.toJSONString(couponDto) +"--"+JSON.toJSONString(batchDto));
                                        }
                                    }
                                } else {
                                    resultDto.setMsg("单张卡券异常");
                                    CommonLogUtil.addInfo("补发卡券", "单张卡券异常：", JSON.toJSONString(couponIds));
                                }
                            } else {
                                resultDto.setMsg("卡券id|补发数量异常");
                                CommonLogUtil.addInfo("补发卡券", "卡券id|补发数量异常：", orderCouponDto.getCouponId()+"--"+orderCouponDto.getCouponCount());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(couponRecordList)) {
                            resultDto.setMsg("发券成功");
                            resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
                            afterSaleCouponRecordWriteMapper.addAfterSaleCouponList(couponRecordList);
                        } else {
                            resultDto.setMsg("卡券记录为空");
                            CommonLogUtil.addInfo("补发卡券", "卡券记录为空：", "");
                        }
                    } else {
                        resultDto.setMsg("查询卡券异常");
                        CommonLogUtil.addInfo("补发卡券", "查询卡券异常：", JSON.toJSONString(couponIds));
                    }
                } else {
                    resultDto.setMsg("无需要补发的卡券");
                    CommonLogUtil.addInfo("补发卡券", "无需要补发的卡券：", "");
                }
            } else {
                CommonLogUtil.addInfo("补发卡券", "无需要补发的订单：", "");
                resultDto.setMsg("订单无需补发卡券");
            }
        } catch (Exception e) {
            resultDto.setCode(StatusCodeEnum.ERROR.getCode());
            resultDto.setMsg(StatusCodeEnum.ERROR.getText());
            CommonLogUtil.addError("补发卡券", "error：", e);
        }
        return resultDto;
    }

    /**
      * @description : 订单卡券数量
      * @author : fxq
      * @param :
      * @return : couponGiveOutCount：已发数量， couponUseCount：核销数量， couponCount：应发数量
      * @date : 2021/12/20 18:34
      */
    @Override
    public Map<String, Integer> getOrderCouponCount(String orderCode, Integer activityId, Integer goodsId, Integer type) {
        CommonLogUtil.addInfo("getOrderCouponCount", "订单卡券数量：", orderCode+"--"+activityId+"--"+goodsId+"--"+type);
        Map<String, Integer> map = new HashMap<>();
        Map<String, Long> orderCouponMap = afterSaleCouponRecordReadMapper.getCouponCountByOrderCode(orderCode);
        Integer couponCountSum = orderCouponMap.get("couponCountSum").intValue();
        Integer couponGiveOutCount = orderCouponMap.get("couponGiveOutCount").intValue();
        Integer couponUseCount = orderCouponMap.get("couponUseCount").intValue();
        Integer couponCount = afterSaleActivityCouponServiceImpl.getCouponCount(activityId, goodsId, type);
        map.put("couponCountSum", couponCountSum);
        map.put("couponGiveOutCount", couponGiveOutCount);
        map.put("couponUseCount", couponUseCount);
        map.put("couponCount", couponCount);
        return map;
    }


}
