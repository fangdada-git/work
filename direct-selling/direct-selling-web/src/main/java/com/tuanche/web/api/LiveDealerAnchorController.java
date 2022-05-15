package com.tuanche.web.api;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.basedata.entity.City;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveDealerAnchorService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveCityDto;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveDealerAnchorVo;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.service.IDistrictApiService;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.model.LocationRequestModel;
import com.tuanche.qiandao.service.IndexService;
import com.tuanche.service.ICityBaseService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-27 13:20
 **/
@Controller
@RequestMapping("/dealerAnchor")
public class LiveDealerAnchorController {

    @Reference
    private LiveDealerAnchorService liveDealerAnchorService;
    @Reference
    private LiveSceneService liveSceneService;
    @Reference
    private ICityBaseService iCityBaseService;
    @Reference
    private IndexService indexService;
    @Reference
    private IDistrictApiService iDistrictApiService;
    @Autowired
    PeriodsService periodsService;

    /**
     * @description: 百城主播排行榜列表
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/3/27 13:31
     */
    @RequestMapping("/LiveDealerAnchorRanking")
    @ResponseBody
    public TcResponse LiveDealerAnchorRanking(HttpServletRequest request, LiveDealerAnchorVo liveDealerAnchorVo) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        Object result = new ArrayList<>();
        try {
            // 取进行中的场次详细信息
            ActivityConfigResponseVo currentActivityInfo = periodsService.getCurrentActivityInfo();
            if (currentActivityInfo == null) {
                LiveScene lastLiveScenePeriodsId = liveSceneService.getLastLiveScenePeriodsId();
                ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
                activityConfigRequestVo.setId(lastLiveScenePeriodsId.getPeriodsId());
                currentActivityInfo = periodsService.queryById(activityConfigRequestVo);
            }

            liveDealerAnchorVo.setPeriodsId(currentActivityInfo.getId());
            liveDealerAnchorVo.setBeginTime(currentActivityInfo.getBeginTime());
            liveDealerAnchorVo.setEndTime(currentActivityInfo.getEndTime());
            liveDealerAnchorVo.setInt_type(StringUtils.isEmpty(liveDealerAnchorVo.getType()) ? 1 : Integer.valueOf(liveDealerAnchorVo.getType().split("_")[0]));
            //查询城市
            if (liveDealerAnchorVo.getInt_type().equals(4)) {
                return getDealerAnchorCity(request, liveDealerAnchorVo);
                //城市下直播间数据
            } else if (liveDealerAnchorVo.getInt_type().equals(5)) {
                return getCityDealerAnchorList(liveDealerAnchorVo);
            } else {
                //排行榜数据
                liveDealerAnchorVo.setAnchorType(GlobalConstants.DEALER_ANCHOR.anchor_type1.getCode());
                List<LiveDealerAnchor> dealerAnchorList = liveDealerAnchorService.LiveDealerAnchorRanking(liveDealerAnchorVo);
                if (CollectionUtil.isNotEmpty(dealerAnchorList)) {
                    result = dealerAnchorList;
                } else {
                    code = StatusCodeEnum.RESULE_DATA_NONE.getCode();
                    msg = StatusCodeEnum.RESULE_DATA_NONE.getText();
                }
            }
        } catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("百城主播排行榜列表error", "参数:" + JSON.toJSONString(liveDealerAnchorVo), e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, result);
    }
    //查询城市
    private TcResponse getDealerAnchorCity(HttpServletRequest request, LiveDealerAnchorVo liveDealerAnchorVo) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        Map<String, Object> map = new HashMap<>();
        //城市列表
        List<LiveCityDto> liveCityDtos = new ArrayList<>();
        //数据列表
        List<LiveDealerAnchor> liveDealerAnchors = new ArrayList<>();
        //当前定位城市
        LiveCityDto cityDto = new LiveCityDto();
        //经纬度获取城市
        try {
            //定位城市
            if (liveDealerAnchorVo.getType().split("_").length>=3) {
                LocationRequestModel locationRequestModel = new LocationRequestModel();
                locationRequestModel.setLongitude(liveDealerAnchorVo.getType().split("_")[1]);
                locationRequestModel.setLatitude(liveDealerAnchorVo.getType().split("_")[2]);
                City city = iCityBaseService.cityLocation(locationRequestModel);
                if (city !=null && city.getId()!=null) {
                    liveDealerAnchorVo.setCityId(city.getId());
                }
            } else {
                String ip = request.getRemoteAddr();
                com.tuanche.qiandao.entity.City city = indexService.getLocationByIp(ip,"2");
                liveDealerAnchorVo.setCityId(city != null && city.getId()!=null ? city.getId() : 10);
            }
            cityDto.setCityId(liveDealerAnchorVo.getCityId());
            //获取城市列表
            List<DistrictOutputBaseDto> districtOutputBaseDtos = liveDealerAnchorService.getAnchorCityList();
            if (CollectionUtil.isNotEmpty(districtOutputBaseDtos)) {
                districtOutputBaseDtos.forEach(districtOutputBaseDto -> {
                    if (cityDto.getCityId() !=null && districtOutputBaseDto.getId().equals(cityDto.getCityId())) {
                        cityDto.setCityName(districtOutputBaseDto.getName());
                    }
                    LiveCityDto liveCityDto = new LiveCityDto();
                    liveCityDto.setCityId(districtOutputBaseDto.getId());
                    liveCityDto.setCityName(districtOutputBaseDto.getName());
                    liveCityDto.setFirst(districtOutputBaseDto.getFirst());
                    liveCityDtos.add(liveCityDto);
                });
            }
            //查询定位城市下数据
            liveDealerAnchors = liveDealerAnchorService.getAnchorLiveDataByCityId(liveDealerAnchorVo);
            //定位城市无数据，查最近城市
            if (CollectionUtil.isEmpty(liveDealerAnchors) && CollectionUtil.isNotEmpty(liveCityDtos)) {
                List<Integer> cityIds = new ArrayList<>();
                liveCityDtos.forEach(liveCityDto -> {
                    cityIds.add(liveCityDto.getCityId());
                });
                List<Integer> resultCityIds = iDistrictApiService.getOrderedCitiesByDistance(liveDealerAnchorVo.getCityId(), cityIds);
                if (CollectionUtil.isNotEmpty(resultCityIds)) {
                    liveDealerAnchorVo.setCityId(resultCityIds.get(0));
                    cityDto.setCityId(resultCityIds.get(0));
                    for (LiveCityDto liveCityDto : liveCityDtos) {
                        if (liveCityDto.getCityId().equals(cityDto.getCityId())) {
                            cityDto.setCityName(liveCityDto.getCityName());
                            break;
                        }
                    }
                }
                liveDealerAnchors = liveDealerAnchorService.getAnchorLiveDataByCityId(liveDealerAnchorVo);
            }
            //无数据，随机查
            if (CollectionUtil.isEmpty(liveDealerAnchors) && CollectionUtil.isNotEmpty(liveCityDtos)) {
                Random random = new Random();
                Integer num = random.nextInt(liveCityDtos.size()-1);
                liveDealerAnchorVo.setCityId(liveCityDtos.get(num).getCityId());
                liveDealerAnchors = liveDealerAnchorService.getAnchorLiveDataByCityId(liveDealerAnchorVo);
                cityDto.setCityId(liveCityDtos.get(num).getCityId());
                cityDto.setCityName(liveCityDtos.get(num).getCityName());
            }
        } catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("百城主播排行榜列表-查询城市error", "参数:" + JSON.toJSONString(liveDealerAnchorVo), e);
        }
        map.put("cityList", liveCityDtos);
        map.put("data", liveDealerAnchors);
        map.put("city", cityDto);
        return new TcResponse(code, System.currentTimeMillis() - st, msg, map);
    }
    //城市下直播间数据
    private TcResponse getCityDealerAnchorList(LiveDealerAnchorVo liveDealerAnchorVo) {
        long st = System.currentTimeMillis();
        Integer code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        List<LiveDealerAnchor> list = new ArrayList<>();
        try {
            liveDealerAnchorVo.setCityId(liveDealerAnchorVo.getType().split("_").length>=2 ? Integer.valueOf(liveDealerAnchorVo.getType().split("_")[1]) : 10);
            list = liveDealerAnchorService.getAnchorLiveDataByCityId(liveDealerAnchorVo);
            if (CollectionUtil.isEmpty(list)) {
                code = StatusCodeEnum.RESULE_DATA_NONE.getCode();
                msg = StatusCodeEnum.RESULE_DATA_NONE.getText();
            }
        }catch (Exception e) {
            code = StatusCodeEnum.ERROR.getCode();
            msg = StatusCodeEnum.ERROR.getText();
            CommonLogUtil.addError("百城主播排行榜列表-城市下直播间数据error", "参数:" + JSON.toJSONString(liveDealerAnchorVo), e);
        }
        return new TcResponse(code, System.currentTimeMillis() - st, msg, list);
    }


    @RequestMapping("/synctblogincookie")
    @ResponseBody
    public ResultDto SyncTbLoginCookie(String cookieStr) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        try {
            resultDto.setResult(liveDealerAnchorService.insertLoginCookie(cookieStr));
            return resultDto;
        } catch (Exception e) {
            CommonLogUtil.addError("同步淘宝直播导购平台账号登录cookie失败", "参数：" + cookieStr, e);
            resultDto.setCode(StatusCodeEnum.ERROR.getCode());
            resultDto.setMsg(e.getMessage());
        }

        return resultDto;
    }
}
