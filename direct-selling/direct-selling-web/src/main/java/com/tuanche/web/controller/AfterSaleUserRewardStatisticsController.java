package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleRewardRecordService;
import com.tuanche.directselling.api.AfterSaleUserRewardStatisticsService;
import com.tuanche.directselling.dto.AfterSaleActivityChannelStatisticsDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto;
import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDetailDto;
import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDto;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.ExportExcelMultipleSheet;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/afterSale/user/reward")
public class AfterSaleUserRewardStatisticsController {

    @Reference
    private AfterSaleUserRewardStatisticsService afterSaleUserRewardStatisticsService;
    @Reference
    private AfterSaleActivityService afterSaleActivityService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private AfterSaleRewardRecordService afterSaleRewardRecordService;

    @RequestMapping("/toStatisticsList")
    public String toStatisticsList(ModelMap modelMap) {
//        AfterSaleActivity afterSaleActivity = new AfterSaleActivity();
//        List<AfterSaleActivity> afterSaleActivities = afterSaleActivityService.selectActivityList(afterSaleActivity);
//        List<CsCompanySimpleInfo> dealerSimpleInfos = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(afterSaleActivities)) {
//            List<Integer> dealerIds = afterSaleActivities.stream().map(AfterSaleActivity::getDealerId).collect(Collectors.toList());
//            CsCompany company = new CsCompany();
//            company.setDealerIdList(dealerIds);
//            dealerSimpleInfos = companyBaseService.getDealerSimpleInfo(company);
//        }
        List<AfterSaleConstants.UserType> usetTypeList = Arrays.asList(AfterSaleConstants.UserType.values().clone());
        modelMap.addAttribute("usetTypeList", usetTypeList);
//        modelMap.addAttribute("dealerSimpleInfos", dealerSimpleInfos);
//        modelMap.addAttribute("afterSaleActivities", afterSaleActivities);
        return "after_sale/stat/user_reward_sata";
    }

    @RequestMapping("/getStatisticsList")
    @ResponseBody
    public PageResult getStatisticsList(PageResult<AfterSaleUserRewardStatisticsDto> page, AfterSaleUserRewardStatisticsDto rewardStatistics) {
        PageResult<AfterSaleUserRewardStatisticsDto> pageResult = afterSaleUserRewardStatisticsService.getStatisticsListByPage(page, rewardStatistics);
        return pageResult;
    }
    @RequestMapping("/getStatisticsCount")
    @ResponseBody
    public ResultDto getStatisticsCount(AfterSaleUserRewardStatisticsDto rewardStatistics) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        try {
            Map<String, Object> map = afterSaleUserRewardStatisticsService.getStatisticsCountMap(rewardStatistics);
            if (map!=null && map.size()>0) {
                dto.setResult(map);
            } else {
                dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
            }
        } catch (Exception e) {
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(StatusCodeEnum.ERROR.getText());
        }
        return dto;
    }

    @RequestMapping("/toUserRewardDetailListByPage")
    public String toUserRewardDetailListByPage(ModelMap modelMap, AfterSaleUserRewardStatisticsDto rewardStatisticsDto) {
        modelMap.addAttribute("rewardStatisticsDto", rewardStatisticsDto);
        return "after_sale/stat/user_reward_sata_detail";
    }

    @RequestMapping("/getUserRewardDetailListByPage")
    @ResponseBody
    public PageResult getUserRewardDetailListByPage(PageResult<AfterSaleUserRewardStatisticsDetailDto> page, Integer activityId, String userWxUnionId, String activityName, String dealerName) {
        PageResult<AfterSaleUserRewardStatisticsDetailDto> detailListByPage = afterSaleRewardRecordService.getUserRewardDetailListByPage(page, activityId, userWxUnionId);
        /*if (detailListByPage!=null && CollectionUtils.isNotEmpty(detailListByPage.getData())) {
            detailListByPage.getData().forEach(v->{
                v.setActivityName(activityName);
                v.setDealerName(dealerName);
            });
        }*/
        return detailListByPage;
    }

    /**
     * @description : C???????????????????????????
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/8/23 22:03
     */
    @RequestMapping("/rewardExport")
    public void rewardExport(HttpServletRequest request, HttpServletResponse response, AfterSaleUserRewardStatisticsDto orderSearchDto) {
        try {
            PageResult<AfterSaleUserRewardStatisticsDto> page = new PageResult<>();
            page.setLimit(10000);
            PageResult statisticsList = getStatisticsList(page, orderSearchDto);
            if (statisticsList!=null && CollectionUtils.isNotEmpty(statisticsList.getData())) {
                Map<String, String> titleValueMap = new LinkedHashMap<>();
                titleValueMap.put("ActivityName", "????????????");
                titleValueMap.put("DealerName", "???????????????");
                titleValueMap.put("LicencePlate", "???????????????");
                titleValueMap.put("UserPhone", "?????????");
                titleValueMap.put("UserTypeName", "??????");
                titleValueMap.put("PurchaseReward", "????????????");
                titleValueMap.put("PayTime", "????????????");
                titleValueMap.put("TradeNo", "?????????????????????");
                titleValueMap.put("InviteesNum", "????????????");
                titleValueMap.put("ShareReward", "????????????");
                titleValueMap.put("ExtraReward", "????????????");
                titleValueMap.put("RewardSun", "????????????");
                titleValueMap.put("WriteOffGiftCertificatesTotalName", "???????????????");
                titleValueMap.put("PaymentSuccessfulAmount", "???????????????");
                titleValueMap.put("PaymentFailureAmount", "???????????????");
                String fileName = "C????????????????????????.xls";
                ExportExcel.exportExcel(fileName, titleValueMap, statisticsList.getData(), request, response);
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, "ebs_web","AfterSaleUserRewardStatisticsController","rewardExport", "C???????????????????????????error", e, JSON.toJSONString(orderSearchDto));
        }
    }

    /**
     * @description : C?????????????????????????????????
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/8/23 22:04
     */
    @RequestMapping("/detailExport")
    public void detailExport(HttpServletRequest request, HttpServletResponse response,AfterSaleUserRewardStatisticsDto rewardStatisticsDto) {
        try {
            PageResult<AfterSaleUserRewardStatisticsDetailDto> page = new PageResult<>();
            page.setLimit(10000);
            PageResult statisticsList = getUserRewardDetailListByPage(page, rewardStatisticsDto.getActivityId(), rewardStatisticsDto.getUserWxUnionId(), rewardStatisticsDto.getActivityName(),rewardStatisticsDto.getDealerName());
            if (statisticsList!=null && CollectionUtils.isNotEmpty(statisticsList.getData())) {

                List<ExportExcelMultipleSheet> sheetList = new LinkedList<>();

                Map<String, String> titleValueMap = new LinkedHashMap<>();
                titleValueMap.put("NickName", "????????????");
                titleValueMap.put("UserWxUnionId", "??????id");
                titleValueMap.put("LicencePlate", "???????????????");
                titleValueMap.put("UserPhone", "?????????");
                titleValueMap.put("PayTime", "????????????");
                titleValueMap.put("UserTypeStr", "????????????");
                titleValueMap.put("InvitationReward", "????????????");
                titleValueMap.put("ExtraReward", "????????????");
                /*//??????????????????????????????
                ExportExcelMultipleSheet sheet2 = new ExportExcelMultipleSheet();
                sheet2.setList(statisticsList.getData());
                sheet2.setTitleValueMap(titleValueMap);
                sheet2.setSheetName("????????????");
                sheetList.add(sheet2);



                Map<String, String> titleValueMap2 = new LinkedHashMap<>();
                titleValueMap2.put("NickName", "????????????");
                titleValueMap2.put("UserWxUnionId", "??????id");
                titleValueMap2.put("LicencePlate", "???????????????");
                titleValueMap2.put("UserPhone", "?????????");
                titleValueMap2.put("PayTime", "????????????");
                titleValueMap2.put("UserTypeStr", "????????????");
                titleValueMap2.put("InvitationReward", "????????????");
                titleValueMap2.put("ExtraReward", "????????????");
                ExportExcelMultipleSheet sheet1 = new ExportExcelMultipleSheet();
                sheet1.setList(list);
                sheet1.setTitleValueMap(getActivityTitleValueMap());
                sheet1.setSheetName("????????????");
                sheetList.add(sheet1);*/


                String fileName = rewardStatisticsDto.getLicencePlate()+"?????????????????????.xls";
                ExportExcel.exportExcel(fileName, titleValueMap, statisticsList.getData(), request, response);
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, "ebs_web","AfterSaleUserRewardStatisticsController","detailExport", "C?????????????????????????????????error", e, JSON.toJSONString(rewardStatisticsDto));

        }
    }

}
