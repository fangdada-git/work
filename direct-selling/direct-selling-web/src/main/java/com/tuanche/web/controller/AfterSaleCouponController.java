package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.certificate.api.api.IUserCertificateService;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleCouponService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.dto.AfterSaleCouponDto;
import com.tuanche.directselling.dto.AfterSaleOrderCouponDto;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.web.util.CommonLogUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/afterSale/coupon")
public class AfterSaleCouponController {

    @Reference
    private AfterSaleCouponService afterSaleCouponService;
    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private IUserCertificateService iUserCertificateService;
    @Reference
    private UserBaseService userBaseService;

    @RequestMapping("/toCouponListPage")
    public String toCouponListPage(ModelMap modelMap, String orderCode) {
        //订单记录
        List<AfterSaleOrderRecord> orderRecordList = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderCode));
        AfterSaleOrderCouponDto orderCouponDto = new AfterSaleOrderCouponDto();
        if (CollectionUtils.isNotEmpty(orderRecordList)) {
            BeanUtils.copyProperties(orderRecordList.get(0), orderCouponDto);
            //商品信息
            BaseResponseDto<CommodityDetailBusinessResponseDto> commodity = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(orderCouponDto.getGoodsId());
            if (commodity !=null && commodity.getData()!=null) {
                orderCouponDto.setGoodsName(commodity.getData().getCommodityName());
            }
            Map<String, Integer> map = afterSaleCouponService.getOrderCouponCount(orderCode,orderCouponDto.getActivityId(), orderCouponDto.getGoodsId(), orderCouponDto.getOrderType());
            orderCouponDto.setCouponCountSum(map.get("couponCountSum"));
            orderCouponDto.setCouponCount(map.get("couponCount"));
            orderCouponDto.setCouponGiveOutCount(map.get("couponGiveOutCount"));
            orderCouponDto.setCouponUseCount(map.get("couponUseCount"));
        }
        modelMap.addAttribute("orderCouponDto",orderCouponDto);
        return "after_sale/order/order_coupon_list";
    }

    @RequestMapping("/getOrderCouponList")
    @ResponseBody
    public PageResult getOrderCouponList(PageResult<AfterSaleCouponDto> pageResult, AfterSaleCouponRecord afterSaleCouponRecord) {
        afterSaleCouponRecord.setCouponStatusList(Arrays.asList(AfterSaleConstants.CouponStatus.USE_NON.getCode(), AfterSaleConstants.CouponStatus.USE.getCode()));
        PageResult<AfterSaleCouponDto> result = afterSaleCouponService.getAfterSaleCouponListByPage(pageResult, afterSaleCouponRecord);
        return result;
    }

    /**
      * @description : 核销待发券-发券
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/12/20 11:00
      */
    @RequestMapping("/scanGetCoupon")
    @ResponseBody
    public ResultDto scanGetCoupon(HttpServletRequest request, String orderCode, Integer couponGiveOutCount) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        dto.setMsg("发券成功");
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        CommonLogUtil.addInfo("AfterSaleCouponController","核销待发券-发券 start", orderCode +"--"+xxlUser.getId()+":"+xxlUser.getEmpName());
        try {
            ResultDto resultDto = afterSaleCouponService.scanGetCoupon(orderCode, AfterSaleConstants.CouponType.DEDUCTION, AfterSaleConstants.CouponStatus.USE_NON);
            if (!resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                dto.setCode(resultDto.getCode());
                dto.setMsg(resultDto.getMsg());
                return dto;
            }
        }catch (Exception e) {
            CommonLogUtil.addError("AfterSaleCouponController","核销待发券-发券 error", e);
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(StatusCodeEnum.ERROR.getText());
        }
        return dto;
    }

    /**
      * @description : 补发券
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/12/20 11:32
      */
    @RequestMapping("/supplyAgainCoupon")
    @ResponseBody
    public ResultDto supplyAgainCoupon(HttpServletRequest request, Integer activityId, String orderCode, Integer couponCountSum) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        dto.setMsg("补发券成功");
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        CommonLogUtil.addInfo("AfterSaleCouponController","补发券 start", activityId+"--"+orderCode +"--"+xxlUser.getId()+":"+xxlUser.getEmpName());
        try {
            if (couponCountSum!=null && couponCountSum==0) {
                ResultDto giveOutCoupon = afterSaleCouponService.giveOutCoupon(orderCode, AfterSaleConstants.CouponGiveOutType.USER_DEDUCTION);
                if (!giveOutCoupon.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                    dto.setCode(giveOutCoupon.getCode());
                    dto.setMsg(giveOutCoupon.getMsg());
                    return dto;
                }
                ResultDto scanGetCoupon = afterSaleCouponService.scanGetCoupon(orderCode, AfterSaleConstants.CouponType.DEDUCTION, AfterSaleConstants.CouponStatus.USE_NON);
                if (!scanGetCoupon.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                    dto.setCode(scanGetCoupon.getCode());
                    dto.setMsg(scanGetCoupon.getMsg());
                    return dto;
                }
            } else {
                ResultDto resultDto = afterSaleCouponService.supplyAgainCoupon(activityId, orderCode, null);
                if (!resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                    dto.setCode(resultDto.getCode());
                    dto.setMsg(resultDto.getMsg());
                    return dto;
                }
            }
        }catch (Exception e) {
            CommonLogUtil.addError("AfterSaleCouponController","补发券 error", e);
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(StatusCodeEnum.ERROR.getText());
        }
        return dto;
    }

    /**
      * @description : 核销卡券
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/12/22 18:14
      */
    @RequestMapping("/checkedCoupon")
    @ResponseBody
    public ResultDto checkedCoupon(HttpServletRequest request, Integer checkedSalesId, Integer userCouponId) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.DATA_IS_WRONG.getCode());
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        CommonLogUtil.addInfo("AfterSaleCouponController","核销卡券 start", checkedSalesId+"--"+userCouponId +"--"+xxlUser.getId()+":"+xxlUser.getEmpName());
        try {
            AfterSaleCouponDto coupon = afterSaleCouponService.getAfterSaleCouponByUserCouponId(userCouponId, null);
            if (coupon==null || coupon.getId()==null) {
                dto.setMsg("卡券核销无数据");
                return dto;
            }
            //卡券系统核销
            iUserCertificateService.useCertificate(Long.parseLong(coupon.getUserPhone()), userCouponId, null);
            //更改卡券记录状态
            CsUser user = userBaseService.getCsUserById(checkedSalesId);
            Date date = new Date();
            AfterSaleCouponRecord couponRecord = new AfterSaleCouponDto();
            couponRecord.setUserCouponId(userCouponId);
            couponRecord.setCheckedDate(date);
            couponRecord.setCheckedSalesId(checkedSalesId);
            couponRecord.setCheckedDealerId(user.getDealerId());
            couponRecord.setCouponStatus(AfterSaleConstants.CouponStatus.USE.getCode());
            Integer update = afterSaleCouponService.updateAfterSaleCouponByUserCouponId(couponRecord);
            if (update==null || update==0) {
                dto.setMsg("卡券核销失败");
                return dto;
            }
        }catch (Exception e) {
            CommonLogUtil.addError("AfterSaleCouponController","补发券 error", e);
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(e.getMessage());
            return dto;
        }
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        dto.setMsg("核销成功");
        return dto;
    }

}
