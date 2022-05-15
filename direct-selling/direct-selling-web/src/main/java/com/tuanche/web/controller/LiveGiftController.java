package com.tuanche.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtils;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigStatusEnum;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.GiftRefuelingcardGiftrecordService;
import com.tuanche.directselling.api.GiftRefuelingcardPeriodsService;
import com.tuanche.directselling.api.GiftRefuelingcardService;
import com.tuanche.directselling.api.LiveSceneDealerConfigService;
import com.tuanche.directselling.dto.GiftRefuelingcardGiftrecordDto;
import com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto;
import com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto;
import com.tuanche.directselling.dto.LiveSceneDealerConfigDto;
import com.tuanche.directselling.model.GiftRefuelingcard;
import com.tuanche.directselling.model.GiftRefuelingcardPeriods;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsGiftNum;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.GiftRefuelingcardGiftrecordVo;
import com.tuanche.directselling.vo.GiftRefuelingcardPeriodsVo;
import com.tuanche.directselling.vo.LiveGiftConfigVo;
import com.tuanche.directselling.vo.LiveSceneDealerConfigVo;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.RefuelCardExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.tuanche.arch.util.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

/**
 * @ClassName: LiveSceneDealerController
 * @Description: 团车直卖-油卡controller
 * @Author: GongBo
 * @Date: 2020年5月27日15:12:25
 * @Version 1.0
 **/
@Controller
@RequestMapping("/gift")
public class LiveGiftController extends BaseController {

    @Reference
    GiftRefuelingcardGiftrecordService giftRefuelingcardGiftrecordService;
    @Reference
    GiftRefuelingcardPeriodsService giftRefuelingcardPeriodsService;
    @Autowired
    PeriodsService periodsService;
    @Reference
    GiftRefuelingcardService giftRefuelingcardService;
    @Reference
    LiveSceneDealerConfigService liveSceneDealerConfigService;

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-油卡列表页面
     * @Date 2020年5月27日15:13:11
     **/
    @RequestMapping("/toGiftPage")
    public String toGiftPage(ModelMap modelMap) {
        return "gift/list";
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡配置页面
     * @Date 2020年5月27日15:13:11
     **/
    @RequestMapping("/toGiftConfigPage")
    public String toGiftConfigPage(ModelMap modelMap) {
        modelMap.addAttribute("periodsList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        return "gift/gift-config-list";
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡统计页面
     * @Date 2020年5月27日15:13:11
     **/
    @RequestMapping("/toGiftStatisticPage")
    public String toGiftStatisticPage(ModelMap modelMap) {
        modelMap.addAttribute("periodsList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        return "gift/gift-statistic-list";
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-导入油卡页面
     * @Date 2020年5月27日15:13:11
     **/
    @RequestMapping("/toAddGiftPage")
    public String toAddGiftPage(ModelMap modelMap) {
        GiftRefuelingcardUsedStatisticsDto usedStatisticsDto = giftRefuelingcardService.refuelCardUsedStatistics();
        modelMap.addAttribute("total", usedStatisticsDto.getTotal());
        modelMap.addAttribute("unUsed", usedStatisticsDto.getUnUsed());
        return "gift/import-gift";
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-下载油卡模板
     * @Date 2020年5月27日15:13:11
     **/
    @RequestMapping("/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request) {
        try {
            //获取要下载的模板名称
            String fileName = "gift-import-template.xlsx";
            //设置要下载的文件的名称
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //通知客服文件的MIME类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //获取文件的路径
            String filePath = getClass().getResource("/static/tempfile/" + fileName).getPath();
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡配置数据加载
     * @Date 2020年5月27日15:13:07
     **/
    @RequestMapping("/searchGiftConfigList")
    @ResponseBody
    public PageResult searchGiftConfigList(@RequestBody LiveGiftConfigVo liveGiftConfigVo) {

        // 调用接口获取场次油卡配置列表
        GiftRefuelingcardPeriodsVo refuelingCardPeriodsVo = new GiftRefuelingcardPeriodsVo();
        if (null != liveGiftConfigVo.getPeriodsId()) {
            refuelingCardPeriodsVo.setPeriodsId(liveGiftConfigVo.getPeriodsId());
        }
        refuelingCardPeriodsVo.setPage(liveGiftConfigVo.getPage());
        refuelingCardPeriodsVo.setLimit(liveGiftConfigVo.getLimit());

        PageResult<GiftRefuelingcardPeriodsDto> result =
                giftRefuelingcardPeriodsService.getFuelCardPeriodsConfigListByPage(refuelingCardPeriodsVo);
        result.setCode("0");
        return result;
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次经销商油卡配置数据加载
     * @Date 2020年5月27日15:13:07
     **/
    @RequestMapping("/searchDealerGifList")
    @ResponseBody
    public PageResult searchDealerGifList(@RequestBody LiveGiftConfigVo liveGiftConfigVo) {

        // 调用接口获取场次油卡配置列表
        LiveSceneDealerConfigVo liveSceneDealerConfigVo = new LiveSceneDealerConfigVo();
        liveSceneDealerConfigVo.setPeriodsId(liveGiftConfigVo.getPeriodsId());
        if (null != liveGiftConfigVo.getCityId()) {
            liveSceneDealerConfigVo.setCityId(liveGiftConfigVo.getCityId());
        }
        if (null != liveGiftConfigVo.getCompanyName()) {
            liveSceneDealerConfigVo.setDealerName(liveGiftConfigVo.getCompanyName());
        }

        PageResult result = new PageResult<>();
        result.setData(liveSceneDealerConfigService.getLiveSceneDealerConfigList(liveSceneDealerConfigVo));
        result.setCode("0");
        return result;
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-油卡数据加载
     * @Date 2020年5月27日15:13:07
     **/
    @RequestMapping("/searchGiftList")
    @ResponseBody
    public PageResult searchDealerList(@RequestBody LiveGiftConfigVo liveGiftConfigVo) {

        // 调用接口获取油卡列表
        GiftRefuelingcardGiftrecordVo giftRecordVo = new GiftRefuelingcardGiftrecordVo();
        if (StringUtils.isNotEmpty(liveGiftConfigVo.getCompanyName())) {
            giftRecordVo.setDealerName(liveGiftConfigVo.getCompanyName());
        }
        giftRecordVo.setPage(liveGiftConfigVo.getPage());
        giftRecordVo.setLimit(liveGiftConfigVo.getLimit());

        PageResult<GiftRefuelingcardGiftrecordDto> giftRecordDtoPageResult =
                giftRefuelingcardGiftrecordService.getGiftRefuelingcardGiftrecordByPage(giftRecordVo);
        giftRecordDtoPageResult.setCode("0");
        return giftRecordDtoPageResult;
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡统计加载
     * @Date 2020年5月27日15:13:07
     **/
    @RequestMapping("/searchGiftStatisticList")
    @ResponseBody
    public PageResult searchGiftStatisticList(@RequestBody LiveGiftConfigVo liveGiftConfigVo) {

        // 调用接口获取场次油卡配置列表
        LiveSceneDealerConfigVo liveSceneDealerConfigVo = new LiveSceneDealerConfigVo();
        if (null != liveGiftConfigVo.getPeriodsId()) {
            liveSceneDealerConfigVo.setPeriodsId(liveGiftConfigVo.getPeriodsId());
        }
        liveSceneDealerConfigVo.setPage(liveGiftConfigVo.getPage());
        liveSceneDealerConfigVo.setLimit(liveGiftConfigVo.getLimit());

        PageResult<LiveSceneDealerConfigDto> result =
                liveSceneDealerConfigService.getDealerRefuelCardStatisticsByPage(liveSceneDealerConfigVo);
        result.setCode("0");
        return result;
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡配置添加页面
     * @Date 2020年5月28日11:17:37
     **/
    @RequestMapping("/toAddGiftConfigPage")
    public String toAddGiftConfigPage(ModelMap modelMap) {
        // 获取场次油卡配置详情
        GiftRefuelingcardPeriodsDto giftRefuelingCardPeriodsDto = new GiftRefuelingcardPeriodsDto();
        List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList = new ArrayList<>();
        fuelCardRuleList.add( new GiftRefuelingcardPeriodsGiftNum());
        giftRefuelingCardPeriodsDto.setFuelCardRuleList(fuelCardRuleList);
        modelMap.addAttribute("giftRefuelingCardPeriodsDto", giftRefuelingCardPeriodsDto);
        modelMap.addAttribute("commodityList", giftRefuelingcardPeriodsService.getCommodityList());
        modelMap.addAttribute("periodList", getPeriodList(ActivityConfigStatusEnum.UN_END));
        return "gift/add-gift-config";
    }

    public List<ActivityConfigResponseVo> getPeriodList(ActivityConfigStatusEnum statusEnum){
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setActivityStatusEnum(statusEnum);
        activityConfigRequestVo.setPageNum(1);
        activityConfigRequestVo.setPageSize(1000);
       return periodsService.getActivityListByPage(activityConfigRequestVo);
    }

    /**
     * @param request
     * @param liveGiftConfigVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 添加场次油卡规则
     * @Date 13:51 2020/5/28
     **/
    @RequestMapping("/addGiftConfig")
    @ResponseBody
    public ResultDto addGiftConfig(HttpServletRequest request, @RequestBody LiveGiftConfigVo liveGiftConfigVo) {
        XxlUser xxlUser = getLoginUser(request);
        ResultDto resultDto = new ResultDto();

        try {
            GiftRefuelingcardPeriodsDto giftRefuelingcardPeriodsDto = new GiftRefuelingcardPeriodsDto();
            giftRefuelingcardPeriodsDto.setPeriodsId(liveGiftConfigVo.getPeriodsId());
            giftRefuelingcardPeriodsDto.setPeriodsName(liveGiftConfigVo.getPeriodsName());
            giftRefuelingcardPeriodsDto.setBeginTime(DateUtils.stringToDate(liveGiftConfigVo.getBeginTime(), YYYY_MM_DD_HH_MM_SS));
            giftRefuelingcardPeriodsDto.setEndTime(DateUtils.stringToDate(liveGiftConfigVo.getEndTime(), YYYY_MM_DD_HH_MM_SS));
            if (null != xxlUser) {
                giftRefuelingcardPeriodsDto.setCreateName(xxlUser.getEmpName());
            }
            List<GiftRefuelingcardPeriodsCommodity> commodityList = JSON.parseArray(liveGiftConfigVo.getCommodityList(),
                    GiftRefuelingcardPeriodsCommodity.class);
            List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList = JSON.parseArray(liveGiftConfigVo.getRuleMapList(),
                    GiftRefuelingcardPeriodsGiftNum.class);
            giftRefuelingcardPeriodsDto.setCommodityList(commodityList);
            giftRefuelingcardPeriodsDto.setFuelCardRuleList(fuelCardRuleList);

            TcResponse tcResponse = giftRefuelingcardPeriodsService.inster(giftRefuelingcardPeriodsDto);
            if (tcResponse.getResponseHeader().getStatus() == 200) {
                resultDto = success();
            } else {
                resultDto = dynamicResult(tcResponse.getResponseHeader().getStatus(), tcResponse.getResponseHeader().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }

    /**
     * @param request
     * @param liveGiftConfigVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 编辑场次油卡规则
     * @Date 13:51 2020/5/28
     **/
    @RequestMapping("/updateGiftConfig")
    @ResponseBody
    public ResultDto updateGiftConfig(HttpServletRequest request, @RequestBody LiveGiftConfigVo liveGiftConfigVo) {
        XxlUser xxlUser = getLoginUser(request);
        ResultDto resultDto = new ResultDto();

        try {
            GiftRefuelingcardPeriodsDto giftRefuelingcardPeriodsDto = new GiftRefuelingcardPeriodsDto();
            giftRefuelingcardPeriodsDto.setId(liveGiftConfigVo.getId());
            giftRefuelingcardPeriodsDto.setPeriodsId(liveGiftConfigVo.getPeriodsId());
            giftRefuelingcardPeriodsDto.setPeriodsName(liveGiftConfigVo.getPeriodsName());
            giftRefuelingcardPeriodsDto.setBeginTime(DateUtils.stringToDate(liveGiftConfigVo.getBeginTime(), YYYY_MM_DD_HH_MM_SS));
            giftRefuelingcardPeriodsDto.setEndTime(DateUtils.stringToDate(liveGiftConfigVo.getEndTime(), YYYY_MM_DD_HH_MM_SS));
            if (null != xxlUser) {
                giftRefuelingcardPeriodsDto.setUpdateName(xxlUser.getEmpName());
            }
            List<GiftRefuelingcardPeriodsCommodity> commodityList = JSON.parseArray(liveGiftConfigVo.getCommodityList(),
                    GiftRefuelingcardPeriodsCommodity.class);
            List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList = JSON.parseArray(liveGiftConfigVo.getRuleMapList(),
                    GiftRefuelingcardPeriodsGiftNum.class);
            giftRefuelingcardPeriodsDto.setCommodityList(commodityList);
            giftRefuelingcardPeriodsDto.setFuelCardRuleList(fuelCardRuleList);

            TcResponse tcResponse = giftRefuelingcardPeriodsService.update(giftRefuelingcardPeriodsDto);
            if (tcResponse.getResponseHeader().getStatus() == 200) {
                resultDto = success();
            } else {
                resultDto = dynamicResult(tcResponse.getResponseHeader().getStatus(), tcResponse.getResponseHeader().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }

    /**
     * @param request
     * @param id
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 删除场次油卡规则
     * @Date 13:51 2020/5/28
     **/
    @RequestMapping("/deleteGiftConfig")
    @ResponseBody
    public ResultDto deleteGiftConfig(HttpServletRequest request, Integer id) {
        XxlUser xxlUser = getLoginUser(request);
        ResultDto resultDto = new ResultDto();

        try {
            GiftRefuelingcardPeriods giftRefuelingcardPeriods = new GiftRefuelingcardPeriods();
            giftRefuelingcardPeriods.setId(id);
            if (null != xxlUser) {
                giftRefuelingcardPeriods.setUpdateName(xxlUser.getEmpName());
            }
            TcResponse tcResponse = giftRefuelingcardPeriodsService.delete(giftRefuelingcardPeriods);
            if (tcResponse.getResponseHeader().getStatus() == 200) {
                resultDto = success();
            } else {
                resultDto = dynamicResult(tcResponse.getResponseHeader().getStatus(), tcResponse.getResponseHeader().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次油卡配置编辑页面
     * @Date 2020年5月28日11:17:37
     **/
    @RequestMapping("/toEditGiftConfigPage")
    public String toEditGiftConfigPage(ModelMap modelMap, Integer id) {
        modelMap.addAttribute("periodList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        // 获取场次油卡配置详情
        GiftRefuelingcardPeriodsDto giftRefuelingCardPeriodsDto = giftRefuelingcardPeriodsService.getDetailsById(id);
        modelMap.addAttribute("giftRefuelingCardPeriodsDto", giftRefuelingCardPeriodsDto);
        modelMap.addAttribute("commodityList", giftRefuelingcardPeriodsService.getCommodityList());
        return "gift/add-gift-config";
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次经销商油卡配置页面
     * @Date 2020年5月28日11:17:37
     **/
    @RequestMapping("/toDealerGiftConfigPage")
    public String toDealerGiftConfigPage(ModelMap modelMap, Integer periodsId) {

        modelMap.addAttribute("cityList", getAllCity());
        modelMap.addAttribute("periodsId", periodsId);

        return "gift/dealer-gift-list";
    }

    /**
     * @param request
     * @param dealerConfigIds
     * @param giftNum
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 批量更新经销商油卡上限配置
     * @Date 13:51 2020/5/28
     **/
    @RequestMapping("/batchUpdateGiftConfig")
    @ResponseBody
    public ResultDto batchUpdateGiftConfig(HttpServletRequest request, String dealerConfigIds, Integer giftNum) {
        XxlUser xxlUser = getLoginUser(request);
        ResultDto resultDto = new ResultDto();

        try {
            List<Integer> listIds = Arrays.asList(dealerConfigIds.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            TcResponse tcResponse = liveSceneDealerConfigService.batchUpdate(giftNum, listIds, xxlUser.getEmpName());
            if (tcResponse.getResponseHeader().getStatus() == 200) {
                resultDto = success();
            } else {
                resultDto = dynamicResult(tcResponse.getResponseHeader().getStatus(), tcResponse.getResponseHeader().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }

    /**
     * 油卡使用统计
     *
     * @param
     * @return com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto
     * @author HuangHao
     * @CreatTime 2020-06-01 18:10
     */
    @RequestMapping("/refuelCardUsedStatistics")
    @ResponseBody
    GiftRefuelingcardUsedStatisticsDto refuelCardUsedStatistics() {
        return giftRefuelingcardService.refuelCardUsedStatistics();
    }

    /**
     * 上传油卡
     *
     * @param file
     * @return com.tuanche.arch.web.model.TcResponse
     * @author HuangHao
     * @CreatTime 2020-06-01 18:14
     */
    @RequestMapping("/refuelcardupload")
    @ResponseBody
    public TcResponse refuelcardUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if (!RefuelCardExcelUtil.isExcel(name)) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "只支持.xls和.xlsx的excel文件");
        }
        XxlUser xxlUser = getLoginUser(request);
        RefuelCardExcelUtil.ExcelList<GiftRefuelingcard> excelList = RefuelCardExcelUtil.analysis(file, xxlUser.getEmpName(),giftRefuelingcardService.getAllCardMap());
        //有效数据
        List<GiftRefuelingcard> list = excelList.getData();
        int successNum = giftRefuelingcardService.batchInsert(list);
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

    /**
     * @param request
     * @param response
     * @param liveGiftConfigVo
     * @return void
     * @Author GongBo
     * @Description 导出油卡统计
     * @Date 15:40 2020/6/3
     **/
    @RequestMapping("/exportGift")
    public void editStatus(HttpServletRequest request, HttpServletResponse response, LiveGiftConfigVo liveGiftConfigVo) {
        try {
            LiveSceneDealerConfigVo liveSceneDealerConfig = new LiveSceneDealerConfigVo();
            if (null != liveGiftConfigVo.getPeriodsId()) {
                liveSceneDealerConfig.setPeriodsId(liveGiftConfigVo.getPeriodsId());
            }
            List<LiveSceneDealerConfigDto> list = liveSceneDealerConfigService.getDealerRefuelCardStatistics(liveSceneDealerConfig);
            ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
            for (int i = 0; i < list.size(); i++) {
                LiveSceneDealerConfigDto liveSceneDealerConfigDto = list.get(i);
                if(StringUtils.isEmpty(liveSceneDealerConfigDto.getPeriodsName())){
                    // 获取场次名称
                    activityConfigRequestVo.setId(liveSceneDealerConfigDto.getPeriodsId());
                    ActivityConfigResponseVo activityConfigResponseVo = periodsService.queryById(activityConfigRequestVo);
                    if(activityConfigResponseVo != null){
                        liveSceneDealerConfigDto.setPeriodsName(activityConfigResponseVo.getName());
                    }
                }

            }
            if (CollectionUtil.isNotEmpty(list)) {
                Map<String, String> titleValueMap = new LinkedHashMap<String, String>();
                titleValueMap.put("PeriodsName", "场次名称");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("PresentedCardNum", "经销商已赠送油卡数量");
                titleValueMap.put("RefuelingCardNum", "赠送油卡上限");
                ExportExcel.exportExcel("油卡统计.xls", titleValueMap, list, request, response);
            }
        } catch (Exception e) {
            CommonLogUtil.addError("LiveGiftController", "导出油卡统计error", e);
        }
    }
}
