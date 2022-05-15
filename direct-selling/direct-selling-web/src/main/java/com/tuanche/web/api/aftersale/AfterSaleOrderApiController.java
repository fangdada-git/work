package com.tuanche.web.api.aftersale;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.IPUtil;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.api.AfterSaleRewardRecordService;
import com.tuanche.directselling.api.WechatWorkService;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.enums.AfterSaleUserType;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.business.CommodityOrderExtendBusinessWithAddressRequestDto;
import com.tuanche.merchant.business.dto.request.order.business.CommodityOrderModifyBusinessRequestDto;
import com.tuanche.merchant.business.dto.request.order.car.CommodityOrderCarRelRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.business.dto.response.order.OrderCodeWithPaymentResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.*;
import com.tuanche.merchant.pojo.dto.enums.order.OrderFormalEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderSourceEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.merchant.pojo.dto.request.payment.InitiatePayRequestDto;
import com.tuanche.web.service.AfterSaleOrderApiService;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/after/sale/order")
public class AfterSaleOrderApiController {

    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private AfterSaleRewardRecordService afterSaleRewardRecordService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private AfterSaleActivityService afterSaleActivityService;
    @Autowired
    private CommonWebService commonWebService;
    @Autowired
    private PayServiceImpl payServiceImpl;
    @Autowired
    private AfterSaleOrderApiService afterSaleOrderApiService;
    @Reference
    private WechatWorkService wechatWorkService;

    /**
     * @description : 售后特卖购买推广卡
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/7/20 19:46
     */
    @RequestMapping("/submit")
    public TcResponse submit(HttpServletRequest request, AfterSaleOrderRecordDto afterSaleOrderRecord, String returnUrl) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "售后特卖购买商品 start ", JSON.toJSONString(afterSaleOrderRecord));
        Object result = null;
        if (afterSaleOrderRecord.getActivityId()==null || afterSaleOrderRecord.getGoodsId()==null
                || StringUtil.isEmpty(afterSaleOrderRecord.getUserWxUnionId()) || StringUtil.isEmpty(afterSaleOrderRecord.getUserWxOpenId())
                || !ManeBaseConsoleConstants.isCarLicensePlate(afterSaleOrderRecord.getLicencePlate()) || afterSaleOrderRecord.getChannel()==null
                || StringUtil.isEmpty(afterSaleOrderRecord.getUserName()) || afterSaleOrderRecord.getOrderType()==null
                || !ManeBaseConsoleConstants.isMobile(afterSaleOrderRecord.getUserPhone())
                || StringUtil.isEmpty(returnUrl) || afterSaleOrderRecord.getDealerId()==null) {
            return DirectCommonUtil.returnParamError("AfterSaleOrderController", "submit", "售后特卖购买商品参数错误", afterSaleOrderRecord, st);
        }
        String lockKey = "after:sale:goods:order:lock:" + afterSaleOrderRecord.getUserWxUnionId()+":"+afterSaleOrderRecord.getGoodsId();
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
                    "AfterSaleOrderApiController", "submit", "");
        }
        try {
            Integer userId = commonWebService.getUserIdByPhone(afterSaleOrderRecord.getUserPhone());
            if (userId==null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "用户验证失败", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
            }
            Date date = new Date();
            //查询商品信息
            BaseResponseDto<CommodityDetailBusinessResponseDto> commodityResult = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(afterSaleOrderRecord.getGoodsId());
            if (commodityResult==null || commodityResult.getData()==null || commodityResult.getData().getCommodityId()==null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品异常", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
            }
            CommodityDetailBusinessResponseDto commodity = commodityResult.getData();
            if (commodity.getSoldNumber()>=commodity.getCommodityCount()) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品已售罄", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
            }
            if (afterSaleOrderRecord.getOrderType().equals(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode())) {
                if (date.before(commodity.getUpShelfTime()) || date.after(commodity.getDownShelfTime())) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品未上架", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
                }
            }
            AfterSaleActivityDto activityDto = afterSaleActivityService.getAfterSaleActivityDtoById(afterSaleOrderRecord.getActivityId());
            if (activityDto==null || activityDto.getOnState()==0) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "活动未开启", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
            }
            if (afterSaleOrderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode())) {
                //是否在活动有效时间内
                if (activityDto.getSaleStartTime().after(date) || activityDto.getSaleEndTime().before(date)) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品不在售卖时间范围内", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
                }
                //验证是否已购买过推广卡
                TcResponse response = afterSaleOrderApiService.getUserBuyCheck(st, afterSaleOrderRecord);
                if (response.getResponseHeader().getStatus()!=StatusCodeEnum.SUCCESS.getCode()) {
                    return response;
                }
                //验证手机号、车牌号是否有待付款得订单
                AfterSaleOrderRecordDto phoneLicencePlateDto = new AfterSaleOrderRecordDto();
                BeanUtils.copyProperties(afterSaleOrderRecord, phoneLicencePlateDto);
                phoneLicencePlateDto.setOrderStatus(AfterSaleConstants.OrderStatus.UNPAID.getCode());
                TcResponse phoneLicencePlateFlag = afterSaleOrderApiService.getUserBuyCheck(st, phoneLicencePlateDto);
                if (phoneLicencePlateFlag.getResponseHeader().getStatus()!=StatusCodeEnum.SUCCESS.getCode()) {
                    return phoneLicencePlateFlag;
                }
                //查询推广卡是否存在待支付订单
                AfterSaleOrderRecord orderRecord = checkUnpaidOrder(afterSaleOrderRecord);
                if (orderRecord!=null) {
                    TcResponse tcResponse = unpaidSubmitOrder(orderRecord.getOrderCode(), afterSaleOrderRecord.getUserWxOpenId(), returnUrl);
                    if (tcResponse.getResponseHeader().getStatus()==StatusCodeEnum.SUCCESS.getCode()) {
                        //写用户裂变任务完成信息
                        afterSaleOrderRecord = setFissionTaskInfo(afterSaleOrderRecord, activityDto);
                        //订单信息变化，更新为最新信息？？？？？？？？？？？？？？？？？？？？
                        modifyOrderData (orderRecord, afterSaleOrderRecord);
                    }
                    return tcResponse;
                }

            }else if (afterSaleOrderRecord.getOrderType().equals(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode())) {
                //套餐卡在线下活动结束之后就不能购买了
                if (activityDto.getSaleStartTime().after(date) || activityDto.getOfflineConvertEndTime().before(date)) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品不在售卖时间范围内", st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
                }
            }
            //下单数据
            CommodityOrderExtendBusinessWithAddressRequestDto orderExtendRequestDto = setOrderExtendBusiness(afterSaleOrderRecord,commodity,userId,returnUrl);
            BaseResponseDto<OrderCodeWithPaymentResponseDto> submitOrder = iOrderService.submitOrder(orderExtendRequestDto);
            if (submitOrder==null || submitOrder.getData()==null || StringUtil.isEmpty(submitOrder.getData().getOrderCode())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), StringUtil.isEmpty(submitOrder.getMsg())?"下单失败":submitOrder.getMsg(), st, "AfterSaleOrderController", "submit", afterSaleOrderRecord);
            }
            afterSaleOrderRecord.setOrderCode(submitOrder.getData().getOrderCode());
            String pageUrl = request.getHeader("Referer");
            afterSaleOrderRecord.setPageUrl(pageUrl);
            afterSaleOrderRecord.setOrderMoney(commodity.getCommodityPrice());
            afterSaleOrderRecord.setIp(IPUtil.getRequestIp(request));
            afterSaleOrderRecord.setUserId(userId);
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "售后特卖购买商品 写订单记录 ", JSON.toJSONString(afterSaleOrderRecord));
            //写用户裂变任务完成信息
            afterSaleOrderRecord = setFissionTaskInfo(afterSaleOrderRecord, activityDto);

            afterSaleOrderRecordService.addAfterSaleOrderRecord(afterSaleOrderRecord);
            //电商渠道匹配企微客服
            if (afterSaleOrderRecord.getChannel()!=null && afterSaleOrderRecord.getChannel().equals(AfterSaleConstants.ChannelType.TYPE_2.getCode())) {
                wechatWorkService.setAfterSaleOrderWechatworkConcact(afterSaleOrderRecord);
            }
            result = submitOrder.getData();
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleOrderController", "售后特卖购买商品 error", e, st,JSON.toJSONString(afterSaleOrderRecord));
        }finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "售后特卖购买商品 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
    }

    private void modifyOrderData(AfterSaleOrderRecord orderRecord, AfterSaleOrderRecordDto afterSaleOrderRecord) {
        boolean editFlag= false;
        boolean busFlag= false;
        CommodityOrderModifyBusinessRequestDto modifyRequestDto = new CommodityOrderModifyBusinessRequestDto();
        if (!orderRecord.getChannel().equals(afterSaleOrderRecord.getChannel())) {
            modifyRequestDto.setOrderChannel(afterSaleOrderRecord.getChannel());
            editFlag = true;
        }
        if (!orderRecord.getUserId().equals(afterSaleOrderRecord.getUserId())) {
            modifyRequestDto.setUserId(afterSaleOrderRecord.getUserId());
            editFlag = true;
        }
        if (!orderRecord.getUserPhone().equals(afterSaleOrderRecord.getUserPhone())) {
            modifyRequestDto.setUserPhone(Long.valueOf(afterSaleOrderRecord.getUserPhone()));
            editFlag = true;
        }
        if (!orderRecord.getUserName().equals(afterSaleOrderRecord.getUserName())) {
            modifyRequestDto.setUserNick(afterSaleOrderRecord.getUserName());
            editFlag = true;
        }
        if (!orderRecord.getLicencePlate().equals(afterSaleOrderRecord.getLicencePlate())) {
            modifyRequestDto.setLicenseNumber(afterSaleOrderRecord.getLicencePlate());
            editFlag = true;
            busFlag = true;
        }
        if (!orderRecord.getDealerId().equals(afterSaleOrderRecord.getDealerId())) {
            modifyRequestDto.setDealerIds(afterSaleOrderRecord.getDealerId()+"");
            editFlag = true;
            busFlag = true;
        }
        if (!orderRecord.getCsId().equals(afterSaleOrderRecord.getCsId())) {
            List<CommodityOrderCarRelRequestDto> carList = getCarRelRequestDtoList(afterSaleOrderRecord);
            modifyRequestDto.setCarRelRequestDtoList(carList);
            editFlag = true;
            busFlag = true;
        }
        if (editFlag) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "售后特卖购买商品 推广卡二次下单有订单信息变化", JSON.toJSONString(orderRecord));
            modifyRequestDto.setOrderCode(orderRecord.getOrderCode());
            modifyRequestDto.setServiceTypeEnum(busFlag ? ServiceTypeEnum.BUSINESS: ServiceTypeEnum.COMMON);
            iOrderService.modifyOrder(modifyRequestDto);
        } else {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "售后特卖购买商品 推广卡二次下单无订单信息变化", "");
        }
    }

    private CommodityOrderExtendBusinessWithAddressRequestDto setOrderExtendBusiness(AfterSaleOrderRecordDto afterSaleOrderRecord, CommodityDetailBusinessResponseDto commodity, Integer userId, String returnUrl) {
        CommodityOrderExtendBusinessWithAddressRequestDto orderExtendRequestDto = new CommodityOrderExtendBusinessWithAddressRequestDto();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        orderExtendRequestDto.setCommodityId(afterSaleOrderRecord.getGoodsId());
        orderExtendRequestDto.setNum(1);
        orderExtendRequestDto.setOrderMoney(commodity.getCommodityPrice());
        orderExtendRequestDto.setUserNick(afterSaleOrderRecord.getUserName());
        orderExtendRequestDto.setUserPhone(Long.valueOf(afterSaleOrderRecord.getUserPhone()));
        orderExtendRequestDto.setDedupKey(uuid);
        orderExtendRequestDto.setOrderCityId(afterSaleOrderRecord.getCityId());
        orderExtendRequestDto.setActivityId(afterSaleOrderRecord.getActivityId());
        orderExtendRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
        orderExtendRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
        String orderTitle = StringUtil.isEmpty(commodity.getCommodityName())
                ? (afterSaleOrderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) ? "用户购买推广卡" : "用户购买套餐卡")
                : (commodity.getCommodityName().length() <= 12 ? commodity.getCommodityName() : commodity.getCommodityName().substring(0, 12) + "…");
        orderExtendRequestDto.setOrderTitle(orderTitle);
        orderExtendRequestDto.setTerminal(TerminalTypeEnum.WAP);
        orderExtendRequestDto.setOpenId(afterSaleOrderRecord.getUserWxOpenId());
        orderExtendRequestDto.setOrderChannel(afterSaleOrderRecord.getChannel());
        orderExtendRequestDto.setOrderValidDuration(10);
        orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.WECHAT);
        orderExtendRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
        orderExtendRequestDto.setOrderFormal(OrderFormalEnum.CHECKOUT);
        orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
        orderExtendRequestDto.setUserId(userId);
        orderExtendRequestDto.setLicenseNumber(afterSaleOrderRecord.getLicencePlate());
        orderExtendRequestDto.setReturnUrl(returnUrl);
        orderExtendRequestDto.setDealerId(afterSaleOrderRecord.getDealerId());
        if (afterSaleOrderRecord.getCbId()!=null || afterSaleOrderRecord.getCsId()!=null || afterSaleOrderRecord.getCarId()!=null) {
            List<CommodityOrderCarRelRequestDto> carList = getCarRelRequestDtoList(afterSaleOrderRecord);
            orderExtendRequestDto.setCarRelRequestDtoList(carList);
        }
        AfterSaleActivity activityById = afterSaleActivityService.getAfterSaleActivityById(afterSaleOrderRecord.getActivityId());
        if (activityById!=null && activityById.getOnSubAccount()!=null && !activityById.getOnSubAccount().equals(AfterSaleConstants.SubAccountType.NO_OPEN.getCode())) {
            orderExtendRequestDto.setProfitSharing(true);
        } else {
            orderExtendRequestDto.setProfitSharing(false);
        }
        return orderExtendRequestDto;
    }

    private List<CommodityOrderCarRelRequestDto> getCarRelRequestDtoList(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        List<CommodityOrderCarRelRequestDto> carList = new ArrayList<>();
        CommodityOrderCarRelRequestDto car  = new CommodityOrderCarRelRequestDto();
        car.setCbId(afterSaleOrderRecord.getCbId());
        car.setCsId(afterSaleOrderRecord.getCsId());
        car.setCarId(afterSaleOrderRecord.getCarId());
        carList.add(car);
        return carList;
    }

    private AfterSaleOrderRecord checkUnpaidOrder(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        AfterSaleOrderRecordDto dto = new AfterSaleOrderRecordDto();
        dto.setUserWxUnionId(afterSaleOrderRecord.getUserWxUnionId());
        dto.setGoodsId(afterSaleOrderRecord.getGoodsId());
        dto.setActivityId(afterSaleOrderRecord.getActivityId());
        dto.setOrderType(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode());
        dto.setOrderStatus(AfterSaleConstants.OrderStatus.UNPAID.getCode());
        AfterSaleOrderRecord record = afterSaleOrderRecordService.getAfterSaleOrderChangeInfo(dto);
        if (record!=null && !StringUtil.isEmpty(record.getOrderCode())) return record;
        return null;
    }

    private AfterSaleOrderRecordDto setFissionTaskInfo(AfterSaleOrderRecordDto afterSaleOrderRecord, AfterSaleActivityDto activityDto) {
        //获取用户类型  0：备案用户 1：流失用户 2：普通用户
        Integer userType = afterSaleRewardRecordService.getUserType(afterSaleOrderRecord);
        afterSaleOrderRecord.setUserType(userType);
        //购买用户是否是备案用户
        boolean isKeepOnRecord = userType.equals(AfterSaleUserType.KEEP_ON_USER.getType());
        //如果【是备案用户】 且 【是推广卡】且【备案用户需要裂变的人数>0】
        if (isKeepOnRecord && afterSaleOrderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) &&
                activityDto.getGoodsFissionCount()!=null && activityDto.getGoodsFissionCount()>0){
            afterSaleOrderRecord.setFinishFissionTask(false);
        }else{
            afterSaleOrderRecord.setFinishFissionTask(true);
        }
        afterSaleOrderRecord.setKeepOnRecordUser(isKeepOnRecord);
        return afterSaleOrderRecord;
    }

    @RequestMapping("/unpaidSubmitOrder")
    public TcResponse unpaidSubmitOrder(String orderCode, String userWxOpenId, String returnUrl) {
        long st = System.currentTimeMillis();
        Object result = null;
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "AfterSaleOrderController", "unpaidSubmitOrder", "售后特卖待支付订单支付 start ", orderCode);
        if (StringUtil.isEmpty(orderCode) || StringUtil.isEmpty(returnUrl)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        //加redis锁，防止重复提交
        String lockKey = "after:sale:goods:order:lock:" + orderCode;
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
                    "AfterSaleOrderController", "unpaidSubmitOrder", orderCode);
        }
        try {
            //查询订单信息
            BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
            baseQueryRequestDto.setQuery(orderCode);
            BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, CommodityOrderResponseDto.class);
            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单信息异常", st,
                        "AfterSaleOrderController", "unpaidSubmitOrder", orderCode);
            }
            CommodityOrderResponseDto order = responseDto.getData();
            if (!order.getTradeStatus().equals(OrderTradeStatusEnum.WAIT_BUYER_PAY) || !order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单状态异常", st,
                        "AfterSaleOrderController", "unpaidSubmitOrder", orderCode);
            }
            //查询商品信息
            CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
            queryRequestDto.setCommodityIds(Arrays.asList(new Integer[]{order.getCommodityId()}));
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            BaseResponseDto<PageableDto<CommodityResponseDto>> goodsDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if (!goodsDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "售后特卖待支付订单支付商品异常", st,
                        "AfterSaleOrderController", "unpaidSubmitOrder", order.getCommodityId());
            }
            InitiatePayRequestDto payRequestDto = new InitiatePayRequestDto();
            payRequestDto.setOrderCode(orderCode);
            payRequestDto.setUserId(order.getUserId());
            payRequestDto.setReturnUrl(returnUrl);
            payRequestDto.setOpenId(userWxOpenId);
            payRequestDto.setTerminalType(TerminalTypeEnum.WAP);
            BaseResponseDto<OrderCodeWithPaymentResponseDto> paymentResponseDto = iOrderService.pay(payRequestDto);
            if (!paymentResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), paymentResponseDto.getMsg(), st,
                        "AfterSaleOrderController", "unpaidSubmitOrder", orderCode);
            }
            result = paymentResponseDto.getData();
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleOrderController", "售后特卖待支付订单支付 error", e, st, orderCode);
        } finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "AfterSaleOrderController", "unpaidSubmitOrder", "售后特卖待支付订单支付 end " + (System.currentTimeMillis()-st), orderCode);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
    }

    /**
     * @description : 售后特卖已购买推广卡列表
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/7/20 19:47
     */
    @RequestMapping("/list")
    public TcResponse list(PageResult<AfterSaleOrderRecord> pageResult, AfterSaleOrderRecord afterSaleOrderRecord) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","list",  "售后特卖已购买推广卡列表 start ", JSON.toJSONString(afterSaleOrderRecord));
        PageResult<AfterSaleOrderRecord> pageList = new PageResult<>();
        try {
            afterSaleOrderRecord.setOrderStatusList(
                    Arrays.asList(
                        AfterSaleConstants.OrderStatus.PAID.getCode(),
                        AfterSaleConstants.OrderStatus.CHECKOUT.getCode(),
                        AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode(),
                        AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode(),
                        AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode(),
                        AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode()
                    )
            );
            afterSaleOrderRecord.setOrderType(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode());
            pageList = afterSaleOrderRecordService.getAfterSaleBuyListByPage(pageResult, afterSaleOrderRecord);
            if (pageList==null || CollectionUtils.isEmpty(pageList.getData())) {
                return DirectCommonUtil.returnResultNull("AfterSaleOrderController", "list", "售后特卖已购买推广卡列表无数据", afterSaleOrderRecord, st);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleOrderController", "售后特卖已购买推广卡列表 error", e, st,JSON.toJSONString(afterSaleOrderRecord));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","list",  "售后特卖已购买推广卡列表 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, pageList);
    }


}
