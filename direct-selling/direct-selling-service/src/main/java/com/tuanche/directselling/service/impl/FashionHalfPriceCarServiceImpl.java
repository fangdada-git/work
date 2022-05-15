package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.DateUtils;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.consol.dubbo.OnlineFestivalFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.OnlineFestivalMethodEnum;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionActivityInfoReqVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionBrandInfoEntityResVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.api.FashionHalfPriceCarService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.FashionCarMarkeWinNumDto;
import com.tuanche.directselling.dto.FashionHalfPriceCarActivityDto;
import com.tuanche.directselling.mapper.read.directselling.FashionCarMarkeWinNumReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FashionHalfPriceCarReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionCarMarkeWinNumWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionHalfPriceCarWriteMapper;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;
import com.tuanche.directselling.model.FashionHalfPriceCar;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FashionHalfPriceBrandVo;
import com.tuanche.directselling.vo.FashionHalfPriceCarActivityVo;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.dto.SmsDto;
import com.tuanche.manubasecenter.util.EmailUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/9/10
 **/
@Service
public class FashionHalfPriceCarServiceImpl implements FashionHalfPriceCarService {

    @Value("${fashion_semivalent_car_receivers}")
    private String fashion_semivalent_car_receivers;
    @Value("${fashion_semivalent_car_phone}")
    private String fashion_semivalent_car_phone;
    @Value("${sms_system_code}")
    private String sms_system_code;
    @Value("${sms_business_code}")
    private String sms_business_code;
    @Value("${sms_business_template_notify}")
    private Integer sms_business_template_notify;
    @Value("${fashion_half_price_winning_hour}")
    private Integer fashion_half_price_winning_hour;

    @Autowired
    FashionHalfPriceCarReadMapper halfPriceCarReadMapper;
    @Autowired
    FashionHalfPriceCarWriteMapper halfPriceCarWriteMapper;
    @Autowired
    FashionCarMarkeWinNumReadMapper fashionCarMarkeWinNumReadMapper;
    @Reference
    OnlineFestivalFacadeService onlineFestivalFacadeService;
    @Reference
    private FashionCarWinningNumberService fashionCarWinningNumberService;
    @Autowired
    private FashionCarMarkeWinNumWriteMapper fashionCarMarkeWinNumWriteMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    RedisService redisService;
    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;

    @Override
    public List<FashionHalfPriceCar> getHalfPriceCarListByPeriodsId(Integer periodsId) {
        if (Objects.isNull(periodsId)) {
            return new ArrayList<>();
        }
        return halfPriceCarReadMapper.selectListByPeriodsId(periodsId);
    }

    @Override
    public void saveFashionHalfPriceCar(List<FashionHalfPriceCar> fashionHalfPriceCarList, Integer optUser) {
        Date now = new Date();
        fashionHalfPriceCarList.sort(Comparator.comparing(FashionHalfPriceCar::getActivityDate));
        for (FashionHalfPriceCar halfPriceCar : fashionHalfPriceCarList) {
            halfPriceCar.setCreatBy(optUser);
            halfPriceCar.setCreatDt(now);
            halfPriceCar.setDeleteFlag(0);
            halfPriceCarWriteMapper.insertSelective(halfPriceCar);
            if (halfPriceCar.getWinningNumber() != null) {
                FashionCarMarkeWinNum fashionCarMarkeWinNum = fashionCarMarkeWinNumReadMapper.selectWinNum(halfPriceCar.getPeriodsId(), halfPriceCar.getWinningNumber());
                if (fashionCarMarkeWinNum != null) {
                    Integer newNum = fashionCarWinningNumberService.changeWinNum(halfPriceCar.getWinningNumber());
                    fashionCarMarkeWinNumWriteMapper.updateByWinNum(halfPriceCar.getPeriodsId(), halfPriceCar.getWinningNumber(), newNum);
                }
            }
        }
    }

    /**
     * @param :
     * @return :
     * @description : 潮车集半价车中奖码录入提醒
     * @author : fxq
     * @date : 2021/9/14 16:35
     */
    @Override
    public void getNotFilledActivity(Integer periodsId) {
        List<FashionHalfPriceCar> list = halfPriceCarReadMapper.getNotFilledActivity(periodsId);
        CommonLogUtil.addInfo(null, "潮车集半价车中奖码录入提醒,未录入活动", JSON.toJSONString(list));
        if (CollectionUtils.isEmpty(list)) {
            //邮件提醒
            EmailUtil.send("中奖码录入", Arrays.asList(fashion_semivalent_car_receivers.split(",")), "请在20点55分前录入中奖码");
            //短信提醒
            SmsDto smsDto = new SmsDto();
            smsDto.setCityId(10);
            smsDto.setSystemCode(sms_system_code);
            smsDto.setBusinessCode(sms_business_code);
            smsDto.setPhones(fashion_semivalent_car_phone);
            smsDto.setTemplateId(sms_business_template_notify);
            smsDto.setParamArray(new String[]{"请在20:55分前录入中奖码"});
            ResultDto resultDto = smsVerifyCodeService.sendSmsByEmptyTemplant(smsDto);
            CommonLogUtil.addInfo(null, "潮车集半价车中奖码录入提醒", resultDto);
        }
    }

    @Override
    public void updateFashionHalfPriceCar(List<FashionHalfPriceCar> fashionHalfPriceCarList, Integer optUser) {
        if (CollectionUtils.isEmpty(fashionHalfPriceCarList)) {
            return;
        }
        List<FashionHalfPriceCar> halfPriceCarList = halfPriceCarReadMapper.selectListByPeriodsId(fashionHalfPriceCarList.get(0).getPeriodsId());
        Map<Integer, FashionHalfPriceCar> dateMap = new HashMap<>();
        for (FashionHalfPriceCar fashionHalfPriceCar : halfPriceCarList) {
            dateMap.put(fashionHalfPriceCar.getId(), fashionHalfPriceCar);
        }
        fashionHalfPriceCarList.sort(Comparator.comparing(FashionHalfPriceCar::getActivityDate));
        Date now = new Date();
        for (FashionHalfPriceCar halfPriceCar : fashionHalfPriceCarList) {
            if (halfPriceCar.getWinningNumber() != null) {
                FashionCarMarkeWinNum fashionCarMarkeWinNum = fashionCarMarkeWinNumReadMapper.selectWinNum(halfPriceCar.getPeriodsId(), halfPriceCar.getWinningNumber());
                if (fashionCarMarkeWinNum != null) {
                    Integer newNum = fashionCarWinningNumberService.changeWinNum(halfPriceCar.getWinningNumber());
                    fashionCarMarkeWinNumWriteMapper.updateByWinNum(halfPriceCar.getPeriodsId(), halfPriceCar.getWinningNumber(), newNum);
                }
            }
            if (Objects.isNull(halfPriceCar.getId())) {
                halfPriceCar.setCreatBy(optUser);
                halfPriceCar.setCreatDt(now);
                halfPriceCar.setDeleteFlag(0);
                halfPriceCarWriteMapper.insertSelective(halfPriceCar);
                continue;
            }
            halfPriceCar.setUpdateBy(optUser);
            halfPriceCar.setUpdateDt(now);
            halfPriceCarWriteMapper.updateByPrimaryKeySelective(halfPriceCar);
            if (dateMap.get(halfPriceCar.getId()) != null) {
                dateMap.remove(halfPriceCar.getId());
            }
        }
        //与潮车集同步删除日期
        for (Map.Entry<Integer, FashionHalfPriceCar> m : dateMap.entrySet()) {
            FashionHalfPriceCar fashionHalfPriceCar = m.getValue();
            fashionHalfPriceCar.setDeleteFlag(1);
            fashionHalfPriceCar.setUpdateBy(optUser);
            fashionHalfPriceCar.setUpdateDt(now);
            halfPriceCarWriteMapper.updateByPrimaryKeySelective(fashionHalfPriceCar);
        }
        try {
            redisService.del(BaseCacheKeys.FASHION_CAR_HALF_PRICE_CACHE.getKey() + fashionHalfPriceCarList.get(0).getPeriodsId());
            redisService.del(BaseCacheKeys.FASHION_CAR_BRAND_LIST_CACHE.getKey() + fashionHalfPriceCarList.get(0).getPeriodsId());
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    /**
     * 中奖用户
     *
     * @return
     */
    @Override
    public PageResult<FashionCarMarkeWinNumDto> getFashionCarMarkeWinnerListByPage(PageResult<FashionCarMarkeWinNumDto> pageResult,
                                                                                   FashionCarMarkeWinNumDto fashionCarMarkeWinNumDto, CarFashionInfoEntityResVo fashionActivity) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<FashionCarMarkeWinNumDto> list = fashionCarMarkeWinNumReadMapper.selectFashionCarMarkeWinnerList(fashionCarMarkeWinNumDto);
        List<CarFashionBrandInfoEntityResVo> brandList = fashionActivity.getBrandList();
        if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(brandList)) {
            Map<Integer, CarFashionBrandInfoEntityResVo> brandMap = brandList.stream().collect(Collectors.toMap(CarFashionBrandInfoEntityResVo::getBrandId, item -> item));
            for (FashionCarMarkeWinNumDto carMarkeWinNumDto : list) {
                CarFashionBrandInfoEntityResVo brand = brandMap.get(carMarkeWinNumDto.getBrandId());
                carMarkeWinNumDto.setBrandName(brand != null ? brand.getBrandName() : "");
            }
        }
        PageInfo<FashionCarMarkeWinNumDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 半价车活动
     *
     * @param halfPriceCarActivityDto
     * @return
     */
    @Override
    public TcResponse getHalfPriceActivityInfoForApi(FashionHalfPriceCarActivityDto halfPriceCarActivityDto, Integer userId) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("半价车首页-数据接口 start", "入参：userId=" + userId, JSON.toJSONString(halfPriceCarActivityDto));
        FashionHalfPriceCarActivityVo halfPriceCarActivityVo = new FashionHalfPriceCarActivityVo();
        try {
            LocalDateTime now = LocalDateTime.now();
            halfPriceCarActivityVo = redisService.get(BaseCacheKeys.FASHION_CAR_HALF_PRICE_CACHE.getKey() + halfPriceCarActivityDto.getPeriodsId(), FashionHalfPriceCarActivityVo.class);
            if (!Objects.isNull(halfPriceCarActivityVo)) {
                decorateHalfPriceCarActivity(halfPriceCarActivityDto, userId, halfPriceCarActivityVo, now);
                CommonLogUtil.addInfo("半价车首页-数据接口 end", "redis返参：", JSON.toJSONString(halfPriceCarActivityVo));
                return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), halfPriceCarActivityVo);
            }
            halfPriceCarActivityVo = new FashionHalfPriceCarActivityVo();
            CarFashionInfoEntityResVo carFashionInfoEntityResVo = getCarFashionInfoEntityResVo(halfPriceCarActivityDto.getPeriodsId());
            if (!Objects.isNull(carFashionInfoEntityResVo)) {
                halfPriceCarActivityVo.setQwUrl(carFashionInfoEntityResVo.getQwUrl());
            }
            List<FashionHalfPriceBrandVo> halfPriceActivityBrandList = getHalfPriceActivityBrandList(halfPriceCarActivityDto);
            halfPriceCarActivityVo.setHalfPriceBrandVoList(halfPriceActivityBrandList);
            //半价车活动时间
            if (!CollectionUtils.isEmpty(halfPriceActivityBrandList)) {
                Date activityStart = halfPriceActivityBrandList.get(0).getActivityStart();
                Date activityEnd = halfPriceActivityBrandList.get(0).getActivityEnd();
                halfPriceCarActivityVo.setFormalDateStart(activityStart.getTime());
                halfPriceCarActivityVo.setFormalDateEnd(activityEnd.getTime());
                halfPriceCarActivityVo.setFormalDateStartStr(DateUtils.dateToString(activityStart, DateUtils.YYYY_MM_DD));
                halfPriceCarActivityVo.setFormalDateEndStr(DateUtils.dateToString(activityEnd, DateUtils.YYYY_MM_DD));
            }
            decorateHalfPriceCarActivity(halfPriceCarActivityDto, userId, halfPriceCarActivityVo, now);
            //防止C端返回空数据，缓冲空数据
            if (!Objects.isNull(carFashionInfoEntityResVo) && !CollectionUtils.isEmpty(halfPriceActivityBrandList)) {
                long exTime = Duration.between(now, now.withHour(23).withMinute(59).withSecond(59).withNano(999)).toMillis();
                redisService.set(BaseCacheKeys.FASHION_CAR_HALF_PRICE_CACHE.getKey() + halfPriceCarActivityDto.getPeriodsId(), halfPriceCarActivityVo, exTime);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        CommonLogUtil.addInfo("半价车首页-数据接口 end", "db 返参：", JSON.toJSONString(halfPriceCarActivityVo));
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), halfPriceCarActivityVo);
    }

    /**
     * HalfPriceCarActivity 扩展用户中奖信息
     */
    private void decorateHalfPriceCarActivity(FashionHalfPriceCarActivityDto halfPriceCarActivityDto, Integer userId, FashionHalfPriceCarActivityVo halfPriceCarActivityVo, LocalDateTime now) {
        boolean countdownFlag = false;
        Date currentDate = org.apache.commons.lang3.time.DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        List<FashionHalfPriceBrandVo> halfPriceBrandVoList = halfPriceCarActivityVo.getHalfPriceBrandVoList();
        for (FashionHalfPriceBrandVo halfPriceBrandVo : halfPriceBrandVoList) {
            halfPriceCarActivityVo.setCountdowns(-1);
            if (halfPriceBrandVo.getJoinDate() == currentDate.getTime()) {
                countdownFlag = true;
                break;
            }
        }
        //倒计时，当天有抽奖日期才展示
        if(countdownFlag) {
            if (now.compareTo(now.withHour(fashion_half_price_winning_hour).withMinute(0).withSecond(0).withNano(0)) > 0) {
                halfPriceCarActivityVo.setCountdowns(0);
            } else {
                halfPriceCarActivityVo.setCountdowns(Duration.between(now, now.withHour(fashion_half_price_winning_hour).withMinute(0).withSecond(0).withNano(0)).toMillis() / 1000);
            }
        }
        if (!Objects.isNull(userId)) {
            FashionCarMarkeWinNum carMarkeWinNum = new FashionCarMarkeWinNum();
            carMarkeWinNum.setUserId(userId);
            carMarkeWinNum.setPeriodsId(halfPriceCarActivityDto.getPeriodsId());
            List<FashionCarMarkeWinNum> halfPriceWinNumList = fashionCarMarkeWinNumReadMapper.getFashionCarMarkeWinNumList(carMarkeWinNum);
            CommonLogUtil.addInfo("半价车首页-是否抽过抽奖码", "返参：", JSON.toJSONString(halfPriceWinNumList));
            halfPriceCarActivityVo.setWinNumFlag(CollectionUtils.isEmpty(halfPriceWinNumList) ? false : true);
        }
        // 判断活动状态 1未开始 2进行中 3已结束
        Date date = new Date();
        if (date.getTime() < halfPriceCarActivityVo.getFormalDateStart()) {
            halfPriceCarActivityVo.setActivityStatus(1);
        } else if (date.getTime() >= halfPriceCarActivityVo.getFormalDateStart() && date.getTime() < DateUtils.dateAddDay(halfPriceCarActivityVo.getFormalDateEnd(),1)) {
            halfPriceCarActivityVo.setActivityStatus(2);
        } else if (date.getTime() > DateUtils.dateAddDay(halfPriceCarActivityVo.getFormalDateEnd(),1)) {
            halfPriceCarActivityVo.setActivityStatus(3);
        }
    }


    /**
     * 潮车集品牌列表
     *
     * @param halfPriceCarActivityDto
     * @return
     */
    @Override
    public List<FashionHalfPriceBrandVo> getHalfPriceActivityBrandList(FashionHalfPriceCarActivityDto halfPriceCarActivityDto) {
        CommonLogUtil.addInfo("潮车集首页-获取品牌数据接口 start", "入参：", JSON.toJSONString(halfPriceCarActivityDto));
        List<FashionHalfPriceBrandVo> brandVoList;
        try {
            String result = redisService.get(BaseCacheKeys.FASHION_CAR_BRAND_LIST_CACHE.getKey() + halfPriceCarActivityDto.getPeriodsId(), String.class);
            if (!StringUtil.isEmpty(result) && !result.equals("null") && !result.equals("[]")) {
                CommonLogUtil.addInfo("潮车集首页-获取品牌数据接口 end", "redis返参：", result);
                return JSON.parseArray(result, FashionHalfPriceBrandVo.class);
            }
            List<FashionHalfPriceCar> halfPriceCarList = halfPriceCarReadMapper.selectListByPeriodsId(halfPriceCarActivityDto.getPeriodsId());
            CarFashionInfoEntityResVo carFashionInfo = getCarFashionInfoEntityResVo(halfPriceCarActivityDto.getPeriodsId());
            brandVoList = new ArrayList<>();
            if(Objects.isNull(carFashionInfo)){
                return brandVoList;
            }
            List<CarFashionBrandInfoEntityResVo> brandList = carFashionInfo.getBrandList();
            Map<Integer, CarFashionBrandInfoEntityResVo> brandMap = brandList.stream().collect(Collectors.toMap(CarFashionBrandInfoEntityResVo::getBrandId, item -> item));
            LocalDateTime now = LocalDateTime.now();
            FashionHalfPriceBrandVo halfPriceBrandVo;
            halfPriceCarList.sort(Comparator.comparing(FashionHalfPriceCar::getActivityDate));
            for (FashionHalfPriceCar halfPriceCar : halfPriceCarList) {
                halfPriceBrandVo = new FashionHalfPriceBrandVo();
                halfPriceBrandVo.setBrandId(halfPriceCar.getBrandId());
                halfPriceBrandVo.setJoinDateStr(DateUtils.dateToString(halfPriceCar.getActivityDate(), DateUtils.YYYY_MM_DD));
                halfPriceBrandVo.setJoinDate(halfPriceCar.getActivityDate().getTime());
                halfPriceBrandVo.setBrandName(brandMap.get(halfPriceCar.getBrandId()).getBrandName());
                halfPriceBrandVo.setImageUrl(brandMap.get(halfPriceCar.getBrandId()).getImageUrl());
                halfPriceBrandVo.setActivityStart(halfPriceCar.getActivityStart());
                halfPriceBrandVo.setActivityEnd(halfPriceCar.getActivityEnd());
                if (halfPriceBrandVo.getJoinDateStr().equals(now.toLocalDate().toString())) {
                    halfPriceBrandVo.setCurrentDayFlag(true);
                }
                brandVoList.add(halfPriceBrandVo);
            }
            //防止C端返回空数据，缓冲空数据
            if (!CollectionUtils.isEmpty(brandVoList)) {
                long exTime = Duration.between(now, now.withHour(23).withMinute(59).withSecond(59).withNano(999)).toMillis();
                redisService.set(BaseCacheKeys.FASHION_CAR_BRAND_LIST_CACHE.getKey() + halfPriceCarActivityDto.getPeriodsId(), JSON.toJSONString(brandVoList), exTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("潮车集首页-获取品牌数据接口 error", "出错：", e);
            return new ArrayList<>();
        }
        CommonLogUtil.addInfo("潮车集首页-获取品牌数据接口 end", "返参：", JSON.toJSONString(brandVoList));
        return brandVoList;
    }

    /**
     * 获取潮车集活动
     *
     * @param periodsId
     * @return
     */
    private CarFashionInfoEntityResVo getCarFashionInfoEntityResVo(Integer periodsId) {
        ConsolParametersVo vo = new ConsolParametersVo("01", "01", OnlineFestivalMethodEnum.API_QUERY_ACTIVITY_INFO);
        vo.setBusinessType(ConsolParametersVo.BUSINESS_TYPE.API_QUERY_ACTIVITY_INFO);
        CarFashionActivityInfoReqVo carFashionActivityInfoReqVo = new CarFashionActivityInfoReqVo();
        carFashionActivityInfoReqVo.setActivityId(periodsId);
        vo.setInfo("param", JSONObject.toJSONString(carFashionActivityInfoReqVo));
        ConsolParametersVo consolParametersVo = onlineFestivalFacadeService.service(vo);
        CommonLogUtil.addInfo("API半价车获取潮车集", "入参：" + JSON.toJSONString(vo), "返参" + JSON.toJSONString(consolParametersVo));
        CarFashionInfoEntityResVo carFashionInfoEntityResVo = JSONObject.parseObject(consolParametersVo.getString("data"), CarFashionInfoEntityResVo.class);
        return carFashionInfoEntityResVo;
    }

    @Override
    public Integer getBrandHalfpriceFlag(@Param("periodsId") Integer periodsId, @Param("brandId") Integer brandId) {
        return halfPriceCarReadMapper.getBrandHalfpriceFlag(periodsId, brandId);
    }

    @Override
    public Map<String, FashionHalfPriceCar> getHalfPriceCarListByPeriodsIdAndDateList(Integer periodsId, List<Date> activityDateList) {
        Map<String, FashionHalfPriceCar> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(activityDateList)) {
            List<FashionHalfPriceCar> andDateList = halfPriceCarReadMapper.getHalfPriceCarListByPeriodsIdAndDateList(periodsId, activityDateList);
            if (!CollectionUtils.isEmpty(andDateList)) {
                andDateList.forEach(v->{
                    map.put(v.getPeriodsId()+"-"+DateUtils.dateToString(v.getActivityDate(),DateUtils.YYYY_MM_DD), v);
                });
            }
        }
        return  map;
    }


}
