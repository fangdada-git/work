package com.tuanche.web.controller;/**
 * @program: direct-selling
 * @description: 裂变活动controller
 * @author: lvsen
 * @create: 2020-09-22 16:58
 **/

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.LivePeriodDealerDto;
import com.tuanche.directselling.enums.BrankAccountType;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.DealerSalesVo;
import com.tuanche.directselling.vo.FissionActivityAwardVo;
import com.tuanche.directselling.vo.FissionActivityExtendVo;
import com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial;
import com.tuanche.eap.api.enums.DealerFinancialAccountTypeEnum;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.CommodityStatusEnum;
import com.tuanche.web.service.DealerSettlementAccountExcelService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.RefuelCardExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lvsen
 * @Description 裂变任务controller
 * @Date 2020/9/22
 **/
@Controller
@RequestMapping("/fission/activity")
public class FissionActivityController extends BaseController {
    @Autowired
    PeriodsService periodsService;
    @Autowired
    DealerSettlementAccountExcelService dealerSettlementAccountExcelService;
    @Reference
    FissionActivityService fissionActivityService;
    @Reference
    CityBaseService cityBaseService;
    @Reference
    LiveSceneService liveSceneService;
    @Reference
    UserBaseService userBaseService;
    @Reference
    FissionSaleService fissionSaleService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    ICommodityBusinessService commodityBusinessService;
    @Reference
    FissionSalesOrderService fissionSalesOrderService;
    @Reference
    FissionActivityGoodsRecordService fissionActivityGoodsRecordService;
    @Reference
    FissionDealerSettlementAccountService fissionDealerSettlementAccountService;
    @Reference
    CsDealerFinancialService csDealerFinancialService;

    @Value("${fission.activity.page.url}")
    private String fissionActivityPageUrl;
    /**
     * 到列表页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/toListPage")
    public String toListPage(ModelMap modelMap) {
        // 获取场次
        modelMap.addAttribute("periodsList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        //活动页链接
        modelMap.addAttribute("fissionActivityPageUrl", fissionActivityPageUrl);
        return "fission/activity/list";
    }

    /**
     * 列表数据
     *
     * @param pageResult
     * @param fissionActivity
     * @return
     */
    @RequestMapping("/searchFissionList")
    @ResponseBody
    public PageResult searchFissionList(PageResult<FissionActivity> pageResult, FissionActivity fissionActivity) {
        PageResult PageList = fissionActivityService.findFissionActivityByPage(pageResult, fissionActivity);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * 到活动新增页面
     * @param modelMap
     * @return
     */
    @RequestMapping("/toAddFissionPage")
    public String toAddFissionPage(ModelMap modelMap) {
        // 获取场次
        modelMap.addAttribute("periodsList", periodsService.getValidActivityList(new ActivityConfigRequestVo()));
        return "fission/activity/add-fission";
    }

    /**
     * 到活动修改页面
     * @param modelMap
     * @return
     */
    @RequestMapping("/toEditFissionPage")
    public String toEditFissionPage(ModelMap modelMap, Integer fissionId) {
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionId);
        if (fissionActivity != null && fissionActivity.getSceneId() != null) {
            LiveScene liveScene = liveSceneService.getLiveSceneById(fissionActivity.getSceneId());
            modelMap.addAttribute("sceneTitle", liveScene.getSceneTitle());
        }
        modelMap.addAttribute("fissionActivity", fissionActivity);
        // 获取场次
        modelMap.addAttribute("periodsList", periodsService.getValidActivityList(new ActivityConfigRequestVo()));
        return "fission/activity/edit-fission";
    }

    /**
     * 裂变活动配置页面
     * @param modelMap
     * @param fissionId
     * @return
     */
    @RequestMapping("/toConfigFissionPage")
    public String toConfigFissionPage(ModelMap modelMap, Integer fissionId) {
        FissionActivityDto activityDto = fissionActivityService.getFissionActivityDtoById(fissionId);
        modelMap.addAttribute("fissionActivity", activityDto);
        modelMap.addAttribute("fissionActivityData", activityDto.getFissionActivityData() != null ? activityDto.getFissionActivityData() : new FissionActivityData());
        List<FissionActivityExtend> cityExtendList = activityDto.getFissionActivityExtend().stream().filter(t -> GlobalConstants.FISSION_EXTEND_TYPE1.equals(t.getDataType())).collect(Collectors.toList());
        List<FissionActivityExtend> productExtendList = activityDto.getFissionActivityExtend().stream().filter(t -> GlobalConstants.FISSION_EXTEND_TYPE2.equals(t.getDataType())).collect(Collectors.toList());
        modelMap.addAttribute("configCityList", cityExtendList.isEmpty() ? cityExtendList.add(new FissionActivityExtend()) : cityExtendList.stream().map(FissionActivityExtend::getDataId).collect(Collectors.toList()));
        modelMap.addAttribute("configProductList", productExtendList.isEmpty() ? productExtendList.add(new FissionActivityExtend()) : productExtendList.stream().map(FissionActivityExtend::getDataId).collect(Collectors.toList()));
        return "fission/activity/config-fission";
    }

    /**
     * 裂变奖励规则配置页面
     * @param modelMap
     * @param fissionId
     * @return
     */
    @RequestMapping("/toConfigAwardPage")
    public String toConfigAwardPage(ModelMap modelMap, Integer fissionId) {
        FissionActivityDto activityDto = fissionActivityService.getFissionActivityDtoById(fissionId);
        if (activityDto != null) {
            if (activityDto.getStartTime()!=null) activityDto.setStartTimeStr(DateFormatUtils.format(activityDto.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
            if (activityDto.getEndTime()!=null) activityDto.setEndTimeStr(DateFormatUtils.format(activityDto.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        List<FissionAwardRule> ruleListb = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        List<FissionAwardRule> ruleListc = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_C);
        List<FissionSalesOrder> orderList = fissionSalesOrderService.getFissionSalesOrderListByFissionId(fissionId);
        double totalAmount = orderList.stream().mapToDouble(order -> Double.valueOf(order.getOrderAmount().toString())).sum();
        modelMap.addAttribute("fissionActivity", activityDto);
        modelMap.addAttribute("bAward", transformAwardField(ruleListb));
        modelMap.addAttribute("cAward", transformAwardField(ruleListc));
        modelMap.addAttribute("dealerCount", orderList.stream().collect(Collectors.groupingBy(FissionSalesOrder::getDealerId)).size());
        modelMap.addAttribute("salesCount", orderList.stream().collect(Collectors.groupingBy(FissionSalesOrder::getSaleId)).size());
        modelMap.addAttribute("totalAmount", totalAmount);
        return "fission/activity/config-award";
    }

    /**
     *  奖励规则字段转化
     * @param ruleList
     * @return
     */
    private FissionActivityAwardVo transformAwardField(List<FissionAwardRule> ruleList) {
        if (CollectionUtils.isEmpty(ruleList)) {
            return new FissionActivityAwardVo();
        }
        FissionActivityAwardVo awardVo = new FissionActivityAwardVo();
        for (FissionAwardRule awardRule : ruleList) {
            BeanUtils.copyProperties(awardRule, awardVo);
            if (StringUtils.isEmpty(awardRule.getTaskCode())) {
                continue;
            }
            switch (awardRule.getTaskCode()) {
                case GlobalConstants.FISSION_AWARD_RULE_TASK_CODE1:
                    awardVo.setBrowseAward(awardRule.getAward());
                    awardVo.setBrowseAwardRule(awardRule.getAwardRule());
                    break;
                case GlobalConstants.FISSION_AWARD_RULE_TASK_CODE2:
                    awardVo.setSubscribeAward(awardRule.getAward());
                    awardVo.setSubscribeAwardRule(awardRule.getAwardRule());
                    break;
                case GlobalConstants.FISSION_AWARD_RULE_TASK_CODE3:
                    awardVo.setBuyActivityAward(awardRule.getAward());
                    awardVo.setBuyActivityAwardRule(awardRule.getAwardRule());
                    break;
                case GlobalConstants.FISSION_AWARD_RULE_TASK_CODE4:
                    awardVo.setWatchAward(awardRule.getAward());
                    awardVo.setWatchAwardRule(awardRule.getAwardRule());
                    break;
                case GlobalConstants.FISSION_AWARD_RULE_TASK_CODE5:
                    awardVo.setBuyBroadcastAward(awardRule.getAward());
                    awardVo.setBuyBroadcastAwardRule(awardRule.getAwardRule());
                    break;
                default:
                    break;
            }
        }
        return awardVo;
    }

    /**
     * 裂变经销商配置页面
     * @param modelMap
     * @param fissionId
     * @return
     */
    @RequestMapping("/toConfigDealerList")
    public String toConfigDealerList(ModelMap modelMap, Integer fissionId) {
        FissionActivityDto activityDto = fissionActivityService.getFissionActivityDtoById(fissionId);
        if (activityDto != null) {
            activityDto.setStartTimeStr(DateFormatUtils.format(activityDto.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
            activityDto.setEndTimeStr(DateFormatUtils.format(activityDto.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        modelMap.addAttribute("fissionActivity", activityDto);
        return "fission/activity/config-dealer-list";
    }

    /**
     * 裂变经销商销售页面
     * @param modelMap
     * @param fissionId
     * @return
     */
    @RequestMapping("/toDealerSalesPage")
    public String toDealerSalesPage(ModelMap modelMap, Integer fissionId, Integer dealerId, String dealerName) {
        CsUser csUser = new CsUser();
        csUser.setDealerId(dealerId);
        csUser.setUlevels(ManuBaseConstants.MANUFACTURER_USER_TYPE4 + "," + ManuBaseConstants.MANUFACTURER_USER_TYPE3);
        List<CsUser> dealerSalesList = userBaseService.getCsUserList(csUser);
        FissionSale fissionSale = new FissionSale();
        fissionSale.setFissionId(fissionId);
        fissionSale.setDealerId(dealerId);
        List<FissionSale> fissionSaleList = fissionSaleService.getFissionSaleList(fissionSale);
        modelMap.addAttribute("salesCount", dealerSalesList.size());
        modelMap.addAttribute("joinSalesCount", fissionSaleList.size());
        modelMap.addAttribute("fissionId", fissionId);
        modelMap.addAttribute("dealerId", dealerId);
        modelMap.addAttribute("dealerName", dealerName);
        return "fission/activity/dealer-sales-list";
    }

    /**
     * @description 保存裂变活动
     * @return com.tuanche.commons.util.ResultDto
     * @date 2020/9/23 16:42
     * @author lvsen
     */
    @RequestMapping("/saveFissionActivity")
    @ResponseBody
    public ResultDto saveFissionActivity(HttpServletRequest request, @RequestBody FissionActivityDto activityDto) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == activityDto) {
                return noData();
            }
            FissionActivity fissionActivity = new FissionActivity();
            BeanUtils.copyProperties(activityDto,fissionActivity);
            fissionActivity.setStartTime(DateUtils.stringToDate(activityDto.getStartTimeStr(),"yyyy-MM-dd HH:mm:ss"));
            fissionActivity.setEndTime(DateUtils.stringToDate(activityDto.getEndTimeStr(),"yyyy-MM-dd HH:mm:ss"));
            // 保存场次活动
            if (fissionActivity.getId() == null) {
                fissionActivity.setCtreateDt(new Date());
                fissionActivity.setCtreateBy(xxlUser.getId());
            } else {
                fissionActivity.setUpdateDt(new Date());
                fissionActivity.setUpdateBy(xxlUser.getId());
            }
            // 获取场次信息
            ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
            activityConfigRequestVo.setId(fissionActivity.getPeriodsId());
            ActivityConfigResponseVo activityConfig = periodsService.queryById(activityConfigRequestVo);
            fissionActivity.setPeriodsName(activityConfig != null ? activityConfig.getName() : "");
            int flag = fissionActivityService.saveFissionActivity(fissionActivity);
            if (flag == 0) {
                return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR,"活动保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @description 保存裂变活动扩展信息
     * @return com.tuanche.commons.util.ResultDto
     * @date 2020/9/23 16:42
     * @author lvsen
     */
    @RequestMapping("/saveFissionActivityConfig")
    @ResponseBody
    public ResultDto saveFissionActivityConfig(HttpServletRequest request, @RequestBody FissionActivityExtendVo fissionActivityExtendVo) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == fissionActivityExtendVo || StringUtils.isEmpty(fissionActivityExtendVo.getHeadPicUrl())
            || fissionActivityExtendVo.getFissionId() == null || StringUtils.isEmpty(fissionActivityExtendVo.getDetailPicUrl1())) {
                return noData();
            }
            if (StringUtils.isEmpty(fissionActivityExtendVo.getCityIds())) {
                return noData();
            }
            // 保存场次活动
            fissionActivityExtendVo.setOperateUser(xxlUser.getId());
            fissionActivityService.saveFissionActivityConfig(fissionActivityExtendVo);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @description 保存裂变活动奖励规则
     * @return com.tuanche.commons.util.ResultDto
     * @date 2020/9/23 16:42
     * @author lvsen
     */
    @RequestMapping("/saveFissionActivityAward")
    @ResponseBody
    public ResultDto saveFissionActivityAward(HttpServletRequest request, @RequestBody FissionActivityAwardVo activityAwardVo) {
        try {
            if (null == activityAwardVo || activityAwardVo.getRuleType() == null || (activityAwardVo.getRuleType() == 2 && activityAwardVo.getPrizePool() == null)
                    || activityAwardVo.getFissionId() == null || activityAwardVo.getPersonMoney() == null) {
                return noData();
            }
            // 保存裂变奖励规则
            fissionActivityService.saveFissionActivityAwardRule(activityAwardVo);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @description 判断任务配置并开启任务
     * @return com.tuanche.commons.util.ResultDto
     * @date 2020/9/24 16:49
     * @author lvsen
     */
    @RequestMapping("/openFission")
    @ResponseBody
    public ResultDto openFission(HttpServletRequest request,Integer fissionId) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == fissionId) {
                return noData();
            }
            FissionActivityDto fissionActivityDto = fissionActivityService.getFissionActivityDtoById(fissionId);
            if (fissionActivityDto == null || fissionActivityDto.getId() == null) {
                return noData();
            }
            if (fissionActivityDto.getEndTime().before(new Date())) {
                return dynamicResult("任务结束时间已过，不能开启任务！");
            }
            List<FissionAwardRule> awardRuleList = fissionActivityDto.getAwardRuleList();
            List<FissionActivityExtend> extendList = fissionActivityDto.getFissionActivityExtend();
            FissionActivityData activityData = fissionActivityDto.getFissionActivityData();
            if (activityData == null || activityData.getBrowseBase() == null || activityData.getShareBase() == null
                    || activityData.getSubscribeBase() == null) {
                return dynamicResult("请完善裂变页面基数配置，再确认开启任务！");
            }
            if (CollectionUtils.isEmpty(awardRuleList)) {
                return dynamicResult("请完善裂变规则，再确认开启任务！");
            }
            if (CollectionUtils.isEmpty(extendList)) {
                return dynamicResult("请完善裂变活动配置，再确认开启任务！");
            }
            Map<Byte, List<FissionActivityExtend>> extendMapList = extendList.stream().collect(Collectors.groupingBy(FissionActivityExtend::getDataType, Collectors.toList()));
            if(CollectionUtils.isEmpty(extendMapList.get(GlobalConstants.FISSION_EXTEND_TYPE1))){
                return dynamicResult("请配置裂变城市，再确认开启任务！");
            }
            if(CollectionUtils.isEmpty(extendMapList.get(GlobalConstants.FISSION_EXTEND_TYPE3))){
                return dynamicResult("请配置裂变经销商，再确认开启任务！");
            }
            fissionActivityService.openFissionActivity(fissionId,fissionActivityDto.getStartTime(),xxlUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @description 经销商参与裂变 joinFlag 0取消 1参与
     * @return com.tuanche.commons.util.ResultDto
     * @date 2020/9/24 16:49
     * @author lvsen
     */
    @RequestMapping("/joinFission")
    @ResponseBody
    public ResultDto joinFission(HttpServletRequest request, Integer fissionId, Integer dealerId, Integer joinFlag) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        try {
            if (null == fissionId || dealerId == null || (joinFlag != 0 && joinFlag != 1)) {
                return noData();
            }
            if (joinFlag == 0) {
                //判断是否有销售支付，有则不允许取消
                FissionSale fissionSale = new FissionSale();
                fissionSale.setFissionId(fissionId);
                fissionSale.setDealerId(dealerId);
                List<FissionSale> fissionSaleList = fissionSaleService.getFissionSaleList(fissionSale);
                if (!CollectionUtils.isEmpty(fissionSaleList)) {
                    ResultDto resultDto = new ResultDto();
                    resultDto.setCode(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode());
                    resultDto.setMsg(StatusCodeEnum.DATA_ALREADY_EXISTED.getText());
                    return resultDto;
                }
                //移除结算账户
                fissionDealerSettlementAccountService.deleteDealerSettlement(fissionId, dealerId);
            } else {
                //加入结算账户
                CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(dealerId, DealerFinancialAccountTypeEnum.AccountType0.getType());
                FissionDealerSettlementAccount settlementAccount = new FissionDealerSettlementAccount();
                if (csDealerFinancial == null) {
                    settlementAccount.setFissionId(fissionId);
                    settlementAccount.setDealerId(dealerId);
                } else {
                    settlementAccount.setFissionId(fissionId);
                    settlementAccount.setDealerId(dealerId);
                    settlementAccount.setBankCardName(csDealerFinancial.getCompany());
                    settlementAccount.setBankAddress(csDealerFinancial.getCardBankAddress());
                    settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber());
                    settlementAccount.setBankName(csDealerFinancial.getCardBank());
                    settlementAccount.setDutyParagraph(csDealerFinancial.getDutyParagraph());
                    settlementAccount.setBankCardName("");
                }
                Date now = new Date();
                settlementAccount.setAccountType(BrankAccountType.ACCOUNT_TYPE_1.getCode());
                settlementAccount.setCreateDt(now);
                settlementAccount.setCreateName(xxlUser.getEmpName());
                fissionDealerSettlementAccountService.addDealerSettlementAccount(settlementAccount);
            }
            fissionActivityService.dealerJoinFission(fissionId, dealerId, xxlUser.getId(), joinFlag);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * 裂变活动经销商列表
     * @return
     */
    @RequestMapping("/searchdealerlist")
    @ResponseBody
    public PageResult<LivePeriodDealerDto> searchPeriodsDealerList(PageResult<LivePeriodDealerDto> pageResult, Integer fissionId,
                                                                   Integer sceneId,Integer periodsId,Integer hasSttlementAccount) {
        LivePeriodDealerDto periodDealerDto = new LivePeriodDealerDto();
        periodDealerDto.setOpenPage(true);
        periodDealerDto.setPeriodsId(periodsId);
        periodDealerDto.setFissionId(fissionId);
        periodDealerDto.setHasSttlementAccount(hasSttlementAccount);
        //兼容老数据
        if (sceneId != null) {
            periodDealerDto.setSceneId(sceneId);
        }
        PageResult<LivePeriodDealerDto> dtoPageResult = liveSceneService.getFissionDealerList(pageResult, periodDealerDto);
        FissionActivityDto activityDto = fissionActivityService.getFissionActivityDtoById(fissionId);
        List<FissionActivityExtend> dealerExtendList = activityDto.getFissionActivityExtend().stream().filter(t -> GlobalConstants.FISSION_EXTEND_TYPE3.equals(t.getDataType())).collect(Collectors.toList());
        reSetPartFieldValue(dtoPageResult,dealerExtendList);
        dtoPageResult.setCode("0");
        return dtoPageResult;
    }

    private void reSetPartFieldValue(PageResult<LivePeriodDealerDto> dtoPageResult, List<FissionActivityExtend> dealerExtendList) {
        if (CollectionUtil.isEmpty(dtoPageResult.getData())) {
            return;
        }
        List<Integer> dealerSelectList = null;
        if (!CollectionUtils.isEmpty(dealerExtendList)) {
            dealerSelectList = dealerExtendList.stream().map(FissionActivityExtend::getDataId).collect(Collectors.toList());
        }
        for (LivePeriodDealerDto dealerDto : dtoPageResult.getData()) {
            if (dealerSelectList != null && dealerSelectList.contains(dealerDto.getDealerId())) {
                dealerDto.setJoinFission(true);
            }
            CsCompany company = companyBaseService.getCsCompanyById(dealerDto.getDealerId());
            if (company != null || dealerDto.getCityId() != null) {
                dealerDto.setCityName(cityBaseService.getCityName(company.getCityId()));
            }

            if (dealerDto.getDealerId() != null) {
                CsUser user = new CsUser();
                user.setUlevel(ManuBaseConstants.MANUFACTURER_USER_TYPE5);
                user.setDealerId(dealerDto.getDealerId());
                CsUser csUser = userBaseService.getCsUser(user);
                if (csUser != null) {
                    dealerDto.setManagerName(csUser.getUname());
                    dealerDto.setManagerPhone(csUser.getUphone());
                }
                //经销商账户信息
                Integer financeAccountType = dealerDto.getAccountType()!=null&&dealerDto.getAccountType()==2? DealerFinancialAccountTypeEnum.AccountType1.getType():DealerFinancialAccountTypeEnum.AccountType0.getType();
                CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(dealerDto.getDealerId(), financeAccountType);
                dealerDto.setSyncStatus(1);
                if (csDealerFinancial != null) {
                    dealerDto.setDealerFinancialInvoiceId(csDealerFinancial.getId());
                    if (!strEquals(csDealerFinancial.getCardNumber(), dealerDto.getBankCardNumber())) {
                        dealerDto.setSyncStatus(0);
                    }
                    if (!strEquals(csDealerFinancial.getCardBank(), dealerDto.getBankName())) {
                        dealerDto.setSyncStatus(0);
                    }
                    if (financeAccountType.equals(DealerFinancialAccountTypeEnum.AccountType0.getType())) {
                        if (!strEquals(csDealerFinancial.getCardBankAddress(), dealerDto.getBankAddress())) {
                            dealerDto.setSyncStatus(0);
                        }
                        if (!strEquals(csDealerFinancial.getDutyParagraph(), dealerDto.getDutyParagraph())) {
                            dealerDto.setSyncStatus(0);
                        }
                    } else {
                        if (!strEquals(csDealerFinancial.getCompany(), dealerDto.getBankCardName())) {
                            dealerDto.setSyncStatus(0);
                        }
                    }
                }
            }
        }
    }

    private boolean strEquals(String str1, String str2) {
        str1 = str1 == null ? "" : str1;
        str2 = str2 == null ? "" : str2;
        return StringUtils.equals(str1, str2);
    }

    /**
     * 裂变活动经销商销售列表
     * @return
     */
    @RequestMapping("/dealerSalesList")
    @ResponseBody
    public PageResult<DealerSalesVo> dealerSalesList(PageResult<DealerSalesVo> pageResult, Integer fissionId, Integer dealerId) {
        CsUser csUser = new CsUser();
        csUser.setDealerId(dealerId);
        csUser.setUlevels(ManuBaseConstants.MANUFACTURER_USER_TYPE5 + "," + ManuBaseConstants.MANUFACTURER_USER_TYPE4 + "," + ManuBaseConstants.MANUFACTURER_USER_TYPE3);
        List<CsUser> dealerSalesList = userBaseService.getCsUserList(csUser);
        List<DealerSalesVo> salesList = new ArrayList<>();
        FissionSale fissionSale = new FissionSale();
        fissionSale.setFissionId(fissionId);
        fissionSale.setDealerId(dealerId);
        List<FissionSale> fissionSaleList = fissionSaleService.getFissionSaleList(fissionSale);
        List<Integer> salesIds = fissionSaleList.stream().map(FissionSale::getSaleId).collect(Collectors.toList());
        DealerSalesVo salesDto;
        if (!CollectionUtils.isEmpty(dealerSalesList)) {
            for (CsUser csUser1 : dealerSalesList) {
                salesDto = new DealerSalesVo();
                BeanUtils.copyProperties(csUser1,salesDto);
                if(salesIds.contains(csUser1.getId())){
                    salesDto.setJoinFission(true);
                }
                salesList.add(salesDto);
            }
        }
        PageInfo<DealerSalesVo> page = new PageInfo<>(salesList);
        pageResult.setCount(page.getTotal());
        pageResult.setMsg("");
        pageResult.setData(salesList);
        pageResult.setCode("0");
        return pageResult;
    }

    /**
     * 裂变活动下商品列表
     * @return
     */
    @RequestMapping("/getProductsByFissionId")
    @ResponseBody
    public List<Map<String, String>> getProductsByFissionId(Integer fissionId, Integer periodsId) {
        List<Map<String, String>> result = new ArrayList<>();
        FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
        goodsRecord.setFissionId(fissionId);
        List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
        if (!CollectionUtils.isEmpty(goodsRecordList)) {
            List<Integer> commodityIds = new ArrayList<>();
            goodsRecordList.forEach(v->{
                commodityIds.add(v.getGoodsId());
            });
            CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
            queryRequestDto.setCommodityIds(commodityIds);
            queryRequestDto.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
            BaseResponseDto<PageableDto<CommodityResponseDto>> commodityList = commodityBusinessService.getCommodityList(queryRequestDto);
//            queryRequestDto.setActivityId(fissionId);
//            queryRequestDto.setPeriodsId(periodsId);
//            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
//            queryRequestDto.setEnablePage(false);
//            BaseResponseDto<PageableDto<CommodityResponseDto>> commodityList = commodityBusinessService.getCommodityList(queryRequestDto);
            if (commodityList == null || !commodityList.getSuccess() || commodityList.getData() == null
                    || CollectionUtils.isEmpty(commodityList.getData().getList())) {
                CommonLogUtil.addInfo("FissionActivityController", "调用商品列表接口返参", JSON.toJSONString(commodityList));
                return result;
            }
            Map<String, String> fissionProductMap;
            for (CommodityResponseDto dto : commodityList.getData().getList()) {
                fissionProductMap = new HashMap<>();
                fissionProductMap.put("name", dto.getCommodityName());
                fissionProductMap.put("value", String.valueOf(dto.getCommodityId()));
                result.add(fissionProductMap);
            }
        }
        return result;
    }
    /**
     * 获取结算账户详情信息
     * @author HuangHao
     * @CreatTime 2021-03-10 17:58
     * @param dealerSttlementAccountId
     * @param dealerId
     * @param
     * @return com.tuanche.directselling.model.FissionDealerSettlementAccount
     */
    @RequestMapping("/accountdetailInfo")
    @ResponseBody
    public FissionDealerSettlementAccount accountDetailInfo(Integer dealerSttlementAccountId,Integer fissionId,Integer dealerId){
        FissionDealerSettlementAccount settlementAccount = null;
        if(dealerSttlementAccountId != null){
            settlementAccount = fissionDealerSettlementAccountService.getDealerSettlementAccountById(dealerSttlementAccountId);
        }else{
            settlementAccount = fissionDealerSettlementAccountService.getDealerLastSettlementAccount(dealerId);
        }
        return settlementAccount;
    }
    /**
     * 保存结算账户
     * @author HuangHao
     * @CreatTime 2021-03-12 17:20
     * @param request
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/saveaccdetail")
    @ResponseBody
    public TcResponse save(HttpServletRequest request, FissionDealerSettlementAccountDto settlementAccount){
        XxlUser user = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if(settlementAccount.getId() == null){
            settlementAccount.setCreateName(user.getEmpName());
        }else{
            settlementAccount.setUpdateName(user.getEmpName());
        }
        return fissionDealerSettlementAccountService.save(settlementAccount);
    }
    /**
     * 设置经销商结算账户类型
     * @author HuangHao
     * @CreatTime 2021-03-12 17:20
     * @param request
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/dealerAccountType")
    @ResponseBody
    public TcResponse dealerAccountType(HttpServletRequest request, FissionDealerSettlementAccountDto settlementAccount) {
        XxlUser user = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (settlementAccount.getId() == null) {
            settlementAccount.setCreateName(user.getEmpName());
        } else {
            settlementAccount.setUpdateName(user.getEmpName());
        }
        //允许使用个人账户
        if (BrankAccountType.ACCOUNT_TYPE_2.getCode().equals(settlementAccount.getAccountType())) {
            CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(settlementAccount.getDealerId(), DealerFinancialAccountTypeEnum.AccountType1.getType());
            //如果还没有个人账户则新增一条
            if (csDealerFinancial == null) {
                CsDealerFinancial dealerFinancial = new CsDealerFinancial();
                dealerFinancial.setDealerId(settlementAccount.getDealerId());
                dealerFinancial.setAccountType(DealerFinancialAccountTypeEnum.AccountType1.getType());
                dealerFinancial.setCreateUserName(user.getEmpName());
                dealerFinancial.setCreateDt(new Date());
                csDealerFinancialService.addOrUpdateFinancial(dealerFinancial);
                settlementAccount.setBankName("");
                settlementAccount.setBankCardNumber("");
                settlementAccount.setBankCardName("");
                settlementAccount.setBankAddress("");
                settlementAccount.setDutyParagraph("");
            } else {
                settlementAccount.setBankName(csDealerFinancial.getCardBank() == null ? "" : csDealerFinancial.getCardBank());
                settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber() == null ? "" : csDealerFinancial.getCardNumber());
                settlementAccount.setBankCardName(csDealerFinancial.getCompany() == null ? "" : csDealerFinancial.getCompany());
                settlementAccount.setBankAddress("");
                settlementAccount.setDutyParagraph("");
            }
            fissionDealerSettlementAccountService.addOrUpdate(settlementAccount);
        } else if (BrankAccountType.ACCOUNT_TYPE_1.getCode().equals(settlementAccount.getAccountType())) {
            CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(settlementAccount.getDealerId(), DealerFinancialAccountTypeEnum.AccountType0.getType());
            if (csDealerFinancial == null) {
                settlementAccount.setBankCardNumber("");
            } else {
                settlementAccount.setBankName(csDealerFinancial.getCardBank());
                settlementAccount.setBankAddress(csDealerFinancial.getCardBankAddress());
                settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber());
                settlementAccount.setDutyParagraph(csDealerFinancial.getDutyParagraph());
                settlementAccount.setBankCardName("");
            }
            fissionDealerSettlementAccountService.addOrUpdate(settlementAccount);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
    }

    /**
     * 同步结算帐号
     * @param request
     * @param settlementAccount
     * @return
     */
    @RequestMapping("/syncDealerAccount")
    @ResponseBody
    public TcResponse syncDealerAccount(HttpServletRequest request, FissionDealerSettlementAccountDto settlementAccount){
        XxlUser user = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (settlementAccount.getId() == null) {
            settlementAccount.setCreateName(user.getEmpName());
        } else {
            settlementAccount.setUpdateName(user.getEmpName());
        }
        return fissionDealerSettlementAccountService.syncDealerSettlement(settlementAccount);
    }

    /**
     * 获取经销商的基础财务信息
     * @author HuangHao
     * @CreatTime 2021-04-23 15:56
     * @param id
     * @return com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial
     */
    @RequestMapping("/getDealerSettlementAccount")
    @ResponseBody
    public FissionDealerSettlementAccount getCsDealerFinancialById(Integer id){
        return fissionDealerSettlementAccountService.getDealerSettlementAccountById(id);
    }
    /**
     * 更新经销商的财务信息
     * @author HuangHao
     * @CreatTime 2021-04-23 15:57
     * @param fissionDealerSettlementAccount
     * @return int
     */
    @RequestMapping("/addOrUpdateFinancial")
    @ResponseBody
    public TcResponse addOrUpdateFinancial(FissionDealerSettlementAccount fissionDealerSettlementAccount){
        return fissionDealerSettlementAccountService.addOrUpdate(fissionDealerSettlementAccount);
    }

    /**
     * excel导入经销商账户
     * @author HuangHao
     * @CreatTime 2021-04-07 17:58
     * @param request
     * @param file
     * @param fissionId
     * @param fissionActivityName
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/uploaddealersettlementaccount")
    @ResponseBody
    public TcResponse uploadDealerSettlementAccount(HttpServletRequest request, @RequestParam("file") MultipartFile file,Integer fissionId, String fissionActivityName) throws Exception {
        String name = file.getOriginalFilename();
        if (!RefuelCardExcelUtil.isExcel(name)) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "只支持.xls和.xlsx的excel文件");
        }
        XxlUser xxlUser = getLoginUser(request);
        DealerSettlementAccountExcelService.ExcelList<FissionDealerSettlementAccount> excelList = dealerSettlementAccountExcelService.analysis(file,fissionId,fissionActivityName, xxlUser.getEmpName(),fissionDealerSettlementAccountService.getMapByFissionId(fissionId));
        //有效数据
        List<FissionDealerSettlementAccount> list = excelList.getData();
        int successNum = fissionDealerSettlementAccountService.batchInsert(list);
        //成功条数
        //int successNum = insertNum;
        String br = "<br>";
        List<String> invalidData = excelList.getInvalidData();
        StringBuilder msg = new StringBuilder();
        msg.append("上传总数：");
        msg.append(excelList.getTotal() > 0 ? excelList.getTotal() - 1 : 0);
        msg.append("条");
        msg.append(br);
        msg.append("上传成功：");
        msg.append(successNum);
        msg.append("条，上传失败：");
        //失败条数
        int invalidNum = invalidData.size();
        msg.append(invalidNum);
        msg.append("条");
        msg.append(br);
        if (invalidNum > 0) {
            msg.append("失败原因：");
            msg.append(br);
            for (int i = 0; i < invalidNum; i++) {
                msg.append(invalidData.get(i));
                msg.append(br);
            }
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), msg.toString());

    }

   /* @RequestMapping("/exportDealerSettlementAccount")
    public void exportDealerSettlementAccount(HttpServletRequest request, HttpServletResponse response, FissionGoodsOrderDto goodsOrderDto) {
        try {
            PageResult<FissionGoodsOrderDto> pageResult = new PageResult<>();
            pageResult.setPage(1);
            pageResult.setLimit(10000);
            PageResult result = getFissionGoodsList(pageResult,  goodsOrderDto);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(result.getData())) {
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
                ExportExcel.exportExcel(fissionActivityById.getActivityName()+"-用户订单.xls", titleValueMap, result.getData(), request, response);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsOrderController","export", "导出 error", e);
        }
    }*/
}
