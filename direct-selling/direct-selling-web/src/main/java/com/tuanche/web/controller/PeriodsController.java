package com.tuanche.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.JSONUtil;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.enums.ResultCodeEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigFormEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigTypeEnum;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LivePeriodDealerDto;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.PeriodsVo;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: LiveSceneController
 * @Description: 团车直卖-场次活动Controller
 * @Author: GongBo
 * @Date: 2020年3月4日18:11:19
 * @Version 1.0
 **/
@Controller
@RequestMapping("/periods")
public class PeriodsController extends BaseController {

    @Autowired
    CommonWebService commonWebService;
    @Autowired
    PeriodsService periodsService;
    @Reference
    LiveSceneService liveSceneService;
    @Reference
    CityBaseService cityBaseService;

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次列表页面
     * @Date 2020年4月16日17:20:24
     **/
    @RequestMapping("/toPeriodsListPage")
    public String toPeriodsListPage(ModelMap modelMap) {
        return "periods/list";
    }


    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次数据加载
     * @Date 2020年4月16日17:21:54
     **/
    @RequestMapping("/searchPeriodsList")
    @ResponseBody
    public PageResult searchPeriodsList(PageResult<ActivityConfigResponseVo> pageResult, ActivityConfigRequestVo activityConfigRequestVo) {
        PageResult PageList = periodsService.queryList(pageResult, activityConfigRequestVo);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次添加页面
     * @Date 2020年4月16日17:23:13
     **/
    @RequestMapping("/toAddPeriodsPage")
    public String toAddPeriodsPage(ModelMap modelMap) {
        List<Map<String, Object>> activityTypes = new ArrayList<>();
        Map<String, Object> map = null;
        for (ActivityConfigTypeEnum value : ActivityConfigTypeEnum.values()) {
            map = new HashMap<>(8);
            map.put("id", value.code());
            map.put("name", value.getName());
            activityTypes.add(map);
        }
        modelMap.addAttribute("activityTypes", activityTypes);
        return "periods/add-periods";
    }


    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次编辑页面
     * @Date 2020年4月16日17:23:17
     **/
    @RequestMapping("/toEditPeriodsPage")
    public String toEditPeriodsPage(ModelMap modelMap, Integer periodsId) {
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setId(periodsId);
        ActivityConfigResponseVo activityConfigResponseVo = periodsService.queryById(activityConfigRequestVo);
        modelMap.addAttribute("activityConfigResponseVo", activityConfigResponseVo);
        List<Map<String, Object>> activityTypes = new ArrayList<>();
        Map<String, Object> map = null;
        for (ActivityConfigTypeEnum value : ActivityConfigTypeEnum.values()) {
            map = new HashMap<>(8);
            map.put("id", value.code());
            map.put("name", value.getName());
            activityTypes.add(map);
        }
        modelMap.addAttribute("activityTypes", activityTypes);
        return "periods/edit-periods";
    }

    /**
     * @param periodsVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次添加 or 修改
     * @Date 2020年3月4日18:21:42
     **/
    @RequestMapping("/addOrUpdatePeriods")
    @ResponseBody
    public ResultDto addOrUpdatePeriods(HttpServletRequest request, @RequestBody PeriodsVo periodsVo) {
        try {
            if (null == periodsVo) {
                return noData();
            }
            Integer resultCode = 10000;
            if (null != periodsVo.getId()) {
                // 编辑场次
                ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
                activityConfigRequestVo.setId(periodsVo.getId());
                activityConfigRequestVo.setBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodsVo.getBeginTime()));
                activityConfigRequestVo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodsVo.getEndTime()));
                activityConfigRequestVo.setName(periodsVo.getPeriodsName());
                activityConfigRequestVo.setActivityFormEnum(ActivityConfigFormEnum.ONLINE);
                activityConfigRequestVo.setActivityTypeEnum(ActivityConfigTypeEnum.getBycode(periodsVo.getActivityType()));
                resultCode = periodsService.edit(activityConfigRequestVo);
            } else {
                // 添加场次
                ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
                activityConfigRequestVo.setBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodsVo.getBeginTime()));
                activityConfigRequestVo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodsVo.getEndTime()));
                activityConfigRequestVo.setName(periodsVo.getPeriodsName());
                activityConfigRequestVo.setActivityFormEnum(ActivityConfigFormEnum.ONLINE);
                activityConfigRequestVo.setActivityTypeEnum(ActivityConfigTypeEnum.getBycode(periodsVo.getActivityType()));
                resultCode = periodsService.save(activityConfigRequestVo);
            }
            if(resultCode == ResultCodeEnum.ACTIVITY_CONFIG_PARAM_INVALID_DATE_DUPLICATE.hashCode()){
                return dynamicResult("场次时间重叠，请修改场次时间范围");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }


    /**
     * @description: 场次-经销商列表
     * @param request
     * @return: java.lang.String
     * @author: dudg
     * @date: 2020/6/23 16:15
     */
    @RequestMapping("/dealerlist")
    public String periodsDealerList(ModelMap modelMap){
        // 获取场次
        modelMap.addAttribute("periodsList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        return "periods/dealer-list";
    }

    /**
     * @param pageResult
     * @param periodDealerDto
     * @description: 分页查询场次经销商列表数据
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/6/23 17:59
     */
    @RequestMapping("/searchdealerlist")
    @ResponseBody
    public PageResult<LivePeriodDealerDto> searchPeriodsDealerList(PageResult<LivePeriodDealerDto> pageResult, LivePeriodDealerDto periodDealerDto) {
        periodDealerDto.setOpenPage(true);
        PageResult<LivePeriodDealerDto> dtoPageResult = liveSceneService.getPeriodDealerList(pageResult, periodDealerDto);

        reSetPartFieldValue(dtoPageResult);
        dtoPageResult.setCode("0");
        return dtoPageResult;
    }

    @RequestMapping("/dealerlist/export")
    public void sceneListExport(HttpServletRequest request, HttpServletResponse response, LivePeriodDealerDto periodDealerDto) {
        try {
            PageResult<LivePeriodDealerDto> pageResult = new PageResult<>();
            PageResult<LivePeriodDealerDto> dtoPageResult = liveSceneService.getPeriodDealerList(pageResult, periodDealerDto);
            if (CollectionUtil.isNotEmpty(dtoPageResult.getData())) {
                Map<String, String> titleValueMap = new LinkedHashMap<String, String>();
                titleValueMap.put("PeriodsName", "直播场次");
                titleValueMap.put("SceneTitle", "直播活动");
                titleValueMap.put("BrandNames", "直播品牌");
                titleValueMap.put("CityName", "城市");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("ContractDealerName", "合同全称");

                reSetPartFieldValue(dtoPageResult);
                ExportExcel.exportExcel("场次经销商列表.xls", titleValueMap, dtoPageResult.getData(), request, response);
            }
        } catch (Exception e) {
            CommonLogUtil.addError("参加直播经销商列表，error","参数："+ JSONUtil.toJson(periodDealerDto), e);
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResultDto deletePeriod(HttpServletRequest request, HttpServletResponse response, ActivityConfigRequestVo activityConfigRequestVo) {
        try {
            activityConfigRequestVo.setDeleteTag(1);
            periodsService.edit(activityConfigRequestVo);
        } catch (Exception e) {
            CommonLogUtil.addError("删除场次，error", "参数：" + JSONUtil.toJson(activityConfigRequestVo), e);
            return systemError();
        }
        return success();
    }

    private void reSetPartFieldValue(PageResult<LivePeriodDealerDto> dtoPageResult){
        if(CollectionUtil.isEmpty(dtoPageResult.getData()))
            return;

        List<Integer> periodsIds = dtoPageResult.getData().stream().map(LivePeriodDealerDto::getPeriodsId).distinct().collect(Collectors.toList());
        Map<Integer, ActivityConfigResponseVo> periodsMap = periodsService.getActivityMap(periodsIds);

        dtoPageResult.getData().forEach(m -> {
            if (m.getCityId() != null) {
                m.setCityName(cityBaseService.getCityName(m.getCityId()));

                if (periodsMap.get(m.getPeriodsId()) != null) {
                    m.setPeriodsName(periodsMap.get(m.getPeriodsId()).getName());
                }
            }
        });
    }
}
