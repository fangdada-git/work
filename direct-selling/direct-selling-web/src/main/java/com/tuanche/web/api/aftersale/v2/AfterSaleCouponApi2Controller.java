package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.certificate.api.api.ICouponService;
import com.tuanche.certificate.api.api.IUserCertificateService;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleCouponService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleCouponDto;
import com.tuanche.directselling.dto.AfterSaleDealerCouponDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.AfterSaleConstants.CouponStatus;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.simpleInfo.CsCompanySimpleInfo;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.request.OrderCheckoutRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/v2/after/sale/coupon")
public class AfterSaleCouponApi2Controller {

    @Reference
    private AfterSaleCouponService afterSaleCouponService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private CarBaseService carBaseService;
    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private AfterSaleActivityService afterSaleActivityService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private ICouponService iCouponService;
    @Reference
    private IUserCertificateService iUserCertificateService;
    @Value("${sales_front_host}")
    private String sales_front_host;

    /**
     * @description : 卡券列表
     * @author : fxq
     * @param : type  1:经销商公众号只展示抵用券，套餐卡兑换券  2:团车公众号展示全部
     * @return :
     * @date : 2021/7/21 10:37
     */
    @RequestMapping("/list")
    public TcResponse list(AfterSaleCouponRecord saleCouponRecord, Integer type) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","list",  "售后特卖卡券列表 start ", JSON.toJSONString(saleCouponRecord)+"--"+type);
        List<AfterSaleDealerCouponDto> list = new ArrayList<>();
        try {
            if (StringUtil.isEmpty(saleCouponRecord.getUserWxUnionId()) || type==null || (type ==1 && saleCouponRecord.getDealerId()==null)) {
                return DirectCommonUtil.returnParamError("AfterSaleCouponController", "list", "卡券列表参数为空", "", st);
            }
            //经销商信息
            Map<Integer, CsCompanySimpleInfo> companyMap = new HashMap<>();
            //经销商下活动信息
            Map<Integer, AfterSaleActivity> activityMap = new HashMap<>();
            //活动关联品牌信息
            Map<Integer, CarBrandDto> brandMap = null;
            //商品信息
            Map<Integer, CommodityResponseDto> goodsMap = new HashMap<>();
            //经销商活动信息
            Map<Integer, AfterSaleDealerCouponDto> dealerActivityMap = new HashMap<>();

            List<Integer> dealerIds = new ArrayList<>();
            List<Integer> activityIds = new ArrayList<>();
            List<Integer> goodsIds = new ArrayList<>();
            List<Integer> cbIds = new ArrayList<>();
            //订单信息
            AfterSaleOrderRecordDto orderRecord = new AfterSaleOrderRecordDto();
            orderRecord.setUserWxUnionId(saleCouponRecord.getUserWxUnionId());
            orderRecord.setOrderStatusList(Arrays.asList(AfterSaleConstants.OrderStatus.PAID.getCode(), AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode()));
            orderRecord.setDealerId(saleCouponRecord.getDealerId());
            orderRecord.setActivityId(saleCouponRecord.getActivityId());
            //经销商公众号只查询套餐卡卡券
            if (type==1) orderRecord.setOrderType(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode());
            List<AfterSaleOrderRecord> saleOrderRecordList = afterSaleOrderRecordService.getAfterSaleOrderRecordList(orderRecord);
            if (CollectionUtils.isNotEmpty(saleOrderRecordList)){
                saleOrderRecordList.forEach(v->{
                    if (!dealerIds.contains(v.getDealerId())) dealerIds.add(v.getDealerId());
                    if (!activityIds.contains(v.getActivityId())) activityIds.add(v.getActivityId());
                    if (!goodsIds.contains(v.getGoodsId())) goodsIds.add(v.getGoodsId());
                    if (!cbIds.contains(v.getCbId()) && v.getCbId()!=null) cbIds.add(v.getCbId());
                });
            }
            //卡券信息
            if (type==1) saleCouponRecord.setCouponType(AfterSaleConstants.CouponType.DEDUCTION.getCode());
            saleCouponRecord.setCouponStatus(CouponStatus.USE_NON.getCode());
            List<AfterSaleCouponDto> couponList = afterSaleCouponService.getAfterSaleCouponList(saleCouponRecord);
            if (CollectionUtils.isNotEmpty(couponList)){
                couponList.forEach(v->{
                    if (!dealerIds.contains(v.getDealerId())) dealerIds.add(v.getDealerId());
                    if (!activityIds.contains(v.getActivityId())) activityIds.add(v.getActivityId());
                    if (!cbIds.contains(v.getCbId()) && v.getCbId()!=null) cbIds.add(v.getCbId());
                });
            }
            CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
            queryRequestDto.setCommodityIds(goodsIds);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            BaseResponseDto<PageableDto<CommodityResponseDto>> baseResponseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if (baseResponseDto!=null && baseResponseDto.getData()!=null && CollectionUtils.isNotEmpty(baseResponseDto.getData().getList())) {
                baseResponseDto.getData().getList().forEach(v->{
                    goodsMap.put(v.getCommodityId(), v);
                });
            }
            CsCompany company = new CsCompany();
            company.setDealerIdList(dealerIds);
            companyMap = companyBaseService.getDealerSimpleInfoMap(company);
            activityMap = afterSaleActivityService.getAfterSaleActivityMap(activityIds);
            brandMap = carBaseService.getBrandMap(cbIds);
            //整理返回数据
            List<AfterSaleCouponDto> couponDtos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(saleOrderRecordList)){
                for (AfterSaleOrderRecord v : saleOrderRecordList) {
                    AfterSaleCouponDto dto = new AfterSaleCouponDto();
                    dto.setActivityId(v.getActivityId());
                    dto.setDealerId(v.getDealerId());
                    dto.setCbId(v.getCbId());
                    dto.setUserWxUnionId(v.getUserWxUnionId());
                    dto.setAgentWxUnionId(v.getAgentWxUnionId());
                    if (goodsMap.get(v.getGoodsId())!=null) dto.setCouponName(goodsMap.get(v.getGoodsId()).getCommodityName());
                    if (activityMap.get(v.getActivityId())!=null) {
                        dto.setChangeStartDate(activityMap.get(v.getActivityId()).getOfflineConvertStartTime());
                        dto.setChangeEndDate(activityMap.get(v.getActivityId()).getOfflineConvertEndTime());
                    }
                    if  (brandMap.get(v.getCbId())!=null) {
                        dto.setCbLogo(brandMap.get(v.getCbId()).getCbLogo());
                    }
                    if (v.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode())
                            && v.getKeepOnRecordUser()!=null && v.getFinishFissionTask()!=null
                            && v.getKeepOnRecordUser() && !v.getFinishFissionTask()) {
                        dto.setCouponStatus(CouponStatus.INVALID.getCode());
                    } else {
                        dto.setCouponStatus(CouponStatus.USE_NON.getCode());
                    }
                    dto.setCouponType(AfterSaleConstants.CouponType.EXCHANGE.getCode());
                    dto.setLicencePlate(v.getLicencePlate());
                    dto.setShortName(companyMap.get(v.getDealerId()).getShortName());
                    dto.setOrderCode(v.getOrderCode());
                    dto.setType(1);
                    couponDtos.add(dto);
                }
            }
            if (CollectionUtils.isNotEmpty(couponList)){
                for (AfterSaleCouponDto v : couponList) {
                    if (v.getCouponType().equals(AfterSaleConstants.CouponType.GIFT.getCode())) {
                        if (activityMap.get(v.getActivityId())!=null) {
                            v.setChangeStartDate(activityMap.get(v.getActivityId()).getOfflineConvertStartTime());
                            v.setChangeEndDate(activityMap.get(v.getActivityId()).getOfflineConvertEndTime());
                        }
                    }
                    if  (brandMap.get(v.getCbId())!=null) {
                        v.setCbLogo(brandMap.get(v.getCbId()).getCbLogo());
                    }
                    v.setShortName(companyMap.get(v.getDealerId()).getShortName());
                    v.setType(2);
                    couponDtos.add(v);
                }
            }
            if (CollectionUtils.isEmpty(couponDtos)) {
                return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "list", "卡券列表无数据", saleCouponRecord, st);
            }
            for (AfterSaleCouponDto v : couponDtos) {
                if (dealerActivityMap.get(v.getDealerId())==null) {
                    AfterSaleDealerCouponDto dealerCouponDto = new AfterSaleDealerCouponDto();
                    dealerCouponDto.setDealerName(companyMap.get(v.getDealerId()).getShortName());
                    List<AfterSaleCouponDto> saleCouponDtoList = new ArrayList<>();
                    saleCouponDtoList.add(v);
                    dealerCouponDto.setCouponList(saleCouponDtoList);
                    dealerActivityMap.put(v.getDealerId(), dealerCouponDto);
                } else {
                    AfterSaleDealerCouponDto dealerCouponDto = dealerActivityMap.get(v.getDealerId());
                    dealerCouponDto.getCouponList().add(v);
                    dealerActivityMap.put(v.getDealerId(), dealerCouponDto);
                }
            }
            dealerActivityMap.values().forEach(v->{
                Collections.sort(v.getCouponList());
                list.add(v);
            });
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleCouponController", "售后特卖卡券列表 error", e, st,JSON.toJSONString(saleCouponRecord));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","list",  "售后特卖卡券列表 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
    }

    /**
     * @description : 卡券详情
     * @author : fxq
     * @param : type  1:订单类卡券详情  2：券码类卡券详情
     * @return :
     * @date : 2021/7/21 10:37
     */
    @RequestMapping("/details")
    public TcResponse details(Integer userCouponId, String orderCode, Integer type, String userWxUnionId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","details",  "卡券详情 start ", userCouponId+"--"+orderCode+"--"+type+"--"+userWxUnionId);
        AfterSaleCouponDto coupon = new AfterSaleCouponDto();
        try {
            if ((userCouponId==null && StringUtil.isEmpty(orderCode)) || type==null || StringUtil.isEmpty(userWxUnionId)) {
                return DirectCommonUtil.returnParamError("AfterSaleCouponController", "details", "卡券详情参数为空", userCouponId+"--"+orderCode+"--"+type, st);
            }
            if (type==1) {
                //是否为当前微信用户卡券
                AfterSaleOrderRecordDto orderRecord = new AfterSaleOrderRecordDto();
                orderRecord.setOrderCode(orderCode);
                orderRecord.setUserWxUnionId(userWxUnionId);
                List<AfterSaleOrderRecord> orderRecordList = afterSaleOrderRecordService.getAfterSaleOrderRecordList(orderRecord);
                if (CollectionUtils.isEmpty(orderRecordList)) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "details", "卡券详情无数据", userCouponId+"--"+orderCode+"--"+type, st);
                }
                BaseQueryRequestDto<String> requestDto = new BaseQueryRequestDto<>();
                requestDto.setQuery(orderCode);
                requestDto.setServiceTypeEnum(ServiceTypeEnum.COMMON);
                BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(requestDto, CommodityOrderResponseDto.class);
                if (responseDto==null || responseDto.getData()==null || StringUtil.isEmpty(responseDto.getData().getCheckoutCode())) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "details", "卡券详情无数据", userCouponId+"--"+orderCode+"--"+type, st);
                }
                CommodityOrderResponseDto order = responseDto.getData();
                //商品信息
                BaseResponseDto<CommodityDetailBusinessResponseDto> commodity = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(order.getCommodityId());
                coupon.setCouponName(commodity.getData().getCommodityName());
                coupon.setAgentWxUnionId(orderRecordList.get(0).getAgentWxUnionId());
                coupon.setCouponType(AfterSaleConstants.CouponType.EXCHANGE.getCode());
                coupon.setCouponStatus(CouponStatus.USE_NON.getCode());
                coupon.setActivityId(orderRecordList.get(0).getActivityId());
                coupon.setLicencePlate(orderRecordList.get(0).getLicencePlate());
                coupon.setDealerId(orderRecordList.get(0).getDealerId());
                coupon.setCheckoutCode(order.getCheckoutCode());
                coupon.setCbId(orderRecordList.get(0).getCbId());
                coupon.setCheckedUrl(sales_front_host+"/coupon/confim?type=1&checkoutCode="+order.getCheckoutCode());
            } else {
                coupon = afterSaleCouponService.getAfterSaleCouponByUserCouponId(userCouponId, userWxUnionId);
                if (coupon!=null) {
                    coupon.setCheckedUrl(sales_front_host+"/coupon/confim?type=2&userCouponId="+coupon.getUserCouponId());
                }
            }
            if (coupon==null) {
                return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "details", "卡券详情无数据", userCouponId+"--"+orderCode+"--"+type+"--"+userWxUnionId, st);
            }
            CsCompanyDetailDto company = companyBaseService.getCsCompanyDetail(coupon.getDealerId());
            if (company!=null) {
                coupon.setDealerAddress(company.getAddress());
                coupon.setDealerName(company.getCompanyName());
                coupon.setDealerTel(company.getTel());
                coupon.setShortName(company.getShortName());
            }
            if (coupon.getCbId()!=null) {
                CarBrandDto brand = carBaseService.getBrandById(coupon.getCbId());
                if (brand!=null) coupon.setCbLogo(brand.getCbLogo());
            }
            if (type==1 || coupon.getCouponType().equals(AfterSaleConstants.CouponType.GIFT.getCode())) {
                AfterSaleActivityDto saleActivitie = afterSaleActivityService.getAfterSaleActivityDtoById(coupon.getActivityId());
                coupon.setChangeStartDate(saleActivitie.getOfflineConvertStartTime());
                coupon.setChangeEndDate(saleActivitie.getOfflineConvertEndTime());
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleCouponController", "卡券详情 error", e, st,userCouponId+"--"+orderCode+"--"+type);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","details",  "卡券详情 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, coupon);
    }

    /**
     * @description : 卡券核销
     * @author : fxq
     * @param : type 1:订单类卡券核销  2：券码类卡券核销
     * @return :
     * @date : 2021/7/21 15:21
     */
    @RequestMapping("/checkedCoupon")
    public TcResponse checkedCoupon(Integer userCouponId,Integer checkedDealerId, Integer checkedSalesId, String checkoutCode, Integer type) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","checkedCoupon",  "卡券核销 start ", userCouponId.toString());
        try {
            if (type==null || (userCouponId==null && StringUtil.isEmpty(checkoutCode))) {
                return DirectCommonUtil.returnParamError("AfterSaleCouponController", "checkedCoupon", "卡券核销参数为空", userCouponId, st);
            }
            if (type==1) {
                BaseQueryRequestDto<String> queryRequestDto = new BaseQueryRequestDto<>();
                queryRequestDto.setQuery(checkoutCode);
                queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.COMMON);
                BaseResponseDto<CommodityOrderResponseDto> orderByCheckoutCode = iOrderService.getOrderByCheckoutCode(queryRequestDto, CommodityOrderResponseDto.class);
                if (orderByCheckoutCode==null || orderByCheckoutCode.getData()==null || StringUtil.isEmpty(orderByCheckoutCode.getData().getOrderCode())) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "checkedCoupon", "卡券订单异常", checkoutCode, st);
                }
                List<AfterSaleOrderRecord> saleOrderRecordByOrderCodes = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderByCheckoutCode.getData().getOrderCode()));
                if (CollectionUtils.isEmpty(saleOrderRecordByOrderCodes)) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "checkedCoupon", "卡券订单记录异常", checkoutCode, st);
                }
                AfterSaleOrderRecord orderRecord = saleOrderRecordByOrderCodes.get(0);
                if (orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) && orderRecord.getKeepOnRecordUser() && !orderRecord.getFinishFissionTask()) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "checkedCoupon", "未完成裂变暂时无法核销", checkoutCode, st);
                }
                OrderCheckoutRequestDto requestDto = new OrderCheckoutRequestDto();
                requestDto.setCheckoutCode(checkoutCode);
                requestDto.setDealerId(checkedDealerId);
                requestDto.setSellerId(checkedSalesId);
                BaseResponseDto<String> responseDto = iOrderService.checkoutOrder(requestDto);
                if (responseDto==null || !responseDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode()) || !responseDto.getSuccess()) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "checkedCoupon", "卡券核销失败", checkoutCode, st);
                }
                AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                record.setOrderCode(responseDto.getData());
                record.setOrderStatus(AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode());
                afterSaleOrderRecordService.updateByOrderCode(record);
                afterSaleCouponService.giveOutCoupon(orderRecord.getOrderCode(), AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION);
            } else {
                AfterSaleCouponDto coupon = afterSaleCouponService.getAfterSaleCouponByUserCouponId(userCouponId, null);
                if (coupon==null || coupon.getId()==null) {
                    return DirectCommonUtil.returnResultNull("AfterSaleCouponController", "checkedCoupon", "卡券核销无数据", userCouponId, st);
                }
                //卡券系统核销
                iUserCertificateService.useCertificate(Long.parseLong(coupon.getUserPhone()), userCouponId, null);
                //更改卡券记录状态
                Date date = new Date();
                AfterSaleCouponRecord couponRecord = new AfterSaleCouponDto();
                couponRecord.setUserCouponId(userCouponId);
                couponRecord.setCheckedDate(date);
                couponRecord.setCheckedSalesId(checkedSalesId);
                couponRecord.setCheckedDealerId(checkedDealerId);
                couponRecord.setCouponStatus(CouponStatus.USE.getCode());
                afterSaleCouponService.updateAfterSaleCouponByUserCouponId(couponRecord);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleCouponController", "卡券核销 error", e, st,userCouponId.toString());
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController","checkedCoupon",  "卡券核销 end", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, 0);
    }


    /**
     * 扫码领券
     *
     * @param request
     * @param orderCodes
     * @return
     */
    @GetMapping("/scanGetCoupon")
    @ResponseBody
    public TcResponse scanGetCoupon(HttpServletRequest request, @RequestParam("orderCodes") String orderCodes, @RequestParam(value = "newLicencePlate", required = false) String newLicencePlate) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController", "scanGetCoupon", "扫码领券", orderCodes);
        if (StringUtils.isBlank(orderCodes)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), StatusCodeEnum.PARAM_IS_INVALID.getText(), st, 0);
        }
        if (StringUtils.isNotBlank(newLicencePlate)) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController", "scanGetCoupon", "修改车牌", newLicencePlate);
            List<String> orderCodeList = new ArrayList<>();
            for (String code : orderCodes.split(",")) {
                orderCodeList.add(code);
            }
            int rows = afterSaleOrderRecordService.updateLicencePlateByOrderCode(orderCodeList, newLicencePlate);
            if (rows == 0) {
                StaticLogUtils.warn(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleCouponController", "scanGetCoupon", "修改车牌失败", newLicencePlate);
            }
        }
        ResultDto resultDto = afterSaleCouponService.scanGetCoupon(orderCodes, AfterSaleConstants.CouponType.DEDUCTION, CouponStatus.USE_NON);
        Map<String, String> qrCodeUrlMap = new HashMap<>(4);
        List<AfterSaleActivity> afterSaleActivities = afterSaleActivityService.getAfterSaleActivityByOrderCodes(Arrays.asList(orderCodes.split(",")));
        if (!afterSaleActivities.isEmpty()) {
            AfterSaleActivity afterSaleActivity = afterSaleActivities.get(0);
            qrCodeUrlMap.put("qrCodeUrl", afterSaleActivity.getPublicQrCode());
        }
        if (resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, qrCodeUrlMap);
        } else {
            return DirectCommonUtil.setTcResponse(resultDto.getCode(), resultDto.getMsg(), st, qrCodeUrlMap);
        }
    }

}
