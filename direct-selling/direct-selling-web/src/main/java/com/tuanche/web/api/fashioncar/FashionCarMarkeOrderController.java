package com.tuanche.web.api.fashioncar;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.po.MemberPo;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.api.FashionCarMarkeHelperUserService;
import com.tuanche.directselling.dto.FashionCarMarkeOrderDto;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;
import com.tuanche.directselling.utils.FashionCarMarkeConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.order.business.CommodityOrderExtendBusinessWithAddressRequestDto;
import com.tuanche.merchant.business.dto.request.order.car.CommodityOrderCarRelRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.order.OrderCodeWithPaymentResponseDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.TerminalChannelEnum;
import com.tuanche.merchant.pojo.dto.enums.TerminalTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderFormalEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderSourceEnum;
import com.tuanche.web.service.FashionCommonService;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/fashioncar/order")
@RestController
public class FashionCarMarkeOrderController {

    @Autowired
    private PayServiceImpl payServiceImpl;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private FashionCarMarkeHelperUserService fashionCarMarkeHelperUserService;
    @Autowired
    private FashionCommonService fashionCommonService;

    /**
      * @description : 潮车集抵扣券下单
      * @author : fxq
      * @param : 
      * @return : 
      * @date : 2021/9/16 10:57
      */
    @RequestMapping("/submit")
    public TcResponse submit(HttpServletRequest request, FashionCarMarkeOrderDto fashionCarMarkeOrderDto) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "潮车集抵扣券下单 start ", JSON.toJSONString(fashionCarMarkeOrderDto));
        Object result = null;
        if (fashionCarMarkeOrderDto.getPeriodsId()==null || fashionCarMarkeOrderDto.getGoodsId()==null || fashionCarMarkeOrderDto.getCityId()==null
                || StringUtil.isEmpty(fashionCarMarkeOrderDto.getUserName()) || fashionCarMarkeOrderDto.getPaySource()==null
                || StringUtil.isEmpty(fashionCarMarkeOrderDto.getDealerIdStr())) {
            return DirectCommonUtil.returnParamError("AfterSaleOrderController", "submit", "潮车集抵扣券下单参数错误", fashionCarMarkeOrderDto, st);
        }
        MemberPo memberPo = DirectCommonUtil.getMemberPo(request);
        String lockKey = "fashion:car:marke:order:lock:" +fashionCarMarkeOrderDto.getPeriodsId()+ ":" + memberPo.getId()+":"+fashionCarMarkeOrderDto.getGoodsId();
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在支付", st,
                    "FashionCarMarkeOrderController", "submit", "");
        }
        try {
            //查询商品信息
            BaseResponseDto<CommodityDetailBusinessResponseDto> commodityResult = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(fashionCarMarkeOrderDto.getGoodsId());
            if (commodityResult==null || commodityResult.getData()==null || commodityResult.getData().getCommodityId()==null) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品异常", st, "AfterSaleOrderController", "submit", fashionCarMarkeOrderDto);
            }
            CommodityDetailBusinessResponseDto commodity = commodityResult.getData();
            if (commodity.getSoldNumber()>=commodity.getCommodityCount()) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品已售罄", st, "AfterSaleOrderController", "submit", fashionCarMarkeOrderDto);
            }
            Date date = new Date();
            if (date.before(commodity.getUpShelfTime()) || date.after(commodity.getDownShelfTime())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "商品未上架", st, "AfterSaleOrderController", "submit", fashionCarMarkeOrderDto);
            }
            //下单
            CommodityOrderExtendBusinessWithAddressRequestDto orderExtendRequestDto = new CommodityOrderExtendBusinessWithAddressRequestDto();
            //商品是否助力成功
            FashionCarMarkeHelperUser helperUser = new FashionCarMarkeHelperUser();
            if (commodity.getNeedHelp()==1 && fashionCarMarkeOrderDto.getUseOriginalPrice()==2) {
                Integer helpNumber = commodity.getHelpNumber();
                helperUser.setPeriodsId(fashionCarMarkeOrderDto.getPeriodsId());
                helperUser.setGoodsId(fashionCarMarkeOrderDto.getGoodsId());
                helperUser.setUserId(memberPo.getId());
                helperUser.setHelperType(FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode());
                helperUser.setBuyFlag(FashionCarMarkeConstants.HelperBuyFlag.HAVE_NOT_PURCHASED.getCode());
                Integer helperUserCount = fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserCount(helperUser);
                if (helperUserCount<helpNumber) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), "未助力成功，请继续邀请助力", st, "AfterSaleOrderController", "submit", fashionCarMarkeOrderDto);
                } else {
                    orderExtendRequestDto.setUseOriginalPrice(false);
                    orderExtendRequestDto.setOrderMoney(commodity.getCommodityPrice());
                }
            } else {
                orderExtendRequestDto.setUseOriginalPrice(true);
                orderExtendRequestDto.setOrderMoney(commodity.getOriginalPrice());
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            orderExtendRequestDto.setCommodityId(fashionCarMarkeOrderDto.getGoodsId());
            orderExtendRequestDto.setNum(1);
            orderExtendRequestDto.setUserNick(fashionCarMarkeOrderDto.getUserName());
            orderExtendRequestDto.setUserPhone(memberPo.getPhone());
            orderExtendRequestDto.setDedupKey(uuid);
            orderExtendRequestDto.setCityId(fashionCarMarkeOrderDto.getCityId());
            orderExtendRequestDto.setBusinessTypeEnum(BusinessTypeEnum.CAR_SET);
            orderExtendRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            String orderTitle = StringUtil.isEmpty(commodity.getCommodityName())
                    ? "商品购买"
                    : (commodity.getCommodityName().length() <= 12 ? commodity.getCommodityName() : commodity.getCommodityName().substring(0, 12) + "…");
            orderExtendRequestDto.setOrderTitle(orderTitle);
            orderExtendRequestDto.setTerminal(fashionCarMarkeOrderDto.getPaySource()==1 ? TerminalTypeEnum.WAP : TerminalTypeEnum.APP);
            orderExtendRequestDto.setOpenId(fashionCarMarkeOrderDto.getUserWxOpenId());
            orderExtendRequestDto.setOrderChannel(fashionCarMarkeOrderDto.getChannel());
            orderExtendRequestDto.setOrderValidDuration(10);
            orderExtendRequestDto.setTerminalChannel(TerminalChannelEnum.WECHAT);
            orderExtendRequestDto.setBusinessTypeEnum(BusinessTypeEnum.CAR_SET);
            orderExtendRequestDto.setOrderFormal(commodity.isPost() ? OrderFormalEnum.POST : OrderFormalEnum.CHECKOUT);
            orderExtendRequestDto.setOrderSource(OrderSourceEnum.FISSION_ACTIVITY.getSourceId());
            orderExtendRequestDto.setUserId(memberPo.getId());
            orderExtendRequestDto.setReturnUrl(fashionCarMarkeOrderDto.getReturnUrl());
            orderExtendRequestDto.setDealerIds(fashionCarMarkeOrderDto.getDealerIdStr());
            if (fashionCarMarkeOrderDto.getCbId()!=null || fashionCarMarkeOrderDto.getCsId()!=null || fashionCarMarkeOrderDto.getCarId()!=null) {
                List<CommodityOrderCarRelRequestDto> carList = new ArrayList<>();
                CommodityOrderCarRelRequestDto car  = new CommodityOrderCarRelRequestDto();
                car.setCbId(fashionCarMarkeOrderDto.getCbId());
                car.setCsId(fashionCarMarkeOrderDto.getCsId());
                car.setCarId(fashionCarMarkeOrderDto.getCarId());
                carList.add(car);
                orderExtendRequestDto.setCarRelRequestDtoList(carList);
            }
            CarFashionInfoEntityResVo entityResVo = fashionCommonService.getCarFashionInfoEntityResVo(fashionCarMarkeOrderDto.getPeriodsId());
            orderExtendRequestDto.setActivityId(entityResVo.getCarFashionId());
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "潮车集抵扣券下单 调订单中心 ", JSON.toJSONString(orderExtendRequestDto));
            BaseResponseDto<OrderCodeWithPaymentResponseDto> submitOrder = iOrderService.submitOrder(orderExtendRequestDto);
            if (submitOrder==null || submitOrder.getData()==null || StringUtil.isEmpty(submitOrder.getData().getOrderCode())) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.CREATE_FAIL.getCode(), StringUtil.isEmpty(submitOrder.getMsg())?"下单失败":submitOrder.getMsg(), st, "AfterSaleOrderController", "submit", fashionCarMarkeOrderDto);
            }
            result = submitOrder.getData();
            if (commodity.getNeedHelp()==1) {
                fashionCarMarkeHelperUserService.updateHelperUserToBuy(helperUser);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleOrderController", "潮车集抵扣券下单 error", e, st,JSON.toJSONString(fashionCarMarkeOrderDto));
        }finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleOrderController","submit",  "潮车集抵扣券下单 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
    }

}
