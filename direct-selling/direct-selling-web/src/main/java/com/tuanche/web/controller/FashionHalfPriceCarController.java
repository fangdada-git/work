package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.OnlineFestivalFacadeService;
import com.tuanche.consol.dubbo.OnlineFestivalMsFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.OnlineFestivalMethodEnum;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionActivityInfoReqVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.api.FashionHalfPriceCarService;
import com.tuanche.directselling.dto.FashionCarMarkeWinNumDto;
import com.tuanche.directselling.dto.FashionHalfPriceCarDto;
import com.tuanche.directselling.model.FashionHalfPriceCar;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.CarFashionInfoHalfPriceVo;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.util.CommonLogUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author lvsen
 * @Description 半价车controller
 * @Date 2021/9/9
 **/
@Controller
@RequestMapping("/fashion/halfprice")
public class FashionHalfPriceCarController extends BaseController {

    @Reference
    FashionHalfPriceCarService fashionHalfPriceCarService;
    @Reference
    OnlineFestivalFacadeService onlineFestivalFacadeService;
    @Reference
    OnlineFestivalMsFacadeService festivalMsFacadeService;

    /**
     * 半价车链接
     */
    @Value("${fashion.half.price.car.url}")
    private String fashionHalfPriceCarUrl;

    /**
     * 到列表页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/toListPage")
    public String toListPage(ModelMap modelMap) {
        return "half_price/list";
    }

    /**
     * 列表数据
     *
     * @param pageResult
     * @return
     */
    @RequestMapping("/searchActivityList")
    @ResponseBody
    public PageResult searchActivityList(PageResult<CarFashionInfoHalfPriceVo> pageResult, CarFashionInfoEntityResVo fashionInfoEntityResVo) {
        ConsolParametersVo vo = new ConsolParametersVo("01", "01", OnlineFestivalMethodEnum.API_QUERY_ACTIVITY_PAGE);
        vo.setBusinessType(ConsolParametersVo.BUSINESS_TYPE.API_QUERY_ACTIVITY_PAGE);
        CarFashionActivityInfoReqVo carFashionActivityInfoReqVo = new CarFashionActivityInfoReqVo();
        carFashionActivityInfoReqVo.setActivityId(fashionInfoEntityResVo.getActivityId());
        carFashionActivityInfoReqVo.setCarFashionName(fashionInfoEntityResVo.getCarFashionName());
        vo.setInfo("param", JSONObject.toJSONString(carFashionActivityInfoReqVo));
        //查询所有潮车集
        ConsolParametersVo consolParametersVo = onlineFestivalFacadeService.service(vo);
//        String ttt = "{\"businessType\":\"API_QUERY_ACTIVITY_PAGE\",\"map\":{\"data\":\"[{\\\"activityId\\\":1000037,\\\"activityName\\\":\\\"测试活动\\\",\\\"brandList\\\":[{\\\"brandId\\\":11,\\\"brandName\\\":\\\"广汽本田\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"金融经销商一\\\",\\\"dealerId\\\":1897},{\\\"dealerName\\\":\\\"金融经销商二\\\",\\\"dealerId\\\":1898},{\\\"dealerName\\\":\\\"金融经销商三\\\",\\\"dealerId\\\":1899},{\\\"dealerName\\\":\\\"广汽本田4s店1-红\\\",\\\"dealerId\\\":16263},{\\\"dealerName\\\":\\\"广汽本田4s店2-红\\\",\\\"dealerId\\\":16264}]},{\\\"cityId\\\":93,\\\"cityName\\\":\\\"安阳\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"安阳1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"安阳2店\\\",\\\"dealerId\\\":2}]}],\\\"cmId\\\":10,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car//20180911/15366593179187398_s.jpg\\\"},{\\\"bVolme\\\":[{\\\"brandId\\\":5,\\\"count\\\":30,\\\"originalPrice\\\":888.00,\\\"path\\\":\\\"http://t.pic.tuanche.com/tmpic/car_tmp/f62e4b57-4319-47ad-9cf4-5e15d28cb7d7.jpg\\\",\\\"price\\\":18.00,\\\"productId\\\":653,\\\"productName\\\":\\\"好礼相送北京现代\\\",\\\"soldNumber\\\":4,\\\"styleIds\\\":[13893],\\\"totalSoldNumber\\\":5,\\\"virtualSoldNumber\\\":1}],\\\"brandId\\\":5,\\\"brandName\\\":\\\"北京现代\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"北京1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"北京2店\\\",\\\"dealerId\\\":2}]},{\\\"cityId\\\":93,\\\"cityName\\\":\\\"安阳\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"安阳1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"安阳2店\\\",\\\"dealerId\\\":2}]}],\\\"cmId\\\":1012,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car/20180611/15286888703019723_s.jpg\\\"},{\\\"brandId\\\":25,\\\"brandName\\\":\\\"比亚迪\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":20,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1413/3256525d81f5476780342e36f585b5e7.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":288,\\\"brandName\\\":\\\"小鹏汽车\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":213,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/6d9f25109cba4712a5cfc0a609625203.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":7600160,\\\"brandName\\\":\\\"长安\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":1076,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/ab23808a4c6e4333b2b629f5ac8f53cc.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":545,\\\"brandName\\\":\\\"宝马\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":26,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/224f5ae889454f1a9fd7d3f0d6ab607e.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":33,\\\"brandName\\\":\\\"奔驰\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":27,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/d8d23cfe6cc6479abefbcd1bee0e3627.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":42,\\\"brandName\\\":\\\"保时捷\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"金融经销商一\\\",\\\"dealerId\\\":1897},{\\\"dealerName\\\":\\\"金融经销商二\\\",\\\"dealerId\\\":1898},{\\\"dealerName\\\":\\\"金融经销商三\\\",\\\"dealerId\\\":1899},{\\\"dealerName\\\":\\\"广汽本田4s店1-红\\\",\\\"dealerId\\\":16263},{\\\"dealerName\\\":\\\"广汽本田4s店2-红\\\",\\\"dealerId\\\":16264}]}],\\\"cmId\\\":36,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car/20160421/14612054484674279_s.jpg\\\"},{\\\"brandId\\\":245,\\\"brandName\\\":\\\"长城汽车\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":19,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/f87872b58bec476899b7766a29143a98.png?x-oss-process=image/format,jpg\\\"}],\\\"carFashionId\\\":1,\\\"carFashionName\\\":\\\"测试活动\\\",\\\"formalDateEnd\\\":1633795200000,\\\"formalDateEndStr\\\":\\\"2021-10-10\\\",\\\"formalDateStart\\\":1632672000000,\\\"formalDateStartStr\\\":\\\"2021-09-27\\\",\\\"headDate\\\":1632067200000,\\\"headDateStr\\\":\\\"2021-09-20\\\",\\\"headImage\\\":\\\"http://t.pic.tuanche.com/material/resourceimg/20210926/16326508514229247.jpeg?x-oss-process=image/watermark,image_bWF0ZXJpYWwvd2F0ZXJtYXJrL2Ntc193YXRlcm1hcmsucG5n,x_5,y_5,g_se&width=750.0&height=587.0\\\",\\\"id\\\":1,\\\"operUrl\\\":\\\"http://tuanche.com\\\",\\\"playing\\\":[],\\\"qwUrl\\\":\\\"http://t.pic.tuanche.com/material/resourceimg/20210926/16326508514229247.jpeg?x-oss-process=image/watermark,image_bWF0ZXJpYWwvd2F0ZXJtYXJrL2Ntc193YXRlcm1hcmsucG5n,x_5,y_5,g_se&width=750.0&height=587.0\\\",\\\"videos\\\":[{\\\"id\\\":\\\"242921767550424781e911c5dd847431\\\",\\\"imageUrl\\\":\\\"tuanche.com\\\",\\\"name\\\":\\\"测试\\\",\\\"url\\\":\\\"tuanche.com\\\",\\\"videoUrl\\\":\\\"https://vod.tuanche.com/242921767550424781e911c5dd847431/6eee3027d38149fe816dc6730a6863a5-95289413ff52119365d20c468a7d621d-fd.mp4\\\"},{\\\"id\\\":\\\"969fe5678b914ac8bb591d7a872cdf70\\\",\\\"imageUrl\\\":\\\"tuanche.com\\\",\\\"name\\\":\\\"测试2\\\",\\\"url\\\":\\\"tuanche.com\\\"}]}]\",\"param\":\"{\\\"carFashionId\\\":1000037,\\\"pageNum\\\":1,\\\"pageSize\\\":10}\"},\"onlineFestivalMethodEnum\":\"API_QUERY_ACTIVITY_PAGE\",\"platform\":\"01\",\"resCode\":\"0000\",\"resDesc\":\"查询成功\",\"sources\":\"01\",\"version\":\"0.1\"}";
//        ConsolParametersVo consolParametersVo = JSONObject.parseObject(ttt,ConsolParametersVo.class);
        CommonLogUtil.addInfo("半价车获取潮车集", "入参：" + JSON.toJSONString(vo), "返参" + JSON.toJSONString(consolParametersVo));
        if (consolParametersVo == null || StringUtils.isEmpty(consolParametersVo.getString("data")) || "null".equals(consolParametersVo.getString("data"))) {
            pageResult.setData(new ArrayList<>());
            pageResult.setCode("0");
            return pageResult;
        }
        List<CarFashionInfoHalfPriceVo> fashionInfoList = JSONObject.parseArray(consolParametersVo.getString("data"), CarFashionInfoHalfPriceVo.class);
        for (CarFashionInfoHalfPriceVo fashionInfoHalfPriceVo : fashionInfoList) {
            fashionInfoHalfPriceVo.setHalfPriceUrl(fashionHalfPriceCarUrl + fashionInfoHalfPriceVo.getActivityId());
        }
        pageResult.setCount(new PageInfo<>(fashionInfoList).getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(fashionInfoList);
        pageResult.setCode("0");
        return pageResult;
    }

    @RequestMapping("/toAddHalfPriceCarPage")
    public String toAddHalfPriceCarPage(ModelMap modelMap, Integer periodsId, Integer carFashionId) {
        //潮车集活动
        ConsolParametersVo consolParametersVo = getFashionActivity(carFashionId);
        List<CarFashionInfoEntityResVo> fashionActivityList = JSONObject.parseArray(consolParametersVo.getString("data"), CarFashionInfoEntityResVo.class);
        if (consolParametersVo == null || StringUtils.isEmpty(consolParametersVo.getString("data")) ||
                "null".equals(consolParametersVo.getString("data")) || CollectionUtils.isEmpty(fashionActivityList)) {
            modelMap.addAttribute("fashionInfoList", new ArrayList<>());
            modelMap.addAttribute("halfPriceCarList", new ArrayList<>());
            modelMap.addAttribute("fashionActivity", new CarFashionInfoEntityResVo());
            modelMap.addAttribute("brandList", new ArrayList<>());
            modelMap.addAttribute("activityId", periodsId);
            return "half_price/config-half-price-car";
        }
        CarFashionInfoEntityResVo fashionActivity = fashionActivityList.get(0);
        List<FashionHalfPriceCar> halfPriceCarList = fashionHalfPriceCarService.getHalfPriceCarListByPeriodsId(periodsId);
        if (!CollectionUtils.isEmpty(halfPriceCarList)) {
            modelMap.addAttribute("activityStart", halfPriceCarList.get(0).getActivityStart());
            modelMap.addAttribute("activityEnd", halfPriceCarList.get(0).getActivityEnd());
            modelMap.addAttribute("activityId",  periodsId);
            halfPriceCarList.forEach(halfPrice -> halfPrice.setWinningNumberStr(GlobalConstants.StringFormat(5, halfPrice.getWinningNumber())));
        } else {
            modelMap.addAttribute("activityStart", fashionActivity.getFormalDateStart());
            modelMap.addAttribute("activityEnd", fashionActivity.getFormalDateEnd());
            FashionHalfPriceCar fashionHalfPriceCar = new FashionHalfPriceCar();
            halfPriceCarList.add(fashionHalfPriceCar);
            modelMap.addAttribute("activityId",  null);
        }
        List<CarFashionInfoEntityResVo> fashionInfoList = new ArrayList<>();
        fashionInfoList.add(fashionActivity);
        modelMap.addAttribute("fashionInfoList", fashionInfoList);
        modelMap.addAttribute("halfPriceCarList", halfPriceCarList);
        modelMap.addAttribute("fashionActivity", fashionActivity);
        modelMap.addAttribute("brandList", fashionActivity.getBrandList());
        return "half_price/config-half-price-car";
    }

    /**
     * 查询潮车集活动
     *
     * @return
     */
    private ConsolParametersVo getFashionActivity(Integer carFashionId) {
        ConsolParametersVo vo = new ConsolParametersVo("01", "01", OnlineFestivalMethodEnum.API_QUERY_ACTIVITY_PAGE);
        vo.setBusinessType(ConsolParametersVo.BUSINESS_TYPE.API_QUERY_ACTIVITY_PAGE);
        CarFashionActivityInfoReqVo carFashionActivityInfoReqVo = new CarFashionActivityInfoReqVo();
        carFashionActivityInfoReqVo.setCarFashionId(carFashionId);
        vo.setInfo("param", JSONObject.toJSONString(carFashionActivityInfoReqVo));
        //查询所有潮车集
        ConsolParametersVo consolParametersVo = onlineFestivalFacadeService.service(vo);
        CommonLogUtil.addInfo("半价车获取潮车集", "入参：" + JSON.toJSONString(vo), "返参" + JSON.toJSONString(consolParametersVo));
//        String ttt = "{\"businessType\":\"API_QUERY_ACTIVITY_PAGE\",\"map\":{\"data\":\"[{\\\"activityId\\\":1000037,\\\"activityName\\\":\\\"测试活动\\\",\\\"brandList\\\":[{\\\"brandId\\\":11,\\\"brandName\\\":\\\"广汽本田\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"金融经销商一\\\",\\\"dealerId\\\":1897},{\\\"dealerName\\\":\\\"金融经销商二\\\",\\\"dealerId\\\":1898},{\\\"dealerName\\\":\\\"金融经销商三\\\",\\\"dealerId\\\":1899},{\\\"dealerName\\\":\\\"广汽本田4s店1-红\\\",\\\"dealerId\\\":16263},{\\\"dealerName\\\":\\\"广汽本田4s店2-红\\\",\\\"dealerId\\\":16264}]},{\\\"cityId\\\":93,\\\"cityName\\\":\\\"安阳\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"安阳1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"安阳2店\\\",\\\"dealerId\\\":2}]}],\\\"cmId\\\":10,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car//20180911/15366593179187398_s.jpg\\\"},{\\\"bVolme\\\":[{\\\"brandId\\\":5,\\\"count\\\":30,\\\"originalPrice\\\":888.00,\\\"path\\\":\\\"http://t.pic.tuanche.com/tmpic/car_tmp/f62e4b57-4319-47ad-9cf4-5e15d28cb7d7.jpg\\\",\\\"price\\\":18.00,\\\"productId\\\":653,\\\"productName\\\":\\\"好礼相送北京现代\\\",\\\"soldNumber\\\":4,\\\"styleIds\\\":[13893],\\\"totalSoldNumber\\\":5,\\\"virtualSoldNumber\\\":1}],\\\"brandId\\\":5,\\\"brandName\\\":\\\"北京现代\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"北京1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"北京2店\\\",\\\"dealerId\\\":2}]},{\\\"cityId\\\":93,\\\"cityName\\\":\\\"安阳\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"安阳1店\\\",\\\"dealerId\\\":1},{\\\"dealerName\\\":\\\"安阳2店\\\",\\\"dealerId\\\":2}]}],\\\"cmId\\\":1012,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car/20180611/15286888703019723_s.jpg\\\"},{\\\"brandId\\\":25,\\\"brandName\\\":\\\"比亚迪\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":20,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1413/3256525d81f5476780342e36f585b5e7.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":288,\\\"brandName\\\":\\\"小鹏汽车\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":213,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/6d9f25109cba4712a5cfc0a609625203.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":7600160,\\\"brandName\\\":\\\"长安\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":1076,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/ab23808a4c6e4333b2b629f5ac8f53cc.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":545,\\\"brandName\\\":\\\"宝马\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":26,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/224f5ae889454f1a9fd7d3f0d6ab607e.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":33,\\\"brandName\\\":\\\"奔驰\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":27,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/d8d23cfe6cc6479abefbcd1bee0e3627.png?x-oss-process=image/format,jpg\\\"},{\\\"brandId\\\":42,\\\"brandName\\\":\\\"保时捷\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"金融经销商一\\\",\\\"dealerId\\\":1897},{\\\"dealerName\\\":\\\"金融经销商二\\\",\\\"dealerId\\\":1898},{\\\"dealerName\\\":\\\"金融经销商三\\\",\\\"dealerId\\\":1899},{\\\"dealerName\\\":\\\"广汽本田4s店1-红\\\",\\\"dealerId\\\":16263},{\\\"dealerName\\\":\\\"广汽本田4s店2-红\\\",\\\"dealerId\\\":16264}]}],\\\"cmId\\\":36,\\\"imageUrl\\\":\\\"http://pic.tuanche.com/car/20160421/14612054484674279_s.jpg\\\"},{\\\"brandId\\\":245,\\\"brandName\\\":\\\"长城汽车\\\",\\\"cityRef\\\":[{\\\"cityId\\\":10,\\\"cityName\\\":\\\"北京\\\",\\\"refrens\\\":[{\\\"dealerName\\\":\\\"经销商1897\\\",\\\"dealerId\\\":1}]}],\\\"cmId\\\":19,\\\"imageUrl\\\":\\\"http://t.pic.tuanche.com/car/20201116/1414/f87872b58bec476899b7766a29143a98.png?x-oss-process=image/format,jpg\\\"}],\\\"carFashionId\\\":1,\\\"carFashionName\\\":\\\"测试活动\\\",\\\"formalDateEnd\\\":1634227200000,\\\"formalDateEndStr\\\":\\\"2021-10-15\\\",\\\"formalDateStart\\\":1633708800000,\\\"formalDateStartStr\\\":\\\"2021-10-09\\\",\\\"headDate\\\":1632067200000,\\\"headDateStr\\\":\\\"2021-09-20\\\",\\\"headImage\\\":\\\"http://t.pic.tuanche.com/material/resourceimg/20210926/16326508514229247.jpeg?x-oss-process=image/watermark,image_bWF0ZXJpYWwvd2F0ZXJtYXJrL2Ntc193YXRlcm1hcmsucG5n,x_5,y_5,g_se&width=750.0&height=587.0\\\",\\\"id\\\":1,\\\"operUrl\\\":\\\"http://tuanche.com\\\",\\\"playing\\\":[],\\\"qwUrl\\\":\\\"http://t.pic.tuanche.com/material/resourceimg/20210926/16326508514229247.jpeg?x-oss-process=image/watermark,image_bWF0ZXJpYWwvd2F0ZXJtYXJrL2Ntc193YXRlcm1hcmsucG5n,x_5,y_5,g_se&width=750.0&height=587.0\\\",\\\"videos\\\":[{\\\"id\\\":\\\"242921767550424781e911c5dd847431\\\",\\\"imageUrl\\\":\\\"tuanche.com\\\",\\\"name\\\":\\\"测试\\\",\\\"url\\\":\\\"tuanche.com\\\",\\\"videoUrl\\\":\\\"https://vod.tuanche.com/242921767550424781e911c5dd847431/6eee3027d38149fe816dc6730a6863a5-95289413ff52119365d20c468a7d621d-fd.mp4\\\"},{\\\"id\\\":\\\"969fe5678b914ac8bb591d7a872cdf70\\\",\\\"imageUrl\\\":\\\"tuanche.com\\\",\\\"name\\\":\\\"测试2\\\",\\\"url\\\":\\\"tuanche.com\\\"}]}]\",\"param\":\"{\\\"carFashionId\\\":1000037,\\\"pageNum\\\":1,\\\"pageSize\\\":10}\"},\"onlineFestivalMethodEnum\":\"API_QUERY_ACTIVITY_PAGE\",\"platform\":\"01\",\"resCode\":\"0000\",\"resDesc\":\"查询成功\",\"sources\":\"01\",\"version\":\"0.1\"}";
//        ConsolParametersVo consolParametersVo = JSONObject.parseObject(ttt,ConsolParametersVo.class);
        return consolParametersVo;
    }

    /**
     * 保存半价车配置信息
     *
     * @param request
     * @param fashionHalfPriceCarDto
     * @return
     */
    @RequestMapping("/saveFashionHalfPriceCar")
    @ResponseBody
    public ResultDto saveFashionHalfPriceCar(HttpServletRequest request, @RequestBody FashionHalfPriceCarDto fashionHalfPriceCarDto) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if (Objects.isNull(fashionHalfPriceCarDto) || CollectionUtils.isEmpty(fashionHalfPriceCarDto.getFashionHalfPriceCarList())) {
            return paramBlank();
        }
        try {
            //carFashionId 为空是新增，不为空是修改
            if (fashionHalfPriceCarDto.getCarFashionId() == null) {
                fashionHalfPriceCarService.saveFashionHalfPriceCar(fashionHalfPriceCarDto.getFashionHalfPriceCarList(), xxlUser.getId());
            } else {
                fashionHalfPriceCarService.updateFashionHalfPriceCar(fashionHalfPriceCarDto.getFashionHalfPriceCarList(), xxlUser.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systemError();
        }
        return success();
    }

    /**
     * 中奖用户
     *
     * @return
     */
    @RequestMapping("/toWinnerList")
    public String toFissionGoodsList(ModelMap modelMap, @RequestParam("periodsId") Integer periodsId, @RequestParam("carFashionId") Integer carFashionId, @RequestParam("carFashionName") String carFashionName) {
        modelMap.addAttribute("carFashionId", carFashionId);
        modelMap.addAttribute("periodsId", periodsId);
        modelMap.addAttribute("carFashionName", carFashionName);
        return "half_price/winner-list";
    }

    /**
     * 中奖用户列表
     *
     * @return
     */
    @RequestMapping("/winnerList")
    @ResponseBody
    public PageResult<FashionCarMarkeWinNumDto> getActivityPackageList(PageResult<FashionCarMarkeWinNumDto> pageResult, FashionCarMarkeWinNumDto fashionCarMarkeWinNumDto) {
        ConsolParametersVo consolParametersVo = getFashionActivity(fashionCarMarkeWinNumDto.getCarFashionId());
        List<CarFashionInfoEntityResVo> fashionActivityList = JSONObject.parseArray(consolParametersVo.getString("data"), CarFashionInfoEntityResVo.class);
        CarFashionInfoEntityResVo fashionActivity = fashionActivityList.get(0);
        PageResult pageList = fashionHalfPriceCarService.getFashionCarMarkeWinnerListByPage(pageResult, fashionCarMarkeWinNumDto, fashionActivity);
        pageList.setCode("0");
        return pageList;
    }
}
