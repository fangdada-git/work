package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtils;
import com.tuanche.directselling.api.LiveProgramService;
import com.tuanche.directselling.api.LiveSceneDealerBrandService;
import com.tuanche.directselling.api.LiveSceneDealerConfigService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveSceneDealerBrandDto;
import com.tuanche.directselling.dto.LiveSceneDealerConfigDto;
import com.tuanche.directselling.enums.CityBrandDataType;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneCityBrand;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveSceneDealerBrandVo;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manu.api.DealerService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.storage.api.brand.CarBrandService;
import com.tuanche.storage.dto.brand.CarBrandDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: LiveSceneDealerController
 * @Description: 团车直卖-场次活动经销商品牌controller
 * @Author: GongBo
 * @Date: 2020/3/6 17:49
 * @Version 1.0
 **/
@Controller
@RequestMapping("/dealer")
public class LiveSceneDealerController extends BaseController {

    @Reference
    LiveSceneDealerBrandService liveSceneDealerBrandService;
    @Reference
    CarBrandService carBrandService;
    @Reference
    LiveProgramService liveProgramService;
    @Reference
    LiveSceneDealerConfigService liveSceneDealerConfigService;
    @Reference
    LiveSceneService liveSceneService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    DealerService dealerService;

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动经销商品牌列表页面
     * @Date 2020年3月6日17:50:55
     **/
    @RequestMapping("/toSceneDealerPage")
    public String toSceneDealerPage(ModelMap modelMap, Integer sceneId, Boolean sceneFlag) {
        modelMap.addAttribute("sceneId", sceneId);
        modelMap.addAttribute("sceneFlag", sceneFlag);
        return "dealer/list";
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动经销商数据加载
     * @Date 2020年3月4日18:17:53
     **/
    @RequestMapping("/searchDealerList")
    @ResponseBody
    public PageResult searchDealerList(PageResult<LiveSceneDealerConfigDto> pageResult, Integer sceneId) {
        PageResult PageList = liveSceneDealerConfigService.findSceneDealerConfigList(pageResult, sceneId);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-添加场次活动经销商页
     * @Date 2020年3月9日11:58:04
     **/
    @RequestMapping("/toAddDealerPage")
    public String toAddDealerPage(ModelMap modelMap, Integer sceneId) {
        // 获取全部品牌
        List<CarBrandDto> carBrandDtoList = carBrandService.listAllCarBrands();
        modelMap.addAttribute("carBrandDtoList", carBrandDtoList);
        modelMap.addAttribute("sceneId", sceneId);
        return "dealer/add-dealer-brand";
    }

    /**
     * @param liveSceneDealerBrandDto
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次活动配置经销商
     * @Date 2020年3月9日14:58:40
     **/
    @RequestMapping("/addDealer")
    @ResponseBody
    public ResultDto addDealer(HttpServletRequest request, @RequestBody LiveSceneDealerBrandDto liveSceneDealerBrandDto) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneDealerBrandDto) {
                return noData();
            }
            ////验证直播场次下经销商是否已参与
            LiveScene liveScene = liveSceneService.getLiveSceneById(liveSceneDealerBrandDto.getSceneId());
            if (liveScene != null) {
                LiveSceneDealerConfig dealerConfig = new LiveSceneDealerConfig();
                dealerConfig.setDealerId(liveSceneDealerBrandDto.getDealerId());
                dealerConfig.setPeriodsId(liveScene.getPeriodsId());
                int num = liveSceneDealerConfigService.selectByLiveSceneDealerConfigCount(dealerConfig);
                if (num > 0) {
                    return dynamicResult("直播场次下已存在此经销商");
                }
            }
//            //验证经销商是否已经添加
//            LiveSceneDealerConfig dealerConfig = new LiveSceneDealerConfig();
//            dealerConfig.setDealerId(liveSceneDealerBrandDto.getDealerId());
//            dealerConfig.setSceneId(liveSceneDealerBrandDto.getSceneId());
//            int num = liveSceneDealerConfigService.selectByLiveSceneDealerConfigCount(dealerConfig);
//            if (num > 0) {
//                return dynamicResult("场次活动下已存在此经销商");
//            }
            // 保存场次活动经销商品牌配置
            if (null != xxlUser) {
                liveSceneDealerBrandDto.setCreateUserId(xxlUser.getId());
                liveSceneDealerBrandDto.setCreateUserName(xxlUser.getEmpName());
            }
            liveSceneDealerBrandDto.setCreateDt(new Date());

            // 添加场次活动经销商配置
            String result = liveSceneDealerBrandService.addDealerConfig(liveSceneDealerBrandDto);
            if (StringUtils.isNotEmpty(result)) {
                return dynamicResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-编辑场次活动经销商页
     * @Date 2020年5月21日14:16:59
     **/
    @RequestMapping("/toEditDealerPage")
    public String toEditDealerPage(ModelMap modelMap, Integer id) {
        LiveSceneDealerConfig liveSceneDealerConfig = liveSceneDealerConfigService.getLiveSceneDealerConfigById(id);
        // 获取经销商品牌数据
        LiveSceneDealerBrandVo params = new LiveSceneDealerBrandVo();
        params.setDealerId(liveSceneDealerConfig.getDealerId());
        params.setSceneId(liveSceneDealerConfig.getSceneId());
        List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandService.getLiveSceneDealerBrandList(params);
        List<Integer> sceneBrandIds = new ArrayList<>();
        for (LiveSceneDealerBrand liveSceneCityBrand : liveSceneDealerBrandList) {
            sceneBrandIds.add(liveSceneCityBrand.getBrandId());
        }
        // 获取配置的品牌ids
        StringBuilder carStyleIds = new StringBuilder();
        Integer cityId = null;
        Integer joinCityId = null;
        for (LiveSceneDealerBrand liveSceneDealerBrand : liveSceneDealerBrandList) {
            if (StringUtils.isNotEmpty(liveSceneDealerBrand.getStyleIds())) {
                carStyleIds.append(liveSceneDealerBrand.getStyleIds());
            }
            cityId = liveSceneDealerBrand.getCityId();
            joinCityId = liveSceneDealerBrand.getJoinCityId();
        }

        // 获取活动信息
        LiveScene liveScene = liveSceneService.getLiveSceneById(liveSceneDealerConfig.getSceneId());
        if (liveScene.getEndTime().after(new Date())) {
            // 活动未结束
            modelMap.addAttribute("sceneIsOver", false);
        } else {
            // 活动结束
            modelMap.addAttribute("sceneIsOver", true);
        }
        modelMap.addAttribute("cityId", cityId);
        modelMap.addAttribute("joinCityId", joinCityId);
        modelMap.addAttribute("brandIds", sceneBrandIds);
        modelMap.addAttribute("carStyleIds", carStyleIds);
        modelMap.addAttribute("liveSceneDealerConfig", liveSceneDealerConfig);
        return "dealer/edit-dealer-brand";
    }


    /**
     * @param liveSceneDealerBrandDto
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-场次活动配置经销商修改
     * @Date 2020年3月9日14:58:40
     **/
    @RequestMapping("/updateDealer")
    @ResponseBody
    public ResultDto updateDealer(HttpServletRequest request, @RequestBody LiveSceneDealerBrandDto liveSceneDealerBrandDto) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == liveSceneDealerBrandDto) {
                return noData();
            }
            // 保存场次活动经销商品牌配置
            if (null != xxlUser) {
                liveSceneDealerBrandDto.setUpdateUserId(xxlUser.getId());
                liveSceneDealerBrandDto.setUpdateUserName(xxlUser.getEmpName());
            }
            liveSceneDealerBrandDto.setUpdateDt(new Date());

            // 修改经销商保量值
            LiveSceneDealerConfig liveSceneDealerConfig = liveSceneDealerConfigService.getLiveSceneDealerConfigById(liveSceneDealerBrandDto.getId());
            liveSceneDealerConfig.setEnsureSize(liveSceneDealerBrandDto.getEnsureSize());
            liveSceneDealerConfig.setUpdateUserId(liveSceneDealerBrandDto.getUpdateUserId());
            liveSceneDealerConfig.setUpdateUserName(liveSceneDealerBrandDto.getUpdateUserName());
            liveSceneDealerConfig.setUpdateDt(liveSceneDealerBrandDto.getUpdateDt());
            liveSceneDealerConfig.setContractDealerName(liveSceneDealerBrandDto.getContractDealerName());
            liveSceneDealerConfigService.updateByLiveSceneDealerConfig(liveSceneDealerConfig);

            // 清空活动经销商配置的品牌
            LiveSceneDealerBrand brandParams = new LiveSceneDealerBrand();
            brandParams.setSceneId(liveSceneDealerConfig.getSceneId());
            brandParams.setDealerId(liveSceneDealerConfig.getDealerId());
            if (null != xxlUser) {
                brandParams.setUpdateUserId(xxlUser.getId());
                brandParams.setUpdateUserName(xxlUser.getEmpName());
            }
            brandParams.setUpdateDt(new Date());
            liveSceneDealerBrandService.deleteByLiveSceneDealerBrand(brandParams);

            // 批量添加经销商品牌
            JSONArray jsonArray = JSON.parseArray(liveSceneDealerBrandDto.getBrandMapList());
            for (int i = 0; i < jsonArray.size(); i++) {
                LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
                liveSceneDealerBrand.setSceneId(liveSceneDealerConfig.getSceneId());
                liveSceneDealerBrand.setDealerId(liveSceneDealerConfig.getDealerId());
                liveSceneDealerBrand.setDealerName(liveSceneDealerConfig.getDealerName());
                liveSceneDealerBrand.setCreateUserId(liveSceneDealerBrandDto.getUpdateUserId());
                liveSceneDealerBrand.setCreateUserName(liveSceneDealerBrandDto.getUpdateUserName());
                liveSceneDealerBrand.setCreateDt(liveSceneDealerBrandDto.getUpdateDt());
                liveSceneDealerBrand.setSceneId(liveSceneDealerConfig.getSceneId());
                liveSceneDealerBrand.setBrandId(jsonArray.getJSONObject(i).getInteger("brandId"));
                liveSceneDealerBrand.setBrandName(jsonArray.getJSONObject(i).getString("brandName"));
                liveSceneDealerBrand.setStyleIds(jsonArray.getJSONObject(i).getString("carStyleIds"));
                liveSceneDealerBrand.setJoinCityId(liveSceneDealerBrandDto.getJoinCityId());
                CsCompany csCompany = companyBaseService.getCsCompanyById(liveSceneDealerBrandDto.getDealerId());
                if (csCompany != null) {
                    liveSceneDealerBrand.setCityId(csCompany.getCityId());
                }
                liveSceneDealerBrandService.insertByLiveSceneDealerBrand(liveSceneDealerBrand);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * @param id
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖-删除经销商
     * @Date 2020年3月9日14:58:40
     **/
    @RequestMapping("/deleteDealer")
    @ResponseBody
    public ResultDto deleteDealer(HttpServletRequest request, Integer id) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (null == id) {
                return noData();
            }

            // 更新活动经销商配置 为删除状态
            LiveSceneDealerConfig liveSceneDealerConfig = new LiveSceneDealerConfig();
            liveSceneDealerConfig.setId(id);
            if (null != xxlUser) {
                liveSceneDealerConfig.setUpdateUserId(xxlUser.getId());
                liveSceneDealerConfig.setUpdateUserName(xxlUser.getEmpName());
            }
            liveSceneDealerConfig.setUpdateDt(new Date());
            liveSceneDealerConfig.setDeleteState(true);
            liveSceneDealerConfigService.deleteByLiveSceneDealerConfig(liveSceneDealerConfig);

        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }


    /**
     * 活动场次管理-添加经销商 根据经销商名称，活动场次ID查经销商
     *
     * @param companyName 经销商名称
     * @param sceneId     活动场次ID
     * @return
     */
    @GetMapping("/getDealerByNameCityBrand")
    @ResponseBody
    public List<com.tuanche.eap.api.bean.manufacturer.CsCompany> getDealerByNameCityBrand(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "sceneId") Integer sceneId) {
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneService.selectLiveSceneCityBrandBySceneId(sceneId);
        List<Integer> cityIdsList = new ArrayList<>();
        List<Integer> brandIds = new ArrayList<>();
        for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
            if (liveSceneCityBrand.getDataType() == CityBrandDataType.CITY.getType()) {
                cityIdsList.add(liveSceneCityBrand.getDataId());
            } else {
                brandIds.add(liveSceneCityBrand.getDataId());
            }
        }
        //如果城市为4级则取上一级城市
        if (!cityIdsList.isEmpty()) {
            DistrictBaseInputDto distDto = new DistrictBaseInputDto();
            distDto.setIds(cityIdsList);
            List<DistrictOutputBaseDto> districtOutputBaseDtos = iDistrictApiService.getBaseDistrictList(distDto);
            for (DistrictOutputBaseDto dist : districtOutputBaseDtos) {
                if (ManuBaseConstants.CityLevel.FOUR.equals(dist.getLevel())) {
                    cityIdsList.add(dist.getPid());
                }
            }
        }
        List<com.tuanche.eap.api.bean.manufacturer.CsCompany> companyList = dealerService.selectCompanyByNameCityBrand(companyName, brandIds, cityIdsList);
        return companyList;
    }
}
