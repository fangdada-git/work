package com.tuanche.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.DateUtils;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.certificate.api.Constants.BusinessIdEnum;
import com.tuanche.certificate.api.api.ICouponService;
import com.tuanche.certificate.api.dto.input.SingleCouponDto;
import com.tuanche.commons.http.HttpUtils;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.utils.*;
import com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import com.tuanche.framework.base.constant.CommonCode;
import com.tuanche.framework.base.entity.DataTransferObject;
import com.tuanche.framework.base.util.HttpClient;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsDealerExtend;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.*;
import com.tuanche.web.dto.PaymentResult;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import com.tuanche.web.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.time.LocalTime;
import java.util.*;

import static com.tuanche.directselling.utils.AfterSaleConstants.SubAccountVerifyState.STATE_111;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/20
 **/
@Controller
@RequestMapping("/afterSale/activity")
public class AfterSaleActivityController extends BaseController {

    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    UserBaseService userBaseService;
    @Reference
    CsDealerFinancialService csDealerFinancialService;
    @Reference
    CarBaseService carBaseService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private ICouponService iCouponService;
    @Reference
    AfterSaleActivityPackageService packageService;
    @Reference
    private AfterSaleAmountAlarmService afterSaleAmountAlarmService;
    @Reference
    private AfterSaleActivityAccountService accountService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    @Value("${order_tuanche_host}")
    private String order_tuanche_host;
    /**
     * ??????????????????
     * ????????????????????????
     * ?????????????????????
     * ?????????????????????
     * ?????????????????????
     * ??????????????????
     * ???????????????
     * ??????????????????
     */
    @Value("${after.sale.activity.spread.card.url}")
    private String afterSaleActivitySpreadCardUrl;
    @Value("${after.sale.activity.spread.card.coupon.url}")
    private String afterSaleActivitySpreadCardCouponUrl;
    @Value("${after.sale.activity.agent.share.url}")
    private String afterSaleActivityAgentShareUrl;
    @Value("${after.sale.activity.agent.gmv.url}")
    private String afterSaleActivityAgentGmvUrl;
    @Value("${after.sale.activity.teach.data.url}")
    private String afterSaleActivityTeachDataUrl;
    @Value("${after.sale.activity.product.url}")
    private String afterSaleActivityProductUrl;
    @Value("${after.sale.activity.product.coupon.url}")
    private String afterSaleActivityProductCouponUrl;
    @Value("${after.sale.activity.verification.url}")
    private String afterSaleActivityVerificationUrl;
    @Value("${after.sale.activity.new.spread.card.url}")
    private String afterSaleActivityNewSpreadCardUrl;

    /**
     *  ???????????????????????????
     */
    @Value("${after.sale.activity.verfiy.create.order.url}")
    private String afterSaleActivityVerfiyCreateOrderUrl;

    @Value("${after.sale.activity.verfiy.profitSharing}")
    private int profitSharing;

    /**
     * ????????????
     */
    public String CREATE_MERCHANTS_URL = "/index";

    /**
     * ????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/toListPage")
    public String toListPage(ModelMap modelMap) {
        AfterSaleAmountAlarm afterSaleAmountAlarm = afterSaleAmountAlarmService.getAfterSaleAmountAlarm();
        modelMap.addAttribute("amount", afterSaleAmountAlarm==null? 0 :afterSaleAmountAlarm.getAmount());
        return "after_sale/activity/list";
    }

    /**
     * ????????????
     *
     * @param pageResult
     * @param afterSaleActivity
     * @return
     */
    @RequestMapping("/searchActivityList")
    @ResponseBody
    public PageResult searchActivityList(PageResult<AfterSaleActivityDto> pageResult, AfterSaleActivity afterSaleActivity) {
        PageResult pageList = afterSaleActivityService.findAfterSaleActivityByPage(pageResult, afterSaleActivity);
        pageList.setCode("0");
        return pageList;
    }

    /**
     * ???????????????
     * @param modelMap
     * @param pageType 1??????????????????2???????????????
     * @return
     */
    @RequestMapping("/toAddAfterSalePage")
    public String toAddAfterSalePage(ModelMap modelMap, Integer activityId, Integer pageType) {
        AfterSaleActivityDto afterSaleActivityDto;
        if (!Objects.isNull(activityId)) {
            afterSaleActivityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
            CsCompanyDetailDto csCompanyDto = companyBaseService.getCsCompanyDetail(afterSaleActivityDto.getDealerId());
            afterSaleActivityDto.setDealerName(csCompanyDto != null ? csCompanyDto.getCompanyName() : null);
            fillActivityInfo(afterSaleActivityDto.getDealerId(), afterSaleActivityDto);
            BaseResponseDto<CommodityDetailBusinessResponseDto> goodsDetail = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(afterSaleActivityDto.getGoodsId());
            if (!Objects.isNull(goodsDetail) && ResultEnum.SUCCESS.getCode().equals(goodsDetail.getCode()) && !Objects.isNull(goodsDetail.getData())) {
                //????????????
                afterSaleActivityDto.setGoodsName(goodsDetail.getData().getCommodityName());
            }
            if (!Objects.isNull(afterSaleActivityDto.getGiftCouponId())) {
                SingleCouponDto certificate = iCouponService.getCertificateById(afterSaleActivityDto.getGiftCouponId());
                if (certificate != null) {
                    afterSaleActivityDto.setGiftCouponName(certificate.getName());
                }
            }
            CsUser user = new CsUser();
            user.setDealerId(afterSaleActivityDto.getDealerId());
            user.setUlevel(5);
            CsUser manageUser = userBaseService.getCsUser(user);
            afterSaleActivityDto.setManagerName(manageUser != null ? manageUser.getUname() : null);
            afterSaleActivityDto.setDealerImg(csCompanyDto.getDealerImg());
            modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        } else {
            afterSaleActivityDto = new AfterSaleActivityDto();
            afterSaleActivityDto.setBusinessType(1);
            List<AlarmInfo> alarmInfoList = new ArrayList<>();
            alarmInfoList.add(new AlarmInfo());
            afterSaleActivityDto.setAlarmInfoList(alarmInfoList);
            modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        }
        if (pageType == 1) {
            return "after_sale/activity/add-activity";
        } else {
            return "after_sale/activity/view-activity";
        }
    }

    /**
     * ?????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/getAfterSaleDealerFinancial")
    @ResponseBody
    public ResultDto getAfterSaleDealerFinancial(ModelMap modelMap, Integer dealerId) {
        ResultDto resultDto = new ResultDto();
        AfterSaleActivityDto afterSaleActivityDto = new AfterSaleActivityDto();
        fillActivityInfo(dealerId, afterSaleActivityDto);
        resultDto.setResult(afterSaleActivityDto);
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        return resultDto;
    }

    /**
     *  ??????????????????
     * @param dealerId
     * @param afterSaleActivityDto
     */
    private void fillActivityInfo(Integer dealerId, AfterSaleActivityDto afterSaleActivityDto) {
        List<CsDealerFinancial> dealerFinancialList = csDealerFinancialService.getCsDealerFinancialByDealerId(dealerId);
        if (!CollectionUtils.isEmpty(dealerFinancialList)) {
            for (CsDealerFinancial dealerFinancialDto : dealerFinancialList) {
                if (dealerFinancialDto.getAccountType() == 0) {
                    afterSaleActivityDto.setPublicCardBank(dealerFinancialDto.getCardBank());
                    afterSaleActivityDto.setPublicCardNumber(dealerFinancialDto.getCardNumber());
                }
                if (dealerFinancialDto.getAccountType() == 1) {
                    afterSaleActivityDto.setPrivateCardNumber(dealerFinancialDto.getCardNumber());
                    afterSaleActivityDto.setPrivateCardBank(dealerFinancialDto.getCardBank());
                }
            }
        }
        CsDealerExtend brandExtend = new CsDealerExtend();
        brandExtend.setDealerId(dealerId);
        brandExtend.setExtendType(1);
        List<CsDealerExtend> brandExtendList = companyBaseService.getCsDealerExtendList(brandExtend);
        List<Map<String, String>> brandList = new ArrayList<>();
        Map<String, String> brandMap;
        for (CsDealerExtend extend : brandExtendList) {
            brandMap = new HashMap<>(16);
            brandMap.put("name", carBaseService.getBrandName(extend.getDataId()));
            brandMap.put("value", extend.getDataId().toString());
            brandList.add(brandMap);
        }
        afterSaleActivityDto.setBrandList(brandList);
    }

    /**
     * @return com.tuanche.commons.util.ResultDto
     * @description ????????????????????????
     * @date 2020/9/23 16:42
     * @author lvsen
     */
    @RequestMapping("/saveAfterSaleActivity")
    @ResponseBody
    public ResultDto saveAfterSaleActivity(HttpServletRequest request, @RequestBody AfterSaleActivityDto activityDto) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == activityDto || activityDto.getDealerId() == null || activityDto.getGoodsId() == null) {
                return noData();
            }
            AfterSaleActivity afterSaleActivity = new AfterSaleActivity();
            BeanUtils.copyProperties(activityDto, afterSaleActivity);
            afterSaleActivity.setSaleStartTime(DateUtils.stringToDate(activityDto.getSaleStartTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            afterSaleActivity.setSaleEndTime(DateUtils.stringToDate(activityDto.getSaleEndTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            afterSaleActivity.setOfflineConvertStartTime(DateUtils.stringToDate(activityDto.getOfflineConvertStartTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            afterSaleActivity.setOfflineConvertEndTime(DateUtils.stringToDate(activityDto.getOfflineConvertEndTimeStr(), "yyyy-MM-dd HH:mm:ss"));

            // ????????????
            if (afterSaleActivity.getId() == null) {
                afterSaleActivity.setCreateDt(new Date());
                afterSaleActivity.setCreateBy(xxlUser.getId());
            } else {
                afterSaleActivity.setUpdateDt(new Date());
                afterSaleActivity.setUpdateBy(xxlUser.getId());
            }
            if (StringUtils.isNotEmpty(activityDto.getGiftCouponName())) {
                SingleCouponDto singleCouponDto = new SingleCouponDto();
                singleCouponDto.setType(3);
                singleCouponDto.setName(afterSaleActivity.getGiftCouponName());
                singleCouponDto.setHasTotalCount(0);
                singleCouponDto.setMoney(BigDecimal.ZERO);
                singleCouponDto.setReceiveCondition(1);
                singleCouponDto.setComposition(0);
                singleCouponDto.setUseWay(1);
                singleCouponDto.setGetRule(1);
                singleCouponDto.setGetStartTime(afterSaleActivity.getSaleStartTime());
                singleCouponDto.setGetEndTime(afterSaleActivity.getSaleEndTime());
                singleCouponDto.setUseRule(1);
                singleCouponDto.setUseStartTime(afterSaleActivity.getOfflineConvertStartTime());
                singleCouponDto.setUseEndTime(afterSaleActivity.getOfflineConvertEndTime());
                singleCouponDto.setRestrictUse(0);
                singleCouponDto.setBusinessTypeEnum(com.tuanche.certificate.api.Constants.BusinessTypeEnum.ACTIVITY);
                singleCouponDto.setBusinessIdEnum(BusinessIdEnum.SALE);
                TcResponse tcCoupon;
                if (afterSaleActivity.getGiftCouponId() == null) {
                    tcCoupon = iCouponService.createSingleCerti(singleCouponDto);
                } else {
                    singleCouponDto.setId(afterSaleActivity.getGiftCouponId());
                    tcCoupon = iCouponService.editSingleCerti(singleCouponDto);
                }
                if (HttpURLConnection.HTTP_OK == tcCoupon.getResponseHeader().getStatus()) {
                    Map<String, Object> objectMap = (HashMap) tcCoupon.getResponse().getResult();
                    afterSaleActivity.setGiftCouponId(Integer.valueOf(objectMap.get("id").toString()));
                    afterSaleActivity.setGiftFissionCount(1);
                } else {
                    return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "??????????????????,???????????????");
                }
            } else {
                afterSaleActivity.setGiftCouponId(null);
                afterSaleActivity.setGiftFissionCount(null);
            }
            List<AlarmInfo> alarmInfoList = JSON.parseArray(activityDto.getAlarmInfoListStr(), AlarmInfo.class);
            if(!CollectionUtils.isEmpty(alarmInfoList)){
                String alarmPhone = "";
                String alarmEmail = "";
                for (AlarmInfo alarmInfo : alarmInfoList) {
                    if(StringUtils.isNotEmpty(alarmInfo.getAlarmInfoPhone())){
                        alarmPhone += alarmInfo.getAlarmInfoPhone() + "##";
                        alarmEmail += alarmInfo.getAlarmInfoEmail() + "##";
                    }
                }
                afterSaleActivity.setAlarmPhone(alarmPhone);
                afterSaleActivity.setAlarmEmail(alarmEmail);
            }
            int flag = afterSaleActivityService.saveAfterSaleActivity(afterSaleActivity);
            if (flag == 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "??????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * ?????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/viewActivityUrls")
    public String viewActivityUrls(ModelMap modelMap, Integer activityId) {
        AfterSaleActivityDto dto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        int timeTemp = Math.abs(LocalTime.now().hashCode());
        String randStr = "";
        for (int i = 0; i < 5; i++) {
            randStr = randStr + (char)(Math.random()*26 + 'a');
        }
        //https://m.tuanche.com/aftersale/index/123/aaa?channel=3&id=90 ?????????https://m.tuanche.com/aftersale/index/
        modelMap.addAttribute("afterSaleActivitySpreadCardUrl", afterSaleActivitySpreadCardUrl + timeTemp + "/" + randStr + "?channel=3&id=" + dto.getId());
        modelMap.addAttribute("afterSaleActivityNewSpreadCardUrl", afterSaleActivityNewSpreadCardUrl + timeTemp + "/" + randStr + "?channel=3&id=" + dto.getId());
        modelMap.addAttribute("afterSaleActivityTelesalesUrl", afterSaleActivitySpreadCardUrl + timeTemp + "/" + randStr + "?channel=2&id=" + dto.getId());
        modelMap.addAttribute("afterSaleActivitySpreadCardCouponUrl", afterSaleActivitySpreadCardCouponUrl);
        modelMap.addAttribute("afterSaleActivityAgentShareUrl", afterSaleActivityAgentShareUrl + dto.getDealerId());
        modelMap.addAttribute("afterSaleActivityAgentGmvUrl", afterSaleActivityAgentGmvUrl);
        modelMap.addAttribute("afterSaleActivityTeachDataUrl", afterSaleActivityTeachDataUrl);
        modelMap.addAttribute("afterSaleActivityProductUrl", afterSaleActivityProductUrl + dto.getId());
        modelMap.addAttribute("afterSaleActivityProductCouponUrl", afterSaleActivityProductCouponUrl + dto.getDealerId());
        modelMap.addAttribute("afterSaleActivityVerificationUrl", afterSaleActivityVerificationUrl + dto.getDealerId());
        return "after_sale/activity/view-activity-url";
    }

    /**
     * ??????????????????
     *
     * @param modelMap
     * @param activityId
     * @return
     */
    @RequestMapping("/toActivityCouponList")
    public String toActivityCouponList(ModelMap modelMap,Integer type,Integer packageId, Integer activityId) {
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("packageId", packageId);
        return "after_sale/activity/coupon-list";
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/getActivityCouponList")
    @ResponseBody
    public PageResult<AfterSaleActivityCoupon> getActivityCouponList(PageResult<AfterSaleActivityCoupon> pageResult, AfterSaleActivityCoupon afterSaleActivityCoupon) {
        PageResult pageList = afterSaleActivityService.findAfterSaleCouponListByPage(pageResult, afterSaleActivityCoupon);
        pageList.setCode("0");
        return pageList;
    }

    /**
     * @return com.tuanche.commons.util.ResultDto
     * @description ????????????
     * @date 2020/9/24 16:49
     * @author lvsen
     */
    @RequestMapping("/openFission")
    @ResponseBody
    public ResultDto openFission(HttpServletRequest request, Integer activityId) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == activityId) {
                return noData();
            }
            AfterSaleActivityDto activityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
            if (activityDto == null) {
                return noData();
            }
            if (activityDto.getSaleEndTime().before(new Date())) {
                return dynamicResult("??????????????????????????????????????????");
            }
            afterSaleActivityService.openAfterSaleActivity(activityDto, xxlUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @return com.tuanche.commons.util.ResultDto
     * @description ???????????????????????????
     * @date 2020/9/24 16:49
     * @author lvsen
     */
    @RequestMapping("/openCloseSubAccount")
    @ResponseBody
    public ResultDto openCloseSubAccount(HttpServletRequest request, Integer activityId) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == activityId) {
                return noData();
            }
            AfterSaleActivityDto activityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
            if (activityDto == null) {
                return noData();
            }
            if (activityDto.getOfflineConvertStartTime().before(new Date())) {
                return dynamicResult("??????????????????????????????????????????????????????");
            }
            activityDto.setUpdateBy(xxlUser.getId());
            activityDto.setUpdateDt(new Date());
            if (AfterSaleConstants.SubAccountType.NO_OPEN.getCode().equals(activityDto.getOnSubAccount())) {
                activityDto.setOnSubAccount(AfterSaleConstants.SubAccountType.OPENED.getCode());
                //??????????????????????????????????????????
                AfterSaleActivityAccount account = accountService.getAfterSaleActivityAccountByActivityId(activityId);
                if (!Objects.isNull(account) && STATE_111.getCode().equals(account.getVerifyState())) {
                    activityDto.setOnSubAccount(AfterSaleConstants.SubAccountType.VERIFY.getCode());
                }
            } else {
                activityDto.setOnSubAccount(AfterSaleConstants.SubAccountType.NO_OPEN.getCode());
            }
            afterSaleActivityService.openCloseSubAccount(activityDto);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * ?????????????????????
     *
     * @param modelMap
     * @return ????????????????????????????????????????????? ?????????????????????10???
     */
    @RequestMapping("/toAddCouponPage")
    public String toAddCouponPage(ModelMap modelMap, Integer id, Integer activityId,Integer packageId,Integer type) {
        AfterSaleActivityDto activityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        if (!Objects.isNull(id)) {
            AfterSaleActivityCoupon afterSaleActivityCoupon = afterSaleActivityService.findAfterSaleActivityCouponById(id);
            afterSaleActivityCoupon.setGetStartTime(activityDto.getSaleStartTime());
            afterSaleActivityCoupon.setGetEndTime(DateUtils.nextMonth(activityDto.getSaleEndTime(),120));
            modelMap.addAttribute("activityCoupon", afterSaleActivityCoupon);
        } else {
            AfterSaleActivityCoupon afterSaleActivityCoupon = new AfterSaleActivityCoupon();
            afterSaleActivityCoupon.setGetStartTime(activityDto.getSaleStartTime());
            afterSaleActivityCoupon.setGetEndTime(DateUtils.nextMonth(activityDto.getSaleEndTime(),120));
            modelMap.addAttribute("activityCoupon", afterSaleActivityCoupon);
        }
        modelMap.addAttribute("onState", activityDto.getOnState());
        modelMap.addAttribute("activityId", activityId);
        modelMap.addAttribute("packageId", packageId);
        modelMap.addAttribute("type", type);
        return "after_sale/activity/add-coupon";
    }

    @RequestMapping("/saveActivityCoupon")
    @ResponseBody
    public ResultDto saveActivityCoupon(HttpServletRequest request, @RequestBody AfterSaleActivityCouponDto afterSaleActivityCoupon) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            // ???????????????
            afterSaleActivityCoupon.setOperateUser(xxlUser.getId());
            TcResponse tcCoupon;
            Date getStartTime = DateUtils.stringToDate(afterSaleActivityCoupon.getGetStartTimeStr(), "yyyy-MM-dd HH:mm:ss");
            Date getEndTime = DateUtils.stringToDate(afterSaleActivityCoupon.getGetEndTimeStr(), "yyyy-MM-dd HH:mm:ss");
            afterSaleActivityCoupon.setGetStartTime(getStartTime);
            afterSaleActivityCoupon.setGetEndTime(getEndTime);
            afterSaleActivityCoupon.setRelTime(new Date());
            afterSaleActivityCoupon.setOperateUser(xxlUser.getId());
            SingleCouponDto singleCouponDto = new SingleCouponDto();
            singleCouponDto.setType(3);
            singleCouponDto.setName(afterSaleActivityCoupon.getCouponName());
            singleCouponDto.setHasTotalCount(0);
            singleCouponDto.setMoney(afterSaleActivityCoupon.getMoney());
            singleCouponDto.setReceiveCondition(1);
            singleCouponDto.setComposition(1);
            singleCouponDto.setUseWay(1);
            singleCouponDto.setGetRule(1);
            singleCouponDto.setGetStartTime(getStartTime);
            singleCouponDto.setGetEndTime(getEndTime);

            singleCouponDto.setUseRule(2);
            singleCouponDto.setUseDays(afterSaleActivityCoupon.getValidity() * 365);
            singleCouponDto.setRestrictUse(0);
            singleCouponDto.setBusinessTypeEnum(com.tuanche.certificate.api.Constants.BusinessTypeEnum.ACTIVITY);
            singleCouponDto.setBusinessIdEnum(BusinessIdEnum.SALE);
            if (afterSaleActivityCoupon.getId() == null) {
                tcCoupon = iCouponService.createSingleCerti(singleCouponDto);
            } else {
                singleCouponDto.setId(afterSaleActivityCoupon.getCouponId());
                tcCoupon = iCouponService.editSingleCerti(singleCouponDto);
            }
            if (HttpURLConnection.HTTP_OK == tcCoupon.getResponseHeader().getStatus()) {
                Map<String, Object> objectMap = (HashMap) tcCoupon.getResponse().getResult();
                afterSaleActivityCoupon.setCouponId(Integer.valueOf(objectMap.get("id").toString()));
            } else {
                CommonLogUtil.addInfo("???????????????", "?????????????????????????????????", JSON.toJSONString(singleCouponDto));
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "?????????????????????,???????????????");
            }
            int flag = afterSaleActivityService.saveActivityCoupon(afterSaleActivityCoupon);
            if (flag == 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "?????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }


    /**
     * @return com.tuanche.commons.util.ResultDto
     * @description ???????????????
     * @date 2020/9/24 16:49
     * @author lvsen
     */
    @RequestMapping("/deleteVoucherCoupon")
    @ResponseBody
    public ResultDto deleteVoucherCoupon(HttpServletRequest request, Integer id) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == id) {
                return noData();
            }
            int flag = afterSaleActivityService.updateAfterSaleActivityCouponById(id);
            if (flag <= 0) {
                return dynamicResult("?????????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * ???????????????
     *
     * @return
     */
    @GetMapping("/dealerDetailList")
    @ResponseBody
    public PageResult<CsCompanyDetailDto> dealerDetailList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "keyword", required = false) String keyword) {
        CsCompany csCompany = new CsCompany();
        csCompany.setCompanyName(keyword);
        List<CsCompanyDetailDto> companyDetailList = companyBaseService.getCsCompanyDetailList(csCompany, page, limit);
        PageResult<CsCompanyDetailDto> pageResult = new PageResult<>();
        pageResult.setCount(companyBaseService.getCsCompanyDetailCount(csCompany));
        pageResult.setMsg("");
        pageResult.setData(companyDetailList);
        pageResult.setCode("0");
        return pageResult;
    }

    /**
     * ????????????
     *
     * @return
     */
    @GetMapping("/getAfterSaleProductList")
    @ResponseBody
    public PageResult<CommodityBusinessResponseDto> getAfterSaleProductList(@RequestParam("page") Integer pageNo, @RequestParam("limit") Integer limit,
                                                                            @RequestParam(value = "keyword", required = false) String keyword,
                                                                            @RequestParam(value = "commodityType") String commodityType) {
        PageResult PageList = new PageResult();
        try {
            CommodityWithBusinessQueryRequestDto queryRequestDto = new CommodityWithBusinessQueryRequestDto();
            queryRequestDto.setCommodityName(keyword);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            queryRequestDto.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
            if ("PACKAGE_CARD".equals(commodityType)) {
                queryRequestDto.setCommodityTypes(Arrays.asList(CommodityTypeEnum.PACKAGE_CARD));
            } else {
                queryRequestDto.setCommodityTypes(Arrays.asList(CommodityTypeEnum.FLOW_CARD));
            }
            BaseResponseDto<PageableDto<CommodityBusinessResponseDto>> responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if (responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData() != null && !CollectionUtils.isEmpty(responseDto.getData().getList())) {
                PageList.setCount(responseDto.getData().getTotalCount());
                PageList.setData(responseDto.getData().getList());
            } else {
                PageList.setCount(0);
                PageList.setData(null);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("AfterSaleActivityController", "getAfterSaleProductList", "????????????????????????????????? error", e);
        }
        PageList.setCode("0");
        return PageList;
    }

    @RequestMapping("/selectActivityList")
    @ResponseBody
    public ResultDto selectActivityList(AfterSaleActivity afterSaleActivity) {
        List<AfterSaleActivity> saleActivities = new ArrayList<>();
        try {
            saleActivities = afterSaleActivityService.selectActivityList(afterSaleActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success(saleActivities);
    }

    /**
     * ???????????????
     * @param modelMap
     * @param activityId
     * @return
     */
    @RequestMapping("/toActivityPackageList")
    public String toActivityPackageList(ModelMap modelMap, Integer activityId) {
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        return "after_sale/activity/package-list";
    }

    /**
     * ????????????
     * @return
     */
    @RequestMapping("/getActivityPackageList")
    @ResponseBody
    public PageResult<AfterSaleActivityPackageDto> getActivityPackageList(PageResult<AfterSaleActivityPackageDto> pageResult, AfterSaleActivityPackageDto activityPackage) {
        PageResult pageList = packageService.getAfterSaleActivityPackageByPage(pageResult, activityPackage);
        pageList.setCode("0");
        return pageList;
    }

    /**
     *  ????????????
     * @param request
     * @param goodsId
     * @param activityId
     * @return
     */
    @RequestMapping("/addActivityPackage")
    @ResponseBody
    public ResultDto addActivityPackage(HttpServletRequest request,Integer goodsId, Integer activityId) {
        if (Objects.isNull(goodsId) || Objects.isNull(activityId)) {
            return paramInvalid();
        }
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        AfterSaleActivityPackage tempPackage = new AfterSaleActivityPackage();
        tempPackage.setGoodsId(goodsId);
        tempPackage.setActivityId(activityId);
        List<AfterSaleActivityPackageDto> packageList = packageService.getAfterSaleActivityPackageList(tempPackage);
        if (!CollectionUtils.isEmpty(packageList)) {
            ResultDto resultDto = new ResultDto();
            resultDto.setCode(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode());
            resultDto.setMsg("???????????????????????????????????????????????????");
            return resultDto;
        }
        try {
            BaseResponseDto<CommodityDetailBusinessResponseDto> commodityDetail = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(goodsId);
            if (!ResultEnum.SUCCESS.getCode().equals(commodityDetail.getCode()) || commodityDetail.getData() == null) {
                return paramInvalid();
            }
            CommodityDetailBusinessResponseDto commodity = commodityDetail.getData();
            AfterSaleActivityPackage activityPackage = new AfterSaleActivityPackage();
            activityPackage.setActivityId(activityId);
            activityPackage.setCommodityName(commodity.getCommodityName());
            activityPackage.setCommodityCount(commodity.getCommodityCount());
            activityPackage.setCommodityPrice(commodity.getCommodityPrice());
            activityPackage.setGoodsId(goodsId);
            activityPackage.setOriginalPrice(commodity.getOriginalPrice());
            activityPackage.setUpShelfTime(commodity.getUpShelfTime());
            activityPackage.setDownShelfTime(commodity.getDownShelfTime());
            activityPackage.setOperateUser(xxlUser.getId());
            activityPackage.setRelStatus(AfterSaleConstants.ActivityCoupon.REL_STATUS_YES.getCode());
            activityPackage.setRelTime(new Date());
            int flag = packageService.saveAfterSaleActivityPackage(activityPackage);
            if (flag <= 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "??????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     *  ????????????
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping("/deleteActivityPackage")
    @ResponseBody
    public ResultDto deleteActivityPackage(HttpServletRequest request, Integer packageId) {
        if (Objects.isNull(packageId)) {
            return paramInvalid();
        }
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            AfterSaleActivityPackage activityPackage = new AfterSaleActivityPackage();
            activityPackage.setId(packageId);
            activityPackage.setRelStatus(AfterSaleConstants.ActivityCoupon.REL_STATUS_NO.getCode());
            activityPackage.setOperateUser(xxlUser.getId());
            activityPackage.setRelTime(new Date());
            int flag = packageService.updateAfterSaleActivityPackage(activityPackage);
            if (flag <= 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "??????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * ???????????????????????????
     * @param modelMap
     * @param activityId
     * @return
     */
    @RequestMapping("/toPublicQrCodePage")
    public String toPublicQrCodePage(ModelMap modelMap, Integer activityId) {
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        return "after_sale/activity/public-qr-code";
    }

    /**
     * ????????????????????????
     * @param request
     * @param activityDto
     * @return
     */
    @RequestMapping("/saveDealerQrCode")
    @ResponseBody
    public ResultDto saveDealerQrCode(HttpServletRequest request, @RequestBody AfterSaleActivityDto activityDto) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == activityDto || activityDto.getActivityId() == null) {
                return noData();
            }
            activityDto.setId(activityDto.getActivityId());
            activityDto.setPublicQrCode(activityDto.getPublicQrCode());
            activityDto.setUpdateBy(xxlUser.getId());
            activityDto.setUpdateDt(new Date());
            int flag = afterSaleActivityService.updateAfterSaleActivityById(activityDto);
            if (flag == 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "?????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }
    /**
     * ??????????????????
     * @author HuangHao
     * @CreatTime 2021-11-17 11:55
     * @param id
     * @return com.tuanche.commons.util.ResultDto
     */
    @RequestMapping("/delActivityCache")
    @ResponseBody
    public ResultDto delActivityCache(Integer id){
        return afterSaleActivityService.delActivityCache(id);
    }

    /**
     * ?????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/setSubAccountPage")
    public String setSubAccountPage(ModelMap modelMap, Integer activityId, Integer dealerId, String dealerName) {
        AfterSaleActivityAccount account = accountService.getAfterSaleActivityAccountByActivityId(activityId);
        modelMap.addAttribute("account", Objects.isNull(account) ? new AfterSaleActivityAccount() : account);
        modelMap.addAttribute("activityId", activityId);
        modelMap.addAttribute("dealerId", dealerId);
        modelMap.addAttribute("dealerName", dealerName);
        return "after_sale/activity/sub_account";
    }

    /**
     * ??????????????????
     *
     * @param account
     * @return
     */
    @RequestMapping("/saveSubAccount")
    @ResponseBody
    public ResultDto saveSubAccount(HttpServletRequest request, @RequestBody AfterSaleActivityAccount account) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (null == account || account.getDealerId() == null || account.getActivityId() == null || Objects.isNull(account.getSubAccountRatio())
                || Objects.isNull(account.getMchId()) || StringUtils.isEmpty(account.getMchName())) {
            return noData();
        }
        Date now = new Date();
        AfterSaleActivity afterSaleActivity = afterSaleActivityService.getAfterSaleActivityById(account.getActivityId());
        //???????????????
        if (now.after(afterSaleActivity.getOfflineConvertStartTime())) {
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
        }
        ResultDto resultDto = accountService.saveAndVerifyAfterSaleActivityAccount(account, xxlUser.getId());
        if (!resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, resultDto.getMsg());
        }
        return success(resultDto.getMsg());
    }

    /**
     * ?????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/toVerfiyPage")
    public String toVerfiyPage(ModelMap modelMap, Integer accountId,String dealerName) {
        AfterSaleActivityAccount account = accountService.getAfterSaleActivityAccountById(accountId);
        modelMap.addAttribute("account", Objects.isNull(account) ? new AfterSaleActivityAccount() : account);
        modelMap.addAttribute("dealerName", dealerName);
        //?????????????????????????????????????????????????????????
        return "after_sale/activity/account_verfiy";
    }

    /**
     * ??????????????????
     *
     * @param account
     * @return
     */
    @RequestMapping("/createOrderPayQr")
    @ResponseBody
    public ResultDto createOrderPayQr(HttpServletRequest request, @RequestBody AfterSaleActivityAccount account) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (null == account || Objects.isNull(account.getId())) {
            return noData();
        }
        AfterSaleActivityAccount activityAccount = accountService.getAfterSaleActivityAccountById(account.getId());
        String orderNo = "";
        if (StringUtils.isEmpty(activityAccount.getOrderNo())) {
            orderNo = String.valueOf(OrderNoGenerateUtil.nextId());
        } else {
            orderNo = activityAccount.getOrderNo();
        }
        BigDecimal orderMoney = BigDecimal.valueOf(0.05);
        Map<String, Object> params = new HashMap<>();
        params.put("productCode", "104");
        params.put("payType", "01");
        params.put("orderNo", orderNo);
        params.put("tradeBusiness", "1_" + orderNo);
        params.put("amount", orderMoney);
        params.put("content", "??????????????????" + activityAccount.getMchId());
        params.put("notifyUrl", order_tuanche_host+"/api/v2/afterSale/subAccountNotice");
        params.put("returnUrl", order_tuanche_host);
        params.put("code", "weChatWapHandle");
        params.put("broswerType", 0);
        params.put("profitSharing", profitSharing);
        activityAccount.setOrderMoney(orderMoney);
        activityAccount.setOrderNo(orderNo);
        activityAccount.setWxTransactionId("1_" + orderNo);
        CommonLogUtil.addInfo("????????????????????????", "??????url:" + afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, "?????????" + JSON.toJSONString(params));
        String result = null;
        try {
            result = HttpClient.post(afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, JSONObject.toJSONString(params), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonLogUtil.addInfo("????????????????????????", "??????url:" + afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, "?????????" + JSON.toJSONString(result));
        if (StringUtils.isEmpty(result)) {
            accountService.saveAfterSaleActivityAccount(activityAccount, xxlUser.getId());
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
        }
        ResultDto payResultDto = JSONObject.parseObject(result, ResultDto.class);
        if (ManuBaseConstants.RESULT_CODE_10000.equals(payResultDto.getCode())) {
            //???????????????
            accountService.saveAfterSaleActivityAccount(activityAccount,xxlUser.getId());
            return success(afterSaleActivityVerfiyCreateOrderUrl+"/weChatQRCode?code="+payResultDto.getResult());
        }
        activityAccount.setFailReason("");
        accountService.saveAfterSaleActivityAccount(activityAccount,xxlUser.getId());
        return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "??????????????????????????????????????????");
    }

    /**
     * ????????????
     *
     * @param account
     * @return
     */
    @RequestMapping("/subAccountVerfiy")
    @ResponseBody
    public ResultDto subAccountVerfiy(HttpServletRequest request, @RequestBody AfterSaleActivityAccount account) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (null == account || Objects.isNull(account.getId())) {
            return noData();
        }
        AfterSaleActivityAccount activityAccount = accountService.getAfterSaleActivityAccountById(account.getId());
        if (StringUtils.isEmpty(activityAccount.getOrderNo()) || !activityAccount.getVerifyState().startsWith("1")
                || StringUtils.isEmpty(activityAccount.getWxTransactionId())) {
            CommonLogUtil.addInfo("??????????????????", "?????????????????????????????????", "?????????" + JSON.toJSONString(activityAccount));
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
        }
        AfterSaleActivity afterSaleActivity = afterSaleActivityService.getAfterSaleActivityById(activityAccount.getActivityId());
        ResultDto resultDto = accountService.verfiyProfitsharing(activityAccount, afterSaleActivity, xxlUser.getId());
        return resultDto;
    }


    /**
     * ??????????????????
     *
     * @param account
     * @return
     */
    @RequestMapping("/refundVerifyOrder")
    @ResponseBody
    public ResultDto refundVerifyOrder(HttpServletRequest request, @RequestBody AfterSaleActivityAccount account) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (null == account || Objects.isNull(account.getId())) {
            return noData();
        }
        AfterSaleActivityAccount activityAccount = accountService.getAfterSaleActivityAccountById(account.getId());
        if (StringUtils.isEmpty(activityAccount.getOrderNo()) || !activityAccount.getVerifyState().startsWith("1")
                || StringUtils.isEmpty(activityAccount.getWxTransactionId())) {
            CommonLogUtil.addInfo("??????????????????", "?????????????????????????????????", "?????????" + JSON.toJSONString(activityAccount));
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tradeId", activityAccount.getSubAccountTransactionDetailId());
        params.put("content", "????????????????????????" + activityAccount.getMchId());
        params.put("amount", activityAccount.getOrderMoney());
        params.put("payType", "02");
        params.put("orderNo", activityAccount.getOrderNo());
        params.put("tradeBusiness", "2_" + activityAccount.getOrderNo());
        params.put("notifyUrl", order_tuanche_host + "/api/v2/afterSale/subAccountNotice");
        params.put("returnUrl", order_tuanche_host);
        params.put("profitsharingReturn", profitSharing);
        CommonLogUtil.addInfo("????????????????????????", "??????url:" + afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, "?????????" + JSON.toJSONString(params));
        activityAccount.setRefundId("2_" + activityAccount.getOrderNo());
        String result = null;
        try {
            result = HttpClient.post(afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, JSONObject.toJSONString(params), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = ParameterUtil.decrypt(result);
        CommonLogUtil.addInfo("????????????????????????", "??????url:" + afterSaleActivityVerfiyCreateOrderUrl + CREATE_MERCHANTS_URL, "?????????" + JSON.toJSONString(result));
        if(StringUtils.isEmpty(result)){
            return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
        }
        PaymentResult payResultDto = JSONObject.parseObject(result, PaymentResult.class);
        String paySubState = activityAccount.getVerifyState().substring(0, 2);
        if (ManuBaseConstants.RESULT_CODE_10000.equals(payResultDto.getCode())) {
            accountService.saveAfterSaleActivityAccount(activityAccount, xxlUser.getId());
            return success();
        }
        activityAccount.setVerifyState(paySubState + "2");
        activityAccount.setFailReason(payResultDto.getMessage());
        accountService.saveAfterSaleActivityAccount(activityAccount,xxlUser.getId());
        return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, "????????????????????????????????????");
    }

}
