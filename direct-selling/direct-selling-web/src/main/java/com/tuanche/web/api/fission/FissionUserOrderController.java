package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.po.MemberPo;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manu.out.dto.CsCompanyCmsDto;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.dto.TcResponseCode;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/fission/manu/user/order")
public class FissionUserOrderController {

//    @Value("${fission_c_pay_product_code}")
//    private String fission_c_pay_product_code;
//    @Value("${order_tuanche_host}")
//    private String order_tuanche_host;
//    @Value("${fission_user_checkout_url}")
//    private String fission_user_checkout_url;
    //订单过期时间
//    @Value("${order_valid_duration}")
//    private String order_valid_duration;
    //裂变订单数据异常接收人
//    @Value("${fission_order_exception_receivers}")
//    private String fission_order_exception_receivers;
//    @Reference
//    private IOrderService iOrderService;
//    @Reference
//    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
//    @Autowired
//    private PayServiceImpl payServiceImpl;
//    @Reference
//    private FissionTradeRecordService fissionTradeRecordService;
//    @Reference
//    private CityBaseService cityBaseService;
//    @Reference
//    private CarBaseService carBaseService;
//    @Reference
//    private UserBaseService userBaseService;
//    @Reference
//    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private FissionSaleService fissionSaleService;
//    @Reference
//    private FissionUserTaskRecordService fissionUserTaskRecordService;
//    @Reference
//    private FissionGoodsHelperUserService fissionGoodsHelperUserService;
//    @Reference
//    private FissionNoticeProducerService fissionNoticeProducerService;
    @Reference
    private CompanyBaseService companyBaseService;

    /**
     * @param :
     * @return :
     * @description : 用户下单
     * @author : fxq
     * @date : 2020/10/15 17:02
     */
//    @RequestMapping("/submitOrder")
//    @ResponseBody
//    public TcResponse submitOrder(HttpServletRequest request, FissionGoodsOrderRecord fissionGoodsOrderRecord,
//                                  FissionGoodsOrderExtend fissionGoodsOrderExtend, String returnUrl, FissionUserTaskRecordVo taskRecordVo) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "submitOrder",
//                "用户下单start " + st, JSON.toJSONString(fissionGoodsOrderRecord) + "--" + JSON.toJSONString(fissionGoodsOrderExtend) + "--" + JSON.toJSONString(taskRecordVo));
//        ResultDto resultDto = new ResultDto();
//        if (fissionGoodsOrderRecord == null || fissionGoodsOrderRecord.getGoodsId() == null || fissionGoodsOrderRecord.getGoodsNum() == null
////                || fissionGoodsOrderRecord.getFissionId()==null
//                || fissionGoodsOrderRecord.getOrderFormal() == null || fissionGoodsOrderRecord.getPageSource() == null
//                || ((StringUtil.isEmpty(fissionGoodsOrderRecord.getUserWxOpenId()) || StringUtil.isEmpty(fissionGoodsOrderRecord.getUserWxUnionId()))
//                && fissionGoodsOrderRecord.getPaySource() != null && !fissionGoodsOrderRecord.getPaySource().equals(Globals.FISSION_GOODS_ORDER.pay_source_app))
//                || (fissionGoodsOrderRecord.getOrderFormal().equals(Globals.FISSION_GOODS_ORDER.goods_post) && StringUtil.isEmpty(fissionGoodsOrderExtend.getReceiverName())
//                && StringUtil.isEmpty(fissionGoodsOrderExtend.getAddress()))) {
//            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
//        }
//
//        submitOrderEmail(fissionGoodsOrderRecord, request);
//
//        MemberPo memberPo = DirectCommonUtil.getMemberPo(request);
//        fissionGoodsOrderRecord.setUserId(memberPo.getId());
//        fissionGoodsOrderRecord.setOptUserId(memberPo.getId());
//        if (StringUtil.isEmpty(fissionGoodsOrderRecord.getUserName()))
//            fissionGoodsOrderRecord.setUserName(memberPo.getName());
//        if (StringUtil.isEmpty(fissionGoodsOrderRecord.getUserPhone()))
//            fissionGoodsOrderRecord.setUserPhone(String.valueOf(memberPo.getPhone()));
//
//        //加redis锁，防止重复提交
//        String lockKey = "fission:user:goods:order:lock:" + fissionGoodsOrderRecord.getFissionId() + "_" + fissionGoodsOrderRecord.getGoodsId() + "_" + memberPo.getId() + "_" + fissionGoodsOrderRecord.getPageSource();
//        String requestId = UUID.randomUUID().toString();
//        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120000)) {
//            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
//                    "FissionUserOrderController", "submitOrder", JSON.toJSONString(fissionGoodsOrderRecord));
//        }
//        try {
//            //查询商品价格
//            CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
//            queryRequestDto.setCommodityIds(Arrays.asList(new Integer[]{fissionGoodsOrderRecord.getGoodsId()}));
////            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
//            BaseResponseDto<PageableDto<CommodityResponseDto>> responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
//            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) || responseDto.getData()==null
//                    || CollectionUtils.isEmpty(responseDto.getData().getList()) || !PutAwayTypeEnum.ONSALE.equals(responseDto.getData().getList().get(0).getPutAwayType())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "商品异常", st,
//                        "FissionUserOrderController", "submitOrder", fissionGoodsOrderRecord.getGoodsId());
//            }
//            CommodityResponseDto goods = responseDto.getData().getList().get(0);
//            if (goods.getCommodityCount() <= goods.getSoldNumber()) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "商品已售罄", st,
//                        "FissionUserOrderController", "submitOrder", fissionGoodsOrderRecord.getGoodsId());
//            }
////            fissionGoodsOrderRecord.setPeroidsId(goods.getPeriodsId());
//            fissionGoodsOrderRecord.setOrderMoney(goods.getCommodityPrice
//                    ().multiply(new BigDecimal(fissionGoodsOrderRecord.getGoodsNum())));
//            CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto = new CommodityOrderFissionActivityWithAddressRequestDto();
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//            orderExtendRequestDto.setCommodityId(fissionGoodsOrderRecord.getGoodsId());
//            orderExtendRequestDto.setUserId(memberPo.getId());
//            orderExtendRequestDto.setNum(fissionGoodsOrderRecord.getGoodsNum());
//            orderExtendRequestDto.setOrderMoney(fissionGoodsOrderRecord.getOrderMoney());
//            orderExtendRequestDto.setUserNick(fissionGoodsOrderRecord.getUserName());
//            orderExtendRequestDto.setUserPhone(Long.valueOf(fissionGoodsOrderRecord.getUserPhone()));
//            orderExtendRequestDto.setOrderFormal(fissionGoodsOrderRecord.getOrderFormal().equals(Globals.FISSION_GOODS_ORDER.goods_post) ? OrderFormalEnum.POST : OrderFormalEnum.CHECKOUT);
//            orderExtendRequestDto.setDedupKey(uuid);
//            orderExtendRequestDto.setOrderCityId(fissionGoodsOrderRecord.getOrderCityId());
//            orderExtendRequestDto = setOrderSource(fissionGoodsOrderRecord.getPageSource(), orderExtendRequestDto);
//            orderExtendRequestDto.setFissionActivityId(fissionGoodsOrderRecord.getFissionId());
////            orderExtendRequestDto.setBusinessType(fissionGoodsOrderRecord.getFissionId() != null ? BusinessTypeEnum.FISSION : BusinessTypeEnum.COMMON);
//            orderExtendRequestDto.setBusinessType(BusinessTypeEnum.FISSION);
//            orderExtendRequestDto.setLiveId(fissionGoodsOrderRecord.getLiveId());
//            orderExtendRequestDto.setReceiverName(fissionGoodsOrderExtend.getReceiverName());
//            orderExtendRequestDto = setCityInfo(orderExtendRequestDto, fissionGoodsOrderExtend);
//            orderExtendRequestDto.setAddress(fissionGoodsOrderExtend.getAddress());
//            orderExtendRequestDto.setMemo(fissionGoodsOrderExtend.getMemo());
//            orderExtendRequestDto.setReceiverPhone(StringUtil.isEmpty(fissionGoodsOrderExtend.getReceiverPhone()) ? null : Long.valueOf(fissionGoodsOrderExtend.getReceiverPhone()));
//            orderExtendRequestDto.setReturnUrl(returnUrl);
//            orderExtendRequestDto.setOrderTitle(StringUtil.isEmpty(goods.getCommodityName()) ? "用户购买裂变商品" : (goods.getCommodityName().length() <= 12 ? goods.getCommodityName() : goods.getCommodityName().substring(0, 12) + "…"));
//            orderExtendRequestDto = setTerminal(orderExtendRequestDto, fissionGoodsOrderRecord.getPaySource(), request);
//            orderExtendRequestDto.setOpenId(fissionGoodsOrderRecord.getUserWxOpenId());
//            orderExtendRequestDto = setCarInfo (orderExtendRequestDto, fissionGoodsOrderRecord);
//            orderExtendRequestDto.setOrderChannel(fissionGoodsOrderRecord.getChannel());
//            orderExtendRequestDto.setOrderValidDuration(Integer.valueOf(order_valid_duration));
//            orderExtendRequestDto = setTerminalChannel(orderExtendRequestDto, fissionGoodsOrderRecord);
//            StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "submitOrder", "下单，调起商品订单库start", JSON.toJSONString(orderExtendRequestDto));
//            //下单，调起商品订单库
//            BaseResponseDto<OrderCodeWithPaymentResponseDto> requestDto = iOrderService.submitOrder(orderExtendRequestDto);
//            //下单，调起商品订单库
//            if (!requestDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), requestDto.getMsg(), st,
//                        "FissionUserOrderController", "submitOrder-用户下单失败", fissionGoodsOrderRecord);
//            }
//            resultDto.setCode(requestDto.getCode());
//            resultDto.setMsg(requestDto.getMsg());
//            resultDto.setResult(requestDto.getData());
//            String orderNo = requestDto.getData().getOrderCode();
//            BigDecimal orderAmount = requestDto.getData().getOrderAmount();
//            //下单成功 增加支商品订单记录
//            addFissionGoodsOrderRecord(fissionGoodsOrderRecord, orderNo, orderAmount);
//            //写用户任务放到redis，支付成功写
//            if (fissionGoodsOrderRecord.getFissionId() != null) {
//                String ip = IPUtil.getRequestIp(request);
//                taskRecordVo.setIp(ip);
//                taskRecordVo.setFissionId(fissionGoodsOrderRecord.getFissionId());
//                payServiceImpl.setFissionUserTaskRecord(taskRecordVo, orderNo);
//            }
//        } catch (Exception e) {
//            return DirectCommonUtil.addErrorLog("FissionUserOrderController", "用户下单 error", e, st, JSON.toJSONString(fissionGoodsOrderRecord));
//        } finally {
//            payServiceImpl.releaseDistributedLock(lockKey, requestId);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "submitOrder", "用户下单end " + st + "--" + (System.currentTimeMillis() - st), JSON.toJSONString(resultDto.getResult()));
//        return DirectCommonUtil.setTcResponse(resultDto.getCode(), resultDto.getMsg(), st, resultDto.getResult());
//    }

//    private CommodityOrderFissionActivityWithAddressRequestDto setOrderSource (Integer pageSource, CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto) {
//        if (pageSource!=null) {
//            switch (pageSource) {
//                case Globals.FISSION_GOODS_ORDER.source_fission:
//                    orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
//                    break;
//                case Globals.FISSION_GOODS_ORDER.source_live:
//                    orderExtendRequestDto.setOrderSource(OrderSourceEnum.TUANCHE_LIVE.getSourceId());
//                    break;
//                case Globals.FISSION_GOODS_ORDER.source_nature:
//                    orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
//                    break;
//                case Globals.FISSION_GOODS_ORDER.source_app:
//                    orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
//                    break;
//                default:
//                    orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
//                    break;
//            }
//        }
//        return orderExtendRequestDto;
//    }
//    private CommodityOrderFissionActivityWithAddressRequestDto setCityInfo (CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto, FissionGoodsOrderExtend fissionGoodsOrderExtend) {
//        if (fissionGoodsOrderExtend.getCityId() != null) {
//            DistrictOutputBaseDto city = cityBaseService.getCity(fissionGoodsOrderExtend.getCityId());
//            orderExtendRequestDto.setCityId(fissionGoodsOrderExtend.getCityId());
//            orderExtendRequestDto.setCityName(city.getName());
//            orderExtendRequestDto.setProvinceId(city.getPid());
//            orderExtendRequestDto.setProvinceName(city.getPname());
//        }
//        if (fissionGoodsOrderExtend.getAreaId() != null) {
//            DistrictOutputBaseDto city = cityBaseService.getCity(fissionGoodsOrderExtend.getAreaId());
//            orderExtendRequestDto.setAreaId(fissionGoodsOrderExtend.getAreaId());
//            orderExtendRequestDto.setAreaName(city.getName());
//        }
//        return orderExtendRequestDto;
//    }
//    private CommodityOrderFissionActivityWithAddressRequestDto setTerminal (CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto,
//                                                                            Integer paySource, HttpServletRequest request) {
//        if (paySource != null) {
//            switch (paySource) {
//                case Globals.FISSION_GOODS_ORDER.pay_source_app:
//                    orderExtendRequestDto.setTerminal(TerminalTypeEnum.APP);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.pay_source_wx_mini_program:
//                    orderExtendRequestDto.setTerminal(TerminalTypeEnum.WECHAT_APPLET);
//                    break;
//                default:
//                    orderExtendRequestDto.setTerminal(Globals.check(request) == 1 ? TerminalTypeEnum.WAP : TerminalTypeEnum.PC) ;
//            }
//        } else {
//            orderExtendRequestDto.setTerminal(Globals.check(request) == 1 ? TerminalTypeEnum.WAP : TerminalTypeEnum.PC);
//        }
//        return orderExtendRequestDto;
//    }
//    private CommodityOrderFissionActivityWithAddressRequestDto setCarInfo (CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto,FissionGoodsOrderRecord fissionGoodsOrderRecord) {
//        if (fissionGoodsOrderRecord.getBrandId() != null || fissionGoodsOrderRecord.getStyleId() != null) {
//            List<CommodityOrderCarRelRequestDto> orderCarRelRequestDtos = new ArrayList<>();
//            CommodityOrderCarRelRequestDto orderCar = new CommodityOrderCarRelRequestDto();
//            if (fissionGoodsOrderRecord.getStyleId() != null) {
//                CarStyleDto style = carBaseService.getStyleById(fissionGoodsOrderRecord.getStyleId());
//                if (style != null) {
//                    orderCar.setCsId(fissionGoodsOrderRecord.getStyleId());
//                    orderCar.setCmbId(style.getCmId());
//                    if (fissionGoodsOrderRecord.getBrandId() == null) orderCar.setCbId(style.getCbId());
//                    else orderCar.setCbId(fissionGoodsOrderRecord.getBrandId());
//                }
//            } else if (fissionGoodsOrderRecord.getBrandId() != null) {
//                CarBrandDto brand = carBaseService.getBrandById(fissionGoodsOrderRecord.getBrandId());
//                if (brand != null) {
//                    orderCar.setCmbId(brand.getCmId());
//                    orderCar.setCbId(brand.getCbId());
//                }
//            }
//            orderExtendRequestDto.setCarRelRequestDtoList(orderCarRelRequestDtos);
//        }
//        return orderExtendRequestDto;
//    }
//    private CommodityOrderFissionActivityWithAddressRequestDto setTerminalChannel (CommodityOrderFissionActivityWithAddressRequestDto orderExtendRequestDto,FissionGoodsOrderRecord fissionGoodsOrderRecord) {
//        if (fissionGoodsOrderRecord.getSid()!=null) {
//            switch (fissionGoodsOrderRecord.getSid()) {
//                case Globals.FISSION_GOODS_ORDER.SID_PC :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.PC);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_MOBILE :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.MOBILE);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_WECHAT :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.WECHAT);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_CALL_CENTER :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.CALL_CENTER);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_THIRD_API :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.THIRD_API);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_MINI_PROGRAM :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.MINI_PROGRAM);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_ACTIVITY :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.TC_ACTIVITY);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_ANDROID :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.ANDROID);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_IOS :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.IOS);
//                    break;
//                case Globals.FISSION_GOODS_ORDER.SID_ALIPAY_PROGRAM :
//                    orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.ALIPAY_PROGRAM);
//                    break;
//            }
//        }
//        return orderExtendRequestDto;
//    }

//    private void submitOrderEmail(FissionGoodsOrderRecord fissionGoodsOrderRecord,HttpServletRequest request) {
//        if (fissionGoodsOrderRecord==null || fissionGoodsOrderRecord.getFissionId()==null || fissionGoodsOrderRecord.getChannel()==null
//            || (fissionGoodsOrderRecord.getDealerId()==null && StringUtil.isEmpty(fissionGoodsOrderRecord.getSaleWxUnionId()))) {
//            String content = JSON.toJSONString(fissionGoodsOrderRecord)+"---"+request.getHeader("referer");
//            StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController","submitOrderEmail", content);
//            EmailUtil.send("裂变活动报名数据异常", Arrays.asList(fission_order_exception_receivers.split(",")), null, content);
//        }
//    }

    /**
     * @param :
     * @return :
     * @description : 待支付订单支付
     * @author : fxq
     * @date : 2020/11/28 21:44
     */
//    @RequestMapping("/unpaidSubmitOrder")
//    @ResponseBody
//    public TcResponse unpaidSubmitOrder(HttpServletRequest request, String orderNo, String userWxOpenId, String returnUrl, FissionUserTaskRecordVo taskRecordVo, Integer paySource) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "unpaidSubmitOrder", "待支付订单支付 start " + st, orderNo);
//        ResultDto resultDto = new ResultDto();
//        if (StringUtil.isEmpty(orderNo) || StringUtil.isEmpty(returnUrl)) {
//            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
//        }
//        //加redis锁，防止重复提交
//        String lockKey = "fission:user:goods:order:lock:" + orderNo;
//        String requestId = UUID.randomUUID().toString();
//        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
//            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
//                    "FissionUserOrderController", "unpaidSubmitOrder", orderNo);
//        }
//        try {
//            //查询订单信息
//            BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
//            baseQueryRequestDto.setQuery(orderNo);
//            BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, CommodityOrderResponseDto.class);
//            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单信息异常", st,
//                        "FissionUserOrderController", "unpaidSubmitOrder", orderNo);
//            }
//            CommodityOrderResponseDto order = responseDto.getData();
//            //查询商品信息
//            FissionCommodityQueryRequestDto queryRequestDto = new FissionCommodityQueryRequestDto();
//            queryRequestDto.setCommodityIds(Arrays.asList(new Integer[]{order.getCommodityId()}));
////            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
//            BaseResponseDto<PageableDto<CommodityResponseDto>> goodsDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
//            if (!goodsDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "商品异常", st,
//                        "FissionUserOrderController", "unpaidSubmitOrder", order.getCommodityId());
//            }
//            CommodityResponseDto goods = goodsDto.getData().getList().get(0);
//            //写用户任务放到redis，支付成功写
//            String ip = IPUtil.getRequestIp(request);
//            taskRecordVo.setIp(ip);
//            FissionGoodsOrderRecord orderRecord = new FissionGoodsOrderRecord();
//            orderRecord.setOrderNo(orderNo);
//            FissionGoodsOrderRecord orderRecordByInfo = fissionGoodsOrderRecordservice.getFissionGoodsOrderRecordByInfo(orderRecord);
//            taskRecordVo.setFissionId(orderRecordByInfo.getFissionId());
//            payServiceImpl.setFissionUserTaskRecord(taskRecordVo, orderNo);
//            //接收银台
//            InitiatePayRequestDto payRequestDto = new InitiatePayRequestDto();
//            payRequestDto.setOrderCode(orderNo);
//            payRequestDto.setUserId(DirectCommonUtil.getMemberPoId(request));
//            payRequestDto.setReturnUrl(returnUrl);
//            payRequestDto.setOpenId(userWxOpenId);
//            payRequestDto.setTerminalType(paySource != null && paySource.equals(Globals.FISSION_GOODS_ORDER.pay_source_app)
//                    ? TerminalTypeEnum.APP
//                    : (Globals.check(request) == 1 ? TerminalTypeEnum.WAP : TerminalTypeEnum.PC));
//            BaseResponseDto<OrderCodeWithPaymentResponseDto> paymentResponseDto = iOrderService.pay(payRequestDto);
//            if (!paymentResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), paymentResponseDto.getMsg(), st,
//                        "FissionUserOrderController", "unpaidSubmitOrder", orderNo);
//            }
//            resultDto.setResult(paymentResponseDto.getData());
//        } catch (Exception e) {
//            return DirectCommonUtil.addErrorLog("FissionUserOrderController", "待支付订单支付 error", e, st, orderNo);
//        } finally {
//            payServiceImpl.releaseDistributedLock(lockKey, requestId);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "unpaidSubmitOrder", "待支付订单支付 end " + st, JSON.toJSONString(resultDto.getResult()));
//        return DirectCommonUtil.setTcResponse(resultDto.getCode(), resultDto.getMsg(), st, resultDto.getResult());
//    }

//    private void addFissionGoodsOrderRecord(FissionGoodsOrderRecord fissionGoodsOrderRecord, String orderNo, BigDecimal orderAmount) {
//        //写裂变商品用户订单记录
//        if (!StringUtil.isEmpty(fissionGoodsOrderRecord.getSaleWxUnionId()) && fissionGoodsOrderRecord.getFissionId() != null) {
//            FissionSale sale = new FissionSale();
//            sale.setSaleWxUnionId(fissionGoodsOrderRecord.getSaleWxUnionId());
//            sale.setFissionId(fissionGoodsOrderRecord.getFissionId());
//            FissionSale fissionSale = fissionSaleService.getFissionSale(sale);
//            if (fissionSale != null) {
//                fissionGoodsOrderRecord.setDealerId(fissionSale.getDealerId());
//                fissionGoodsOrderRecord.setSaleId(fissionSale.getSaleId());
//            }
//        }
//        fissionGoodsOrderRecord.setOrderNo(orderNo);
//        fissionGoodsOrderRecord.setOrderStatus(FissionGoodsOrderStatus.UNPAID.getType());
//        fissionGoodsOrderRecord.setOrderAmount(orderAmount);
//        fissionGoodsOrderRecordservice.addFissionGoodsOrderRecord(fissionGoodsOrderRecord);
//    }

    /**
     * @param :
     * @return :
     * @description : 根据订单编号查看订单
     * @author : fxq
     * @date : 2020/10/22 16:39
     */
//    @RequestMapping("/getOrderByOrderCode")
//    @ResponseBody
//    public TcResponse getOrderByOrderCode(String orderCode) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "getOrderByOrderCode", "根据订单编号查看订单 start " + st, orderCode);
//        BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
//        baseQueryRequestDto.setQuery(orderCode);
//        BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, CommodityOrderResponseDto.class);
//        if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData() != null) {
//            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "根据订单编号查看订单 失败", st,
//                    "FissionUserOrderController", "getOrderByOrderCode", orderCode);
//        }
//        FissionGoodsOrderDto fissionGoodsOrderDto = new FissionGoodsOrderDto();
//        fissionGoodsOrderDto.setExpiredTime(responseDto.getData().getExpiredTime());
//        fissionGoodsOrderDto.setOrderNo(responseDto.getData().getOrderCode());
//        if (!StringUtil.isEmpty(responseDto.getData().getCheckoutCode())) {
//            fissionGoodsOrderDto.setOrderFormal(Globals.FISSION_GOODS_ORDER.goods_checkout);
//            fissionGoodsOrderDto.setCheckoutCode(responseDto.getData().getCheckoutCode());
//            String url = fission_user_checkout_url.replace("{checkoutCode}", responseDto.getData().getCheckoutCode());
//            fissionGoodsOrderDto.setCheckoutUrl(url);
//        } else {
//            fissionGoodsOrderDto.setOrderFormal(Globals.FISSION_GOODS_ORDER.goods_post);
//        }
//        fissionGoodsOrderDto.setNum(responseDto.getData().getNum());
//        fissionGoodsOrderDto.setOrderCreateDt(responseDto.getData().getOrderCreateDt());
//        fissionGoodsOrderDto.setPayTime(responseDto.getData().getPayTime());
//        fissionGoodsOrderDto.setOrderMoney(responseDto.getData().getOrderMoney());
//        fissionGoodsOrderDto = getOrderStatus(responseDto.getData(), fissionGoodsOrderDto);
//        fissionGoodsOrderDto.setGoodsId(responseDto.getData().getCommodityId());
//        BaseResponseDto<CommodityDetailResponseDto> goodsResult = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(responseDto.getData().getCommodityId());
//        if (goodsResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//            CommodityDetailResponseDto goods = goodsResult.getData();
//            fissionGoodsOrderDto.setGoodsName(goods.getCommodityName());
//            fissionGoodsOrderDto.setSeckill(goods.getCommodityType() == CommodityTypeEnum.SECKILL ? Globals.FISSION_GOODS.seckill : Globals.FISSION_GOODS.seckill_no);
//            fissionGoodsOrderDto.setGoodsPrice(goods.getOriginalPrice());
//            if (!CollectionUtils.isEmpty(goods.getHeadImages()))
//                fissionGoodsOrderDto.setGoodsHeadImage(goods.getHeadImages().get(0).getPath());
//            fissionGoodsOrderDto.setCommodityCount(goods.getCommodityCount());
//            fissionGoodsOrderDto.setSoldNumber(goods.getSoldNumber());
//            fissionGoodsOrderDto.setVirtualSoldNumber(goods.getVirtualSoldNumber());
//            fissionGoodsOrderDto.setPutAwayType(goods.getPutAwayType() != null && goods.getPutAwayType().equals(PutAwayTypeEnum.ONSALE) ? 1 : 0);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "getOrderByOrderCode", "根据订单编号查看订单 end " + st, JSON.toJSONString(fissionGoodsOrderDto));
//        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, fissionGoodsOrderDto);
//    }

    /**
     * @param :
     * @return :
     * @description : 用户获取订单列表
     * @author : fxq
     * @date : 2020/10/22 15:02
     */
//    @RequestMapping("/getOrderList")
//    @ResponseBody
//    public TcResponse getOrderList(HttpServletRequest request, Integer pageNo, Integer pageSize, Integer liveId, Integer fissionId) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController","getOrderList",  "用户获取订单列表 start " +st, liveId + "-" +fissionId);
//        List<FissionGoodsOrderDto> fissionGoodsOrderDtos = new ArrayList<>();
//        PageableRequestDto<FissionActivityCommodityOrderQueryRequestDto> dto = new PageableRequestDto<>();
//        FissionActivityCommodityOrderQueryRequestDto queryRequestDto = new FissionActivityCommodityOrderQueryRequestDto();
//        Integer memberId = DirectCommonUtil.getMemberPoId(request);
//        queryRequestDto.setUserId(memberId);
//        queryRequestDto.setLiveId(liveId);
//        queryRequestDto.setFissionActivityId(fissionId);
//        dto.setQuery(queryRequestDto);
//        dto.setPageIndex(pageNo==null || pageNo<1 ?1:pageNo);
//        dto.setPageSize(pageSize==null || pageSize<1 ?10:pageSize);
//        dto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
//        BaseResponseDto<PageableDto<CommodityOrderResponseDto>> responseDto = iOrderService.listCommodityOrder(dto, CommodityOrderResponseDto.class);
//        if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) || CollectionUtils.isEmpty(responseDto.getData().getList())) {
//            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "用户获取订单列表 失败", st,
//                    "FissionUserOrderController", "getOrderList", memberId+"");
//        }
//        List<CommodityOrderResponseDto> orderList = responseDto.getData().getList();
//        Map<Integer, CommodityResponseDto> goodsMap = new HashMap<>();
//        if (!CollectionUtils.isEmpty(orderList)) {
//            orderList.forEach(v -> {
//                BaseResponseDto<CommodityDetailResponseDto> goodsDto = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(v.getCommodityId());
//                if (goodsDto!=null && goodsDto.getData()!=null && goodsDto.getData().getCommodityId()!=null)
//                    goodsMap.put(goodsDto.getData().getCommodityId(), goodsDto.getData());
//            });
//        }
//        for (Iterator<CommodityOrderResponseDto> it = orderList.iterator(); it.hasNext(); ) {
//            CommodityOrderResponseDto orderResponseDto = it.next();
//            FissionGoodsOrderDto fissionGoodsOrderDto = new FissionGoodsOrderDto();
//            fissionGoodsOrderDto.setOrderNo(orderResponseDto.getOrderCode());
//            fissionGoodsOrderDto.setCheckoutCode(orderResponseDto.getCheckoutCode());
//            fissionGoodsOrderDto.setOrderCreateDt(orderResponseDto.getOrderCreateDt());
//            fissionGoodsOrderDto.setPayTime(orderResponseDto.getPayTime());
//            fissionGoodsOrderDto.setOrderMoney(orderResponseDto.getOrderMoney());
//            fissionGoodsOrderDto.setGoodsId(orderResponseDto.getCommodityId());
//            fissionGoodsOrderDto.setOrderCreateDt(orderResponseDto.getOrderCreateDt());
//            fissionGoodsOrderDto = getOrderStatus(orderResponseDto, fissionGoodsOrderDto);
//            CommodityResponseDto goods = goodsMap.get(orderResponseDto.getCommodityId());
//            if (goods != null) {
//                fissionGoodsOrderDto.setGoodsName(goods.getCommodityName());
//                fissionGoodsOrderDto.setGoodsPrice(goods.getOriginalPrice());
//                fissionGoodsOrderDto.setSeckill(goods.getCommodityType() == CommodityTypeEnum.SECKILL ? Globals.FISSION_GOODS.seckill : Globals.FISSION_GOODS.seckill_no);
//                fissionGoodsOrderDto.setNum(goods.getCommodityCount());
//                fissionGoodsOrderDto.setSoldNumber(goods.getSoldNumber());
//                fissionGoodsOrderDto.setVirtualSoldNumber(goods.getVirtualSoldNumber());
//                if (!CollectionUtils.isEmpty(goods.getHeadImages()))
//                    fissionGoodsOrderDto.setGoodsHeadImage(goods.getHeadImages().get(0).getPath());
//            }
//            fissionGoodsOrderDto.setExpiredTime(orderResponseDto.getExpiredTime());
//            fissionGoodsOrderDtos.add(fissionGoodsOrderDto);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "getOrderList", "用户获取订单列表 end " + st, fissionGoodsOrderDtos.size() + "");
//        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, fissionGoodsOrderDtos);
//    }

//    private FissionGoodsOrderDto getOrderStatus(CommodityOrderResponseDto orderResponseDto, FissionGoodsOrderDto fissionGoodsOrderDto) {
//        if (orderResponseDto.getIsExpired()) {
//            fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.TRADE_CLOSED);
//        } else if (orderResponseDto.getTradeRefundStatus() != null && orderResponseDto.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND)) {
//            switch (orderResponseDto.getTradeStatus()) {
//                case WAIT_BUYER_PAY:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.UNPAID);
//                    break;
//                case PAY_SUCCESS:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.PAID);
//                    break;
//                case WAIT_SELLER_SEND_GOODS:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.WAIT_SELLER_SEND_GOODS);
//                    break;
//                case WAIT_BUYER_CONFIRM_GOODS:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.WAIT_BUYER_CONFIRM_GOODS);
//                    break;
//                case TRADE_CLOSED:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.TRADE_CLOSED);
//                    break;
//                case TRADE_FINISHED:
//                    fissionGoodsOrderDto.setCancelDate(orderResponseDto.getCheckoutTime());
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.CHECKOUT);
//                    break;
//            }
//        } else {
//            switch (orderResponseDto.getTradeRefundStatus()) {
//                case WAIT_SELLER_AGREE:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.APPLY_REFUND);
//                    break;
//                case SUCCESS:
//                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.REFUND_SUCCESS);
//                    break;
//            }
//        }
//        return fissionGoodsOrderDto;
//    }

    /**
     * @param :
     * @return :
     * @description : 用户申请退款
     * @author : fxq
     * @date : 2020/10/22 16:39
     */
//    @RequestMapping("/applyRefund")
//    @ResponseBody
//    public TcResponse applyRefund(HttpServletRequest request, String orderCode) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "applyRefund", "用户申请退款 start " + st, orderCode);
//        if (StringUtil.isEmpty(orderCode)) {
//            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
//        }
//        Integer memberId = DirectCommonUtil.getMemberPoId(request);
//        try {
//            BaseResponseDto<Boolean> responseDto = iOrderService.applyRefund(orderCode, memberId);
//            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "用户申请退款 失败", st,
//                        "FissionUserOrderController", "applyRefund", orderCode + "--" + memberId);
//            }
//            FissionGoodsOrderRecord goodsOrderRecord = new FissionGoodsOrderRecord();
//            goodsOrderRecord.setOrderNo(orderCode);
//            goodsOrderRecord.setOptUserId(memberId);
//            goodsOrderRecord.setOrderStatus(FissionGoodsOrderStatus.APPLY_REFUND.getType());
//            fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(goodsOrderRecord);
//        } catch (Exception e) {
//            return DirectCommonUtil.addErrorLog("FissionUserOrderController", "用户申请退款error", e, st, orderCode);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "applyRefund", "用户申请退款 end " + st, orderCode);
//        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, 0);
//    }

    /**
     * @param :
     * @return :
     * @description : 退款回调
     * @author : fxq
     * @date : 2020/11/24 13:53
     */
//    @RequestMapping(value = "/userOrderRefundNotice")
//    @ResponseBody
//    public String userOrderRefundNotice(HttpServletRequest request) {
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionUserOrderController", "userOrderRefundNotice", "退款回调 start...", "");
//        final DataTransferObject<List<Map<String, Object>>> dto = new DataTransferObject<List<Map<String, Object>>>();
//        try {
//            String result = ParameterUtil.getBodyHeadMapFromDesForInter2(request);
//            if (result != null) {
//                List<OrderBackInfo> list = JSON.parseArray(result, OrderBackInfo.class);
//                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionUserOrderController", "userOrderRefundNotice", "退款回调 参数列表...", JSON.toJSONString(list));
//                //回调  返回的多个结果
//                final List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
//                for (OrderBackInfo backInfo : list) {
//                    Map<String, Object> map = null;
//                    if (backInfo.getStatus().equals("10000")) {
//                        map = payServiceImpl.refundOrderNotice(backInfo);
//                        rstList.add(map);
//                    } else {
//                        map = new HashMap<String, Object>();
//                        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionUserOrderController", "userOrderRefundNotice", "退款回调 失败", JSON.toJSONString(backInfo));
//                        map.put("code", backInfo.getStatus());
//                        map.put("id", backInfo.getId());
//                    }
//                }
//                dto.setData(rstList);
//                String returnInfo = ParameterUtil.encrypt(dto.toString());
//                return returnInfo;
//            } else {
//                dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
//                dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
//                return ParameterUtil.encrypt(dto.toString());
//            }
//        } catch (Exception e) {
//            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionUserOrderController", "userOrderRefundNotice", "退款回调 error", e);
//            dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
//            dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
//            return ParameterUtil.encrypt(dto.toString());
//        }
//    }

    /**
     * @param :
     * @return :
     * @description : 根据订单流水查询订单信息
     * @author : fxq
     * @date : 2020/12/1 17:32
     */
//    @RequestMapping("/getOrderByOrderTradeId")
//    @ResponseBody
//    public TcResponse getOrderByOrderTradeId(HttpServletRequest request, String orderTradeId) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "getOrderByOrderTradeId", "根据订单流水查询订单信息 start " + st, orderTradeId);
//        FissionGoodsOrderDto orderRecord = null;
//        if (StringUtil.isEmpty(orderTradeId)) {
//            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
//        }
//        try {
//            orderRecord = fissionGoodsOrderRecordservice.getOrderByOrderTradeId(orderTradeId, GlobalConstants.FissionTradeType.GOODS_PAY.getCode());
//            if (orderRecord == null || orderRecord.getOrderId() == null) {
//                return DirectCommonUtil.setTcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "订单流水无效", st, 0);
//            }
//        } catch (Exception e) {
//            return DirectCommonUtil.addErrorLog("FissionUserOrderController", "根据订单流水查询订单信息error", e, st, orderTradeId);
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserOrderController", "getOrderByOrderTradeId", "根据订单流水查询订单信息 end " + st, JSON.toJSONString(orderRecord));
//        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, orderRecord);
//    }

    /**
     * @param :
     * @return :
     * @description : 获取登陆人姓名手机号
     * @author : fxq
     * @date : 2020/12/1 22:37
     */
    @RequestMapping("/getMemberPo")
    @ResponseBody
    public TcResponse getMemberPo(HttpServletRequest request) {
        long st = System.currentTimeMillis();
        MemberPo memberPo = DirectCommonUtil.getMemberPo(request);
        if (memberPo == null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.USER_NOT_LOGGED_IN.getCode(), "请重新登陆", st, 0);
        }
        Map<String, String> map = new HashMap<>();
        map.put("name", memberPo.getName());
        map.put("phone", memberPo.getPhone() + "");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, map);
    }

    /**
     * 裂变 销售的经销商信息
     *
     * @param request
     * @param fissionId
     * @param saleWxUnionid
     * @return
     */
    @RequestMapping("/saleDealerInfo")
    @ResponseBody
    public TcResponse saleDealerInfo(HttpServletRequest request, Integer fissionId, String saleWxUnionid) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController", "saleDealerInfo",
                "销售所属经销商 start ", "裂变id:" + fissionId + "saleWxUnionid" + saleWxUnionid);
        if (fissionId == null || StringUtil.isEmpty(saleWxUnionid)) {
            return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), TcResponseCode.PARAM_INVALID.getName(), st, null);
        }
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        CsCompanyCmsDto simpleInfo = new CsCompanyCmsDto();
        FissionSale fissionSale = new FissionSale();
        fissionSale.setFissionId(fissionId);
        fissionSale.setSaleWxUnionId(saleWxUnionid);
        List<FissionSale> fissionSaleList = fissionSaleService.getFissionSaleList(fissionSale);
        if (CollectionUtils.isEmpty(fissionSaleList)) {
            return DirectCommonUtil.setTcResponse(code, msg, st, simpleInfo);
        }
        CsCompany company = companyBaseService.getCsCompanyById(fissionSaleList.get(0).getDealerId());
        if (company == null) {
            return DirectCommonUtil.setTcResponse(code, msg, st, simpleInfo);
        }
        simpleInfo.setId(company.getId());
        simpleInfo.setCompanyName(company.getCompanyName());
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController", "saleDealerInfo",
                "返参：", JSON.toJSONString(simpleInfo));
        return DirectCommonUtil.setTcResponse(code, msg, st, simpleInfo);
    }


}
