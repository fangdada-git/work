package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.commons.util.DataTableResult;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordTotalByCarDto;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import com.tuanche.directselling.model.AfterSaleUser;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.DateUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.api.WxTemplateMessageBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.dto.WxDataDto;
import com.tuanche.manubasecenter.dto.WxTemplateInfoDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.request.OrderCheckoutRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderExtendBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderExtendResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.ExportExcelCallback;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/afterSale/order")
public class AfterSaleOrderController {

    @Reference
    private IOrderService iOrderService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private AfterSaleActivityService afterSaleActivityService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private UserBaseService userBaseService;
    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private AfterSaleCouponService afterSaleCouponService;
    @Reference
    WxTemplateMessageBaseService wxTemplateMessageBaseService;
    @Reference
    AfterSaleUserService afterSaleUserService;
    @Reference
    AfterSaleOrderWechatworkConcactService afterSaleOrderWechatworkConcactService;
    @Value("${refund_template_id}")
    private String refund_template_id;
    @Value("${aftersale_appid}")
    private String aftersale_appid;
    //?????????????????????????????????????????????id eg : ,1,2,3,
    @Value("${plain_user_phone_id}")
    private String plain_user_phone_id;

    @RequestMapping("/toList")
    public String toFissionGoodsList() {
        return "after_sale/order/list";
    }

    /**
     * ????????????
     * @author HuangHao
     * @CreatTime 2021-12-15 15:17
     * @param
     * @return java.lang.String
     */
    @RequestMapping("/sub_account_list_page")
    public String sub_account_list_page() {
        return "after_sale/order/sub_account_list";
    }


    @RequestMapping("/getOrderList")
    @ResponseBody
    public PageResult getOrderList(HttpServletRequest request,PageResult<AfterSaleOrderRecordDto> pageResult, AfterSaleOrderRecordDto afterSaleOrderRecord) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (plain_user_phone_id.indexOf(","+xxlUser.getId()+",")!=-1) {
            afterSaleOrderRecord.setDesensitizationPhone(false);
        } else {
            afterSaleOrderRecord.setDesensitizationPhone(null);
        }
        if (!StringUtil.isEmpty(afterSaleOrderRecord.getWwUserId())) {
            List<String> orderCodeList = afterSaleOrderWechatworkConcactService.getOrderByMaxWwUserId(afterSaleOrderRecord.getWwUserId(), afterSaleOrderRecord.getActivityId());
            if (CollectionUtils.isNotEmpty(orderCodeList)) afterSaleOrderRecord.setOrderCodeList(orderCodeList);
        }
        PageResult<AfterSaleOrderRecordDto> result = afterSaleOrderRecordService.getOrderSharerAndAgentListByPage(pageResult, afterSaleOrderRecord);
        if (result!=null && CollectionUtils.isNotEmpty(result.getData())) {
            setWxUnionNameAndWechatworkConcact(result.getData());
        }
        result.setCode("0");
        return result;
    }

    @RequestMapping("/orderListExport")
    public void orderListExport(HttpServletRequest request, HttpServletResponse response, AfterSaleOrderRecordDto afterSaleOrderRecord) {
//        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
//        if (plain_user_phone_id.indexOf(","+xxlUser.getId()+",")!=-1) {
//            afterSaleOrderRecord.setDesensitizationPhone(false);
//        } else {
//            afterSaleOrderRecord.setDesensitizationPhone(true);
//        }
        Map<String, ExportExcelCallback> callBackMap = new HashMap<>(16);
        callBackMap.put("OrderType", (ExportExcelCallback<AfterSaleOrderRecordDto>) object -> {
            if (object.getOrderType() == 1) {
                return "?????????";
            }
            if (object.getOrderType() == 2) {
                return "?????????";
            }
            return "";
        });
        callBackMap.put("LicencePlate", (ExportExcelCallback<AfterSaleOrderRecordDto>) object -> {
            if (object.getKeepOnRecordUser()) {
                return object.getLicencePlate() + "??";
            }
            return object.getLicencePlate();
        });
        callBackMap.put("Channel", (ExportExcelCallback<AfterSaleOrderRecordDto>) object -> {
            if (object.getChannel() == 1) {
                return "?????????";
            }
            if (object.getChannel() == 2) {
                return "????????????";
            }
            if (object.getChannel() == 3) {
                return "????????????";
            }
            return "";
        });
        List<AfterSaleOrderRecordDto> result = afterSaleOrderRecordService.getAfterSaleOrderRecordDtoList(afterSaleOrderRecord);
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("OrderCode", "????????????");
        titleValueMap.put("ActivityName", "????????????");
        titleValueMap.put("DealerName", "???????????????");
        titleValueMap.put("GoodsName", "????????????");
        titleValueMap.put("OrderType", "??????");
        titleValueMap.put("LicencePlate", "????????????");
        titleValueMap.put("CsName", "??????");
        titleValueMap.put("UserPhone", "???????????????");
        titleValueMap.put("UserTypeFormat", "????????????");
        titleValueMap.put("OrderMoney", "????????????");
        titleValueMap.put("OrderStatusName", "????????????");
        titleValueMap.put("Channel", "??????");
        titleValueMap.put("PayTime", "????????????");
        ExportExcel.exportExcel("????????????????????????.xls", titleValueMap, callBackMap, result, request, response);
    }
    //??????????????????
    @RequestMapping("/subAccount/excelexport")
    public void orderSubAccountExport(HttpServletRequest request, HttpServletResponse response, AfterSaleOrderRecordDto afterSaleOrderRecord) {
        List<AfterSaleOrderRecordDto> result = afterSaleOrderRecordService.getAfterSaleOrderRecordDtoList(afterSaleOrderRecord);
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("ActivityName", "????????????");
        titleValueMap.put("GoodsName", "????????????");
        titleValueMap.put("LicencePlate", "????????????");
        titleValueMap.put("CsName", "??????");
        titleValueMap.put("OrderMoney", "????????????");
        titleValueMap.put("PayTime", "????????????");
        titleValueMap.put("SubAccountStatusFormat", "????????????");
        titleValueMap.put("SubAccountDesc", "????????????");
        titleValueMap.put("SubAccountAmount", "????????????");
        titleValueMap.put("SubAccountTime", "????????????");
        titleValueMap.put("SubAccountTransactionDetailId", "???????????????");
        titleValueMap.put("SubAccountReturnTime", "????????????");
        titleValueMap.put("SubAccountReturnNo", "???????????????");
        ExportExcel.exportExcel("??????????????????????????????.xls", titleValueMap, result, request, response);
    }

    private CommodityOrderExtendBusinessQueryRequestDto setOrderStatus(CommodityOrderExtendBusinessQueryRequestDto query, AfterSaleOrderRecord saleOrderRecord) {
        if (saleOrderRecord.getOrderStatus() != null) {
            if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.UNPAID.getCode())) {
                query.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_BUYER_PAY);
                query.setIsExpired(0);
            } else if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.PAID.getCode())) {
                query.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_SELLER_SEND_GOODS);
            } else if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.CHECKOUT.getCode())) {
                query.setTradeStatusEnum(OrderTradeStatusEnum.TRADE_FINISHED);
            } else if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode()) || saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode())) {
                query.setRefundStatusEnum(OrderTradeRefundStatusEnum.SUCCESS);
            } else if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.APPLY_REFUND.getCode())) {
                query.setRefundStatusEnum(OrderTradeRefundStatusEnum.WAIT_SELLER_AGREE);
            } else  if (saleOrderRecord.getOrderStatus().equals(AfterSaleConstants.OrderStatus.TRADE_CLOSED.getCode())) {
                query.setTradeStatusEnum(OrderTradeStatusEnum.TRADE_CLOSED);
                query.setIsExpired(0);
            }
        }
        return query;
    }

    @RequestMapping("/batchRefund")
    @ResponseBody
    public ResultDto batchRefund(Integer id) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        String msg = "";
        String keyWord = null;
        try {
            AfterSaleOrderRecord order = afterSaleOrderRecordService.getById(id);
            if(order==null || order.getOrderStatus()==null || order.getOrderStatus()<3){
                dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
                dto.setMsg("???????????????");
                return dto;
            }
            String orderCode = order.getOrderCode();
            Integer userId = order.getUserId();
            keyWord = "??????????????????"+orderCode+"-tk";
            CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"??????",  null);
            if(order.getOrderStatus() == AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode()
                    || order.getOrderStatus() == AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode()){
                dto.setCode(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode());
                dto.setMsg("????????????????????????????????????");
                return dto;
            }
            BaseResponseDto<Boolean> refund = null;
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(order.getUserType().equals(AfterSaleConstants.OrderType.SYNTHESIZE_CARD.getCode())
                && (order.getSubAccountStatus().equals(AfterSaleConstants.OrderSubAccountStatus.STATUS_4.getCode())
                    || order.getSubAccountStatus().equals(AfterSaleConstants.OrderSubAccountStatus.STATUS_6.getCode())
                    || order.getSubAccountStatus().equals(AfterSaleConstants.OrderSubAccountStatus.STATUS_8.getCode()))){
                CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"?????????????????????",  null);
                //???????????????
                refund = iOrderService.directRefund(orderCode, userId,1);
            }else{
                CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"????????????????????????",  null);
                //??????????????????
                refund = iOrderService.directRefund(orderCode, userId);
            }
            CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"?????????????????????????????????",  refund);
            if (!refund.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("????????????"+orderCode+"???"+refund.getMsg()+"<br>");
            } else {
                dto.setCode(StatusCodeEnum.SUCCESS.getCode());
                dto.setMsg("???????????????????????????????????????");
                /*Date date = new Date();
                //??????????????????
                AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                record.setOrderCode(orderCode);
                record.setOrderStatus(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode());
                afterSaleOrderRecordService.updateByOrderCode(record);
                //???????????????
                afterSaleCouponService.delCoupon(orderCode, Arrays.asList(AfterSaleConstants.CouponType.GIFT.getCode()));
                //??????????????????
                List<AfterSaleOrderRecord> recordList = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
                AfterSaleOrderRecord record1 = recordList.get(0);
                CsCompanyDetailDto company = companyBaseService.getCsCompanyDetail(record1.getDealerId());
                WxDataDto dataDto = new WxDataDto("???????????????????????????????????????????????????????????????",
                        orderCode,
                        company.getCompanyName(),
                        "????????????",
                        record1.getOrderMoney().toString(),
                        DateUtil.formart(date, DateUtil.FORMART_YMD_HMS),
                        company.getCompanyName()+" ?????????"+company.getTel()+" ?????????"+company.getAddress());
                WxTemplateInfoDto templateInfoDto = new WxTemplateInfoDto(record1.getUserWxOpenId(), refund_template_id,null, null, dataDto);
                com.tuanche.commons.util.ResultDto resultDto = wxTemplateMessageBaseService.sendNew(aftersale_appid, templateInfoDto);
                CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"???????????????????????????", JSON.toJSONString(resultDto));
                dto.setMsg("????????????");*/
            }

        } catch (Exception e) {
            dto = DirectCommonUtil.addError("AfterSaleOrderController",keyWord +"?????????batchRefund????????????????????????", "?????? error", e);
        }
        return dto;
    }

    /**
      * @description : ??????????????????????????????
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/22 16:28
      */
    @RequestMapping("/toCarOrderPage")
    public String toCarOrderPage(ModelMap modelMap, AfterSaleOrderRecordTotalByCarDto saleOrderRecordTotalByCarDto) {
        modelMap.addAttribute("saleOrderRecordTotalByCarDto", saleOrderRecordTotalByCarDto);
        return "after_sale/order/car_list";
    }

    /**
      * @description : ??????????????????????????????
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/22 16:28
      */
    @RequestMapping("/carOrderList")
    @ResponseBody
    public PageResult carOrderList(PageResult<AfterSaleOrderRecordTotalByCarDto> pageResult, AfterSaleOrderRecordTotalByCarDto saleOrderRecordTotalByCarDto) {
        DataTableResult<AfterSaleOrderRecordTotalByCarDto> dataTyable = new DataTableResult<>();
        PageResult<AfterSaleOrderRecordTotalByCarDto> result = getAfterSaleOrderTotalByCar(pageResult, saleOrderRecordTotalByCarDto);
        return result;
    }

    /**
      * @description : ??????????????????????????????
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/22 16:28
      */
    @RequestMapping(value = "/carOrderExcelexport")
    public void carOrderExcelexport(HttpServletRequest request, HttpServletResponse response, AfterSaleOrderRecordTotalByCarDto saleOrderRecordTotalByCarDto) {
        PageResult<AfterSaleOrderRecordTotalByCarDto> page = new PageResult<>();
        page.setPage(1);
        page.setLimit(1000);
        PageResult<AfterSaleOrderRecordTotalByCarDto> pageResult = getAfterSaleOrderTotalByCar(page, saleOrderRecordTotalByCarDto);
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("CbName", "??????");
        titleValueMap.put("CsName", "??????");
        titleValueMap.put("OrderCount", "????????????");
        ExportExcel.exportExcel("??????????????????.xls", titleValueMap, pageResult.getData(), request, response);
    }

    private PageResult<AfterSaleOrderRecordTotalByCarDto> getAfterSaleOrderTotalByCar (PageResult<AfterSaleOrderRecordTotalByCarDto> pageResult,AfterSaleOrderRecordTotalByCarDto orderRecordTotalByCarDto) {
        return afterSaleOrderRecordService.getAfterSaleOrderTotalByCar(pageResult, orderRecordTotalByCarDto);
    }

    private void setWxUnionNameAndWechatworkConcact (List<AfterSaleOrderRecordDto> list) {
        List<AfterSaleUser> userList = new ArrayList<>();
        List<String> orderCodes = new ArrayList<>();
        list.forEach(v->{
            AfterSaleUser user = new AfterSaleUser();
            user.setActivityId(v.getActivityId());
            user.setUserWxUnionId(v.getUserWxUnionId());
            userList.add(user);
            if (v.getChannel().equals(AfterSaleConstants.ChannelType.TYPE_2.getCode())) orderCodes.add(v.getOrderCode());
        });
        Map<String, AfterSaleUser> afterSaleUserMap = afterSaleUserService.getUserMapByActivityIdAndUnionId(userList);
        Map<String, AfterSaleOrderWechatworkConcact> mapByOrderCode = afterSaleOrderWechatworkConcactService.getMapByOrderCode(orderCodes);
        list.forEach(v->{
            if (afterSaleUserMap.get(v.getActivityId()+":"+v.getUserWxUnionId())!=null) {
                v.setUserNickName(afterSaleUserMap.get(v.getActivityId()+":"+v.getUserWxUnionId()).getNickName());
            }
            if (mapByOrderCode.get(v.getOrderCode())!=null) {
                v.setWwUserName(mapByOrderCode.get(v.getOrderCode()).getWwUserName());
                v.setWwCreateDt(mapByOrderCode.get(v.getOrderCode()).getWwCreateDt());
            }
        });
    }

    @RequestMapping("/toCheckedOrderPage")
    public String toCheckedOrderPage(ModelMap modelMap, String orderCode) {
        BaseQueryRequestDto<String> requestDto = new BaseQueryRequestDto<>();
        requestDto.setQuery(orderCode);
        requestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
        BaseResponseDto<CommodityOrderExtendResponseDto> orderByOrderCode = iOrderService.getOrderByOrderCode(requestDto, CommodityOrderExtendResponseDto.class);
        BaseResponseDto<CommodityDetailBusinessResponseDto> commodity = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(orderByOrderCode.getData().getCommodityId());
        CsCompany company = companyBaseService.getCsCompanyById(orderByOrderCode.getData().getDealerId());
        modelMap.addAttribute("goodsName", commodity !=null && commodity.getData()!=null ? commodity.getData().getCommodityName() : "");
        modelMap.addAttribute("dealerId", orderByOrderCode.getData().getDealerId());
        modelMap.addAttribute("dealerName", company.getCompanyName());
        modelMap.addAttribute("checkoutCode", orderByOrderCode.getData().getCheckoutCode());
        return "after_sale/order/checked_order";
    }

    /**
      * @description : ????????????
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/12/15 21:49
      */
    @RequestMapping("/checkedOrder")
    @ResponseBody
    public ResultDto checkedOrder(HttpServletRequest request, Integer checkedSalesId, String checkoutCode) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        CommonLogUtil.addInfo("AfterSaleOrderController","???????????? start", checkedSalesId +"--"+checkoutCode+"--"+xxlUser.getId()+":"+xxlUser.getEmpName());
        try {
            BaseQueryRequestDto<String> queryRequestDto = new BaseQueryRequestDto<>();
            queryRequestDto.setQuery(checkoutCode);
            queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.COMMON);
            BaseResponseDto<CommodityOrderResponseDto> orderByCheckoutCode = iOrderService.getOrderByCheckoutCode(queryRequestDto, CommodityOrderResponseDto.class);
            if (orderByCheckoutCode==null || orderByCheckoutCode.getData()==null || StringUtil.isEmpty(orderByCheckoutCode.getData().getOrderCode())) {
                dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
                dto.setMsg("???????????????");
                return dto;
            }
            CommodityOrderResponseDto order = orderByCheckoutCode.getData();
            if (order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.TRADE_FINISHED)) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("?????????????????????");
                return dto;
            }
            if (!(order.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND) && order.getTradeStatus().equals(OrderTradeStatusEnum.PAY_SUCCESS))) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("??????????????????????????????");
                return dto;
            }
            List<AfterSaleOrderRecord> saleOrderRecordByOrderCodes = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderByCheckoutCode.getData().getOrderCode()));
            if (CollectionUtils.isEmpty(saleOrderRecordByOrderCodes)) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("??????????????????");
                return dto;
            }
            AfterSaleOrderRecord orderRecord = saleOrderRecordByOrderCodes.get(0);
            if (orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) && orderRecord.getKeepOnRecordUser() && !orderRecord.getFinishFissionTask()) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("?????????????????????????????????");
                return dto;
            }
            CsUser user = userBaseService.getCsUserById(checkedSalesId);
            OrderCheckoutRequestDto requestDto = new OrderCheckoutRequestDto();
            requestDto.setCheckoutCode(checkoutCode);
            requestDto.setDealerId(user.getDealerId());
            requestDto.setSellerId(checkedSalesId);
            BaseResponseDto<String> responseDto = iOrderService.checkoutOrder(requestDto);
            if (responseDto==null || !responseDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode()) || !responseDto.getSuccess()) {
                dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
                dto.setMsg("??????????????????");
                return dto;
            }
            AfterSaleOrderRecord record = new AfterSaleOrderRecord();
            record.setOrderCode(responseDto.getData());
            record.setOrderStatus(orderRecord.getOrderType().equals(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode()) ? AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode() : AfterSaleConstants.OrderStatus.CHECKOUT.getCode());
            record.setCheckedDealerId(user.getDealerId());
            record.setCheckedSalesId(user.getId());
            record.setCheckedDate(new Date());
            afterSaleOrderRecordService.updateByOrderCode(record);
            //???????????????
            afterSaleCouponService.giveOutCoupon(responseDto.getData(), AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION);
            //???????????????
            afterSaleCouponService.scanGetCoupon(responseDto.getData(), AfterSaleConstants.CouponType.DEDUCTION, AfterSaleConstants.CouponStatus.USE_NON);
        }catch (Exception e) {
            CommonLogUtil.addError("AfterSaleOrderController","???????????? error", e);
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(StatusCodeEnum.ERROR.getText());
            return dto;
        }
        dto.setMsg("????????????");
        return dto;
    }


}
