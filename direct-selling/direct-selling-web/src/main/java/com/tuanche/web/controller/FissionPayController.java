package com.tuanche.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.tuanche.arch.util.IntegerUtils;
import com.tuanche.commons.util.JSONUtil;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.api.FissionUserRewardService;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionUserReward;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.ExportExcel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: FissionPayController
 * @description: 裂变财务相关
 * @author: dudg
 * @create: 2020-09-27 14:10
 */
@Controller
@RequestMapping("/fission")
public class FissionPayController extends BaseController{

    @Reference
    FissionSaleService fissionSaleService;
    @Reference
    FissionUserRewardService userRewardService;
    @Reference
    FissionActivityService fissionActivityService;
    @Reference
    UserBaseService userBaseService;
    @Value("${fission_sale_audit_role:0}")
    private Integer fission_sale_audit_role;
    @Value("${fission_sale_pay_role:0}")
    private Integer fission_sale_pay_role;


    /**
     * @description: 销售付款审核列表
     * @param request
     * @return: java.lang.String
     * @author: dudg
     * @date: 2020/9/27 14:12
    */
    @RequestMapping("/salePayList")
    public String SalePayList(HttpServletRequest request, Model model) {
        FissionActivityDto activityDto = new FissionActivityDto();
        activityDto.setActivityStatus(2);
        List<FissionActivityDto> fissionActivityDtos = fissionActivityService.selectActivityDtoList(activityDto);
        model.addAttribute("activtiyList", fissionActivityDtos);

        return "fission/pay/salepay-list";
    }

    /**
     * @description: 获取活动详情信息
     * @param fissionId
     * @return: com.tuanche.commons.util.ResultDto
     * @author: dudg
     * @date: 2020/10/9 15:53
    */
    @RequestMapping("/getActivityDtoById")
    @ResponseBody
    public ResultDto selectActivityById(HttpServletRequest request, Integer fissionId){
        FissionActivityDto fissionActivityDto = fissionActivityService.getFissionActivityDtoById(fissionId);

        FissionSale fissionSale = new FissionSale();
        fissionSale.setFissionId(fissionId);
//        fissionSale.setFinancialAuditStatus(validateLoginRole(request));
        fissionActivityDto.setDivideUpPrizeSaleNum(fissionSaleService.selectSalePayListCountByFissionId(fissionSale));
        fissionActivityDto.setTotalReward(fissionSaleService.getSalePrizePoolByFissionId(fissionId));
        return success(fissionActivityDto);
    }

    /**
     * @description: 销售打款审核列表
     * @param pageResult
     * @param fissionSale
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/9/27 14:24
    */
    @RequestMapping("/searchSalePayList")
    @ResponseBody
    public PageResult searchSalePayList(HttpServletRequest request, PageResult<FissionSalePayDto> pageResult, FissionSalePayDto fissionSale) {
        if(!StringUtil.isEmpty(fissionSale.getSalePhone())){
            CsUser csUser = userBaseService.getCsUserByPhone(fissionSale.getSalePhone());
            fissionSale.setSaleId(csUser != null?csUser.getId():-1);
        }
//        fissionSale.setFinancialAuditStatus(validateLoginRole(request));
        PageResult pResult = fissionSaleService.selectSalePayListByFissionId(pageResult, fissionSale);
        return pResult;
    }

    /**
     * 验证角色
     * @param request
     * @return
     */
    private Integer validateLoginRole(HttpServletRequest request){
        XxlUser loginUser = getLoginUser(request);
        List<Integer> result = Lists.newArrayList();
        if(StringUtils.isNotEmpty(loginUser.getRoleIds())){
            String[] strArr = loginUser.getRoleIds().split(",");
            for (int i = 0; i < strArr.length; i++) {
                if(strArr[i].equals(fission_sale_audit_role.toString())){
                    return 0;
                }
                else if(strArr[i].equals(fission_sale_audit_role.toString())){
                    return 1;
                }
            }
        }

        return null;
    }

    /**
     * @description: 裂变活动销售（审核/打款/冻结/解冻）
     * @param request
     * @param fissionSalePayDto
     * @return: com.tuanche.commons.util.ResultDto
     * @author: dudg
     * @date: 2020/9/27 18:34
    */
    @RequestMapping("/salePayAudit")
    @ResponseBody
    public ResultDto salePayAudit(HttpServletRequest request, FissionSalePayDto fissionSalePayDto){
        try {
            XxlUser loginUser = getLoginUser(request);

            // 参数校验
            if (fissionSalePayDto.getAuditType() == null || fissionSalePayDto.getFissionId() == null) {
                return paramInvalid();
            }

            FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionSalePayDto.getFissionId());
            if (fissionActivity == null) {
                return dynamicResult(StatusCodeEnum.RESULE_DATA_NONE,null);
            }

            if(fissionActivity.getEndTime().after(new Date())){
                return dynamicResult(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动进行中,无法操作");
            }

            // 审核&打款操作
            if (Lists.newArrayList(1, 2).contains(fissionSalePayDto.getAuditType())) {
                if (IntegerUtils.isNotNullAndEquals(fissionSalePayDto.getAuditType(), 1)) {
                    fissionSalePayDto.setAuditName(loginUser.getEmpName());
                } else {
                    fissionSalePayDto.setPayName(loginUser.getEmpName());
                }
                fissionSalePayDto.setFinancialAuditStatus(fissionSalePayDto.getAuditType());
                fissionSaleService.financialAuditSalePay(fissionSalePayDto);
            }
            // 冻结&解冻操作
            else if (Lists.newArrayList(3, 4).contains(fissionSalePayDto.getAuditType())) {
                if (fissionSalePayDto.getSaleId() == null) {
                    return paramInvalid();
                }

                if (IntegerUtils.isNotNullAndEquals(fissionSalePayDto.getAuditType(), 3)) {
                    fissionSalePayDto.setDeleteFlag(true);
                } else {
                    fissionSalePayDto.setDeleteFlag(false);
                }
                fissionSalePayDto.setUpdateName(loginUser.getEmpName());
                fissionSalePayDto.setSaleIdList(Lists.newArrayList(fissionSalePayDto.getSaleId()));
                fissionSaleService.freezeOrUnFreezeFissionSale(fissionSalePayDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }

        return success();
    }

    /**
     * @description: C端用户奖励列表
     * @param request
     * @param model
     * @return: java.lang.String
     * @author: dudg
     * @date: 2020/10/9 17:34
    */
    @RequestMapping("/userRewardList")
    public String UserRewardList(HttpServletRequest request, Model model) {
        FissionActivityDto activityDto = new FissionActivityDto();
        activityDto.setOnState(GlobalConstants.FISSION_ON_STATUS_1);
        List<FissionActivityDto> fissionActivityDtos = fissionActivityService.selectActivityDtoList(activityDto);
        model.addAttribute("activtiyList", fissionActivityDtos);

        return "fission/pay/reward-list";
    }

    /**
     * @description: 获取活动C端奖励详情信息
     * @param fissionId
     * @return: com.tuanche.commons.util.ResultDto
     * @author: dudg
     * @date: 2020/10/9 15:53
     */
    @RequestMapping("/getActivityRewardDetailById")
    @ResponseBody
    public ResultDto selectActivityRewardDetailById(HttpServletRequest request, Integer fissionId){
        FissionActivityDto fissionActivityDto = fissionActivityService.getFissionActivityDtoById(fissionId);

        FissionUserReward userReward = new FissionUserReward();
        userReward.setFissionId(fissionId);
        fissionActivityDto.setTotalReward(userRewardService.selectTotalAmountByFissionId(userReward));
        return success(fissionActivityDto);
    }

    /**
     * @description: C端用户奖励列表
     * @param pageResult
     * @param fissionSale
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/9/27 14:24
     */
    @RequestMapping("/searchUserRewardList")
    @ResponseBody
    public PageResult searchUserRewardList(PageResult<FissionUserRewardDto> pageResult, FissionUserRewardDto userRewardDto) {
        userRewardDto.setOpenPage(true);
        PageResult pResult = userRewardService.selectUserRewardListByFissionId(pageResult,userRewardDto);
        return pResult;
    }

    /**
     * @description: C端奖励金列表导出
     * @param request
     * @param response
     * @param userRewardDto
     * @return: void
     * @author: dudg
     * @date: 2020/10/10 14:27
    */
    @RequestMapping("/rewardList/export")
    public void rewardListExport(HttpServletRequest request, HttpServletResponse response, FissionUserRewardDto userRewardDto) {
        try {
            PageResult<FissionUserRewardDto> dtoPageResult = userRewardService.selectUserRewardListByFissionId(new PageResult<FissionUserRewardDto>(), userRewardDto);
            Map<String, String> titleValueMap = new LinkedHashMap<String, String>();
            titleValueMap.put("UserWxUnionId", "微信UnionId");
            titleValueMap.put("ActivityName", "活动名称");
            titleValueMap.put("TotalAmount", "打款金额");
            if (CollectionUtil.isNotEmpty(dtoPageResult.getData())) {
                ExportExcel.exportExcel(MessageFormat.format("{0}-C端奖励金明细汇总.xls",dtoPageResult.getData().get(0).getActivityName()), titleValueMap, dtoPageResult.getData(), request, response);
            }
            else{
                ExportExcel.exportExcel("C端奖励金明细汇总.xls", titleValueMap, dtoPageResult.getData(), request, response);
            }

        } catch (Exception e) {
            CommonLogUtil.addError("C端奖励金列表导出，error","参数："+ JSONUtil.toJson(userRewardDto), e);
        }
    }
}
