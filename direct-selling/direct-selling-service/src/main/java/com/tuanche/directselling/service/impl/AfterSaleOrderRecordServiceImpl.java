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
            //商品
            Map<Integer, CommodityResponseDto> goodsMap = getGoodsInfoMap(new ArrayList<>(goodsIds));
            //车型信息
            Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(new ArrayList<>(csIds));
            //经销商信息
            Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
            //活动信息
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
     * 根据商品ID列表获取商品信息
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
        CommonLogUtil.addInfo("addAfterSaleOrderRecord" , "售后特卖用户下单", JSON.toJSONString(afterSaleOrderRecord));
        /*String nickName = null;
        if (StringUtils.isEmpty(afterSaleOrderRecord.getUserName())) {
            nickName = "匿名";
        } else {
            //微信昵称特殊字符过滤
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
        CommonLogUtil.addInfo(null, "售后特卖更新支付成功订单编号:"+afterSaleOrderRecord!=null?afterSaleOrderRecord.getOrderCode():""+"，数据："+JSON.toJSONString(afterSaleOrderRecord), null);
        if (afterSaleOrderRecord == null || afterSaleOrderRecord.getId() == null) {
            return;
        }
        AfterSaleOrderRecord record = afterSaleOrderRecordReadMapper.selectByPrimaryKey(afterSaleOrderRecord.getId());
        if (record != null && record.getOrderStatus() < 3) {
            if(record.getUserType()==null){
                //获取用户类型  0：备案用户 1：流失用户 2：普通用户
                Integer userType = afterSaleRewardRecordServiceImpl.getUserType(record);
                record.setUserType(userType);
                //购买用户是否是备案用户
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
            //套餐卡设置是否分账
            if (AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode().equals(record.getOrderType())) {
                AfterSaleActivity activity = afterSaleActivityService.getAfterSaleActivityById(record.getActivityId());
                if(afterSaleOrderProfitSharingServiceImpl.isProfitSharing(activity)){
                    record.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_2.getCode());
                }
            }
            CommonLogUtil.addInfo(null, "支付成功-更新订单", record);
            afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(record);
            //推广卡才计算奖励
            if (AfterSaleConstants.OrderType.PROMOTION_CARD.getCode().equals(record.getOrderType())) {
                afterSaleRewardRecordServiceImpl.reward(record);

            }
        }
    }

    /**
     * 获取非备案的裂变总人数
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
     * 获取裂变的人数
     * @author HuangHao
     * @CreatTime 2021-10-21 16:45 
     * @param afterSaleOrderRecord 
     * @return int
     */
    public int getFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord) {
        return afterSaleOrderRecordWriteMapper.getFissionTotal(afterSaleOrderRecord);
    }

    /**
     * 获取非备案的裂变信息列表
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
     * 获取裂变人的裂变列表
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
     * 获取购买人的单个订单信息
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
            CommonLogUtil.addInfo("售后服务 本库查询订单：" + JSON.toJSONString(query), " 返参：", JSON.toJSONString(record));
            return record;
        }
        BaseQueryRequestDto<String> requestDto = new BaseQueryRequestDto<>();
        requestDto.setQuery(record.getOrderCode());
        BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(requestDto, CommodityOrderResponseDto.class);
        CommonLogUtil.addInfo("售后服务 从架构查询订单入参：" + JSON.toJSONString(requestDto), " 返参：", JSON.toJSONString(responseDto));
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
     * @description : 备案用户自动退款
     * @author : fxq
     * @date : 2021/7/29 20:07
     */
    @Override
    public void keepOnRecordUserRefund(Integer activityId, Integer dealerId) {
        executorService.execute(() -> {
            CommonLogUtil.addInfo("备案用户自动退款", "退款用户start", "");
            try {
                List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.keepOnRecordUserRefund(activityId, dealerId);
                if (CollectionUtils.isNotEmpty(list)) {
                    CommonLogUtil.addInfo("备案用户自动退款", "退款用户", JSON.toJSONString(list));
                    list.forEach(v -> {
                        //订单中心退款操作
                        BaseResponseDto<Boolean> responseDto = iOrderService.directRefund(v.getOrderCode(), v.getUserId());
                        CommonLogUtil.addInfo("备案用户自动退款", "订单中心返回信息", JSON.toJSONString(responseDto));
                        if (responseDto != null && responseDto.getSuccess()) {
                            v.setOrderStatus(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode());
                            afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(v);
                            //收回礼品券
                            afterSaleCouponServiceImpl.delCoupon(v.getOrderCode(), Arrays.asList(AfterSaleConstants.CouponType.GIFT.getCode()));
                        } else {
                            CommonLogUtil.addInfo("备案用户自动退款", "退款失败", JSON.toJSONString(v));
                        }
                    });
                }

            } catch (Exception e) {
                CommonLogUtil.addError("备案用户自动退款", "退款error", e);
            }
            CommonLogUtil.addInfo("备案用户自动退款", "退款用户end", "");
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
        CommonLogUtil.addInfo("售后特卖-发券", "修改车牌号", "orderCodes:" + StringUtils.join(orderCodes, ",") + "licencePlate:" + licencePlate);
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
     * 核销统计
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
        //售卖套餐卡数量
        int packageCardTotal = 0;
        //核销推广卡数量
        int promotionCardWriteOffTotal = 0;
        //核销套餐卡数量
        int packageCardWriteOffTotal = 0;
        //套餐核销总金额
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
     * 获取用户邀请人数-map
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
     * 获取用户身份 1：代理人 2：CC代理人 3：普通用户 4：备案用户
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
     * 活动数据统计
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
     * 活动渠道数据统计
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
     * 获取代理人已经核销了的礼品券
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
     * 获取分享人已经核销了的礼品券
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
     * 核销提醒
     * @author HuangHao
     * @CreatTime 2021-09-14 14:05
     * @param day 核销前day天开始提醒
     * @return void
     */
    public void writeOffReminder(Integer day){
        List<AfterSaleActivityDto> activityList = afterSaleActivityService.getOfflineOngoingActivityIdsMap(day);
        if(CollectionUtils.isEmpty(activityList)){
            CommonLogUtil.addInfo(null, AfterSaleActivityWriteOffReminderJob.LOG_KEY_WORD +"，无线下兑换中的活动");
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
            activity.setOfflineConvertEndTimeStr(DateUtil.formart(activity.getOfflineConvertEndTime(),"yyyy年MM月dd日 HH时mm分"));
            activityIds.add(activity.getId());
            activityMap.put(activity.getId(), activity);
        }
        CommonLogUtil.addInfo(null, AfterSaleActivityWriteOffReminderJob.LOG_KEY_WORD +"，线下兑换中的活动："+JSON.toJSONString(activityIds));
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getNotWrittenOffUserOpenIdList(activityIds);
        if(CollectionUtils.isNotEmpty(list)){
            for (AfterSaleOrderRecord orderRecord : list) {
                AfterSaleActivityDto activityDto = activityMap.get(orderRecord.getActivityId());
                String msg = "尊敬的车主，您购买的保养卡将在"+DateUtil.formart(activityDto.getOfflineConvertStartTime(),DateUtil.FORMART_YMD)+"日至"+DateUtil.formart(activityDto.getOfflineConvertEndTime(),DateUtil.FORMART_YMD)+"日开启线下兑换长期服务卡，" +
                            "兑换日期截止至"+activityDto.getOfflineConvertEndTimeStr()+"。请及时到店兑换，过期作废。您可关注“团车养车”公众号查看卡券";
                afterSaleRewardRecordServiceImpl.sendMessage(orderRecord.getUserPhone(),msg,null);
            }
        }
    }

    /**
     * 未完任务的备案用户提醒
     * @author HuangHao
     * @CreatTime 2021-09-14 14:05
     * @param day 核销前day天开始提醒
     * @return void
     */
    public void unfinishedTaskReminder(Integer day){
        Map<Integer, ResultMapDto> activityMap = afterSaleActivityService.getEndTimeBeforActivityMap(day);
        if(activityMap == null || activityMap.size() < 1){
            CommonLogUtil.addInfo(null, AfterSaleActivityUnfinishedTaskReminderJob.LOG_KEY_WORD +"，无明天结束的活动");
            return;
        }
        Map<Integer, AfterSaleActivity> afterSaleActivityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityMap.keySet()));
        CommonLogUtil.addInfo(null, AfterSaleActivityUnfinishedTaskReminderJob.LOG_KEY_WORD +"，线下兑换中的活动："+JSON.toJSONString(activityMap));
        List<AfterSaleOrderRecord> list = afterSaleOrderRecordReadMapper.getUnfinishedTaskUserList(activityMap.keySet());
        if(CollectionUtils.isNotEmpty(list)){
            for (AfterSaleOrderRecord orderRecord : list) {
                AfterSaleActivity activity = afterSaleActivityMap.get(orderRecord.getActivityId());
                ResultMapDto activityDto = activityMap.get(orderRecord.getActivityId().toString());
                String msg = "尊敬的车主，您购买的服务卡还未生效，于"+activityDto.getMapValue()+
                            "日前邀请"+activity.getGoodsFissionCount()+"位车主购买即可生效，还可获得现金"+(StringUtils.isEmpty(activity.getGiftCouponName())?"":"和收纳箱")+"奖励，请关注“团车养车”公众号“我的卡券”查看详情";
                afterSaleRewardRecordServiceImpl.sendMessage(orderRecord.getUserPhone(),msg,null);
            }
        }
    }
    /**
     * 获取订单的分享人和代理人信息列表
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
     * 获取经销订单列表（带分享人和代理人信息）-分页
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
     * 获取订单列表（带分享人和代理人信息）-分页
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
     * 处理经销商订单详细信息
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
            //车型信息
            Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(new ArrayList<>(csIds));
            //活动信息
            Map<Integer, AfterSaleActivity> activityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityIds));
            //商品信息
            Map<Integer, CommodityResponseDto> goodsMap = getGoodsInfoMap(new ArrayList<>(goodsIds));
            //核销人信息
            Map<Integer, CsUser> csUserMap = csUserService.getCsUserByIds(new ArrayList<>(saleIds));
            //经销商信息
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
        //代理人按普通计算
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
     * 用户红包列表
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
                redPacket.setFailReason("未生效");
                redPacket.setReasonToast("该用户未完成拼单");
            }else if (redPacket.getPayStatus() == AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()) {
                redPacket.setFailReason("未到账");
                if (StringUtils.isNotEmpty(redPacket.getErrCode())) {
                    if(redPacket.getErrCode().equals(AfterSaleConstants.ErrCode.V2_ACCOUNT_SIMPLE_BAN.getCode()) ||
                            redPacket.getErrCode().equals(AfterSaleConstants.ErrCode.NO_AUTH.getCode())){
                        redPacket.setReasonToast(AfterSaleConstants.ErrCode.getDesc(redPacket.getErrCode()));
                    }else{
                        redPacket.setReasonToast("系统支付账号升级中，升级后将为您补发奖励");
                    }
                }else{
                    redPacket.setReasonToast("系统支付账号升级中，升级后将为您补发奖励");
                }
            }
        });
        PageInfo<AfterSaleRedPacketDto> page = new PageInfo<>(list);
        //count返回的是分享人数，pageCount总共多少条，msg 总金额
        pageResult.setCount(afterSaleOrderRecordReadMapper.selectAfterShareCountByUser(activityVo));
        pageResult.setPageTotal(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg(getUserRedPacketTotalMoney(activityVo).toString());
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 个人红包总金额
     *
     * @return
     */
    private BigDecimal getUserRedPacketTotalMoney(AfterSaleActivityVo activityVo) {
        BigDecimal sumMoney = afterSaleOrderRecordReadMapper.selectAfterRedPacketsUserTotal(activityVo);
        return Objects.isNull(sumMoney) ? BigDecimal.ZERO : sumMoney;
    }

    /**
      * @description : 售后特卖手动核销
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
    @Override
    public String checkoutCouponByOrderCode (Integer checkoutType, String orderCode,Integer dealerId, Integer salesId, Integer type, Integer userCouponId) {
        try {
            if (dealerId==null || salesId==null
                    || checkoutType==null
                    || (checkoutType==1 && StringUtil.isEmpty(orderCode))
                    || (checkoutType==2 && userCouponId==null)) {
                return "参数错误";
            }
            Date date = new Date();
            if (checkoutType==1) {
                BaseQueryRequestDto<String> request = new BaseQueryRequestDto<>();
                request.setQuery(orderCode);
                BaseResponseDto<CommodityOrderResponseDto> response = iOrderService.getOrderByOrderCode(request, CommodityOrderResponseDto.class);
                if (response==null || response.getData()==null || StringUtil.isEmpty(response.getData().getOrderCode())) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，订单异常", orderCode);
                    return "订单异常";
                }
                CommodityOrderResponseDto order = response.getData();
                if (order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_FINISHED)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，该卡券已被核销", orderCode);
                    return "该卡券已被核销";
                }
                if (!(order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，订单不是支付成功状态不能核销", orderCode);
                    return "订单不是支付成功状态不能核销";
                }
                List<AfterSaleOrderRecord> saleOrderRecordByOrderCodes = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
                if (org.apache.commons.collections.CollectionUtils.isEmpty(saleOrderRecordByOrderCodes)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，订单记录异常", orderCode);
                    return "订单记录异常";
                }
                AfterSaleOrderRecord orderRecord = saleOrderRecordByOrderCodes.get(0);
                if (orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) && orderRecord.getKeepOnRecordUser() && !orderRecord.getFinishFissionTask()) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，未完成裂变暂时无法核销", orderCode);
                    return "未完成裂变暂时无法核销";
                }
                OrderCheckoutRequestDto requestDto = new OrderCheckoutRequestDto();
                requestDto.setCheckoutCode(order.getCheckoutCode());
                requestDto.setDealerId(dealerId);
                requestDto.setSellerId(salesId);
                BaseResponseDto<String> responseDto = iOrderService.checkoutOrder(requestDto);
                if (responseDto==null || !responseDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode()) || !responseDto.getSuccess()) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，卡券核销失败", orderCode);
                    return "卡券核销失败";
                }
                //更改订单记录状态
                AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                record.setOrderCode(responseDto.getData());
                record.setOrderStatus(orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) ? AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode() : AfterSaleConstants.OrderStatus.CHECKOUT.getCode());
                record.setCheckedDealerId(dealerId);
                record.setCheckedSalesId(salesId);
                record.setCheckedDate(date);
                afterSaleOrderRecordService.updateByOrderCode(record);
                //发放抵用券
                afterSaleCouponServiceImpl.giveOutCoupon(responseDto.getData(), AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION);
                if (type==null || type==0) {
                    afterSaleCouponServiceImpl.scanGetCoupon(orderCode, AfterSaleConstants.CouponType.DEDUCTION, AfterSaleConstants.CouponStatus.USE_NON);
                }
            } else if (checkoutType==2) {
                AfterSaleCouponDto coupon = afterSaleCouponServiceImpl.getAfterSaleCouponByUserCouponId(userCouponId, null);
                if (coupon==null || coupon.getId()==null) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，卡券核销无数据", orderCode);
                    return "卡券核销无数据";
                }
                if (coupon.getCouponStatus().equals(AfterSaleConstants.CouponStatus.USE.getCode())) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，卡券已核销", orderCode);
                    return "卡券已核销";
                }
                if (!coupon.getDealerId().equals(dealerId)) {
                    CommonLogUtil.addInfo("checkoutCouponByOrderCode", "售后特卖手动核销，非本店卡券无法核销", orderCode);
                    return "非本店卡券无法核销";
                }
                //卡券系统核销
                UseConditionDto useConditionDto = new UseConditionDto();
                useConditionDto.setMerchantId(dealerId);
                useConditionDto.setCityId(salesId);
                iUserCertificateService.useCertificate(Long.parseLong(coupon.getUserPhone()), userCouponId, useConditionDto);
                //更改卡券记录状态
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
                            //更改订单记录状态
                            AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                            record.setOrderCode(coupon.getOrderCode());
                            record.setOrderStatus(AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode());
                            afterSaleOrderRecordService.updateByOrderCode(record);
                        }
                    }
                }
            }
        }catch (Exception e) {
            CommonLogUtil.addError("售后特卖手动核销", "error", e);
            return "系统错误："+e.getMessage();
        }
        return "成功";
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
     * 获取未分账的订单
     * @author HuangHao
     * @CreatTime 2021-12-13 17:24
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getUndistributeAccountOrderList(AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        return afterSaleOrderRecordReadMapper.getUndistributeAccountOrderList(afterSaleOrderRecordDto);
    }
    /**
     * 批量更新
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
