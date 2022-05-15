package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tc.systemauth.interfaces.dto.UserDto;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveProgramService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveSceneDto;
import com.tuanche.directselling.enums.CityBrandDataType;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneCityBrand;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.CarBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDto;
import com.tuanche.manubasecenter.model.CsDealerExtend;
import com.tuanche.storage.api.brand.CarBrandService;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.storage.dto.common.ApiDto;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName: LiveSceneController
 * @Description: 团车直卖-场次活动Controller
 * @Author: GongBo
 * @Date: 2020年3月4日18:11:19
 * @Version 1.0
 **/
@Controller
@RequestMapping("/scene")
public class LiveSceneController extends BaseController {

    @Reference
    LiveSceneService liveSceneService;
    @Reference
    LiveProgramService liveProgramService;
    @Autowired
    CommonWebService commonWebService;
    @Reference
    CityBaseService cityBaseService;
    @Autowired
    PeriodsService periodsService;
    @Reference
    CarBaseService carBaseService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    CarBrandService carBrandService;

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动列表页面
     * @Date 2020年3月4日18:12:46
     **/
    @RequestMapping("/toLiveSceneListPage")
    public String toLiveSceneListPage(ModelMap modelMap) {
        // 获取场次
//        modelMap.addAttribute("periodsList", periodsService.getActivityList(new ActivityConfigRequestVo()));
        // 获取城市
//        modelMap.addAttribute("cityList", getAllCityList());
//
//        List<CarBrandDto> carBrandDtos = carBaseService.getAllBrandList();
//        List<CarBrandVo> brandVoList = new ArrayList<>();
//        CarBrandVo carBrandVo = null;
//        for (CarBrandDto carBrandDto : carBrandDtos) {
//            carBrandVo = new CarBrandVo();
//            carBrandVo.setId(carBrandDto.getCbId());
//            carBrandVo.setName(carBrandDto.getCbName() + "-(" + carBrandDto.getCmName() + ")");
//            brandVoList.add(carBrandVo);
//        }
//        modelMap.addAttribute("brandList", brandVoList);
        return "scene/list";
    }


    /**
     * @Description 团车直卖-场次活动数据加载
     * @Date 2020年3月4日18:17:53
     **/
    @RequestMapping("/searchSceneList")
    @ResponseBody
    public PageResult searchSceneList(PageResult<LiveSceneDto> pageResult, LiveSceneVo liveSceneVo) {
        PageResult PageList = liveSceneService.findSceneListPage(pageResult, liveSceneVo);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动添加页面
     * @Date 2020年3月4日18:17:48
     **/
    @RequestMapping("/toAddScenePage")
    public String toAddScenePage(ModelMap modelMap) {
//        modelMap.addAttribute("cityList", getAllCityList());
//        modelMap.addAttribute("periodList", periodsService.getActivityList(new ActivityConfigRequestVo()));
//        modelMap.addAttribute("brandList", getBrand(null));
        return "scene/add-scene";
    }


    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动编辑页面
     * @Date 2020年3月4日18:19:28
     **/
    @RequestMapping("/toEditScenePage")
    public String toEditScenePage(ModelMap modelMap, Integer sceneId) {
        CommonLogUtil.addInfo("LiveSceneController","场次活动修改", "参数:" + JSON.toJSONString(sceneId));
        LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
        boolean readyIsOver = false;
        if (liveScene != null && null != liveScene.getReadyEndTime() && liveScene.getReadyEndTime().before(new Date())) {
            // 场次活动预热结束
            readyIsOver = true;
        }
        CommonLogUtil.addInfo("LiveSceneController","场次活动修改", "查询场次结果:" + JSON.toJSONString(liveScene));
        modelMap.addAttribute("readyIsOver", readyIsOver);
        modelMap.addAttribute("liveScene", Objects.isNull(liveScene) ? new LiveScene() : liveScene);
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneService.selectLiveSceneCityBrandBySceneId(sceneId);
        List<Integer> cityIds = new ArrayList<>();
        List<Integer> brandIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(liveSceneCityBrands)){
            for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
                if (liveSceneCityBrand.getDataType() == CityBrandDataType.CITY.getType()) {
                    cityIds.add(liveSceneCityBrand.getDataId());
                } else {
                    brandIds.add(liveSceneCityBrand.getDataId());
                }
            }
        }
        modelMap.addAttribute("cityIds", cityIds);
        modelMap.addAttribute("brandIds", brandIds);
        return "scene/edit-scene";
    }

    /**
     * @param liveSceneVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次活动添加
     * @Date 2020年3月4日18:21:42
     **/
    @RequestMapping("/addScene")
    @ResponseBody
    public ResultDto addScene(HttpServletRequest request, @RequestBody LiveSceneVo liveSceneVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneVo) {
                return noData();
            }
//            List<LiveScene> liveSceneList = liveSceneService.getLiveSceneList(liveSceneVo);
//            if(liveSceneList.size() > 0){
//                return dynamicResult("存在时间重叠的场次活动，请修改时间");
//            }

            return liveSceneService.addScene(xxlUser.getId(), xxlUser.getEmpName(), liveSceneVo);
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
    }

    /**
     * @param sceneId
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-上下架场次活动
     * @Date 2020年3月5日19:27:14
     **/
    @RequestMapping("/upOrDownScene")
    @ResponseBody
    public ResultDto upOrDownScene(HttpServletRequest request, Integer sceneId, Integer upState) {
        XxlUser xxlUser = getLoginUser(request);
        ResultDto resultDto = new ResultDto();
        try {
            // 获取场次活动数据
            LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
            if (null == liveScene) {
                resultDto = noData();
            } else {
                liveScene.setUpState(upState);
                liveScene.setUpdateDt(new Date());
                if (null != xxlUser) {
                    liveScene.setUpdateUserId(xxlUser.getId());
                    liveScene.setUpdateUserName(xxlUser.getEmpName());
                }
                liveSceneService.updateByScene(liveScene);
                resultDto = success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }


    /**
     * @param liveSceneVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次活动修改
     * @Date 2020年3月4日18:28:11
     **/
    @RequestMapping("/updateScene")
    @ResponseBody
    public ResultDto updateScene(HttpServletRequest request, @RequestBody LiveSceneVo liveSceneVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneVo) {
                return noData();
            }
            return liveSceneService.updateScene(xxlUser.getId(), xxlUser.getEmpName(), liveSceneVo);
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("场次活动修改,发生异常", "参数:" + JSON.toJSONString(liveSceneVo), e);
            return systemError();
        }
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-加载分配导播页面
     * @Date 2020年3月4日18:19:28
     **/
    @RequestMapping("/loadDirectorPage")
    public String loadDirectorPage(ModelMap modelMap, Integer sceneId) {
        // 获取场次活动信息
        LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
        // 获取直播平台导播数据
        List<UserDto> userDtoList = commonWebService.getDirectorList();
        modelMap.addAttribute("liveScene", liveScene);
        modelMap.addAttribute("userDtoList", userDtoList);
        return "scene/edit-director";
    }

    /**
     * @param liveSceneVo
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次活动配置导播
     * @Date 2020年3月6日17:02:22
     **/
    @RequestMapping("/setDirector")
    @ResponseBody
    public ResultDto setDirector(HttpServletRequest request, @RequestBody LiveSceneVo liveSceneVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneVo) {
                return noData();
            }
            // 获取场次活动信息
            LiveScene liveScene = liveSceneService.getLiveSceneById(liveSceneVo.getSceneId());
            if (null == liveScene) {
                return noData();
            }

            // 保存场次活动
            if (null != xxlUser) {
                liveScene.setUpdateUserId(xxlUser.getId());
                liveScene.setUpdateUserName(xxlUser.getEmpName());
            }
            liveScene.setUpdateDt(new Date());
            liveScene.setDirectorId(liveSceneVo.getDirectorId());
            liveScene.setDirectorName(liveSceneVo.getDirectorName());
            liveSceneService.updateByScene(liveScene);

            // 同步直播平台导播
            Map<String, Object> params = new HashMap<>();
            params.put("type", GlobalConstants.Common.KAFKA_LIVE_NOTICE_TYPE_0);
            params.put("authId", liveScene.getDirectorId());
            params.put("authName", liveScene.getDirectorName());
            params.put("planId", liveScene.getPlanId());
            liveSceneService.setDirectorSendKafkaNotice(JSON.toJSONString(params));

        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("场次活动配置导播,发生异常", "参数:" + JSON.toJSONString(liveSceneVo), e);
            return systemError();
        }
        return success();
    }


    @RequestMapping("/selectlist")
    @ResponseBody
    public List<LiveSceneDto> getSelectLiveSceneDtoList(LiveSceneVo sceneVo) {
        List<LiveSceneDto> liveSceneDtoList = liveSceneService.selectLiveSceneTitleList(sceneVo);
        return liveSceneDtoList;
    }


    /**
     * 根据团车直卖活动和经销商经营品牌 获取品牌
     *
     * @return List<CarBrandVo>
     */
    @GetMapping("/getBrand")
    @ResponseBody
    public List<CarBrandVo> getBrand(@RequestParam(value = "sceneId") Integer sceneId, @RequestParam(value = "dealerId") Integer dealerId) {
        CsDealerExtend params = new CsDealerExtend();
        params.setDealerId(dealerId);
        CsCompanyDto csCompanyDto = companyBaseService.getCsCompanyDtoById(dealerId);
        List<Integer> brandIds = new ArrayList<>();
        if (null != csCompanyDto && null != csCompanyDto.getDealerBrandList()) {
            List<CsDealerExtend> dealerBrandList = csCompanyDto.getDealerBrandList();
            for (CsDealerExtend dealerExtend : dealerBrandList) {
                brandIds.add(dealerExtend.getDataId());
            }
        }
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneService.selectLiveSceneCityBrandBySceneId(sceneId);
        List<Integer> sceneBrandIds = new ArrayList<>();
        for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
            if (liveSceneCityBrand.getDataType() == CityBrandDataType.BRAND.getType()) {
                sceneBrandIds.add(liveSceneCityBrand.getDataId());
            }
        }
        brandIds.retainAll(sceneBrandIds);
        List<CarBrandDto> carBrandDtos = carBrandService.listByCbIds(new ApiDto(brandIds, 0));
        List<CarBrandVo> result = new ArrayList<>();
        CarBrandVo carBrandVo = null;
        for (CarBrandDto carBrandDto : carBrandDtos) {
            carBrandVo = new CarBrandVo();
            carBrandVo.setId(carBrandDto.getCbId());
            carBrandVo.setName(carBrandDto.getCbName() + "-(" + carBrandDto.getCmName() + ")");
            result.add(carBrandVo);
        }
        return result;
    }

    private List<CarBrandVo> getBrand(Integer sceneId) {
        List<CarBrandDto> carBrandDtos = null;
        if (sceneId == null) {
            carBrandDtos = carBaseService.getAllBrandList();
        } else {
            List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneService.selectLiveSceneCityBrandBySceneId(sceneId);
            List<Integer> brandIds = new ArrayList<>();
            for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
                if (liveSceneCityBrand.getDataType() == CityBrandDataType.BRAND.getType()) {
                    brandIds.add(liveSceneCityBrand.getDataId());
                }
            }
            carBrandDtos = carBrandService.listByCbIds(new ApiDto(brandIds, 0));
        }

        List<CarBrandVo> result = new ArrayList<>();
        CarBrandVo carBrandVo = null;
        for (CarBrandDto carBrandDto : carBrandDtos) {
            carBrandVo = new CarBrandVo();
            carBrandVo.setId(carBrandDto.getCbId());
            carBrandVo.setName(carBrandDto.getCbName() + "-(" + carBrandDto.getCmName() + ")");
            result.add(carBrandVo);
        }
        return result;
    }


    /**
     * 所有场次
     *
     * @return
     */
    @GetMapping("/getAllPeriod")
    @ResponseBody
    public List<ActivityConfigResponseVo> getAllPeriod() {
        return periodsService.getActivityList(new ActivityConfigRequestVo());
    }

    /**
     * 根据id获取所有场次
     *
     * @return
     */
    @GetMapping("/getPeriodById")
    @ResponseBody
    public ActivityConfigResponseVo getPeriodById(@RequestParam("id") Integer id) {
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setId(id);
        List<ActivityConfigResponseVo> activityConfigResponseVoList = periodsService.getActivityList(activityConfigRequestVo);
        if (activityConfigResponseVoList != null && !activityConfigResponseVoList.isEmpty()) {
            return activityConfigResponseVoList.get(0);
        } else {
            return null;
        }

    }


    /**
     * 所有品牌
     *
     * @return
     */
    @GetMapping("/getAllBrand")
    @ResponseBody
    public List<CarBrandVo> getAllBrand() {
        List<CarBrandDto> carBrandDtos = carBaseService.getAllBrandList();
        List<CarBrandVo> brandVoList = new ArrayList<>();
        CarBrandVo carBrandVo = null;
        for (CarBrandDto carBrandDto : carBrandDtos) {
            carBrandVo = new CarBrandVo();
            carBrandVo.setId(carBrandDto.getCbId());
            carBrandVo.setName(carBrandDto.getCbName() + "-(" + carBrandDto.getCmName() + ")");
            brandVoList.add(carBrandVo);
        }
        return brandVoList;
    }


    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultDto delete(LiveSceneVo liveSceneVo, HttpServletRequest request) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneVo) {
                return noData();
            }
            // 获取场次活动信息
            LiveScene liveScene = liveSceneService.getLiveSceneById(liveSceneVo.getSceneId());
            if (null == liveScene) {
                return noData();
            }

            // 保存场次活动
            if (null != xxlUser) {
                liveScene.setUpdateUserId(xxlUser.getId());
                liveScene.setUpdateUserName(xxlUser.getEmpName());
            }
            liveScene.setDeleteState(true);
            liveScene.setUpdateDt(new Date());
            liveSceneService.updateByScene(liveScene);
        } catch (Exception e) {
            CommonLogUtil.addError("删除场次活动,error", "参数:" + JSON.toJSONString(liveSceneVo), e);
            return systemError();
        }
        return success();
    }
}
