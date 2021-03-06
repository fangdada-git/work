package com.tuanche.directselling.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.certificate.api.api.IUserCertificateService;
import com.tuanche.certificate.api.dto.input.UseConditionDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.enums.AfterSaleUserType;
import com.tuanche.directselling.job.AfterSaleActivityUnfinishedTaskReminderJob;
import com.tuanche.directselling.job.AfterSaleActivityWriteOffReminderJob;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleOrderRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleOrderRecordWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.*;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.manubasecenter.model.ManufacturerUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.request.OrderCheckoutRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.business.CommodityOrderModifyBusinessRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.storage.dto.carstyle.CarStyleDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service(retries = 0, timeout = 20000)
public class AfterSaleOrderRecordServiceImpl implements AfterSaleOrderRecordService {

    @Autowired
    private AfterSaleActivityService afterSaleActivityService;
    @Autowired
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Autowired
    private AfterSaleOrderRecordReadMapper afterSaleOrderRecordReadMapper;
    @Autowired
    private AfterSaleOrderRecordWriteMapper afterSaleOrderRecordWriteMapper;
    @Autowired
    private AfterSaleRewardRecordServiceImpl afterSaleRewardRecordServiceImpl;
    @Reference
    private IOrderService iOrderService;
    @Autowired
    private AfterSaleCouponServiceImpl afterSaleCouponServiceImpl;
    @Autowired
    private AfterSaleOrderProfitSharingServiceImpl afterSaleOrderProfitSharingServiceImpl;
    @Reference
    UserBaseService csUserService;
//    @Value("#{${after_sale_push_template_ids}}")
//    private Map<String, Map<String, String>> after_sale_push_template_map;
//    @Value("${after.sale.activity.index.myCoupon.url}")
//    private String index_myCoupon_url;
//    @Reference
//    WxTemplateMessageBaseService wxTemplateMessageBaseService;
//    @Value("${aftersale_app_id}")
//    private String aftersale_app_id;
    @Autowired
    @Qualifier("executorService")
    ExecutorService executorService;
    @Reference
    private CarBaseService carBaseService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    CompanyBaseService companyBaseService;
    @Autowired
    AfterSaleActivityReadMapper afterSaleActivityReadMapper;
    @Reference
    IUserCertificateService iUserCertificateService;

    @Override
    public AfterSaleOrderRecord getById(Integer id){
        if(id==null || id <1){
            return null;
        }
        return afterSaleOrderRecordReadMapper.selectByPrimaryKey(id);
    }
    @Override
    public List<AfterSaleOrderRecord> getAfterSaleOrderRecordList(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        return afterSaleOrderRecordReadMapper.getAfterSaleOrderRecordList(afterSaleOrderRecord);
    }

    @Override
    public AfterSaleOrderRecord getAfterSaleOrderChangeInfo(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        return afterSaleOrderRecordReadMapper.getAfterSaleOrderChangeInfo(afterSaleOrderRecord);
    }

    @Override
    public PageResult<AfterSaleOrderRecordDto> getAfterSaleOrderRecordListByPage(int page, int limit, AfterSaleOrderRecordDto afterSaleOrderRecord) {
        Page pageHelp = PageHelper.startPage(page, limit);
        List<AfterSaleOrderRecordDto> afterSaleOrderRecordDtoList = getAfterSaleOrderRecordDtoList(afterSaleOrderRecord);
        PageInfo<AfterSaleOrderRecordDto> pageInfo = new PageInfo<>(pageHelp.getResult());
        PageResult<AfterSaleOrderRecordDto> pageResult = new PageResult<>();
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(afterSaleOrderRecordDtoList);
        return pageResult;
    }

    @Override
    public List<AfterSaleOrderRecordDto> getAfterSaleOrderRecordDtoList(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        orderStatus(afterSaleOrderRecord);
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getAfterSaleOrderRecordListByPage(afterSaleOrderRecord);
        List<AfterSaleOrderRecordDto> afterSaleOrderRecordDtos = new ArrayList<>();
        if (!list.isEmpty()) {
            Set<Integer> goodsIds =new HashSet<>();
            Set<Integer> dealerIds = new HashSet<>();
            Set<Integer> activityIds = new HashSet<>();
            Set<Integer> csIds = new HashSet<>();
            list.forEach(v -> {
                goodsIds.add(v.getGoodsId());
                dealerIds.add(v.getDealerId());
                activityIds.add(v.getActivityId());
                if(v.getCsId() != null){
                    csIds.add(v.getCsId());
                }
            });
            //??????
            Map<Integer, CommodityResponseDto> goodsMap = getGoodsInfoMap(new ArrayList<>(goodsIds));
            //????????????
            Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(new ArrayList<>(csIds));
            //???????????????
            Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
            //????????????
            Map<Integer, AfterSaleActivity> activityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityIds));

            list.forEach(v -> {
                AfterSaleOrderRecordDto recordDto = new AfterSaleOrderRecordDto();
                BeanUtils.copyProperties(v, recordDto);
                recordDto.setActivityName(activityMap.get(v.getActivityId()).getActivityName());
                recordDto.setGoodsName(goodsMap.get(v.getGoodsId()) == null ? "" : goodsMap.get(v.getGoodsId()).getCommodityName());
                if(afterSaleOrderRecord.getDesensitizationPhone() != null && afterSaleOrderRecord.getDesensitizationPhone()){
                    recordDto.setUserPhone(v.getUserPhone().toString().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                }
                if (companyMap.get(v.getDealerId()) != null) {
                    recordDto.setDealerName(companyMap.get(v.getDealerId()).getCompanyName());
                }
                if (companyMap.get(v.getCheckedDealerId()) != null) {
                    recordDto.setCheckedDealerName(companyMap.get(v.getCheckedDealerId()).getCompanyName());
                }
                CarStyleDto carStyleDto = styleMap.get(v.getCsId());
                if(carStyleDto != null){
                    recordDto.setCsName(carStyleDto.getCsName());
                }
                afterSaleOrderRecordDtos.add(recordDto);
            });
        }
        return afterSaleOrderRecordDtos;
    }

    /**
     * ????????????ID????????????????????????
     * @author HuangHao
     * @CreatTime 2021-10-11 14:08
     * @param commodityIds
     * @return java.util.Map<java.lang.Integer,com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto>
     */
    Map<Integer, CommodityResponseDto> getGoodsInfoMap(List<Integer> commodityIds){
        Map<Integer, CommodityResponseDto> goodsMap = new HashMap<>();
        CommodityWithBusinessQueryRequestDto gooodsquery = new CommodityWithBusinessQueryRequestDto();
        gooodsquery.setCommodityIds(commodityIds);
        gooodsquery.setServiceTypeEnum(ServiceTypeEnum.COMMON);
        BaseResponseDto<PageableDto<CommodityResponseDto>> goodsResponseDto = iCommodityBusinessService.getCommodityList(gooodsquery);
        if (goodsResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && goodsResponseDto.getData() != null
                && org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsResponseDto.getData().getList())) {
            goodsResponseDto.getData().getList().forEach(v -> {
                goodsMap.put(v.getCommodityId(), v);
            });
        }
        return goodsMap;
    }

    @Override
    public PageResult<AfterSaleOrderRecord> getAfterSaleBuyListByPage(PageResult<AfterSaleOrderRecord> pageResult, AfterSaleOrderRecord afterSaleOrderRecord) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getAfterSaleBuyListByPage(afterSaleOrderRecord);
        PageInfo<AfterSaleOrderRecord> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    @Override
    public List<AfterSaleOrderRecord> getAfterSaleOrderRecordByOrderCodes(List<String> OrderCodes) {
        if (CollectionUtils.isEmpty(OrderCodes)) {
            return null;
        }
        return afterSaleOrderRecordReadMapper.getAfterSaleOrderRecordByOrderCodes(OrderCodes);
    }

    @Override
    public Integer addAfterSaleOrderRecord(AfterSaleOrderRecord afterSaleOrderRecord) {
        CommonLogUtil.addInfo("addAfterSaleOrderRecord" , "????????????????????????", JSON.toJSONString(afterSaleOrderRecord));
        /*String nickName = null;
        if (StringUtils.isEmpty(afterSaleOrderRecord.getUserName())) {
            nickName = "??????";
        } else {
            //??????????????????????????????
            nickName = EmojiFilter.emojiConverter(afterSaleOrderRecord.getUserName());
        }
        afterSaleOrderRecord.setUserName(nickName);*/
        String pageUrl = afterSaleOrderRecord.getPageUrl();
        if (!StringUtil.isEmpty(pageUrl) && pageUrl.length() > 256) {
            afterSaleOrderRecord.setPageUrl(pageUrl.substring(0, 255));
        }
        afterSaleOrderRecordWriteMapper.insertSelective(afterSaleOrderRecord);
        return afterSaleOrderRecord.getId();
    }

    @Override
    public Integer updateAfterSaleOrderRecord(AfterSaleOrderRecord afterSaleOrderRecord) {
        if (afterSaleOrderRecord == null || afterSaleOrderRecord.getId() == null) {
            return null;
        }
        Integer num = afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(afterSaleOrderRecord);
        return num;
    }

    @Override
    public Integer updateByOrderCode(AfterSaleOrderRecord record) {
        if (record == null || StringUtil.isEmpty(record.getOrderCode()) || record.getOrderStatus() == null) {
            return null;
        }
        Integer num = afterSaleOrderRecordWriteMapper.updateByOrderCode(record);
        return num;
    }

    @Override
    public void updateAfterSaleOrderPaySuccess(AfterSaleOrderRecord afterSaleOrderRecord) {
        CommonLogUtil.addInfo(null, "??????????????????????????????????????????:"+afterSaleOrderRecord!=null?afterSaleOrderRecord.getOrderCode():""+"????????????"+JSON.toJSONString(afterSaleOrderRecord), null);
        if (afterSaleOrderRecord == null || afterSaleOrderRecord.getId() == null) {
            return;
        }
        AfterSaleOrderRecord record = afterSaleOrderRecordReadMapper.selectByPrimaryKey(afterSaleOrderRecord.getId());
        if (record != null && record.getOrderStatus() < 3) {
            if(record.getUserType()==null){
                //??????????????????  0??????????????? 1??????????????? 2???????????????
                Integer userType = afterSaleRewardRecordServiceImpl.getUserType(record);
                record.setUserType(userType);
                //?????????????????????????????????
                boolean isKeepOnRecord = userType.equals(AfterSaleUserType.KEEP_ON_USER.getType());
                if (isKeepOnRecord){
                    record.setFinishFissionTask(false);
                }else{
                    record.setFinishFissionTask(true);
                }
                record.setKeepOnRecordUser(isKeepOnRecord);
            }
            record.setTcTransactionId(afterSaleOrderRecord.getTcTransactionId());
            record.setWxTransactionId(afterSaleOrderRecord.getWxTransactionId());
            record.setOrderStatus(AfterSaleConstants.OrderStatus.PAID.getCode());
            record.setPayTime(afterSaleOrderRecord.getPayTime()==null?new Date():afterSaleOrderRecord.getPayTime());
            //???????????????????????????
            if (AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode().equals(record.getOrderType())) {
                AfterSaleActivity activity = afterSaleActivityService.getAfterSaleActivityById(record.getActivityId());
                if(afterSaleOrderProfitSharingServiceImpl.isProfitSharing(activity)){
                    record.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_2.getCode());
                }
            }
            CommonLogUtil.addInfo(null, "????????????-????????????", record);
            afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(record);
            //????????????????????????
            if (AfterSaleConstants.OrderType.PROMOTION_CARD.getCode().equals(record.getOrderType())) {
                afterSaleRewardRecordServiceImpl.reward(record);

            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param afterSaleOrderRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-07-22 18:24
     */
    @Override
    public int getNonFilingFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord) {
        return afterSaleOrderRecordWriteMapper.getNonFilingFissionTotal(afterSaleOrderRecord);
    }
    /**
     * ?????????????????????
     * @author HuangHao
     * @CreatTime 2021-10-21 16:45 
     * @param afterSaleOrderRecord 
     * @return int
     */
    public int getFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord) {
        return afterSaleOrderRecordWriteMapper.getFissionTotal(afterSaleOrderRecord);
    }

    /**
     * ????????????????????????????????????
     *
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     * @author HuangHao
     * @CreatTime 2021-07-28 22:47
     */
    @Override
    public List<AfterSaleOrderRecord> getNonFilingFissionList(AfterSaleOrderRecord afterSaleOrderRecord) {
        return afterSaleOrderRecordWriteMapper.getNonFilingFissionList(afterSaleOrderRecord);
    }

    /**
     * ??????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-10-22 17:40
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    @Override
    public List<AfterSaleOrderRecord> getFissionList(AfterSaleOrderRecord afterSaleOrderRecord){
        return afterSaleOrderRecordWriteMapper.getFissionList(afterSaleOrderRecord);
    }
    /**
     * ????????????????????????????????????
     *
     * @param activityId
     * @param
     * @param orderType
     * @return com.tuanche.directselling.model.AfterSaleOrderRecord
     * @author HuangHao
     * @CreatTime 2021-07-25 10:58
     */
    @Override
    public AfterSaleOrderRecord getBuyerOrder(Integer activityId, String userWxUnionId, Integer orderType) {
        return afterSaleOrderRecordWriteMapper.getBuyerOrder(activityId, userWxUnionId, orderType);
    }

    @Override
    public AfterSaleOrderRecord getUserBuyFlag(AfterSaleOrderRecord query) {
        AfterSaleOrderRecord record = afterSaleOrderRecordWriteMapper.getOrderByUserUnionId(query);
        if (record == null) {
            return null;
        }
        if (record.getOrderStatus() != null && record.getOrderStatus() >= AfterSaleConstants.OrderStatus.PAID.getCode()) {
            CommonLogUtil.addInfo("???????????? ?????????????????????" + JSON.toJSONString(query), " ?????????", JSON.toJSONString(record));
            return record;
        }
        BaseQueryRequestDto<String> requestDto = new BaseQueryRequestDto<>();
        requestDto.setQuery(record.getOrderCode());
        BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(requestDto, CommodityOrderResponseDto.class);
        CommonLogUtil.addInfo("???????????? ??????????????????????????????" + JSON.toJSONString(requestDto), " ?????????", JSON.toJSONString(responseDto));
        if (responseDto != null && responseDto.getData() != null &&
                !OrderTradeStatusEnum.WAIT_BUYER_PAY.equals(responseDto.getData().getTradeStatus()) &&
                !OrderTradeStatusEnum.TRADE_CLOSED.equals(responseDto.getData().getTradeStatus())) {
            record.setOrderStatus(AfterSaleConstants.OrderStatus.PAID.getCode());
            return record;
        }
        return record;
    }

    /**
     * @param :
     * @return :
     * @description : ????????????????????????
     * @author : fxq
     * @date : 2021/7/29 20:07
     */
    @Override
    public void keepOnRecordUserRefund(Integer activityId, Integer dealerId) {
        executorService.execute(() -> {
            CommonLogUtil.addInfo("????????????????????????", "????????????start", "");
            try {
                List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.keepOnRecordUserRefund(activityId, dealerId);
                if (CollectionUtils.isNotEmpty(list)) {
                    CommonLogUtil.addInfo("????????????????????????", "????????????", JSON.toJSONString(list));
                    list.forEach(v -> {
                        //????????????????????????
                        BaseResponseDto<Boolean> responseDto = iOrderService.directRefund(v.getOrderCode(), v.getUserId());
                        CommonLogUtil.addInfo("????????????????????????", "????????????????????????", JSON.toJSONString(responseDto));
                        if (responseDto != null && responseDto.getSuccess()) {
                            v.setOrderStatus(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode());
                            afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(v);
                            //???????????????
                            afterSaleCouponServiceImpl.delCoupon(v.getOrderCode(), Arrays.asList(AfterSaleConstants.CouponType.GIFT.getCode()));
                        } else {
                            CommonLogUtil.addInfo("????????????????????????", "????????????", JSON.toJSONString(v));
                        }
                    });
                }

            } catch (Exception e) {
                CommonLogUtil.addError("????????????????????????", "??????error", e);
            }
            CommonLogUtil.addInfo("????????????????????????", "????????????end", "");
        });
    }

    @Override
    public int updateLicencePlateByOrderCode(List<String> orderCodes, String licencePlate) {
        if (orderCodes.isEmpty()) {
            return 0;
        }
        if (licencePlate == null) {
            return 0;
        }
        CommonLogUtil.addInfo("????????????-??????", "???????????????", "orderCodes:" + StringUtils.join(orderCodes, ",") + "licencePlate:" + licencePlate);
        CommodityOrderModifyBusinessRequestDto requestDto = null;
        for (String orderCode : orderCodes) {
            requestDto = new CommodityOrderModifyBusinessRequestDto();
            requestDto.setOrderCode(orderCode);
            requestDto.setLicenseNumber(licencePlate);
            requestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            requestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            iOrderService.modifyOrder(requestDto);
        }
        return afterSaleOrderRecordWriteMapper.updateLicencePlateByOrderCode(orderCodes, licencePlate);
    }

    @Override
    public int validatePlateByOrderCode(List<String> orderCodes, String licencePlate) {
        if (orderCodes.isEmpty()) {
            return 1;
        }
        if (licencePlate == null) {
            return 1;
        }
        return afterSaleOrderRecordWriteMapper.getPlateCountByOrderCode(orderCodes, licencePlate);
    }

    /**
     * ????????????
     *
     * @param orderRecord
     * @return java.util.List<com.tuanche.directselling.vo.AfterSaleOrderRecordWriteOffStatisticsVo>
     * @author HuangHao
     * @CreatTime 2021-08-09 11:09
     */
    @Override
    public List<AfterSaleOrderRecordWriteOffStatisticsDto> writeOffStatistics(AfterSaleOrderRecordDto orderRecord) {
        if (orderRecord == null || orderRecord.getActivityId() == null) {
            return null;
        }
        //?????????????????????
        int packageCardTotal = 0;
        //?????????????????????
        int promotionCardWriteOffTotal = 0;
        //?????????????????????
        int packageCardWriteOffTotal = 0;
        //?????????????????????
        BigDecimal packageCardAmount = BigDecimal.ZERO;
        int count = 0;
        List<AfterSaleOrderRecordWriteOffStatisticsDto> list = afterSaleOrderRecordReadMapper.writeOffStatistics(orderRecord);
        if (CollectionUtils.isNotEmpty(list)) {
            for (AfterSaleOrderRecordWriteOffStatisticsDto statisticsDto : list) {
                count++;
                packageCardTotal += statisticsDto.getPackageCardTotal();
                promotionCardWriteOffTotal += statisticsDto.getPromotionCardWriteOffTotal();
                packageCardWriteOffTotal += statisticsDto.getPackageCardWriteOffTotal();
                packageCardAmount = packageCardAmount.add(statisticsDto.getPackageCardAmount());
                if (statisticsDto.getSaleId() != null) {
                    ManufacturerUser csUser = csUserService.getManufacturerUserCache(statisticsDto.getSaleId());
                    if (csUser != null) {
                        statisticsDto.setName(csUser.getUname());
                        statisticsDto.setPhone(csUser.getUphone());
                    }
                }
            }
        }
        AfterSaleOrderRecordWriteOffStatisticsDto dto = new AfterSaleOrderRecordWriteOffStatisticsDto();
        dto.setName(String.valueOf(count));
        dto.setPackageCardTotal(packageCardTotal);
        dto.setPromotionCardWriteOffTotal(promotionCardWriteOffTotal);
        dto.setPackageCardWriteOffTotal(packageCardWriteOffTotal);
        dto.setPackageCardAmount(packageCardAmount);
        list.add(0, dto);
        return list;
    }

    @Override
    public List<AfterSaleOrderRecord> getUserBuyCheck(AfterSaleOrderRecord query) {
        return  afterSaleOrderRecordReadMapper.getUserBuyCheck(query);
//        if (CollectionUtils.isNotEmpty(recordList)) {
//            List<String> orderCodes = recordList.stream().map(AfterSaleOrderRecord::getOrderCode).collect(Collectors.toList());
//            PageableRequestDto<CommodityOrderExtendBusinessQueryRequestDto> orderRequest = new PageableRequestDto<>();
//            CommodityOrderExtendBusinessQueryRequestDto queryRequestDto = new CommodityOrderExtendBusinessQueryRequestDto();
//            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
//            queryRequestDto.setOrderCodes(orderCodes);
//            orderRequest.setQuery(queryRequestDto);
//            orderRequest.setServiceTypeEnum(ServiceTypeEnum.COMMON);
//            orderRequest.setPageIndex(1);
//            orderRequest.setPageSize(orderCodes.size());
//            BaseResponseDto<PageableDto<CommodityOrderResponseDto>> orderResponseDto = iOrderService.listCommodityOrder(orderRequest, CommodityOrderResponseDto.class);
//            if (orderResponseDto!=null && orderResponseDto.getData()!=null && CollectionUtils.isNotEmpty(orderResponseDto.getData().getList())) {
//                for (CommodityOrderResponseDto v : orderResponseDto.getData().getList()) {
//                    if (!v.getTradeStatus().equals(OrderTradeStatusEnum.WAIT_BUYER_PAY) && !v.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_CLOSED)
//                            && v.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND)) {
//                        return true;
//                    }
//                }
//            }
//        }
    }

    /**
     * ????????????????????????-map
     * @author HuangHao
     * @CreatTime 2021-08-18 15:07
     * @param list
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordShareStatDto>
     */
    @Override
    public Map<String,AfterSaleOrderRecordShareStatDto> getShareTotalMap(List<AfterSaleUserRewardStatistics> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<AfterSaleOrderRecordShareStatDto> shareStatList = afterSaleOrderRecordReadMapper.getShareTotal(list);
        Map<String,AfterSaleOrderRecordShareStatDto> resultMap = null;
        if(!CollectionUtils.isEmpty(shareStatList)){
            resultMap = new HashMap<>(shareStatList.size()*2);
            for (AfterSaleOrderRecordShareStatDto statDto : shareStatList) {
                String key = GlobalConstants.underlineStitching(statDto.getActivityId(),statDto.getShareWxUnionId());
                resultMap.put(key, statDto);
            }
        }
        return resultMap;
    }
    /**
     * ?????????????????? 1???????????? 2???CC????????? 3??????????????? 4???????????????
     * @author HuangHao
     * @CreatTime 2021-08-18 16:51
     * @param list
     * @return Map<String,AfterSaleOrderRecordUserTypeDto>
     */
    @Override
    public Map<String, ResultMapDto> getUserTypeMap(List<AfterSaleUserRewardStatistics> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleOrderRecordReadMapper.getUserType(list);
    }

    /**
     * ??????????????????
     * @author HuangHao
     * @CreatTime 2021-08-20 11:00
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    @Override
    public List<AfterSaleActivityStatistics> activityStatistics(List<Integer> activityIds){
        if (CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleOrderRecordReadMapper.activityStatistics(activityIds);
    }
    /**
     * ????????????????????????
     * @author HuangHao
     * @CreatTime 2021-9-13 11:33:27
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    @Override
    public List<AfterSaleActivityChannelStatistics> activityChannelStatistics(List<Integer> activityIds){
        if (CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleOrderRecordReadMapper.activityChannelStatistics(activityIds);
    }

    /**
     * ??????????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @Override
    public Map<String, ResultMapDto> getAgentWriteOffGgiftCertificatesMap(List<AfterSaleUserRewardStatistics> list){
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleOrderRecordReadMapper.getAgentWriteOffGgiftCertificatesMap(list);
    }
    /**
     * ??????????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-08-23 17:47
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @Override
    public Map<String,ResultMapDto> getShareWriteOffGgiftCertificatesMap(List<AfterSaleUserRewardStatistics> list){
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleOrderRecordReadMapper.getShareWriteOffGgiftCertificatesMap(list);
    }

    @Override
    public HashMap<String, Object> getOrderStatistics (AfterSaleOrderRecordDto recordDto) {
        orderStatus(recordDto);
        return afterSaleOrderRecordReadMapper.getOrderStatistics(recordDto);
    }


    /**
     * ????????????
     * @author HuangHao
     * @CreatTime 2021-09-14 14:05
     * @param day ?????????day???????????????
     * @return void
     */
    public void writeOffReminder(Integer day){
        List<AfterSaleActivityDto> activityList = afterSaleActivityService.getOfflineOngoingActivityIdsMap(day);
        if(CollectionUtils.isEmpty(activityList)){
            CommonLogUtil.addInfo(null, AfterSaleActivityWriteOffReminderJob.LOG_KEY_WORD +"??????????????????????????????");
            return;
        }
        int size = activityList.size()*2;
        Map<Integer,AfterSaleActivityDto> activityMap = new HashMap<>();
        List<Integer> activityIds = new ArrayList<>(size);
        for (AfterSaleActivityDto activity : activityList) {
            CsCompanyDetailDto detail = companyBaseService.getCsCompanyDetail(activity.getDealerId());
            if (!Objects.isNull(detail)) {
                activity.setDealerName(detail.getCompanyName());
                activity.setDealerAddress(detail.getAddress());
                activity.setDealerTel(detail.getTel());
                activity.setPosition(detail.getPosition());
            }
            activity.setOfflineConvertEndTimeStr(DateUtil.formart(activity.getOfflineConvertEndTime(),"yyyy???MM???dd??? HH???mm???"));
            activityIds.add(activity.getId());
            activityMap.put(activity.getId(), activity);
        }
        CommonLogUtil.addInfo(null, AfterSaleActivityWriteOffReminderJob.LOG_KEY_WORD +"??????????????????????????????"+JSON.toJSONString(activityIds));
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getNotWrittenOffUserOpenIdList(activityIds);
        if(CollectionUtils.isNotEmpty(list)){
            for (AfterSaleOrderRecord orderRecord : list) {
                AfterSaleActivityDto activityDto = activityMap.get(orderRecord.getActivityId());
                String msg = "?????????????????????????????????????????????"+DateUtil.formart(activityDto.getOfflineConvertStartTime(),DateUtil.FORMART_YMD)+"??????"+DateUtil.formart(activityDto.getOfflineConvertEndTime(),DateUtil.FORMART_YMD)+"???????????????????????????????????????" +
                            "?????????????????????"+activityDto.getOfflineConvertEndTimeStr()+"?????????????????????????????????????????????????????????????????????????????????????????????";
                afterSaleRewardRecordServiceImpl.sendMessage(orderRecord.getUserPhone(),msg,null);
            }
        }
    }

    /**
     * ?????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-09-14 14:05
     * @param day ?????????day???????????????
     * @return void
     */
    public void unfinishedTaskReminder(Integer day){
        Map<Integer, ResultMapDto> activityMap = afterSaleActivityService.getEndTimeBeforActivityMap(day);
        if(activityMap == null || activityMap.size() < 1){
            CommonLogUtil.addInfo(null, AfterSaleActivityUnfinishedTaskReminderJob.LOG_KEY_WORD +"???????????????????????????");
            return;
        }
        Map<Integer, AfterSaleActivity> afterSaleActivityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityMap.keySet()));
        CommonLogUtil.addInfo(null, AfterSaleActivityUnfinishedTaskReminderJob.LOG_KEY_WORD +"??????????????????????????????"+JSON.toJSONString(activityMap));
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getUnfinishedTaskUserList(activityMap.keySet());
        if(CollectionUtils.isNotEmpty(list)){
            for (AfterSaleOrderRecord orderRecord : list) {
                AfterSaleActivity activity = afterSaleActivityMap.get(orderRecord.getActivityId());
                ResultMapDto activityDto = activityMap.get(orderRecord.getActivityId().toString());
                String msg = "?????????????????????????????????????????????????????????"+activityDto.getMapValue()+
                            "????????????"+activity.getGoodsFissionCount()+"????????????????????????????????????????????????"+(StringUtils.isEmpty(activity.getGiftCouponName())?"":"????????????")+"???????????????????????????????????????????????????????????????????????????";
                afterSaleRewardRecordServiceImpl.sendMessage(orderRecord.getUserPhone(),msg,null);
            }
        }
    }
    /**
     * ????????????????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    public List<AfterSaleOrderRecordDto> getDealerOrderSharerAndAgentList(AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        if(afterSaleOrderRecordDto == null || afterSaleOrderRecordDto.getDealerId() == null){
            return null;
        }
        orderStatus(afterSaleOrderRecordDto);
        List<AfterSaleOrderRecordDto> list = afterSaleOrderRecordReadMapper.getOrderSharerAndAgentList(afterSaleOrderRecordDto);
        dealerOrderSharerAndAgentListHandler(afterSaleOrderRecordDto,list);
        return list;
    }
    public void orderStatus(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        if (afterSaleOrderRecord.getOrderStatus() == null) {
            afterSaleOrderRecord.setOrderStatusList(Arrays.asList(
                    AfterSaleConstants.OrderStatus.PAID.getCode(),
                    AfterSaleConstants.OrderStatus.CHECKOUT.getCode(),
                    AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode(),
                    AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode(),
                    AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode(),
                    AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode()
            ));
        } else {
            if (afterSaleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_FINISH.getCode())) {
                afterSaleOrderRecord.setOrderStatusList(Arrays.asList(
                        AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode(),
                        AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode()
                ));
                afterSaleOrderRecord.setOrderStatus(null);
            }else  if (afterSaleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.NOT_EFFECTIVE.getCode())) {
                afterSaleOrderRecord.setKeepOnRecordUser(true);
                afterSaleOrderRecord.setFinishFissionTask(false);
                afterSaleOrderRecord.setOrderStatus(AfterSaleConstants.OrderStatus.PAID.getCode());
            }
        }
    }
    /**
     * ????????????????????????????????????????????????????????????-??????
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    public PageResult<AfterSaleOrderRecordDto> getDealerOrderSharerAndAgentListByPage(PageResult<AfterSaleOrderRecordDto> pageResult, AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        if(afterSaleOrderRecordDto == null || afterSaleOrderRecordDto.getDealerId() == null){
            pageResult.setCount(0);
            return pageResult;
        }
        return getOrderSharerAndAgentListByPage(pageResult, afterSaleOrderRecordDto);
    }
    /**
     * ??????????????????????????????????????????????????????-??????
     * @author HuangHao
     * @CreatTime 2021-10-11 13:53
     * @param afterSaleOrderRecordDto
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleOrderRecordDto>
     */
    public PageResult<AfterSaleOrderRecordDto> getOrderSharerAndAgentListByPage(PageResult<AfterSaleOrderRecordDto> pageResult, AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        if(afterSaleOrderRecordDto == null){
            pageResult.setCount(0);
            return pageResult;
        }
        orderStatus(afterSaleOrderRecordDto);
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleOrderRecordDto> list = afterSaleOrderRecordReadMapper.getOrderSharerAndAgentList(afterSaleOrderRecordDto);
        PageInfo<AfterSaleOrderRecordDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        dealerOrderSharerAndAgentListHandler(afterSaleOrderRecordDto,list);
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ?????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-10-11 15:33
     * @param list
     * @return void
     */
    void dealerOrderSharerAndAgentListHandler(AfterSaleOrderRecordDto afterSaleOrderRecordDto,List<AfterSaleOrderRecordDto> list){
        if(!CollectionUtils.isEmpty(list)){
            Set<Integer> goodsIds =new HashSet<>();
            Set<Integer> activityIds =new HashSet<>();
            Set<Integer> csIds =new HashSet<>();
            Set<Integer> saleIds =new HashSet<>();
            Set<Integer> dealerIds = new HashSet<>();
            list.forEach(order ->{
                goodsIds.add(order.getGoodsId());
                activityIds.add(order.getActivityId());
                dealerIds.add(order.getDealerId());
                if(order.getCsId() != null){
                    csIds.add(order.getCsId());
                }
                if(order.getCheckedSalesId() != null){
                    saleIds.add(order.getCheckedSalesId());
                }
            });
            //????????????
            Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(new ArrayList<>(csIds));
            //????????????
            Map<Integer, AfterSaleActivity> activityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityIds));
            //????????????
            Map<Integer, CommodityResponseDto> goodsMap = getGoodsInfoMap(new ArrayList<>(goodsIds));
            //???????????????
            Map<Integer, CsUser> csUserMap = csUserService.getCsUserByIds(new ArrayList<>(saleIds));
            //???????????????
            Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
            Date now = new Date();
            list.forEach(order ->{
                AfterSaleActivity activity = null;
                if(activityMap != null && (activity=activityMap.get(order.getActivityId())) != null){
                    order.setActivityName(activity.getActivityName());
                    Integer lookPhoneBeforeDays = activity.getLookPhoneBeforeDays();
                    Date lookDay = DateUtil.getNextDate(now,lookPhoneBeforeDays==null?1:lookPhoneBeforeDays);
                    if(afterSaleOrderRecordDto.getDesensitizationPhone() == null || afterSaleOrderRecordDto.getDesensitizationPhone() ){
                        order.setUserPhone(GlobalConstants.desensitizationPhone(order.getUserPhone()));
                    }
                }
                CommodityResponseDto commodity = null;
                if(goodsMap != null && (commodity=goodsMap.get(order.getGoodsId())) != null){
                    order.setGoodsName(commodity.getCommodityName());
                }
                CarStyleDto carStyle = null;
                if(styleMap != null && (carStyle=styleMap.get(order.getCsId())) != null){
                    order.setCsName(carStyle.getCsName());
                }
                CsUser csUser = null;
                if(csUserMap != null && (csUser=csUserMap.get(order.getCheckedSalesId())) != null){
                    order.setCheckedSalesName(csUser.getUname());
                }
                CsCompany company = null;
                if(companyMap != null && (company=companyMap.get(order.getDealerId())) != null){
                    order.setDealerName(company.getCompanyName());
                }
            });
        }
    }

    @Override
    public PageResult<AfterSaleRedPacketListDto> getAfterSaleRedPackListByPage(PageResult<AfterSaleRedPacketListDto> pageResult, AfterSaleOrderRecord afterSaleOrderRecord) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleRedPacketListDto> list = afterSaleOrderRecordReadMapper.selectAfterRedPacketListByPage(afterSaleOrderRecord);
        AfterSaleActivity afterSaleActivity = afterSaleActivityReadMapper.selectByPrimaryKey(afterSaleOrderRecord.getActivityId());
        //????????????????????????
        if(CollectionUtils.isNotEmpty(list)) {
            list.forEach(redPacket -> {
                if (!Objects.isNull(redPacket.getAgentType()) && AfterSaleConstants.AgentType.TYPE_0.getCode().equals(redPacket.getAgentType())) {
                    redPacket.setGetInviteMoney(afterSaleActivity.getShareAwardBase().multiply(BigDecimal.valueOf(redPacket.getShareCount())).
                            add(afterSaleActivity.getShareAwardInviterCount() <= 0 ? BigDecimal.valueOf(0) : (afterSaleActivity.getShareAwardExtra().multiply(BigDecimal.valueOf(redPacket.getShareCount() / afterSaleActivity.getShareAwardInviterCount())))));
                }
            });
            Collections.sort(list, Comparator.comparing(AfterSaleRedPacketListDto::getGetInviteMoney).reversed());
        }
        PageInfo<AfterSaleRedPacketListDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ??????????????????
     * @param pageResult
     * @param activityVo
     * @return
     */
    @Override
    public PageResult<AfterSaleRedPacketDto> getSaleRedPacketsByUser(PageResult<AfterSaleRedPacketDto> pageResult, AfterSaleActivityVo activityVo) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleRedPacketDto> list = afterSaleOrderRecordReadMapper.selectAfterRedPacketsByUser(activityVo);
        list.forEach(redPacket -> {
            redPacket.setPayTimeMils(!Objects.isNull(redPacket.getPayTime()) ? redPacket.getPayTime().getTime() : 0);
            redPacket.setWxPayTimeMils(!Objects.isNull(redPacket.getWxPayTime()) ? redPacket.getWxPayTime().getTime() : 0);
            if (redPacket.getPayStatus() == AfterSaleConstants.RewardPayStatus.PayStatus4.getCode()) {
                redPacket.setFailReason("?????????");
                redPacket.setReasonToast("????????????????????????");
            }else if (redPacket.getPayStatus() == AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()) {
                redPacket.setFailReason("?????????");
                if (StringUtils.isNotEmpty(redPacket.getErrCode())) {
                    if(redPacket.getErrCode().equals(AfterSaleConstants.ErrCode.V2_ACCOUNT_SIMPLE_BAN.getCode()) ||
                            redPacket.getErrCode().equals(AfterSaleConstants.ErrCode.NO_AUTH.getCode())){
                        redPacket.setReasonToast(AfterSaleConstants.ErrCode.getDesc(redPacket.getErrCode()));
                    }else{
                        redPacket.setReasonToast("????????????????????????????????????????????????????????????");
                    }
                }else{
                    redPacket.setReasonToast("????????????????????????????????????????????????????????????");
                }
            }
        });
        PageInfo<AfterSaleRedPacketDto> page = new PageInfo<>(list);
        //count???????????????????????????pageCount??????????????????msg ?????????
        pageResult.setCount(afterSaleOrderRecordReadMapper.selectAfterShareCountByUser(activityVo));
        pageResult.setPageTotal(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg(getUserRedPacketTotalMoney(activityVo).toString());
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    private BigDecimal getUserRedPacketTotalMoney(AfterSaleActivityVo activityVo) {
        BigDecimal sumMoney = afterSaleOrderRecordReadMapper.selectAfterRedPacketsUserTotal(activityVo);
        return Objects.isNull(sumMoney) ? BigDecimal.ZERO : sumMoney;
    }

    /**
      * @description : ????????????????????????
      * @author : fxq
      * @param : checkoutType ???????????? 1:?????????????????????  2????????????????????????
      * @param : orderCode ????????????
      * @param : dealerId ?????????id
      * @param : salesId ??????id
      * @param : type ??????????????????????????????????????????????????? null,0????????????  ????????????
      * @param : userCouponId ????????????id
      * @return :
      * @date : 2021/11/15 15:25
      */
    @Override
    public String checkoutCouponByOrderCode (Integer checkoutType, String orderCode,Integer dealerId, Integer salesId, Integer type, Integer userCouponId) {
        try {
            if (dealerId==null || salesId==null
                    || checkoutType==null
                    || (checkoutType==1 && StringUtil.isEmpty(orderCode))
                    || (checkoutType==2 && userCouponId==null)) {
                return "????????????";
            }
            Date date = new Date();
            if (checkoutType==1) {
                BaseQueryRequestDto<String> request = new BaseQueryRequestDto<>();
                request.setQuery(orderCode);
                BaseResponseDto<CommodityOrderResponseDto> response = iOrderService.getOrderByOrderCode(request, CommodityOrderResponseDto.class);
                if (response==null || response.getData()==null || StringUtil.isEmpty(response.getData().getOrderCode())) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "???????????????????????????????????????", orderCode);
                    return "????????????";
                }
                CommodityOrderResponseDto order = response.getData();
                if (order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_FINISHED)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "????????????????????????????????????????????????", orderCode);
                    return "?????????????????????";
                }
                if (!(order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "?????????????????????????????????????????????????????????????????????", orderCode);
                    return "??????????????????????????????????????????";
                }
                List<AfterSaleOrderRecord> saleOrderRecordByOrderCodes = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
                if (org.apache.commons.collections.CollectionUtils.isEmpty(saleOrderRecordByOrderCodes)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "?????????????????????????????????????????????", orderCode);
                    return "??????????????????";
                }
                AfterSaleOrderRecord orderRecord = saleOrderRecordByOrderCodes.get(0);
                if (orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) && orderRecord.getKeepOnRecordUser() && !orderRecord.getFinishFissionTask()) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "????????????????????????????????????????????????????????????", orderCode);
                    return "?????????????????????????????????";
                }
                OrderCheckoutRequestDto requestDto = new OrderCheckoutRequestDto();
                requestDto.setCheckoutCode(order.getCheckoutCode());
                requestDto.setDealerId(dealerId);
                requestDto.setSellerId(salesId);
                BaseResponseDto<String> responseDto = iOrderService.checkoutOrder(requestDto);
                if (responseDto==null || !responseDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode()) || !responseDto.getSuccess()) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "?????????????????????????????????????????????", orderCode);
                    return "??????????????????";
                }
                //????????????????????????
                AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                record.setOrderCode(responseDto.getData());
                record.setOrderStatus(orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) ? AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode() : AfterSaleConstants.OrderStatus.CHECKOUT.getCode());
                record.setCheckedDealerId(dealerId);
                record.setCheckedSalesId(salesId);
                record.setCheckedDate(date);
                afterSaleOrderRecordService.updateByOrderCode(record);
                //???????????????
                afterSaleCouponServiceImpl.giveOutCoupon(responseDto.getData(), AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION);
                if (type==null || type==0) {
                    afterSaleCouponServiceImpl.scanGetCoupon(orderCode, AfterSaleConstants.CouponType.DEDUCTION, AfterSaleConstants.CouponStatus.USE_NON);
                }
            } else if (checkoutType==2) {
                AfterSaleCouponDto coupon = afterSaleCouponServiceImpl.getAfterSaleCouponByUserCouponId(userCouponId, null);
                if (coupon==null || coupon.getId()==null) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "????????????????????????????????????????????????", orderCode);
                    return "?????????????????????";
                }
                if (coupon.getCouponStatus().equals(AfterSaleConstants.CouponStatus.USE.getCode())) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "??????????????????????????????????????????", orderCode);
                    return "???????????????";
                }
                if (!coupon.getDealerId().equals(dealerId)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "??????????????????????????????????????????????????????", orderCode);
                    return "???????????????????????????";
                }
                //??????????????????
                UseConditionDto useConditionDto = new UseConditionDto();
                useConditionDto.setMerchantId(dealerId);
                useConditionDto.setCityId(salesId);
                iUserCertificateService.useCertificate(Long.parseLong(coupon.getUserPhone()), userCouponId, useConditionDto);
                //????????????????????????
                AfterSaleCouponRecord couponRecord = new AfterSaleCouponDto();
                couponRecord.setUserCouponId(userCouponId);
                couponRecord.setCheckedDate(date);
                couponRecord.setCheckedSalesId(salesId);
                couponRecord.setCheckedDealerId(dealerId);
                couponRecord.setCouponStatus(AfterSaleConstants.CouponStatus.USE.getCode());
                afterSaleCouponServiceImpl.updateAfterSaleCouponByUserCouponId(couponRecord);
                if (coupon.getCouponType().equals(AfterSaleConstants.CouponType.GIFT.getCode())) {
                    List<AfterSaleOrderRecord> saleOrderRecordByOrderCodes = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(coupon.getOrderCode()));
                    if (!org.springframework.util.CollectionUtils.isEmpty(saleOrderRecordByOrderCodes)) {
                        AfterSaleOrderRecord order = saleOrderRecordByOrderCodes.get(0);
                        if (order.getOrderStatus().equals(AfterSaleConstants.OrderStatus.PAID.getCode())) {
                            //????????????????????????
                            AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                            record.setOrderCode(coupon.getOrderCode());
                            record.setOrderStatus(AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode());
                            afterSaleOrderRecordService.updateByOrderCode(record);
                        }
                    }
                }
            }
        }catch (Exception e) {
            CommonLogUtil.addError("????????????????????????", "error", e);
            return "???????????????"+e.getMessage();
        }
        return "??????";
    }

    @Override
    public PageResult<AfterSaleOrderRecordTotalByCarDto> getAfterSaleOrderTotalByCar(PageResult<AfterSaleOrderRecordTotalByCarDto> pageResult, AfterSaleOrderRecordTotalByCarDto orderRecordTotalByCarDto) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleOrderRecordTotalByCarDto> list = afterSaleOrderRecordReadMapper.getAfterSaleOrderTotalToCarByPage(orderRecordTotalByCarDto);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> csIds = new ArrayList<>();
            list.forEach(v->{
                if (v.getCsId()!=null) csIds.add(v.getCsId());
            });
            Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(csIds);
            list.forEach(v->{
                if (styleMap.get(v.getCsId())!=null) {
                    v.setCbName(styleMap.get(v.getCsId()).getCbName());
                    v.setCsName(styleMap.get(v.getCsId()).getCsName());
                }
            });
        }
        PageInfo<AfterSaleOrderRecordTotalByCarDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode("0");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ????????????????????????
     * @author HuangHao
     * @CreatTime 2021-12-13 17:24
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getUndistributeAccountOrderList(AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        return afterSaleOrderRecordReadMapper.getUndistributeAccountOrderList(afterSaleOrderRecordDto);
    }
    /**
     * ????????????
     * @author HuangHao
     * @CreatTime 2021-12-20 17:27
     * @param list
     * @return int
     */
    @Override
    public int updateBatchByIds(List<AfterSaleOrderRecord> list){
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        return afterSaleOrderRecordWriteMapper.updateBatchByIds(list);
    }

}
