package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.framework.base.util.HttpClient;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.web.dto.PayParamDto;
import com.tuanche.web.dto.PaymentResult;
import com.tuanche.web.dto.TcTrade;
import com.tuanche.web.util.DesUtils;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import com.tuanche.web.util.ParameterUtil;
import com.tuanche.web.util.httpclient.HttpProtocolHandler;
import com.tuanche.web.util.httpclient.HttpRequest;
import com.tuanche.web.util.httpclient.HttpResponse;
import com.tuanche.web.util.httpclient.HttpResultType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
public class PayServiceImpl {

    @Value("${pay_host}")
    private String pay_host;
    @Value("${order_tuanche_host}")
    private String order_tuanche_host;

    @Reference
    private FissionSalesOrderService fissionSalesOrderService;
    @Reference
    private FissionTradeRecordService fissionTradeRecordService;
    @Reference
    private FissionSaleService fissionSaleService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    ExecutorService executorService;


    public ResultDto pay(HttpServletRequest request, PayParamDto payParamDto) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", payParamDto.getOrderNo());
        map.put("content", payParamDto.getContent());

        map.put("productCode", payParamDto.getProductCode());
        map.put("amount", payParamDto.getAmount());
        // ???????????? ?????????url ????????????????????????????????????r
        map.put("notifyUrl", payParamDto.getNotifyUrl());
        // ????????????  ?????????url??????????????????
        map.put("returnUrl", payParamDto.getReturnUrl());
        map.put("openId", payParamDto.getOpenId());
        map.put("payType", payParamDto.getPayType());
        map.put("tradeBusiness", payParamDto.getTradeBusiness());
        map.put("broswerType", payParamDto.getBroswerType());
        String result = null;
        try {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "pay", "?????????????????????  ??????????????? stat????????????", JSON.toJSONString(map));
            result = HttpClient.post(pay_host + "/index", com.alibaba.fastjson.JSONObject.toJSONString(map), null);
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "pay", "?????????????????????  ??????????????? ???????????? ", e);
        }
        result = ParameterUtil.decrypt(result);
        if (StringUtils.isNotBlank(result)) {
            com.alibaba.fastjson.JSONObject jObj = com.alibaba.fastjson.JSONObject.parseObject(result);
            if (jObj != null && "10000".equals(jObj.getString("code"))) {
                if (payParamDto.getBroswerType() != null && payParamDto.getBroswerType().equals(Globals.FISSION_GOODS_ORDER.pay_source_app)) {
                    com.alibaba.fastjson.JSONObject jsonData = jObj.getJSONObject("json");
                    jsonData.put("orderTradeId", payParamDto.getTradeBusiness());
                    dto.setResult(jsonData);
                } else {
                    dto.setResult(jObj.getString("json"));
                }
                dto.setCode(StatusCodeEnum.SUCCESS.getCode());
            } else {
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "pay", "?????????????????????  ??????????????? ??????code???=10000", result);
                dto.setCode(StatusCodeEnum.INTERFACE_OUTTER_INVOKE_ERROR.getCode());
                dto.setMsg("?????????????????????");
                dto.setResult(payParamDto.getOrderNo());
                return dto;
            }
        } else {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionSalesOrderController", "pay", "?????????????????????  ??????????????? ??????result =null", result);
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg("????????????????????????????????????");
            dto.setResult(payParamDto.getOrderNo());
            return dto;
        }
        return dto;
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * @param :
     * @return :
     * @description : ??????????????????????????????
     * @author : fxq
     * @date : 2020/10/27 11:04
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        try {
            String result = redisService.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
        } catch (RedisException e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "??????????????????????????????error", lockKey, e);
            return false;
        }
        return false;
    }

    /**
     * @param :
     * @return :
     * @description : ????????????
     * @author : fxq
     * @date : 2020/10/27 11:05
     */
    public void releaseDistributedLock(String lockKey, String requestId) {
        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Object result = redisService.eval(script, Arrays.asList(lockKey.split(",")), Arrays.asList(requestId.split(",")));
            } catch (Exception e) {
                StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "????????????error", lockKey, e);
            }
        });
    }

    public void setSaleWxUnionid(Integer saleId, String wxUnionid) {
        try {
            //3?????????
            redisService.set(DirectCommonUtil.FISSION_SALE_OPENID_REDIS + saleId, wxUnionid, 259200000);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    public String getSaleWxUnionid(Integer saleId) {
        try {
            return redisService.get(DirectCommonUtil.FISSION_SALE_OPENID_REDIS + saleId, String.class);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setFissionUserTaskRecord(FissionUserTaskRecordVo taskRecordVo, String orderNo) {
        if (taskRecordVo == null) return;
        try {
            //3?????????
            redisService.set(DirectCommonUtil.FISSION_USER_SALE_TASK_ORDERNO + orderNo, JSON.toJSONString(taskRecordVo), 259200000);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    public FissionUserTaskRecordVo getFissionUserTaskRecord(String orderNo) {
        try {
            String result = redisService.get(DirectCommonUtil.FISSION_USER_SALE_TASK_ORDERNO + orderNo, String.class);
            if (!StringUtil.isEmpty(result)) {
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "?????????????????????????????????", orderNo + "-" + result);
                redisService.del(DirectCommonUtil.FISSION_USER_SALE_TASK_ORDERNO + orderNo);
                return JSON.parseObject(result, FissionUserTaskRecordVo.class);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param : orderNo ????????????
     * @param : tradeType ???????????????1 ??????????????? 2 ???????????? 3 ???????????? 4  ???????????? 5  ??????C???????????????
     * @param : payMethod ???????????? 0-???????????? 1-pos 2-??????
     * @return :
     * @description : ??????
     * @author : fxq
     * @date : 2020/11/16 16:49
     */
//    public ResultDto refund(String orderNo, Integer tradeType, String payMethod, String notifyUrl, Integer userId) {
//        ResultDto dto = new ResultDto();
//        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
//        try {
//            if (StringUtil.isEmpty(orderNo)) return DirectCommonUtil.addParamNull();
//            //???????????????????????????????????????
//            BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
//            baseQueryRequestDto.setQuery(orderNo);
//            BaseResponseDto<CommodityOrderResponseDto> responseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, CommodityOrderResponseDto.class);
//            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.addResultInfo(StatusCodeEnum.DATA_IS_WRONG.getCode(), "????????????");
//            }
//            if (!(responseDto.getData().getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.WAIT_SELLER_AGREE) || responseDto.getData().getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
//                return DirectCommonUtil.addResultInfo(StatusCodeEnum.DATA_IS_WRONG.getCode(), "???????????????????????????");
//            }
//            FissionTradeRecord fissionTradeRecord = new FissionTradeRecord();
//            fissionTradeRecord.setOrderNo(orderNo);
//            fissionTradeRecord.setTradeType(tradeType);
//            fissionTradeRecord.setTradeStatus(GlobalConstants.FissionTrade.TRADE_STATUS_PAY_SUCCESS);
//            FissionTradeRecord fissionTradeRecordByInfo = fissionTradeRecordService.getFissionTradeRecordByInfo(fissionTradeRecord);
//            if (fissionTradeRecordByInfo == null)
//                return DirectCommonUtil.addResultInfo(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "??????????????????");
//
//            final HttpRequest req = new HttpRequest(HttpResultType.STRING);
//            req.setCharset("utf-8");
//            req.setMethod(HttpRequest.METHOD_POST);
//            req.setUrl(pay_host + "/index");
//            TcTrade tcTrade = this.setPayInfo(fissionTradeRecordByInfo, payMethod, notifyUrl);
//            req.setQueryString(JSON.toJSONString(tcTrade));
//            HttpResponse res = null;
//            String result = null;
//            PaymentResult payResult = null;
//            try {
//                res = httpHandler.execute(req);
//                String responseResultString = res.getStringResult();
//                result = DesUtils.decrypt(responseResultString);
//                payResult = JSON.parseObject(result, PaymentResult.class);
//            } catch (Exception e) {
//                StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "??????error", orderNo, e);
//            }
//            if (payResult != null && !payResult.getCode().equals(10000)) {
//                dto.setCode(payResult.getCode());
//                dto.setMsg(payResult.getMessage());
//            } else {
//                OrderBackInfo orderBackInfo = null;
//                if (payResult.getJson() != null) {
//                    orderBackInfo = JSON.parseObject(payResult.getJson(), OrderBackInfo.class);
//                } else {
//                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "???????????????????????????", orderNo + "-" + tradeType + "-" + payMethod);
//                    dto.setMsg("???????????????????????????");
//                }
//                updateOrderStatus(userId, tcTrade, orderBackInfo);
//            }
//        } catch (Exception e) {
//            dto = DirectCommonUtil.addError("PayServiceImpl", "refund", "?????? error", e);
//        }
//        return dto;
//    }

    /**
     * @param :
     * @return :
     * @description : ????????????????????????
     * @author : fxq
     * @date : 2020/11/20 17:35
     */
    public ResultDto updateOrderStatus(Integer userId, TcTrade tcTrade, OrderBackInfo orderBackInfo) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        try {
            BaseResponseDto<Boolean> orderresponseDto = iOrderService.directRefund(tcTrade.getOrderNo(), userId);
            if (!orderresponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "???????????????????????? ???????????????????????????", JSON.toJSONString(tcTrade), new Exception("????????????????????????"));
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("????????????????????????");
                return dto;
            }
            FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
            record.setOrderNo(tcTrade.getOrderNo());
            record.setOptUserId(userId);
            record.setOrderStatus(FissionGoodsOrderStatus.REFUND_SUCCESS.getType());
            int n = fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(record);
            if (n == 0) {
                StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "???????????????????????? ??????????????????????????????", JSON.toJSONString(tcTrade), new Exception("??????????????????????????????"));
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("??????????????????????????????");
                return dto;
            }
            //???????????????
            FissionTradeRecord tradeRecord = new FissionTradeRecord();
            tradeRecord.setOrderNo(tcTrade.getOrderNo());
            tradeRecord.setOrderTradeId(tcTrade.getTradeBusiness());
            tradeRecord.setTradeAmount(tcTrade.getAmount());
            tradeRecord.setTradeRemark(tcTrade.getContent());
            tradeRecord.setTradeNo(orderBackInfo.getResId());
            tradeRecord.setPayTime(orderBackInfo.getBizTime());
            tradeRecord.setTradeType(GlobalConstants.FissionTradeType.GOODS_REFUND.getCode());
            int i = fissionTradeRecordService.insertSelective(tradeRecord);
        } catch (Exception e) {
            dto.setCode(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode());
            dto.setMsg("????????????????????????error");
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "PayServiceImpl", "????????????????????????error", JSON.toJSONString(tcTrade), e);
        }
        return dto;
    }

    private TcTrade setPayInfo(FissionTradeRecord fissionTradeRecordByInfo, String payMethod, String notifyUrl) {
        TcTrade tcTrade = new TcTrade();
        tcTrade.setTradeId(fissionTradeRecordByInfo.getSerialNo());
        tcTrade.setOrderNo(fissionTradeRecordByInfo.getOrderNo());
        tcTrade.setContent(fissionTradeRecordByInfo.getTradeRemark());
        tcTrade.setNotifyUrl(notifyUrl);
        tcTrade.setAmount(fissionTradeRecordByInfo.getTradeAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        tcTrade.setPayType("02");
        tcTrade.setRemark("??????");
        tcTrade.setPayMethod(payMethod);
        String tradeBusiness = Globals.orderTradeByTime("fission_user_refund_", fissionTradeRecordByInfo.getOrderNo());
        tcTrade.setTradeBusiness(tradeBusiness);
        return tcTrade;
    }

    protected static Gson gson = new Gson();
    private static final HttpProtocolHandler httpHandler = HttpProtocolHandler.getInstance();

    /**
     * @param :
     * @return :
     * @description : ????????????
     * @author : fxq
     * @date : 2020/11/24 11:48
     */
    public Map<String, Object> refundOrderNotice(OrderBackInfo backInfo) throws Exception {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl", "refundOrderNotice", "???????????? ing????????????", JSON.toJSONString(backInfo));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date date = new Date();
        FissionTradeRecord fissionTradeRecord = new FissionTradeRecord();
        fissionTradeRecord.setSerialNo(backInfo.getSerialNo());
        List<FissionTradeRecord> recordList = fissionTradeRecordService.getFissionTradeRecordList(fissionTradeRecord);
        //????????????????????????
        if (CollectionUtils.isEmpty(recordList)) {
            //??? orderTradeId ????????????
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl", "refundOrderNotice", "???????????? ??????????????????????????????????????????????????????", JSON.toJSONString(backInfo));
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //???????????????????????????????????????
        BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
        baseQueryRequestDto.setQuery(recordList.get(0).getOrderNo());
        BaseResponseDto<CommodityOrderResponseDto> orderResponseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, CommodityOrderResponseDto.class);
        //????????????
        if (!orderResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            //??? orderTradeId ????????????
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl", "refundOrderNotice", "???????????? ????????????", JSON.toJSONString(backInfo));
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //??????????????????
        if (orderResponseDto.getData().getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.SUCCESS)) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl",
                    "refundOrderNotice", "???????????? ???????????????", JSON.toJSONString(orderResponseDto.getData()));
            resultMap.put("code", 10000);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //?????????????????????
        if (!orderResponseDto.getData().getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.WAIT_SELLER_AGREE)) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl",
                    "refundOrderNotice", "???????????? ??????????????????????????????", JSON.toJSONString(orderResponseDto.getData()));
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //??????????????????
        BaseResponseDto<Boolean> responseDto = iOrderService.refund(recordList.get(0).getOrderNo(), 0);
        if (!orderResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "PayServiceImpl", "refundOrderNotice", "???????????? ??????????????????", recordList.get(0).getOrderNo());
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //?????? ??????????????????????????? ?????? 
        FissionGoodsOrderRecord goodsOrderRecord = new FissionGoodsOrderRecord();
        goodsOrderRecord.setOrderNo(orderResponseDto.getData().getOrderCode());
        goodsOrderRecord.setOrderStatus(FissionGoodsOrderStatus.REFUND_SUCCESS.getType());
        fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(goodsOrderRecord);
        //???????????????
        FissionTradeRecord tradeRecord = new FissionTradeRecord();
        tradeRecord.setOrderNo(orderResponseDto.getData().getOrderCode());
        tradeRecord.setOrderTradeId(backInfo.getRefundBusiness());
        tradeRecord.setTradeAmount(orderResponseDto.getData().getOrderMoney());
        tradeRecord.setTradeRemark(backInfo.getContent());
        tradeRecord.setTradeNo(backInfo.getResId());
        tradeRecord.setPayTime(backInfo.getBizTime());
        tradeRecord.setTradeType(GlobalConstants.FissionTradeType.GOODS_REFUND.getCode());
        int i = fissionTradeRecordService.insertSelective(tradeRecord);
        resultMap.put("code", 10000);
        resultMap.put("id", backInfo.getId());
        return resultMap;
    }


}
