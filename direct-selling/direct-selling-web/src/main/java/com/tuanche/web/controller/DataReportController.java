package com.tuanche.web.controller;/**
 * @program: direct-selling
 * @description: ${description}
 * @author: lvsen
 * @create: 2020-06-30 18:01
 **/

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveSceneCityDto;
import com.tuanche.directselling.dto.LiveSceneDataReportDto;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LivePeriodsDataDetailVo;
import com.tuanche.directselling.vo.LiveSceneBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.web.service.PeriodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author lvsen
 * @Description 直卖数据报表
 * @Date 2020/6/30
 **/
@Controller
@RequestMapping("/dataReport")
public class DataReportController extends BaseController {

    @Reference
    LiveSceneService liveSceneService;
    @Autowired
    PeriodsService periodsService;
    @Reference
    CarBaseService carBaseService;


    /**
     * @description 统计页面
     * @date 2020/7/2 17:06
     * @author lvsen
     */
    @RequestMapping("/toDealerClueDataPage")
    public String toDealerClueDataPage(ModelMap modelMap) {
        // 获取场次，默认当前进行中的，没进行中的取上个场次
        List<ActivityConfigResponseVo> periodsList = periodsService.getActivityList(new ActivityConfigRequestVo());
        List<LiveSceneCityDto> cityList = new ArrayList<>();
        List<LiveSceneBrandVo> brandList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(periodsList)) {
            Date now = new Date();
            boolean flag = false;
            for (ActivityConfigResponseVo periods : periodsList) {
                if (now.after(periods.getBeginTime()) && now.before(periods.getEndTime())) {
                    //用于设置默认选中
                    periods.setHeadImage("1");
                    flag = true;
                    cityList = liveSceneService.getPeriodsCityList(periods.getId());
                    brandList = liveSceneService.getPeriodsBrandList(periods.getId());
                }
            }
            if (!flag) {
                Collections.sort(periodsList, Comparator.comparing(ActivityConfigResponseVo::getEndTime).reversed());
                periodsList.get(0).setHeadImage("1");
                cityList = liveSceneService.getPeriodsCityList(periodsList.get(0).getId());
                brandList = liveSceneService.getPeriodsBrandList(periodsList.get(0).getId());
            }
        }
        if (!CollectionUtils.isEmpty(brandList)) {
            List<Integer> cbIds = new ArrayList<>();
            brandList.forEach(v->{
                cbIds.add(v.getId());
            });
            Map<Integer, CarBrandDto> cbMap = carBaseService.getBrandMap(cbIds);
            brandList.forEach(v->{
                v.setName(v.getName() + " (" + cbMap.get(v.getId()).getCmName() + ")");
            });
        }
        modelMap.addAttribute("periodsList", periodsList);
        modelMap.addAttribute("cityList", cityList);
        modelMap.addAttribute("brandList", brandList);
        return "periods/data-report";
    }

    /**
     * @description 统计数据
     * @date 2020/7/2 17:06
     * @author lvsen
     */
    @RequestMapping("/searchPeriodsDataReport")
    @ResponseBody
    public PageResult searchPeriodsDataReport(PageResult<LiveSceneDataReportDto> pageResult, LiveSceneVo liveSceneVo) {
        PageResult pageList = liveSceneService.getSceneDataReportPeriodsId(pageResult, liveSceneVo);
        pageList.setCode("0");
        return pageList;
    }

    /**
     * @return java.lang.String
     * @description 场次下的城市
     * @date 2020/7/3 11:44
     * @author lvsen
     */
    @RequestMapping("/getPeriodsSceneCityList")
    @ResponseBody
    public List<LiveSceneCityDto> getPeriodsSceneCityList(@RequestParam("periodsId") Integer periodsId) {
        // 获取城市
        List<LiveSceneCityDto> cityList = liveSceneService.getPeriodsCityList(periodsId);
        return cityList;
    }

    /**
     * @return java.lang.String
     * @description 场次下的品牌
     * @date 2020/7/3 11:44
     * @author lvsen
     */
    @RequestMapping("/getPeriodsBrandList")
    @ResponseBody
    public List<LiveSceneBrandVo> getPeriodsBrandList(@RequestParam("periodsId") Integer periodsId) {
        // 获取品牌
        List<LiveSceneBrandVo> brandList = liveSceneService.getPeriodsBrandList(periodsId);
        return brandList;
    }

    /**
     * @return java.lang.String
     * @description 场次经销商七天线索统计
     * @date 2020/7/3 11:44
     * @author lvsen
     */
    @RequestMapping("/toPeriodsDealerDataPage")
    public String toPeriodsDealerDataPage(ModelMap modelMap,@RequestParam("periodsId") Integer periodsId,@RequestParam("dealerId") Integer dealerId) {
        modelMap.addAttribute("periodsId", periodsId);
        modelMap.addAttribute("dealerId", dealerId);
        return "periods/data-report-detail";
    }

    @RequestMapping("/getPeriodsDealerDetail")
    @ResponseBody
    public PageResult getPeriodsBrandList(@RequestParam("periodsId") Integer periodsId,@RequestParam("dealerId") Integer dealerId) {
        PageResult pageResult = new PageResult();
        List<LivePeriodsDataDetailVo> dealerDataDetail = liveSceneService.getPeriodsDealerDataDetail(periodsId,dealerId);
        pageResult.setCode("0");
        pageResult.setData(dealerDataDetail);
        return pageResult;
    }
}
