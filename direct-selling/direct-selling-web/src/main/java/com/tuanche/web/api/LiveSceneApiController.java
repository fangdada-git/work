package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.LiveSceneDealerBrandService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveSceneCityDto;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.*;
import com.tuanche.web.controller.BaseController;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: LiveSceneController
 * @Description: 团车直卖-场次活动对外接口
 * @Author: GongBo
 * @Date: 2020年3月4日18:11:19
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api/scene")
public class LiveSceneApiController extends BaseController {

    @Reference
    LiveSceneService liveSceneService;
    @Autowired
    CommonWebService commonWebService;
    @Reference
    private LiveSceneDealerBrandService liveSceneDealerBrandService;

    /**
     * @param cityId
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 团车直卖api-获取场次活动城市
     * @Date 2020年4月2日11:25:38
     **/
    @RequestMapping("/selectSceneCity")
    @ResponseBody
    public ResultDto selectSceneCity(Integer cityId, Integer periodsId, Integer brandId) {
        ResultDto resultDto = new ResultDto();
        try {
            // 获取场次活动城市
            LiveSceneVo liveSceneVo = new LiveSceneVo();
            liveSceneVo.setCityId(cityId);
            liveSceneVo.setPeriodsId(periodsId);
            liveSceneVo.setBrandId(brandId);
            List<LiveSceneCityDto> liveSceneCityDtoList = liveSceneService.getLiveSceneCityDtoList(liveSceneVo);

            if (null == liveSceneCityDtoList) {
                resultDto = noData();
            } else {
                //按首字母分组
                Map<String,List<LiveSceneCityDto>> result = commonWebService.groupByPinYinHeadCharByLambda(liveSceneCityDtoList);
                resultDto = success(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = systemError();
        }
        return resultDto;
    }

    /**
     * @param cityId,periodsId
     * @return: com.tuanche.arch.web.model.TcResponse
     * @description: 获取品牌集合 直播投放页面使用
     * @author: czy
     * @create: 2020/4/22 17:07
     **/
    @RequestMapping("/brands")
    @ResponseBody
    public TcResponse getBrandListByPeriodsIdAndCityId(@RequestParam("periodsId") Integer periodsId
            , @RequestParam("cityId") Integer cityId){
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        if (periodsId == null || periodsId<=0 || cityId == null || cityId<=0){
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
        }
        Map<String,List<LiveSceneBrandListVo>> map = new HashMap<>();
        try {
            map = liveSceneDealerBrandService.getBrandListByPeriodsIdAndCityId(periodsId, cityId);
        }catch (Exception e){
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("getBrandListByPeriodsIdAndCityId","获取品牌集合 直播投放页面使用异常",e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, map);
    }

    /**
     * @param periodsId
     * @return: com.tuanche.arch.web.model.TcResponse
     * @description:
     * @author: czy
     * @create: 2020/4/30 11:00
     **/
    @RequestMapping("/brands/a")
    @ResponseBody
    public TcResponse getBrandListByPeriodsId(@RequestParam("periodsId") Integer periodsId){
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        if (periodsId == null || periodsId<=0 ){
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
        }
        List<LiveSceneBrandVo> list = new ArrayList<>();
        try {
            list = liveSceneDealerBrandService.getBrandListByPeriodsId(periodsId);
        }catch (Exception e){
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("getBrandListByPeriodsIdAndCityId","获取参展品牌List集合异常",e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, list);
    }

    /**
     * @param periodsId
     * @param cityId
     * @param masterBrandId
     * @return com.tuanche.arch.web.model.TcResponse
     * @Author GongBo
     * @Description 前端根据大场次ID & 城市ID & 主品牌ID 获取直卖二级品牌车型数据
     * @Date 9:58 2020/6/8
     **/
    @RequestMapping("/carStyles")
    @ResponseBody
    public TcResponse getLiveSceneCarStyleList(@RequestParam("periodsId") Integer periodsId
            , @RequestParam("cityId") Integer cityId, @RequestParam("masterBrandId") Integer masterBrandId) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        Map<String, List<LiveSceneBrandVo>> map = new HashMap<>();
        if (periodsId == null || periodsId <= 0 || cityId == null || cityId <= 0 || masterBrandId == null || masterBrandId <= 0) {
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
            return new TcResponse(code, System.currentTimeMillis() - st, msg, map);
        }
        try {
            map = liveSceneDealerBrandService.getLiveSceneCarStyleList(periodsId, cityId, masterBrandId);
        } catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("getLiveSceneCarStyleList", "获取品牌集合 直卖投放页面使用异常", e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, map);
    }

    /**
     * @param periodsId
     * @param cityId
     * @param excludeBrandId
     * @param size
     * @return TcResponse
     * @Author GongBo
     * @Description 根据场次 & 城市 & 条数 & 排除二级品牌ID  随机获取场次下的品牌
     * @Date 10:23 2020/6/15
     **/
    @RequestMapping("/randomBrands")
    @ResponseBody
    public TcResponse randomBrands(@RequestParam("periodsId") Integer periodsId, @RequestParam("cityId") Integer cityId
            , @RequestParam("size") Integer size, @RequestParam("excludeBrandId") Integer excludeBrandId) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        List<Map<String, Object>> result = new ArrayList<>();
        if (periodsId == null || periodsId <= 0 || cityId == null || cityId <= 0 ||
                excludeBrandId == null || excludeBrandId <= 0 || size == null || size <= 0) {
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
            return new TcResponse(code, System.currentTimeMillis() - st, msg, result);
        }
        try {
            result = liveSceneDealerBrandService.getRandomBrands(periodsId, cityId, excludeBrandId, size);
        } catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("randomBrands", "随机获取场次下的品牌 直卖投放页面使用异常", e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, result);
    }

    /**
     * @param cityId,periodsId
     * @return: com.tuanche.arch.web.model.TcResponse
     * @description: 根据场次 & 城市  获取直卖获取二级品牌集合
     * @author: gongbo
     * @create: 2020年7月7日15:12:23
     **/
    @RequestMapping("/secondBrands")
    @ResponseBody
    public TcResponse getSecondBrandListByPeriodsIdAndCityId(@RequestParam("periodsId") Integer periodsId
            , @RequestParam("cityId") Integer cityId){
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        if (periodsId == null || periodsId<=0 || cityId == null || cityId<=0){
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
        }
        List<LiveSceneSecondBrandListVo> list = new ArrayList<>();
        try {
            list = liveSceneDealerBrandService.getSecondBrandListByPeriodsIdAndCityId(periodsId, cityId);
        }catch (Exception e){
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("getSecondBrandListByPeriodsIdAndCityId","根据场次 & 城市  获取直卖获取二级品牌集合异常",e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, list);
    }

    /**
     * @param periodsId
     * @param cityId
     * @param secondBrandId
     * @return com.tuanche.arch.web.model.TcResponse
     * @Author GongBo
     * @Description 前端根据大场次ID & 城市ID & 二级品牌ID 获取直卖车型数据
     * @Date 2020年7月7日15:14:29
     **/
    @RequestMapping("/carStylesByBrandId")
    @ResponseBody
    public TcResponse getCarStylesByBrandId(@RequestParam("periodsId") Integer periodsId
            , @RequestParam("cityId") Integer cityId, @RequestParam("secondBrandId") Integer secondBrandId) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        List<LiveSceneCarStyleVo> result = new ArrayList<>();
        if (periodsId == null || periodsId <= 0 || cityId == null || cityId <= 0 || secondBrandId == null || secondBrandId <= 0) {
            code = StatusCodeEnum.PARAM_IS_BLANK.getCode();
            msg = StatusCodeEnum.PARAM_IS_BLANK.getText();
            return new TcResponse(code, System.currentTimeMillis() - st, msg, result);
        }
        try {
            result = liveSceneDealerBrandService.getLiveSceneCarStyleListBySecondBrandId(periodsId, cityId, secondBrandId);
        } catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("getCarStylesByBrandId", "前端根据大场次ID & 城市ID & 二级品牌ID 获取直卖车型数据异常", e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, result);
    }
}
