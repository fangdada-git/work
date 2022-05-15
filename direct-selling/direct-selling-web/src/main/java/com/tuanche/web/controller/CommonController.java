package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.tuanche.commons.page.impl.Pagination;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.CarBrandVo;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.input.KeyVaueParam;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.dto.output.DistrictOutputExpandDto;
import com.tuanche.district.api.service.IDistrictApiService;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manu.api.CsCompanyService;
import com.tuanche.manu.api.DealerCarService;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.dto.CsCompanyDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsDealerCarstyle;
import com.tuanche.manubasecenter.model.CsDealerExtend;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.storage.api.brand.CarBrandService;
import com.tuanche.storage.api.carstyle.CarStyleService;
import com.tuanche.storage.api.masterbrand.CarMasterBrandService;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.storage.dto.carstyle.CarStyleDto;
import com.tuanche.storage.dto.carstyle.CarStyleUnifyDto;
import com.tuanche.storage.dto.masterbrand.CarMasterBrandDto;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.vo.CityVo;
import com.tuanche.web.vo.IdNameVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommonController
 * @Description: 公共接口
 * @Author: GongBo
 * @Date: 2020年3月4日18:09:16
 * @Version 1.0
 **/
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController{

    @Reference
    DealerCarService dealerCarService;
    @Reference
    CsCompanyService csCompanyService;
    @Reference
    LiveSceneService liveSceneService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    CarBaseService carBaseService;
    @Reference
    CarBrandService carBrandService;
    @Reference
    CarStyleService carStyleService;
    @Reference
    CarMasterBrandService carMasterBrandService;
    @Reference
    IDistrictApiService iDistrictApiService;
    @Reference
    CityBaseService cityBaseService;
    @Reference
    private UserBaseService userBaseService;

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-根据品牌查经销商数据
     * @Date 2020年3月9日11:58:04
     **/
//    @RequestMapping("/getDealerByBrandId")
//    @ResponseBody
//    public List<CsCompany> getDealerByBrandId(Integer brandId, Integer sceneId) {
////        // 获取场次活动信息
////        LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
//        // 获取经销商集合
//        CsCompany companyParams = new CsCompany();
//        companyParams.setBrandId(brandId);
//        companyParams.setGrade(1);
//        companyParams.setPerfectStatus(1);
////        companyParams.setCityId(liveScene.getCityId());
//        List<CsCompany> companyList = csCompanyService.getCsCompanyList(companyParams);
//        return companyList;
//    }


//    /**
//     * @return java.lang.String
//     * @Author GongBo
//     * @Description 团车直卖-根据经销商名称搜索经销商
//     * @Date 2020年4月15日12:11:26
//     **/
//    @RequestMapping("/getDealerByCompanyName")
//    @ResponseBody
//    public List<CsCompany> getDealerByCompanyName(String keyword) {
//        // 获取经销商集合
//        CsCompany companyParams = new CsCompany();
//        companyParams.setGrade(1);
//        companyParams.setPerfectStatus(1);
//        companyParams.setCompanyName(keyword);
//        List<CsCompany> companyList = csCompanyService.getCsCompanyList(companyParams);
//        return companyList;
//    }

    /**
     * @param dealerId
     * @param brandId
     * @return java.util.List
     * @Author GongBo
     * @Description 团车直卖-根据经销商ID  &  品牌ID 获取车型集合
     * @Date 16:36 2019/10/23
     **/
    @RequestMapping("/loadCarStyle")
    @ResponseBody
    public List loadCarStyle(Integer dealerId, Integer brandId) {
        CsDealerExtend params = new CsDealerExtend();
        params.setDealerId(dealerId);
        params.setDataId(brandId);
        List<CsDealerCarstyle> carstyleList = dealerCarService.getCsDealerCarstyleList(params);
        return carstyleList;
    }

    /**
     * @param dealerId
     * @return java.util.List
     * @Author GongBo
     * @Description 团车直卖-根据经销商ID  获取品牌集合
     * @Date 2020年4月15日12:12:04
     **/
    @RequestMapping("/getBrandByDealerId")
    @ResponseBody
    public List getBrandByDealerId(Integer dealerId) {
        List<Map<String, String>> result = new ArrayList<>();
        CsDealerExtend params = new CsDealerExtend();
        params.setDealerId(dealerId);
        CsCompanyDto csCompanyDto = companyBaseService.getCsCompanyDtoById(dealerId);
        if (null != csCompanyDto && null != csCompanyDto.getDealerBrandList()) {
            for (int i = 0; i < csCompanyDto.getDealerBrandList().size(); i++) {
                Integer brandId = csCompanyDto.getDealerBrandList().get(i).getDataId();
                Map<String, String> brandMap = new HashMap<>();
                brandMap.put("brandId", brandId.toString());
                CarBrandDto brand = carBaseService.getBrandById(brandId);
                brandMap.put("brandName", brand.getCbName() + "-(" + brand.getCmName() + ")");
                result.add(brandMap);
            }
        }
        return result;
    }

    /**
     * 获取所有品牌(包括停产)
     *
     * @return List<CarBrandVo>
     */
    @GetMapping("/getAllBrand")
    @ResponseBody
    public ResultDto getAllBrand() {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
        dto.setMsg(StatusCodeEnum.RESULE_DATA_NONE.getText());
        List<CarBrandDto> carBrandDtos = carBrandService.listAllCarBrands(true);
        List<CarBrandVo> result = new ArrayList<>();
        CarBrandVo carBrandVo = null;
        for (CarBrandDto carBrandDto : carBrandDtos) {
            carBrandVo = new CarBrandVo();
            carBrandVo.setId(carBrandDto.getCbId());
            carBrandVo.setName(carBrandDto.getCbName() + "-(" + carBrandDto.getCmName() + ")");
            result.add(carBrandVo);
        }
        if (!result.isEmpty()) {
            dto.setCode(StatusCodeEnum.SUCCESS.getCode());
            dto.setMsg(StatusCodeEnum.SUCCESS.getText());
            dto.setResult(result);
        }
        return dto;
    }

    /**
     * @Author : fxq
     * @Description : 根据品牌获取车型列表
     * @Date : 2021/1/13 11:49
     * @Param : brandId
     * @return : ResultDto
     **/
    @RequestMapping("/getStyleListByBrandId")
    @ResponseBody
    public ResultDto getStyleListByBrandId(Integer brandId) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
        dto.setMsg(StatusCodeEnum.RESULE_DATA_NONE.getText());
        try {
            List<CarStyleUnifyDto> styleList = carBaseService.getStyleListByBrandId(brandId);
            if (!CollectionUtils.isEmpty(styleList)) {
                dto.setCode(StatusCodeEnum.SUCCESS.getCode());
                dto.setMsg(StatusCodeEnum.SUCCESS.getText());
                dto.setResult(styleList);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("CommonController", "getStyleListByBrandId", "根据品牌获取车型列表 error", e);
        }
        return dto;
    }

    /**
     * 所有主品牌
     *
     * @return
     */
    @RequestMapping("/getAllMasterBrands")
    @ResponseBody
    public ResultDto getAllMasterBrands() {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
        dto.setMsg(StatusCodeEnum.RESULE_DATA_NONE.getText());
        try {
            List<IdNameVo> idNameVos = new ArrayList<>();
            IdNameVo idNameVo = null;
            List<CarMasterBrandDto> masterBrandDtoList = carMasterBrandService.listAllCarMasterBrands();
            for (CarMasterBrandDto carMasterBrandDto : masterBrandDtoList) {
                idNameVo = new IdNameVo();
                idNameVo.setId(carMasterBrandDto.getCmId());
                idNameVo.setName(carMasterBrandDto.getCmName());
                idNameVos.add(idNameVo);
            }
            dto.setCode(StatusCodeEnum.SUCCESS.getCode());
            dto.setMsg(StatusCodeEnum.SUCCESS.getText());
            dto.setResult(idNameVos);
        } catch (Exception e) {
            DirectCommonUtil.addError("CommonController", "getAllMasterBrands", "获取所有主品牌 error", e);
        }
        return dto;
    }

    /**
     * 所有车型
     *
     * @return
     */
    @RequestMapping("/getAllCarStyles")
    @ResponseBody
    public ResultDto getAllCarStyles(Integer cbId) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
        dto.setMsg(StatusCodeEnum.RESULE_DATA_NONE.getText());
        try {
            List<IdNameVo> idNameVos = new ArrayList<>();
            IdNameVo idNameVo = null;
            List<CarStyleDto> carStyleDtos = carStyleService.queryByBrandId(cbId, true);
            for (CarStyleDto carStyleDto : carStyleDtos) {
                idNameVo = new IdNameVo();
                idNameVo.setId(carStyleDto.getCsId());
                idNameVo.setName(carStyleDto.getCsName());
                idNameVos.add(idNameVo);
            }
            dto.setCode(StatusCodeEnum.SUCCESS.getCode());
            dto.setMsg(StatusCodeEnum.SUCCESS.getText());
            dto.setResult(idNameVos);
        } catch (Exception e) {
            DirectCommonUtil.addError("CommonController", "getAllMasterBrands", "获取所有车型 error", e);
        }
        return dto;
    }

    /**
     * @param provinceId
     * @return com.tuanche.arch.web.model.TcResponse
     * @Author GongBo
     * @Description 根据省ID 获取城市list
     * @Date 13:48 2020/6/11
     **/
    @RequestMapping("/getProvinceList")
    @ResponseBody
    public List<DistrictOutputBaseDto> getProvinceList(Integer provinceId) {
        List<DistrictOutputBaseDto> baseDistrictList = null;
        try {
            DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
            KeyVaueParam keyVaueParam = new KeyVaueParam();
            keyVaueParam.setKey("first");
            keyVaueParam.setValue("asc");

            districtBaseInputDto.setLevel(Lists.newArrayList(2));
            districtBaseInputDto.setOrders(Lists.newArrayList(keyVaueParam));
            baseDistrictList = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baseDistrictList;
    }
    /**
     * @param provinceId
     * @return com.tuanche.arch.web.model.TcResponse
     * @Author GongBo
     * @Description 根据省ID 获取城市list
     * @Date 13:48 2020/6/11
     **/
    @RequestMapping("/loadCity")
    @ResponseBody
    public List<DistrictOutputBaseDto> loadCity(Integer provinceId) {
        DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
        List<Integer> pidlist = new ArrayList<>();
        pidlist.add(provinceId);
        List<KeyVaueParam> ordersList = new ArrayList<KeyVaueParam>();
        KeyVaueParam keyVaueParam = new KeyVaueParam();
        keyVaueParam.setKey("first");
        keyVaueParam.setValue("asc");
        ordersList.add(keyVaueParam);
        districtBaseInputDto.setOrders(ordersList);
        districtBaseInputDto.setPid(pidlist);
        List<DistrictOutputBaseDto> cityList = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
        return cityList;
    }

    /**
     * 获取三四级城市
     * @param
     * @return
     */
    @RequestMapping("/getAllCityThreeFour")
    @ResponseBody
    public List getAllCityThreeFour() {
        List<DistrictOutputBaseDto> allCity = super.getAllCity();
        List<Map<String, String>> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(allCity)){
            return result;
        }
        Map<String, String> cityMap;
        Map<String, String> chinaCityMap = new HashMap<>();
        chinaCityMap.put("name","全国");
        chinaCityMap.put("value","-1");
        result.add(chinaCityMap);
        for (DistrictOutputBaseDto districtOutputBaseDto : allCity) {
            cityMap = new HashMap<>();
            cityMap.put("name",districtOutputBaseDto.getName());
            cityMap.put("value",districtOutputBaseDto.getId().toString());
            result.add(cityMap);
        }
        return result;
    }

    /**
     * 获取三四级城市
     *
     * @return
     *//*
    @RequestMapping("/getAllCityThreeFour")
    @ResponseBody
    public List getAllCityThreeFour() {
        List<DistrictOutputBaseDto> allCity = super.getAllCity();
        List<Map<String, String>> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(allCity)) {
            return result;
        }
        Map<String, String> cityMap;
        Map<String, String> chinaCityMap = new HashMap<>();
        chinaCityMap.put("name", "全国");
        chinaCityMap.put("value", "-1");
        result.add(chinaCityMap);
        for (DistrictOutputBaseDto districtOutputBaseDto : allCity) {
            cityMap = new HashMap<>();
            cityMap.put("name", districtOutputBaseDto.getName());
            cityMap.put("value", districtOutputBaseDto.getId().toString());
            result.add(cityMap);
        }
        return result;
    }*/

    @GetMapping("/getAllCityTree")
    @ResponseBody
    public List<CityVo> getAllCityTree(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "dealerId", required = false) Integer dealerId) {
        List<CityVo> cityTree = new ArrayList<>();
        List<DistrictOutputBaseDto> districtOutputBaseDtos = null;
        List<DistrictOutputExpandDto> districtOutputExpandDtos = null;
        if (name == null && dealerId == null) {
            districtOutputBaseDtos = super.getAllCity();
        } else if (name != null) {
            //根据名称生成树形结构
            List<String> nameList = new ArrayList<>();
            nameList.add(name);
            districtOutputExpandDtos = iDistrictApiService.getDistrict(null, nameList);
        } else if (dealerId != null) {
            //查供应商所在城市的下级
            CsCompanyDto csCompanyDto = companyBaseService.getCsCompanyDtoById(dealerId);
            List<Integer> idsList = new ArrayList<>();
            idsList.add(csCompanyDto.getCityId());
            districtOutputExpandDtos = iDistrictApiService.getDistrict(idsList, null);
        }
        Map<Integer, List<CityVo>> level4Map = new HashMap<>();
        for (DistrictOutputBaseDto dist : districtOutputBaseDtos == null ? districtOutputExpandDtos : districtOutputBaseDtos) {
            if (ManuBaseConstants.CityLevel.THREE.equals(dist.getLevel())) {
                CityVo cityVo = new CityVo(dist.getId(), dist.getName(), new ArrayList<>());
                if (dealerId != null) {
                    cityVo.setDisabled(true);
                }
                cityTree.add(cityVo);
            } else if (ManuBaseConstants.CityLevel.FOUR.equals(dist.getLevel())) {
                if (level4Map.get(dist.getPid()) == null) {
                    level4Map.put(dist.getPid(), new ArrayList<>());
                }
                level4Map.get(dist.getPid()).add(new CityVo(dist.getId(), dist.getName()));
            }
        }
        for (CityVo city : cityTree) {
            List<CityVo> level4Dist = level4Map.get(city.getId());
            if (level4Dist != null) {
                city.getChildren().addAll(level4Dist);
            }
        }
        return cityTree;
    }

    /**
     * 下载
     * @author HuangHao
     * @CreatTime 2021-04-07 18:21
     * @param response
     * @param request
     * @param fileName
     * @return void
     */
    @RequestMapping("/download")
    @ResponseBody
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request,String fileName,String contentType) {
        try {
            //设置要下载的文件的名称
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //通知客服文件的MIME类型
            response.setContentType(contentType);
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

    @RequestMapping("/getSaleList")
    @ResponseBody
    public List<CsUser> getSaleList(Integer dealerId) {
        CsUser user = new CsUser();
        user.setDealerId(dealerId);
        user.setStat(1);
        user.setUlevel(4);
        List<CsUser> userList = userBaseService.getCsUserList(user);
        return userList;
    }
    @GetMapping("/getSaleListToLayui")
    @ResponseBody
    public PageResult<CsUser> getSaleListToLayui(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,@RequestParam("dealerId") Integer dealerId, @RequestParam(value = "keyword", required = false) String keyword) {
        CsUser user = new CsUser();
        user.setDealerId(dealerId);
        user.setStat(1);
        user.setUlevel(4);
        if (!StringUtil.isEmpty(keyword)) user.setUname(keyword);
        Pagination pagination = new Pagination();
        pagination.setPageNo(page);
        pagination.setPageSize(limit);
        user.setPage(pagination);
        List<CsUser> userList = userBaseService.getCsUserList(user);
        PageResult<CsUser> pageResult = new PageResult<>();
        pageResult.setCount(userBaseService.getCsUserListCount(user));
        pageResult.setMsg("");
        pageResult.setData(userList);
        pageResult.setCode("0");
        return pageResult;
    }
}
