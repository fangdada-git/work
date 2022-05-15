package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.FissionGoodsOrderDto;
import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.framework.base.constant.CommonCode;
import com.tuanche.framework.base.entity.DataTransferObject;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.OrderCheckoutRequestDto;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.web.dto.PayParamDto;
import com.tuanche.web.service.FissionLoginServiceImpl;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import com.tuanche.web.util.ParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-25 14:46
 **/
@Controller
@RequestMapping("/api/fission/manu/sales/order")
public class FissionSalesOrderController {

    @Value("${fission_b_pay_product_code}")
    private String fission_b_pay_product_code;
    @Value("${order_tuanche_host}")
    private String order_tuanche_host;
    
    @Reference
    private FissionSalesOrderService fissionSalesOrderService;
    @Reference
    private FissionTradeRecordService fissionTradeRecordService;
    @Reference
    private FissionSaleService fissionSaleService; 
    @Autowired
    private FissionLoginServiceImpl fissionLoginServiceImpl;
    @Autowired
    private PayServiceImpl payServiceImpl;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Reference
    private UserBaseService userBaseService;
    @Reference
    private CompanyBaseService companyBaseService;
    

    /**
     * @description: 销售支付对赌金
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 14:54
     */
    @RequestMapping("/salesPayGambleMoney")
    @ResponseBody
    public TcResponse salesPayGambleMoney (HttpServletRequest request, FissionSalesOrder fissionSalesOrder, String returnUrl) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","salesPayGambleMoney",  "销售支付对赌金 start " +st, JSON.toJSONString(fissionSalesOrder));
        if (fissionSalesOrder==null || fissionSalesOrder.getFissionId()==null || StringUtil.isEmpty(fissionSalesOrder.getWxOpenId())
                || fissionSalesOrder.getPeriodsId()==null || StringUtil.isEmpty(fissionSalesOrder.getWxUnionId())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
        //加redis锁，防止重复提交
        String lockKey = "fission:sale:order:lock:" + fissionSalesOrder.getPeriodsId()+"_"+fissionSalesOrder.getFissionId()+"_"+csUser.getDealerId()+"_"+csUser.getId();
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120*1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
                    "FissionSalesOrderController", "salesPayGambleMoney", JSON.toJSONString(fissionSalesOrder));
        }
        try {
            //验证活动下此销售|微信是否已支付，销售奖励按照微信发放
            FissionSale fissionSale = new FissionSale();
            fissionSale.setSaleWxUnionId(fissionSalesOrder.getWxUnionId());
            fissionSale.setFissionId(fissionSalesOrder.getFissionId());
            FissionSale sale = fissionSaleService.getFissionSale(fissionSale);
            if (sale!=null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该场次活动此微信已支付过对赌金", st,
                        "FissionSalesOrderController", "salesPayGambleMoney", JSON.toJSONString(fissionSalesOrder));
            }
            //验证活动下此销售|微信是否已支付，销售奖励按照微信发放
            FissionSale sale1 = new FissionSale();
            sale1.setSaleId(csUser.getId());
            sale1.setFissionId(fissionSalesOrder.getFissionId());
            FissionSale sale2 = fissionSaleService.getFissionSale(sale1);
            if (sale2!=null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该场次活动您已支付过对赌金", st,
                        "FissionSalesOrderController", "salesPayGambleMoney", JSON.toJSONString(fissionSalesOrder));
            }
            //验证对赌金金额
            List<FissionAwardRule> fissionAwardRules = fissionActivityService.getAwardRuleListByFissionId(fissionSalesOrder.getFissionId(), 1);
            if (CollectionUtils.isEmpty(fissionAwardRules) || fissionAwardRules.get(0).getPersonMoney()==null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "对赌金金额异常", st,
                        "FissionSalesOrderController", "salesPayGambleMoney", csUser.getId()+"");
            }
            fissionSalesOrder.setOrderAmount(fissionAwardRules.get(0).getPersonMoney());
            //销售Unionid放到redis，支付成功写参与活动销售使用
            payServiceImpl.setSaleWxUnionid(csUser.getId(), fissionSalesOrder.getWxUnionId()+":"+fissionSalesOrder.getWxOpenId());
            //验证场次，活动，销售是否已经支付过
            FissionSalesOrder querySalesOrder = new FissionSalesOrder();
            querySalesOrder.setSaleId(csUser.getId());
            querySalesOrder.setDealerId(csUser.getDealerId());
            querySalesOrder.setPeriodsId(fissionSalesOrder.getPeriodsId());
            querySalesOrder.setFissionId(fissionSalesOrder.getFissionId());
            List<FissionSalesOrderDto> orderList = fissionSalesOrderService.getFissionSalesOrderList(querySalesOrder);
            String orderTradeId = "";
            Date date = new Date();
            fissionSalesOrder.setUpdateUserId(csUser.getId());
            fissionSalesOrder.setSaleId(csUser.getId());
            fissionSalesOrder.setDealerId(csUser.getDealerId());
            //已支付
            if (!CollectionUtils.isEmpty(orderList)) {
                if (orderList.get(0).getOrderStatus().equals(GlobalConstants.fission_sales_order.STATUS_PAID)) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "您已支付过对赌金,请刷新", st,
                            "FissionSalesOrderController", "salesPayGambleMoney", csUser.getId()+"");
                }
                fissionSalesOrder.setId(orderList.get(0).getId());
                fissionSalesOrder.setOrderNo(orderList.get(0).getOrderNo());
                //未支付
            } else {
                //创建订单
                fissionSalesOrder.setCreateDt(date);
                fissionSalesOrder.setOrderStatus(GlobalConstants.fission_sales_order.STATUS_NON_PAYMENT);
                fissionSalesOrder.setOrderType(GlobalConstants.fission_sales_order.TYPE_FISSION);
                FissionSalesOrder salesOrder = fissionSalesOrderService.insertSelective(fissionSalesOrder);
                if (salesOrder ==null || salesOrder.getId()==0) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "购买失败", st,
                            "FissionSalesOrderController", "salesPayGambleMoney", csUser.getId()+"");
                }
                fissionSalesOrder.setId(salesOrder.getId());
                fissionSalesOrder.setOrderNo(salesOrder.getOrderNo());
            }
            orderTradeId = Globals.orderTradeByTime("fission_sale_", fissionSalesOrder.getOrderNo());
            //写支付流水
            addTradeRecord(fissionSalesOrder, orderTradeId,date);
            //接收银台7
            ResultDto resultDto = pay(request, fissionSalesOrder, orderTradeId, returnUrl, fissionSalesOrder.getFissionId());
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","salesPayGambleMoney",  "销售支付对赌金 end " +st, resultDto.getMsg());
            return DirectCommonUtil.setTcResponse(resultDto.getCode(), resultDto.getMsg(), st, resultDto.getResult());
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "销售支付对赌金 error", e, st, JSON.toJSONString(fissionSalesOrder));
        } finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
    }
    
    private ResultDto pay (HttpServletRequest request, FissionSalesOrder fissionSalesOrder, String orderTradeId, String returnUrl, Integer fissionId) {
        ResultDto resultDto = null;
        PayParamDto payParamDto = new PayParamDto();
        payParamDto.setOrderNo(fissionSalesOrder.getOrderNo());
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionId);
        if (fissionActivity==null || StringUtil.isEmpty(fissionActivity.getActivityName())) {
            payParamDto.setContent("用户购买裂变商品");
        } else if (fissionActivity.getActivityName().length()<=12) {
            payParamDto.setContent(fissionActivity.getActivityName());
        } else {
            payParamDto.setContent(fissionActivity.getActivityName().substring(0, 12)+"…");
        }
        payParamDto.setContent(fissionActivity==null ? "裂变活动销售支付对赌金" : fissionActivity.getActivityName());
        payParamDto.setAmount(fissionSalesOrder.getOrderAmount());
        // 支付成功 回调的url （回调修改订单相关信息）
        payParamDto.setNotifyUrl(order_tuanche_host+"/api/fission/manu/sales/order/salesOrderNotice");
        // 支付成功  返回的url（页面返回）
        payParamDto.setReturnUrl(returnUrl);
        payParamDto.setOpenId(fissionSalesOrder.getWxOpenId());
        payParamDto.setPayType("01");
        payParamDto.setTradeBusiness(orderTradeId);
        payParamDto.setProductCode(fission_b_pay_product_code);
        payParamDto.setBroswerType(Globals.check(request));
        resultDto = payServiceImpl.pay(request, payParamDto);
        return resultDto;
    }
    
    private void updateSalesOrderStatus (Integer orderId, Integer orderStatus, Integer updateUserId) {
        FissionSalesOrder salesOrder = new FissionSalesOrder();
        salesOrder.setOrderStatus(orderStatus);
        salesOrder.setUpdateDt(new Date());
        salesOrder.setId(orderId);
        salesOrder.setUpdateUserId(updateUserId);
        fissionSalesOrderService.updateByPrimaryKeySelective(salesOrder);
    }
    private void addTradeRecord (FissionSalesOrder fissionSalesOrder,String orderTradeId, Date date) {
        FissionTradeRecord fissionTradeRecord = new FissionTradeRecord();
        fissionTradeRecord.setOrderTradeId(orderTradeId);
        fissionTradeRecord.setOrderNo(fissionSalesOrder.getOrderNo());
        fissionTradeRecord.setDeleteFlag(false);
        fissionTradeRecord.setCreateDt(date);
        fissionTradeRecord.setTradeType(GlobalConstants.FissionTradeType.ORDER_PAY.getCode());
        fissionTradeRecord.setTradeAmount(fissionSalesOrder.getOrderAmount());
        fissionTradeRecord.setTradeRemark("销售支付对赌金");
        fissionTradeRecord.setPayUserId(fissionSalesOrder.getSaleId()+"");
        fissionTradeRecord.setTradeTime(date);
        fissionTradeRecordService.insertSelective(fissionTradeRecord);
    }

    /**
     * @description: 店员查看订单列表
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 17:19
     */
    @RequestMapping("/fissionSalesOrderList")
    @ResponseBody
    public TcResponse fissionSalesOrderList (HttpServletRequest request) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","fissionSalesOrderList",  "店员查看订单列表 start " +st, "");
        List<FissionSalesOrderDto> list = null;
        try {
            CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
            FissionSalesOrder salesOrder = new FissionSalesOrder();
            salesOrder.setSaleId(csUser.getId());
            List<FissionSalesOrderDto> salesOrders = fissionSalesOrderService.getFissionSalesOrderList(salesOrder);
            if (CollectionUtils.isEmpty(salesOrders)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "无订单", st,
                        "FissionSalesOrderController", "fissionSalesOrderList", csUser.getId()+"");
            }
            list = salesOrders;
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "店员查看订单列表 error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","fissionSalesOrderList",  "店员查看订单列表 end " +st, "");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
    }
    
    /**
     * @description : 销售支付对赌金回调 
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/10/10 11:04
     */
    @RequestMapping(value = "/salesOrderNotice")
    @ResponseBody
    public String salesOrderNotice(HttpServletRequest request) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "salesOrderNotice", "salesOrderNotice start...", "");
        final DataTransferObject<List<Map<String, Object>>> dto = new DataTransferObject<List<Map<String, Object>>>();
        try {
            String result = ParameterUtil.getBodyHeadMapFromDesForInter2(request);
            if(result!=null){
                List<OrderBackInfo> list = JSON.parseArray(result, OrderBackInfo.class);
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "salesOrderNotice", "销售支付对赌金回调  参数列表...", JSON.toJSONString(list));
                //回调  返回的多个结果
                final List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
                for(OrderBackInfo backInfo : list){
                    Map<String, Object> map = null;
                    if(backInfo.getStatus().equals("10000")){
                        map = salesOrderNotice(backInfo);
                        rstList.add(map);
                    }else{
                        map = new HashMap<String, Object>();
                        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "salesOrderNotice", "销售支付对赌金回调 失败", JSON.toJSONString(backInfo));
                        map.put("code", backInfo.getStatus());
                        map.put("id", backInfo.getId());
                    }
                }
                dto.setData(rstList);
                String returnInfo =ParameterUtil.encrypt(dto.toString());
                return returnInfo;
            }else{
                dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
                dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
                return ParameterUtil.encrypt(dto.toString());
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "salesOrderNotice", "销售支付对赌金回调error", e);
            dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
            dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
            return ParameterUtil.encrypt(dto.toString());
        }
    }

    /**
     * @description : 销售支付成功
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/10/20 19:44
     */
    public Map<String, Object> salesOrderNotice(OrderBackInfo backInfo) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "TcMallOrderServiceImpl", "salesOrderNotice", "销售支付对赌金回调_销售支付成功  ing…………", JSON.toJSONString(backInfo));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        FissionTradeRecord fissionTradeRecord = new FissionTradeRecord();
        fissionTradeRecord.setOrderTradeId(backInfo.getBizId());
        fissionTradeRecord.setTradeType(GlobalConstants.FissionTradeType.ORDER_PAY.getCode());
        List<FissionTradeRecord> recordList = fissionTradeRecordService.getFissionTradeRecordListByWrite(fissionTradeRecord);
        if(!CollectionUtils.isEmpty(recordList)){
            FissionSalesOrder salesOrder = new FissionSalesOrder();
            salesOrder.setOrderNo(recordList.get(0).getOrderNo());
            List<FissionSalesOrderDto> fissionSalesOrderList = fissionSalesOrderService.getFissionSalesOrderListByWrite(salesOrder);
            if(CollectionUtils.isEmpty(fissionSalesOrderList)
                    || !fissionSalesOrderList.get(0).getOrderStatus().equals(GlobalConstants.fission_sales_order.STATUS_NON_PAYMENT)){
                if(!CollectionUtils.isEmpty(fissionSalesOrderList) &&  fissionSalesOrderList.get(0).getOrderStatus().equals(GlobalConstants.fission_sales_order.STATUS_PAID)){
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", 
                            "salesOrderNotice", "销售支付对赌金回调_销售支付成功 订单已支付", JSON.toJSONString(fissionSalesOrderList));
                    resultMap.put("code", 10000);
                    resultMap.put("id", backInfo.getId());
                    return resultMap;
                }
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", 
                        "salesOrderNotice", "销售支付对赌金回调_销售支付成功 不是待支付订单", JSON.toJSONString(fissionSalesOrderList));
                resultMap.put("code", 10001);
                resultMap.put("id", backInfo.getId());
                return resultMap;
            }
            //修改订单状态
            FissionSalesOrder order = new FissionSalesOrder();
            order.setId(fissionSalesOrderList.get(0).getId());
            order.setOrderStatus(GlobalConstants.fission_sales_order.STATUS_PAID);
            order.setPayType(backInfo.getPayType());
            order.setPayTime(backInfo.getBizTime());
            fissionSalesOrderService.updateByPrimaryKeySelective(order);
            //修改流水记录
            FissionTradeRecord tradeRecord = new FissionTradeRecord();
            tradeRecord.setId(recordList.get(0).getId());
            tradeRecord.setTradeStatus(GlobalConstants.FissionTrade.TRADE_STATUS_PAY_SUCCESS);
            tradeRecord.setTradeNo(backInfo.getResId());
            tradeRecord.setPayTime(backInfo.getBizTime());
            tradeRecord.setSerialNo(backInfo.getSerialNo());
            fissionTradeRecordService.updateByPkeySelective(tradeRecord);
            //新增裂变销售
            FissionSale fissionSale = new FissionSale();
            fissionSale.setFissionId(fissionSalesOrderList.get(0).getFissionId());
            fissionSale.setDealerId(fissionSalesOrderList.get(0).getDealerId());
            fissionSale.setSaleId(fissionSalesOrderList.get(0).getSaleId());
            CsUser csUser = userBaseService.getCsUserById(fissionSale.getSaleId());
            if (csUser!=null) {
                fissionSale.setSaleName(StringUtil.isEmpty(csUser.getUname())? csUser.getUphone() : csUser.getUname());
            }
            fissionSale.setPayAmount(fissionSalesOrderList.get(0).getOrderAmount());
            fissionSale.setOrderNo(fissionSalesOrderList.get(0).getOrderNo());
            //获取销售微信Unionid
            fissionSale.setSaleWxUnionId(payServiceImpl.getSaleWxUnionid(fissionSale.getSaleId()).split(":")[0]);
            fissionSale.setSaleWxOpenId(payServiceImpl.getSaleWxUnionid(fissionSale.getSaleId()).split(":")[1]);
            fissionSaleService.insertFissionSale(fissionSale);
        }else{
            //无 orderTradeId 操作失败
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "salesOrderNotice", "销售支付对赌金回调_销售支付成功 无 orderTradeId 操作失败", JSON.toJSONString(backInfo));
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        resultMap.put("code", 10000);
        resultMap.put("id", backInfo.getId());
        return resultMap;
    }

    /**
     * @description : 用户核销订单
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/10/22 16:39
     */
    @RequestMapping("/checkoutOrder")
    @ResponseBody
    public TcResponse checkoutOrder (HttpServletRequest request, String checkoutCode) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","checkoutOrder",  "用户核销订单 start " +st, checkoutCode);
        if (StringUtil.isEmpty(checkoutCode)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        PageableRequestDto requestDto = new PageableRequestDto();
        CommodityOrderQueryRequestDto queryRequestDto = new CommodityOrderQueryRequestDto();
        queryRequestDto.setCheckoutCode(checkoutCode);
        requestDto.setQuery(queryRequestDto);
        requestDto.setPageIndex(1);
        requestDto.setPageSize(1);
        BaseResponseDto<PageableDto<CommodityOrderResponseDto>> baseResponseDto = iOrderService.listCommodityOrder(requestDto, CommodityOrderResponseDto.class);
        if (!baseResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) || CollectionUtils.isEmpty(baseResponseDto.getData().getList())) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该券码无效", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        CommodityOrderResponseDto order = baseResponseDto.getData().getList().get(0);
        if (order.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_FINISHED)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode(), "券码已核销", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        if (!(order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单不是支付成功状态", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        OrderCheckoutRequestDto checkoutRequestDto = new OrderCheckoutRequestDto();
        checkoutRequestDto.setCheckoutCode(checkoutCode);
        BaseResponseDto<String> responseDto = iOrderService.checkoutOrder(checkoutRequestDto);
        if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "用户核销订单 失败", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
        FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
        record.setOrderNo(responseDto.getData());
        record.setCancelSale(csUser.getId());
        record.setCancelDealerId(csUser.getDealerId());
        record.setOrderStatus(FissionGoodsOrderStatus.CHECKOUT.getType());
        int num = fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(record);
        if (num==0) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "用户核销订单 修改订单记录异常", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode+"--"+responseDto.getData());
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","checkoutOrder",  "用户核销订单 end " +st, checkoutCode);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, 0);
    }
    /**
     * @description : 根据订单核销码获取订单详情
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/13 13:56
     */
    @RequestMapping("/getOrderByCheckoutCode")
    @ResponseBody
    public TcResponse getOrderByCheckoutCode (HttpServletRequest request, String checkoutCode) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","getOrderByCheckoutCode",  "根据订单核销码获取订单详情 start " +st, checkoutCode);
        if (StringUtil.isEmpty(checkoutCode)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        PageableRequestDto requestDto = new PageableRequestDto();
        CommodityOrderQueryRequestDto queryRequestDto = new CommodityOrderQueryRequestDto();
        queryRequestDto.setCheckoutCode(checkoutCode);
        requestDto.setQuery(queryRequestDto);
        requestDto.setPageIndex(1);
        requestDto.setPageSize(2);
        BaseResponseDto<PageableDto<CommodityOrderResponseDto>> responseDto = iOrderService.listCommodityOrder(requestDto, CommodityOrderResponseDto.class);
        if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) || CollectionUtils.isEmpty(responseDto.getData().getList())) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该券码无效", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        CommodityOrderResponseDto order = responseDto.getData().getList().get(0);
        if (order.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_FINISHED)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode(), "券码已核销", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        if (!(order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单不是支付成功状态", st,
                    "FissionSalesOrderController","checkoutOrder", checkoutCode);
        }
        FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
        record.setOrderNo(order.getOrderCode());
        FissionGoodsOrderRecord recordByInfo = fissionGoodsOrderRecordservice.getFissionGoodsOrderRecordByInfo(record);
        if (recordByInfo==null) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "根据订单核销码获取订单详情 订单记录异常", st,
                    "FissionSalesOrderController","getOrderByCheckoutCode", checkoutCode);
        }
        FissionGoodsOrderDto goodsOrderDto = new FissionGoodsOrderDto();
        goodsOrderDto.setOrderNo(recordByInfo.getOrderNo());
        goodsOrderDto.setCheckoutCode(checkoutCode);
        goodsOrderDto.setOrderMoney(order.getOrderMoney());
        FissionActivity fission = fissionActivityService.getFissionActivityById(recordByInfo.getFissionId());
        if (fission!=null) {
            goodsOrderDto.setFissionActivityName(fission.getActivityName());
            goodsOrderDto.setFissionActivityStartDate(fission.getStartTime());
            goodsOrderDto.setFissionActivityEndDate(fission.getEndTime());
        }
        goodsOrderDto.setSaleName(userBaseService.getUserNameById(recordByInfo.getSaleId()));
        goodsOrderDto.setDealerName(companyBaseService.getCsCompanyName(recordByInfo.getDealerId()));
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","getOrderByCheckoutCode",  "根据订单核销码获取订单详情 end " +st, JSON.toJSONString(goodsOrderDto));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, goodsOrderDto);
    }

    /**
     * @description : 根据订单流水查询订单信息
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/12/1 17:32
     */
    @RequestMapping("/getOrderByOrderTradeId")
    @ResponseBody
    public TcResponse getOrderByOrderTradeId (HttpServletRequest request, String orderTradeId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","getOrderByOrderTradeId",  "根据订单流水查询订单信息 start " +st, orderTradeId);
        FissionGoodsOrderDto orderRecord = null;
        if (StringUtil.isEmpty(orderTradeId)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            orderRecord = fissionGoodsOrderRecordservice.getOrderByOrderTradeId(orderTradeId, GlobalConstants.FissionTradeType.ORDER_PAY.getCode());
            if (orderRecord==null || orderRecord.getOrderId()==null) {
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "订单流水无效", st, 0);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionSalesOrderController", "根据订单流水查询订单信息error", e, st, orderTradeId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionSalesOrderController","getOrderByOrderTradeId",  "根据订单流水查询订单信息 end " +st, JSON.toJSONString(orderRecord));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, orderRecord);
    }
    

}
