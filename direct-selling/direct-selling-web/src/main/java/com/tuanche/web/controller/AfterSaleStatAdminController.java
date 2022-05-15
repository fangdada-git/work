package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleActivityStatisticsService;
import com.tuanche.directselling.api.AfterSaleStatService;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.AfterSaleActivityDataBaseVo;
import com.tuanche.directselling.vo.AfterSaleActivityDataVerifyVo;
import com.tuanche.eap.api.bean.manufacturer.CsCompany;
import com.tuanche.eap.api.dto.manufacturer.CsCompanyDto;
import com.tuanche.eap.api.service.manufacturer.CsCompanyService;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.ExportExcelCallback;
import com.tuanche.web.util.ExportExcelMultipleSheet;
import com.tuanche.web.vo.AfterSaleActivityDataSumVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/20 13:36
 **/
@Controller
@RequestMapping("/afterSale/stat")
public class AfterSaleStatAdminController {

    @Reference
    AfterSaleStatService afterSaleStatService;
    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Reference
    AfterSaleActivityStatisticsService afterSaleActivityStatisticsService;
    @Reference
    CsCompanyService csCompanyService;

    /**
     * 活动数据页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/activityDataPage")
    public String page(ModelMap model) {
        return "after_sale/stat/activity_data";
    }


    @RequestMapping("/activityDataSum")
    @ResponseBody
    public ResultDto activityDataSum(AfterSaleActivityDataBaseVo afterSaleActivityDataBaseVo) {
        ResultDto resultDto = new ResultDto();
        AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto = new AfterSaleActivityDataBaseDto();
        BeanUtils.copyProperties(afterSaleActivityDataBaseVo, afterSaleActivityDataBaseDto);
        AfterSaleActivityDataVerifyDto afterSaleActivityDataVerifyDto = afterSaleStatService.selectAfterSaleActivityDataVerifySum(afterSaleActivityDataBaseDto);
        if (afterSaleActivityDataVerifyDto == null) {
            resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
            resultDto.setResult(0);
            return resultDto;
        }
        AfterSaleActivityDataSumVo dataSumVo = new AfterSaleActivityDataSumVo();
        dataSumVo.setUv(afterSaleActivityDataVerifyDto.getUv());
        if (afterSaleActivityDataVerifyDto.getUv() == 0) {
            dataSumVo.setPromotionPercent(BigDecimal.ZERO);
        } else {
            //推广转化率  推广卡售卖数（包括退款） / 点击人数
            dataSumVo.setPromotionPercent(new BigDecimal(String.valueOf(afterSaleActivityDataVerifyDto.getPromotionCardTotal().floatValue() / afterSaleActivityDataVerifyDto.getUv().floatValue())));
        }
        if (afterSaleActivityDataVerifyDto.getPromotionCardTotal() == 0) {
            dataSumVo.setVisitorsPercent(BigDecimal.ZERO);
        } else {
            //到店率 = 到店人数 / 推广卡售出数（包括退款）
            dataSumVo.setVisitorsPercent(new BigDecimal(String.valueOf(afterSaleActivityDataVerifyDto.getVisitorsTotal().floatValue() / afterSaleActivityDataVerifyDto.getPromotionCardTotal().floatValue())));
        }

        if (afterSaleActivityDataVerifyDto.getVisitorsTotal() == 0) {
            dataSumVo.setPackagePercent(BigDecimal.ZERO);
        } else {
            //套餐转化率 = 套餐售出数 / 到店人数
            dataSumVo.setPackagePercent(new BigDecimal(String.valueOf(afterSaleActivityDataVerifyDto.getPackageCardTotal().floatValue() / afterSaleActivityDataVerifyDto.getVisitorsTotal().floatValue())));
        }
        dataSumVo.setPromotionCardNetRevenue(afterSaleActivityDataVerifyDto.getPromotionCardNetRevenue());
        dataSumVo.setPackageCardAmount(afterSaleActivityDataVerifyDto.getPackageCardAmount());
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        resultDto.setResult(dataSumVo);
        return resultDto;
    }

    /**
     * 活动数据
     *
     * @return
     */
    @RequestMapping("/activityDataList")
    @ResponseBody
    public PageResult list(Integer page, Integer limit, AfterSaleActivityDataBaseVo afterSaleActivityDataBaseVo) {
        AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto = new AfterSaleActivityDataBaseDto();
        BeanUtils.copyProperties(afterSaleActivityDataBaseVo, afterSaleActivityDataBaseDto);
        PageResult pageResult = afterSaleStatService.selectAfterSaleActivityDataVerifyPage(page, limit, afterSaleActivityDataBaseDto);
        /*List<AfterSaleActivityDataVerifyDto> data = pageResult.getData();
        List<AfterSaleActivityDataVerifyVo> result = new ArrayList<>();
        AfterSaleActivityDataVerifyVo vo = null;
        for (AfterSaleActivityDataVerifyDto dto : data) {
            vo = new AfterSaleActivityDataVerifyVo();
            BeanUtils.copyProperties(dto, vo);
            if (dto.getUv() == 0) {
                vo.setPromotionPercent(BigDecimal.ZERO);
            } else {
                //推广转化率  推广卡售卖数（包括退款） / 点击人数
                vo.setPromotionPercent(new BigDecimal(String.valueOf(dto.getPromotionCardTotal().floatValue() / dto.getUv().floatValue())));
            }
            if (dto.getPromotionCardTotal() == 0) {
                vo.setVisitorsPercent(BigDecimal.ZERO);
                vo.setRefundPercent(BigDecimal.ZERO);
                vo.setAgentFissionTotalPercent(BigDecimal.ZERO);
                vo.setAutonomousFissionTotalPercent(BigDecimal.ZERO);
                vo.setKeepOnRecordUserTotalPercent(BigDecimal.ZERO);
            } else {
                //到店率 = 到店人数 / 推广卡售出数（包括退款）
                vo.setVisitorsPercent(new BigDecimal(String.valueOf(dto.getVisitorsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //退款率 = 总退款数 / 推广卡售出数（包括退款）
                vo.setRefundPercent(new BigDecimal(String.valueOf(dto.getPromotionCardRefundsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
//                //代理人裂变占比 =（代理人直接裂变的+代理人2级裂变的）/ 总售出推广卡人数
//                vo.setAgentFissionTotalPercent(new BigDecimal(String.valueOf((dto.getAgentFissionTotalOne().floatValue() + dto.getAgentFissionTotalTwo().floatValue()) / dto.getPromotionCardTotal().floatValue())));
//                //自主占比 = 自主裂变人数 / 总售出推广卡人数
//                vo.setAutonomousFissionTotalPercent(new BigDecimal(String.valueOf(dto.getAutonomousFissionTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //备案人数占比 = 备案人购买数 / 总售出推广卡人数
//                vo.setKeepOnRecordUserTotalPercent(new BigDecimal(String.valueOf(dto.getKeepOnRecordUserTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
            }
            if (dto.getVisitorsTotal() == 0) {
                vo.setPackagePercent(BigDecimal.ZERO);
            } else {
                //套餐转化率 = 套餐售出数 / 到店人数
                vo.setPackagePercent(new BigDecimal(String.valueOf(dto.getPackageCardTotal().floatValue() / dto.getVisitorsTotal().floatValue())));
            }

            result.add(vo);
        }
        pageResult.setData(result);*/
        pageResult.setCode(0);
        return pageResult;
    }

    /**
     * 活动数据导出
     *
     * @return
     */
    @RequestMapping("/activityDataExport")
    public void activityDataExport(HttpServletRequest request, HttpServletResponse response, AfterSaleActivityDataBaseVo afterSaleActivityDataBaseVo) {
        AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto = new AfterSaleActivityDataBaseDto();
        BeanUtils.copyProperties(afterSaleActivityDataBaseVo, afterSaleActivityDataBaseDto);
        List<AfterSaleActivityDataVerifyVo> dataVerifyDtos = afterSaleStatService.selectAfterSaleActivityDataVerify(afterSaleActivityDataBaseDto);
        /*List<AfterSaleActivityDataVerifyVo> result = new ArrayList<>();
        AfterSaleActivityDataVerifyVo vo = null;
        for (AfterSaleActivityDataVerifyVo dto : dataVerifyDtos) {
            vo = new AfterSaleActivityDataVerifyVo();
            BeanUtils.copyProperties(dto, vo);
            if (dto.getUv() == 0) {
                vo.setPromotionPercent(BigDecimal.ZERO);
            } else {
                //推广转化率  推广卡售卖数（包括退款） / 点击人数
                vo.setPromotionPercent(new BigDecimal(String.valueOf(dto.getPromotionCardTotal().floatValue() / dto.getUv().floatValue())));
            }
            if (dto.getPromotionCardTotal() == 0) {
                vo.setVisitorsPercent(BigDecimal.ZERO);
                vo.setRefundPercent(BigDecimal.ZERO);
                vo.setAgentFissionTotalPercent(BigDecimal.ZERO);
                vo.setAutonomousFissionTotalPercent(BigDecimal.ZERO);
                vo.setKeepOnRecordUserTotalPercent(BigDecimal.ZERO);
            } else {
                //到店率 = 到店人数 / 推广卡售出数（包括退款）
                vo.setVisitorsPercent(new BigDecimal(String.valueOf(dto.getVisitorsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //退款率 = 总退款数 / 推广卡售出数（包括退款）
                vo.setRefundPercent(new BigDecimal(String.valueOf(dto.getPromotionCardRefundsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //备案人数占比 = 备案人购买数 / 总售出推广卡人数
                vo.setKeepOnRecordUserTotalPercent(new BigDecimal(String.valueOf(dto.getKeepOnRecordUserTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
            }
            if (dto.getVisitorsTotal() == 0) {
                vo.setPackagePercent(BigDecimal.ZERO);
            } else {
                //套餐转化率 = 套餐售出数 / 到店人数
                vo.setPackagePercent(new BigDecimal(String.valueOf(dto.getPackageCardTotal().floatValue() / dto.getVisitorsTotal().floatValue())));
            }

            result.add(vo);
        }*/
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("ActivityName", "活动名");
        titleValueMap.put("DealerName", "所属车商");
        titleValueMap.put("ActivityStatus", "活动状态");
        titleValueMap.put("SaleStartTime", "开始时间");
        titleValueMap.put("OfflineConvertEndTime", "线下结束时间");
        titleValueMap.put("Uv", "点击人数");
        titleValueMap.put("PromotionCardTotal", "推广卡售出数");
        titleValueMap.put("PromotionCardAmount", "推广售出金额");
        titleValueMap.put("PromotionPercent", "推广转化率");
        titleValueMap.put("PromotionCardRefundsTotal", "总退款数");
        titleValueMap.put("PromotionCardRefundsAmount", "退款金额");
        titleValueMap.put("RefundPercent", "退款率");
        titleValueMap.put("VisitorsTotal", "到店人数");
        titleValueMap.put("VisitorsPercent", "到店率");
        titleValueMap.put("PromotionCardNetRevenue", "推广期净盈余");
        titleValueMap.put("PackageCardTotal", "套餐卡售出数");
        titleValueMap.put("PackageCardAmount", "套餐卡售金额");
        titleValueMap.put("PackagePercent", "套餐转化率");
        /*Map<String, ExportExcelCallback> callBackMap = new HashMap<>(16);
        callBackMap.put("OnState", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            if (object.getOnState() == 0) {
                return "未开始";
            }
            Date now = new Date();
            if (object.getSaleStartTime().after(now)) {
                return "未开始";
            }
            if (object.getSaleStartTime().before(now) && object.getOfflineConvertEndTime().after(now)) {
                return "进行中";
            }
            if (object.getOfflineConvertEndTime().before(now)) {
                return "已结束";
            }
            return "未开始";
        });
        BigDecimal hundred = new BigDecimal("100");
        DecimalFormat df = new DecimalFormat("0.00");
        callBackMap.put("PromotionPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getPromotionPercent().multiply(hundred)) + "%";
        });
        callBackMap.put("PackagePercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getPackagePercent().multiply(hundred)) + "%";
        });
        callBackMap.put("RefundPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getRefundPercent().multiply(hundred)) + "%";
        });
        callBackMap.put("VisitorsPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getVisitorsPercent().multiply(hundred)) + "%";
        });
        callBackMap.put("AgentFissionTotalPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getAgentFissionTotalPercent().multiply(hundred)) + "%";
        });
        callBackMap.put("AutonomousFissionTotalPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getAutonomousFissionTotalPercent().multiply(hundred)) + "%";
        });
        callBackMap.put("KeepOnRecordUserTotalPercent", (ExportExcelCallback<AfterSaleActivityDataVerifyVo>) object -> {
            return df.format(object.getKeepOnRecordUserTotalPercent().multiply(hundred)) + "%";
        });*/
        ExportExcel.exportExcel("活动数据导出.xls", titleValueMap, dataVerifyDtos, request, response);
    }


    /**
     * 活动统计详情
     * @author HuangHao
     * @CreatTime 2021-09-13 16:58
     * @param activityId
     * @return com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto
     */
    @RequestMapping("/activityStatisticsDetail")
    public String activityStatisticsDetail(ModelMap model,Integer activityId) {
        model.addAttribute("activityData", afterSaleStatService.getActivityDataDetailDto(activityId));
        return "after_sale/stat/activity_data_detail";
    }


    /**
     * 活动详情数据导出
     *
     * @return
     */
    @RequestMapping("/activityDataDetailExport")
    public void activityDataDetailExport(HttpServletRequest request, HttpServletResponse response,Integer activityId){
        AfterSaleActivityDataDetailDto activityDataDetailDto = afterSaleStatService.getActivityDataDetailDto(activityId);
        List<AfterSaleActivityDataDetailDto> list = new ArrayList<>();
        list.add(activityDataDetailDto);
        List<ExportExcelMultipleSheet> sheetList = new LinkedList<>();

        //活动渠道统计信息列表
        List<AfterSaleActivityChannelStatisticsDto> channelList = activityDataDetailDto.getChannelList();
        ExportExcelMultipleSheet sheet2 = new ExportExcelMultipleSheet();
        sheet2.setList(channelList);
        sheet2.setTitleValueMap(getActivityChannelTitleValueMap());
        sheet2.setSheetName("渠道数据");
        sheetList.add(sheet2);

        ExportExcelMultipleSheet sheet1 = new ExportExcelMultipleSheet();
        sheet1.setList(list);
        sheet1.setTitleValueMap(getActivityTitleValueMap());
        sheet1.setSheetName("活动数据");
        sheetList.add(sheet1);


        ExportExcel.multipleSheet("活动数据详情导出.xls", sheetList, request, response);
    }

    @RequestMapping("/dealer")
    @ResponseBody
    public List<AfterSaleActivityDealerDto> dealer() {
        List<AfterSaleActivityDealerDto> afterSaleDealer = afterSaleActivityService.getAfterSaleDealer();
        return afterSaleDealer;
    }

    @RequestMapping("/activity")
    @ResponseBody
    public List<AfterSaleActivityDto> activity(@RequestParam(required = false) Integer dealerId) {
        List<AfterSaleActivityDto> afterSaleDealer = afterSaleActivityService.getAfterSaleActivityByDealerId(dealerId);
        return afterSaleDealer;
    }

    public Map<String, String> getActivityChannelTitleValueMap(){
        Map<String, String> titleValueMap2 = new LinkedHashMap<>(32);
        titleValueMap2.put("ChannelName", "渠道");
        titleValueMap2.put("PromotionCardTotal", "推广卡售出数");
        titleValueMap2.put("PromotionCardsSellPercent", "售出占比");
        titleValueMap2.put("PrimaryFissionTotal", "直接转化人数");
        titleValueMap2.put("BeyondPrimaryFissionTotal", "转介绍人数");
        titleValueMap2.put("VisitorsTotal", "到店数");
        titleValueMap2.put("PromotionCardsWrittenOffTotal", "核销推广卡数");
        titleValueMap2.put("PromotionCardRefundsTotal", "渠道总退款数");
        titleValueMap2.put("PackageCardTotal", "套餐卡售出数");
        titleValueMap2.put("PackagePercent", "套餐转化率");
        return titleValueMap2;
    }
    public Map<String, String> getActivityTitleValueMap(){
        Map<String, String> titleValueMap = new LinkedHashMap<>(64);
        titleValueMap.put("ActivityName", "活动名称");
        titleValueMap.put("DealerName", "所属经销商");
        titleValueMap.put("ActivityStatus", "活动状态");
        titleValueMap.put("SaleStartTime", "活动开始时间");
        titleValueMap.put("SaleEndTime", "活动结束时间");
        titleValueMap.put("OfflineConvertStartTime", "线下开始时间");
        titleValueMap.put("OfflineConvertEndTime", "线下结束时间");
        titleValueMap.put("Uv", "推广页访问总人数");
        titleValueMap.put("BrowseTotal", "推广页浏览总次数");
        titleValueMap.put("PromotionCardAmount", "推广卡售出金额");
        titleValueMap.put("RewardTotal", "奖励总金额");
        titleValueMap.put("PromotionCardRefundsAmount", "退款总金额");
        titleValueMap.put("PromotionCardNetRevenue", "推广期盈余");
        titleValueMap.put("PromotionCardTotal", "推广卡售出数");
        titleValueMap.put("PromotionPercent", "推广转化率");
        titleValueMap.put("PackageCardTotal", "套餐售出数");
        titleValueMap.put("PackageCardAmount", "套餐售金额");
        titleValueMap.put("PackagePercent", "套餐转化率");
        titleValueMap.put("PromotionCardRefundsTotal", "总退款数");
        titleValueMap.put("RefundPercent", "退款率");
        titleValueMap.put("PromotionCardRefundsTotalPassive", "自动退款备案用户数");
        titleValueMap.put("PrimaryFissionTotal", "直接转化人数");
        titleValueMap.put("PrimaryFissionTotalPercent", "直接转化占比");
        titleValueMap.put("BeyondPrimaryFissionTotal", "转介绍人数");
        titleValueMap.put("BeyondPrimaryFissionTotalPercent", "转介绍占比");
        titleValueMap.put("LostUserTotal", "流失客户购买数");
        titleValueMap.put("LostUserTotalPercent", "流失客户购买占比");
        titleValueMap.put("KeepOnRecordUserTotal", "备案客户购买数");
        titleValueMap.put("KeepOnRecordUserTotalPercent", "备案客户购占比");
        titleValueMap.put("KeepOnRecordFinishUserTotal", "备案客户完成裂变数");
        titleValueMap.put("KeepOnRecordFinishUserTotalPercent", "备案客户完成裂变占比");
        titleValueMap.put("GiftCertificatesTotal", "发放礼品券数");
        titleValueMap.put("WriteOffGiftVoucherTotal", "核销礼品券");
        titleValueMap.put("VisitorsTotal", "到店人数");
        titleValueMap.put("VisitorsPercent", "到店率");
        titleValueMap.put("PromotionCardsWaitingReleaseTotal", "推广卡待发券数");
        titleValueMap.put("PromotionCardsWrittenOffTotal", "核销推广卡数");
        titleValueMap.put("PromotionCardsWrittenOffTotalPercent", "核销推广卡率");
        return titleValueMap;
    }

}
