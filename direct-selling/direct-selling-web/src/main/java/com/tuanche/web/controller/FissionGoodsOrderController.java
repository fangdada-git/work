package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.broadcast.rpc.service.MsBroadcastRoomBusinessFacadeService;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDictService;
import com.tuanche.directselling.api.FissionGoodsOrderRecordservice;
import com.tuanche.directselling.api.FissionTradeRecordService;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionGoodsOrderDto;
import com.tuanche.directselling.dto.FissionGoodsOrderSoucreDto;
import com.tuanche.directselling.enums.FissionDictType;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionDict;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityVo;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderExtendBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderAggsTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.ExportExcel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/fission/goods/order")
public class FissionGoodsOrderController {

    @Reference
    private IOrderService iOrderService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Autowired
    private PayServiceImpl payServiceImpl;
    @Reference
    private CityBaseService cityBaseService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private UserBaseService userBaseService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private FissionDictService fissionDictService;
    @Reference
    private MsBroadcastRoomBusinessFacadeService msBroadcastRoomBusinessFacadeService;
    @Reference
    private FissionTradeRecordService fissionTradeRecordService;

    @Value("${order_tuanche_host}")
    private String order_tuanche_host;

    @RequestMapping("/toGoodsOrderList")
    public String toFissionGoodsList(ModelMap modelMap) {
        FissionActivity fissionActivity = new FissionActivityDto();
        List<FissionActivityVo> fissionActivityList = fissionActivityService.findFissionActivityList(fissionActivity);
        List<FissionDict> fissionDictList = fissionDictService.getFissionDictByType(FissionDictType.CHANNEL.getType());
        List<FissionGoodsOrderSoucreDto> orderSourceList = fissionGoodsOrderRecordservice.getOrderSource();
        modelMap.addAttribute("orderSourceList", orderSourceList);
        modelMap.addAttribute("fissionDictList", fissionDictList);
        modelMap.addAttribute("fissionActivityList", fissionActivityList);
        return "fission/activity/goods-order-list";
    }

    @RequestMapping("/getLiveList")
    @ResponseBody
    public ResultDto getLiveList(Integer fissionId) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        try {
            if (fissionId==null) return DirectCommonUtil.addParamNull();
            List<JSONObject> liveVoList = fissionGoodsOrderRecordservice.getLiveList(null, null, fissionId);
//                liveVo.get("id"));
//                liveVo.get("roomTitle"));
            if (CollectionUtils.isNotEmpty(liveVoList)) {
                dto.setResult(liveVoList);
            } else {
                dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
                dto.setMsg(StatusCodeEnum.RESULE_DATA_NONE.getText());
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsOrderController","getFissionOrderStatistics", "获取活动下订单统计 error", e);
        }
        return dto;
    }



    @RequestMapping("/getGoodsOrderList")
    @ResponseBody
    public PageResult getFissionGoodsList(PageResult<FissionGoodsOrderDto> pageResult, FissionGoodsOrderDto goodsOrderDto) {
        PageResult page = fissionGoodsOrderRecordservice.getFissionGoodsList(pageResult, goodsOrderDto);
        if (page!=null && CollectionUtils.isNotEmpty(page.getData())) {
            List<FissionGoodsOrderDto> goodsOrderDtoList = page.getData();
            goodsOrderDtoList.forEach(v->{
                if (!StringUtil.isEmpty(v.getUserPhone()) && v.getUserPhone().length()>=7) {
                    v.setUserPhone(v.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                }
            });
        }
        return page;
    }
    
    private List<String> getOrderNoList (FissionGoodsOrderDto goodsOrderDto) {
        List<String> orderNos = new ArrayList<>();
        //查询下单经销商
        if (!StringUtil.isEmpty(goodsOrderDto.getDealerName())) {
            CsCompany company = new CsCompany();
            company.setCompanyName(goodsOrderDto.getDealerName());
            List<CsCompany> csCompanyByName = companyBaseService.getCsCompanyByName(company);
            if (CollectionUtils.isNotEmpty(csCompanyByName)) {
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                List<Integer> dealerIdList = new ArrayList<>();
                csCompanyByName.forEach(v->{
                    dealerIdList.add(v.getId());
                });
                record.setDealerIdList(dealerIdList);
                List<FissionGoodsOrderRecord> fissionGoodsOrderRecordList = fissionGoodsOrderRecordservice.getFissionGoodsOrderRecordList(record);
                fissionGoodsOrderRecordList.forEach(v->{
                    orderNos.add(v.getOrderNo());
                });
            }
        }
        //查询核销经销商
        if (!StringUtil.isEmpty(goodsOrderDto.getCancelDealerName())) {
            CsCompany company = new CsCompany();
            company.setCompanyName(goodsOrderDto.getCancelDealerName());
            List<CsCompany> csCompanyByName = companyBaseService.getCsCompanyByName(company);
            if (CollectionUtils.isNotEmpty(csCompanyByName)) {
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                List<Integer> dealerIdList = new ArrayList<>();
                csCompanyByName.forEach(v->{
                    dealerIdList.add(v.getId());
                });
                record.setCancelDealerIdList(dealerIdList);
                List<FissionGoodsOrderRecord> fissionGoodsOrderRecordList = fissionGoodsOrderRecordservice.getFissionGoodsOrderRecordList(record);
                fissionGoodsOrderRecordList.forEach(v->{
                    orderNos.add(v.getOrderNo());
                });
            }
        }
        return orderNos;
    }

    /**
     * @description : 获取活动下订单统计
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/16 16:29
     */
    @RequestMapping("/getFissionOrderStatistics")
    @ResponseBody
    public ResultDto getFissionOrderStatistics(Integer fissionId, Integer liveId) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        try {
//            if (fissionId==null) return DirectCommonUtil.addParamNull();
            BaseQueryRequestDto<CommodityOrderExtendBusinessQueryRequestDto> requestDto = new BaseQueryRequestDto<>();
            CommodityOrderExtendBusinessQueryRequestDto queryRequestDto = new CommodityOrderExtendBusinessQueryRequestDto();
            queryRequestDto.setActivityId(fissionId);
            queryRequestDto.setLiveId(liveId);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
            requestDto.setQuery(queryRequestDto);
            List<OrderAggsTypeEnum> typeEnums = new ArrayList<>();
            for (OrderAggsTypeEnum value : OrderAggsTypeEnum.values()) {
                typeEnums.add(value);
            }
            requestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            BaseResponseDto<Map<OrderAggsTypeEnum, Object>> mapBaseResponseDto = iOrderService.mapCommodityOrderAggs(requestDto, typeEnums);
            if (!mapBaseResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("活动订单统计错误");
            } else {
                Map<OrderAggsTypeEnum, Object> map = mapBaseResponseDto.getData();
                dto.setResult(map);
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsOrderController","getFissionOrderStatistics", "获取活动下订单统计 error", e);
        }
        return dto;
    }
    
    /**
     * @description : 驳回退款
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/16 16:29
     */
    @RequestMapping("/rejectRefund")
    @ResponseBody
    public ResultDto rejectRefund(HttpServletRequest request, String orderNos) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        dto.setMsg(StatusCodeEnum.SUCCESS.getText());
        try {
            if (StringUtil.isEmpty(orderNos)) return DirectCommonUtil.addParamNull();
            XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
            if (xxlUser == null || xxlUser.getId() == null)  return DirectCommonUtil.addResultInfo(StatusCodeEnum.USER_NOT_LOGGED_IN.getCode(), "请重新登录");
            JSONArray array = (JSONArray) JSONArray.parse(orderNos);
            for (Object obj : array) {
                JSONObject o = (JSONObject) obj;
                String orderNo = (String) o.get("orderNo");
                Integer userId = (Integer) o.get("userId");
                BaseResponseDto<Boolean> responseDto = iOrderService.refuseRefund(orderNo, userId);
                if (!(responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getSuccess())) {
                    return DirectCommonUtil.addResultInfo(StatusCodeEnum.DATA_IS_WRONG.getCode(), "订单状态变更失败");
                }
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                record.setOrderNo(orderNo);
                record.setOrderStatus(FissionGoodsOrderStatus.PAID.getType());
                record.setOptUserId(xxlUser.getId());
                fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(record);
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsOrderController","rejectRefund", "驳回退款 error", e);
        }
        return dto;
    }

    /**
     * @description : 批量退款
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/24 14:06
     */
    @RequestMapping("/batchRefund")
    @ResponseBody
    public ResultDto batchRefund(String orderNos) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        CommonLogUtil.addInfo("FissionPayController","批量退款订单号：",JSONObject.toJSONString(orderNos));
        String msg = "";
        try {
            if (StringUtil.isEmpty(orderNos)) {
                dto = DirectCommonUtil.addParamNull();
            } else {
                JSONArray array = (JSONArray) JSONArray.parse(orderNos);
                List<String> orderCodeList = new ArrayList<>();
                array.forEach(v->{
                    JSONObject o = (JSONObject) v;
                    String orderNo = (String) o.get("orderNo");
                    orderCodeList.add(orderNo);
                });
                Map<String, CommodityOrderResponseDto> map = new HashMap<>();
                PageableRequestDto<? extends CommodityOrderQueryRequestDto> query = new PageableRequestDto<>();
                CommodityOrderQueryRequestDto queryRequestDto = new CommodityOrderQueryRequestDto();
                queryRequestDto.setOrderCodes(orderCodeList);
                query.setServiceTypeEnum(ServiceTypeEnum.COMMON);
                query.setPageSize(orderCodeList.size());
                query.setPageIndex(1);
                BaseResponseDto<PageableDto<CommodityOrderResponseDto>> orderresponseDto = iOrderService.listCommodityOrder(query, CommodityOrderResponseDto.class);
                if (orderresponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && CollectionUtils.isNotEmpty(orderresponseDto.getData().getList())) {
                    orderresponseDto.getData().getList().forEach(v->{
                        map.put(v.getOrderCode(), v);
                    });
                }
                for (Object obj : array) {
                    JSONObject o = (JSONObject) obj;
                    String orderNo = (String) o.get("orderNo");
                    CommodityOrderResponseDto goods = map.get(orderNo);
                    if (goods!=null && goods.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS)
                            && (goods.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) || goods.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.WAIT_SELLER_AGREE))) {
                        Integer userId = (Integer) o.get("userId");
                        BaseResponseDto<Boolean> refund = iOrderService.directRefund(orderNo, userId);
                        if (!refund.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                            msg += "订单编号"+orderNo+"："+refund.getMsg()+"<br>";
                        }
                    } else {
                        msg += "订单编号"+orderNo+"：订单异常<br>";
                    }
                }
            }
            if (!msg.equals("")) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg(msg);
            } else {
                dto.setMsg("退款成功");
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsOrderController","batchRefund", "批量退款 error", e);
        }
        return dto;
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, FissionGoodsOrderDto goodsOrderDto) {
        try {
            PageResult<FissionGoodsOrderDto> pageResult = new PageResult<>();
            pageResult.setPage(1);
            pageResult.setLimit(10000);
            PageResult result = getFissionGoodsList(pageResult,  goodsOrderDto);
            if (CollectionUtils.isNotEmpty(result.getData())) {
                FissionActivity fissionActivityById = fissionActivityService.getFissionActivityById(goodsOrderDto.getFissionActivityId());
                Map<String, String> titleValueMap = new LinkedHashMap<>();
                titleValueMap.put("OrderNo", "订单编号");
                titleValueMap.put("UserPhone", "用户手机号");
                titleValueMap.put("UserName", "用户姓名");
                titleValueMap.put("OrderChannelName", "渠道");
                titleValueMap.put("OrderCreateDtStr", "下单时间");
                titleValueMap.put("PayTime", "支付时间");
                titleValueMap.put("OrderCityName", "下单人城市");
                titleValueMap.put("GoodsName", "商品名称");
                titleValueMap.put("OrderMoney", "订单金额");
                titleValueMap.put("FissionActivityName", "所属活动名称");
                titleValueMap.put("LiveName", "直播间名称");
                titleValueMap.put("WatchLiveName", "是否看过直播");
                titleValueMap.put("SaleName", "下单所属销售");
                titleValueMap.put("DealerName", "下单所属经销商");
                titleValueMap.put("OrderStatusName", "订单状态");
                titleValueMap.put("CancelSaleName", "核销销售");
                titleValueMap.put("CancelDealerName", "核销经销商");
                titleValueMap.put("TradeNo", "微信交易流水号");
                titleValueMap.put("CancelDate", "核销时间");
                titleValueMap.put("RefundTradeNo", "退款交易流水号");
                titleValueMap.put("RefundDate", "退款时间");
                String fileName = "用户订单.xls";
                if (fissionActivityById != null) {
                    fileName = fissionActivityById.getActivityName() + "-" + fileName;
                }
                ExportExcel.exportExcel(fileName, titleValueMap, result.getData(), request, response);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsOrderController","export", "导出 error", e);
        }
    }
    
}
