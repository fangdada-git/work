package com.tuanche.directselling.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.LiveSceneDealerBrandService;
import com.tuanche.directselling.api.LiveSceneDealerConfigService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.LiveSceneDealerBrandDto;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneDealerBrandReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneReadMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerConfigWriteMapper;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import com.tuanche.directselling.service.impl.dubbo.DubboService;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.*;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.storage.dto.carstyle.CarStyleDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


/**
 * @ClassName: LiveSceneDealerBrandServiceImpl
 * @Description: ????????????-??????????????????????????????????????????
 * @Author: GongBo
 * @Date: 2020/3/6 18:00
 * @Version 1.0
 **/
@Service
public class LiveSceneDealerBrandServiceImpl implements LiveSceneDealerBrandService {

    @Autowired
    LiveSceneDealerBrandReadMapper liveSceneDealerBrandReadMapper;
    @Autowired
    LiveSceneDealerBrandWriteMapper liveSceneDealerBrandWriteMapper;
    @Autowired
    LiveSceneDealerConfigWriteMapper liveSceneDealerConfigWriteMapper;
    @Autowired
    LiveSceneReadMapper liveSceneReadMapper;
    @Autowired
    private DubboService dubboService;
    @Autowired
    LiveSceneDealerConfigService liveSceneDealerConfigService;
    @Autowired
    LiveSceneDealerBrandService liveSceneDealerBrandService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Reference
    CompanyBaseService companyBaseService;

    /*??????????????????*/
    ExecutorService executorService = ThreadUtil.newExecutor(2);

    /**
     * @param pageResult
     * @param sceneId
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description ????????????-???????????????????????????????????????
     * @Date 18:01 2020/3/6
     **/
    @Override
    public PageResult findSceneDealerBrandList(PageResult<LiveSceneDealerBrand> pageResult, Integer sceneId) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "id desc");
        List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.selectListSceneDealerBrandList(sceneId);
        PageInfo<LiveSceneDealerBrand> page = new PageInfo<LiveSceneDealerBrand>(liveSceneDealerBrandList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveSceneDealerBrandList);
        return pageResult;
    }

    /**
     * @param liveSceneDealerBrandVo
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerBrand>
     * @Author GongBo
     * @Description ????????????ID &  ?????????ID ?????????????????????????????????????????????
     * @Date 14:51 2020/3/9
     **/
    @Override
    public List<LiveSceneDealerBrand> getLiveSceneDealerBrandList(LiveSceneDealerBrandVo liveSceneDealerBrandVo) {
        List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.selectListSceneDealerBrandListByVo(liveSceneDealerBrandVo);
        return liveSceneDealerBrandList;
    }

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description ????????????-???????????????????????????????????????
     * @Date 14:57 2020/3/9
     **/
    @Override
    public void insertByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand) {
        liveSceneDealerBrand.setDeleteState(false);
        liveSceneDealerBrandWriteMapper.insertSelective(liveSceneDealerBrand);

    }

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description ????????????-???????????????????????????????????????
     * @Date 16:01 2020/3/9
     **/
    @Override
    public void updateByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand) {
        liveSceneDealerBrandWriteMapper.updateByPrimaryKeySelective(liveSceneDealerBrand);
    }

    /**
     * @param id
     * @return com.tuanche.directselling.model.LiveSceneDealerBrand
     * @Author GongBo
     * @Description ????????????-???????????????????????????
     * @Date 16:23 2020/3/9
     **/
    @Override
    public LiveSceneDealerBrand getLiveSceneDealerBrandById(Integer id) {
        return liveSceneDealerBrandReadMapper.selectByPrimaryKey(id);
    }

    /**
     * @program: periodsId , cityId
     * @return: java.util.List<com.tuanche.directselling.vo.LiveSceneBrandListVo>
     * @description: ?????????????????? ????????????????????????
     * @author: czy
     * @create: 2020/4/22 15:53
     **/
    @Override
    public Map<String, List<LiveSceneBrandListVo>> getBrandListByPeriodsIdAndCityId(Integer periodsId, Integer cityId) {
        try {
            Map<String, List<LiveSceneBrandListVo>> map1 = new HashMap<>();
            List<Integer> cbList = liveSceneDealerBrandReadMapper.getBrandListByPeriodsIdAndCityId(periodsId, cityId);
            CommonLogUtil.addInfo("??????getBrandListByPeriodsIdAndCityId===", "??????",
                    "periodsId=" + periodsId + " ;cityId=" + cityId + " ;cbList==" + JSON.toJSONString(cbList));
            if (cbList != null && cbList.size() > 0) {
                List<LiveSceneBrandListVo> list = new ArrayList<>();
                List<CarBrandDto> carBrandDtos = dubboService.getBrandListByCbIdList(cbList);
                if (carBrandDtos != null) {
                    for (CarBrandDto carBrandDto : carBrandDtos) {
                        LiveSceneBrandListVo liveSceneBrandListVo = new LiveSceneBrandListVo();
                        if (carBrandDto != null) {
                            liveSceneBrandListVo.setCmEnName(carBrandDto.getCmEnName());
                            liveSceneBrandListVo.setCmId(carBrandDto.getCmId());
                            liveSceneBrandListVo.setCmInitial(carBrandDto.getCmInitial());
                            liveSceneBrandListVo.setCmLogo(carBrandDto.getCmLogo());
                            liveSceneBrandListVo.setCmName(carBrandDto.getCmName());
                            if (!list.contains(liveSceneBrandListVo)) {
                                list.add(liveSceneBrandListVo);
                            }
                        }
                    }
                }
                Map<String, List<LiveSceneBrandListVo>> map = list.parallelStream()
                        .collect(Collectors.groupingBy(LiveSceneBrandListVo::getCmInitial));
                map1 = sortMapByKey(map);
            }
            return map1;
        } catch (Exception e) {
            CommonLogUtil.addError("getBrandListByPeriodsIdAndCityId", "", e);
        }
        return null;
    }

    /**
     * @param periodsId
     * @return: java.util.List<com.tuanche.directselling.vo.LiveSceneBrandVo>
     * @description:
     * @author: czy
     * @create: 2020/4/30 10:57
     **/
    @Override
    public List<LiveSceneBrandVo> getBrandListByPeriodsId(Integer periodsId) {
        List<LiveSceneBrandVo> list = new ArrayList<>();
        List<Integer> brandListByPeriodsId = liveSceneDealerBrandReadMapper.getBrandListByPeriodsId(periodsId);
        if (brandListByPeriodsId != null) {
            brandListByPeriodsId.forEach(v -> {
                if (v != null) {
                    String brandName = dubboService.getBrandName(v);
                    LiveSceneBrandVo liveSceneBrandVo = new LiveSceneBrandVo(v, brandName);
                    list.add(liveSceneBrandVo);
                }
            });
        }
        return list;
    }

    @Override
    public List<LiveSceneDealerBrand> getLiveSceneDealerBrandListByPeriodsDealerId(Integer periodsId, Integer dealerId, Integer brandId) {
        if (periodsId == null || dealerId == null || brandId == null) {
            return new ArrayList<>();
        }
        List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.getLiveSceneDealerBrandListByPeriodsIdDealerIdBrandId(periodsId, dealerId, brandId);
        return liveSceneDealerBrandList;
    }


    private Map<String, List<LiveSceneBrandListVo>> sortMapByKey(Map<String, List<LiveSceneBrandListVo>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, List<LiveSceneBrandListVo>> sortMap = new TreeMap<String, List<LiveSceneBrandListVo>>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description ????????????-?????????????????????????????????
     * @Date 2020???5???21???14:13:33
     **/
    @Override
    public void deleteByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand) {
        liveSceneDealerBrandWriteMapper.deleteByLiveSceneDealerBrand(liveSceneDealerBrand);
    }

    /**
     * @param liveSceneDealerBrandDto
     * @return void
     * @Author GongBo
     * @Description ????????????-??????????????????????????????
     * @Date 14:31 2020/5/22
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public String addDealerConfig(LiveSceneDealerBrandDto liveSceneDealerBrandDto) throws Exception {
        JSONArray jsonArray = JSON.parseArray(liveSceneDealerBrandDto.getBrandMapList());
        LiveSceneDealerConfig params = new LiveSceneDealerConfig();
        params.setSceneId(liveSceneDealerBrandDto.getSceneId());
        params.setDealerId(liveSceneDealerBrandDto.getDealerId());
        for (int i = 0; i < jsonArray.size(); i++) {

            Integer configCount = liveSceneDealerConfigService.selectByLiveSceneDealerConfigCount(params);
            if (configCount > 0) {
                return "??????????????????????????????";
            }

            // ???????????????????????????
            LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
            BeanUtils.copyProperties(liveSceneDealerBrandDto, liveSceneDealerBrand);
            liveSceneDealerBrand.setBrandId(jsonArray.getJSONObject(i).getInteger("brandId"));
            liveSceneDealerBrand.setBrandName(jsonArray.getJSONObject(i).getString("brandName"));
            liveSceneDealerBrand.setStyleIds(jsonArray.getJSONObject(i).getString("carStyleIds"));
            CsCompany csCompany = companyBaseService.getCsCompanyById(liveSceneDealerBrandDto.getDealerId());
            if (csCompany != null) {
                liveSceneDealerBrand.setCityId(csCompany.getCityId());
            }
            liveSceneDealerBrandService.insertByLiveSceneDealerBrand(liveSceneDealerBrand);

        }

        // ?????????????????????????????????
        LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(liveSceneDealerBrandDto.getSceneId());
        LiveSceneDealerConfig obj = new LiveSceneDealerConfig();
        obj.setDealerId(liveSceneDealerBrandDto.getDealerId());
        obj.setDealerName(liveSceneDealerBrandDto.getDealerName());
        obj.setEnsureSize(liveSceneDealerBrandDto.getEnsureSize());
        obj.setCreateUserId(liveSceneDealerBrandDto.getCreateUserId());
        obj.setCreateUserName(liveSceneDealerBrandDto.getCreateUserName());
        obj.setCreateDt(new Date());
        obj.setDeleteState(false);
        obj.setPeriodsId(null != liveScene.getPeriodsId() ? liveScene.getPeriodsId() : 0);
        obj.setSceneId(liveSceneDealerBrandDto.getSceneId());
        obj.setDealerName(liveSceneDealerBrandDto.getDealerName());
        obj.setContractDealerName(liveSceneDealerBrandDto.getContractDealerName());
        liveSceneDealerConfigService.insertByLiveSceneDealerConfig(obj);
        // ??????????????????????????????key
        try {
            redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param periodsId
     * @param cityId
     * @return java.util.Map<java.lang.String, java.util.List < com.tuanche.directselling.vo.LiveSceneBrandListVo>>
     * @Author GongBo
     * @Description ???????????? & ?????? & ?????????ID  ??????????????????  ????????????
     * @Date 17:25 2020/6/5
     **/
    @Override
    public Map<String, List<LiveSceneBrandVo>> getLiveSceneCarStyleList(Integer periodsId, Integer cityId, Integer masterBrandId) {
        Map<String, List<LiveSceneBrandVo>> result = new HashMap<>();
        List<LiveSceneBrandVo> curlist = new ArrayList<>();
        try {
            // ???????????????ID????????????????????????
            List<CarBrandDto> carBrandDtoList = dubboService.listByMasterBrandId(masterBrandId);
            if (null == carBrandDtoList || carBrandDtoList.size() <= 0) {
                return result;
            }
            List<Integer> brandIds = carBrandDtoList.stream().map(CarBrandDto::getCbId).collect(Collectors.toList());

            // ???????????? ?????? ??????ids  ??????????????????
            List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.getBrandList(periodsId, cityId, brandIds);
            // ????????????????????????map
            Map<Integer, String> brandCarStyleMap = new HashMap<>();
            List<Integer> cbList = new ArrayList<>();
            for (int i = 0; i < liveSceneDealerBrandList.size(); i++) {
                LiveSceneDealerBrand liveSceneDealerBrand = liveSceneDealerBrandList.get(i);
                brandCarStyleMap.put(liveSceneDealerBrand.getBrandId(), liveSceneDealerBrand.getStyleIds());
                cbList.add(liveSceneDealerBrand.getBrandId());
            }

            CommonLogUtil.addInfo("LiveSceneDealerBrandServiceImpl-getLiveSceneCarStyleList",
                    "????????????&??????&?????????ID??????????????????????????????",
                    "periodsId=" + periodsId + " ;cityId=" + cityId + " ;cbList==" + JSON.toJSONString(cbList));

            if (cbList != null && cbList.size() > 0) {
                // ????????????ids  ?????????????????????????????????
                List<CarBrandDto> carBrandDtos = dubboService.getBrandListByCbIdList(cbList);
                if (carBrandDtos != null) {
                    for (CarBrandDto carBrandDto : carBrandDtos) {
                        // ????????????????????????
                        LiveSceneBrandVo liveSceneBrandVo = new LiveSceneBrandVo();
                        liveSceneBrandVo.setId(carBrandDto.getCbId());
                        liveSceneBrandVo.setName(carBrandDto.getCbName());
                        liveSceneBrandVo.setPinyin(carBrandDto.getCbEnName());

                        // ????????????????????????
                        List<LiveSceneBrandCarStyleVo> liveSceneBrandCarStyleVoList = new ArrayList<>();
                        String carStyleIds = brandCarStyleMap.get(carBrandDto.getCbId());
                        if (StringUtils.isEmpty(carStyleIds) || "0".equals(carStyleIds)) {
                            // ?????????????????????????????????  ???????????????????????????????????????
                            List<CarStyleDto> carStyleDtoList = dubboService.getStyleListByBrandId(carBrandDto.getCbId());
                            liveSceneBrandSetCarStyle(liveSceneBrandVo, liveSceneBrandCarStyleVoList, carStyleDtoList);
                        } else {
                            // ?????????????????????????????????  ????????????????????????????????????
                            List<Integer> listIds = Arrays.asList(carStyleIds.split(",")).stream().
                                    map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                            List<CarStyleDto> carStyleDtoList = dubboService.getStyleListByIds(listIds);
                            liveSceneBrandSetCarStyle(liveSceneBrandVo, liveSceneBrandCarStyleVoList, carStyleDtoList);
                        }
                        curlist.add(liveSceneBrandVo);
                    }
                }
            }
            result.put("curlist", curlist);
        } catch (Exception e) {
            CommonLogUtil.addError("getBrandListByPeriodsIdAndCityId", "", e);
        }
        return result;
    }

    private void liveSceneBrandSetCarStyle(LiveSceneBrandVo liveSceneBrandVo, List<LiveSceneBrandCarStyleVo> liveSceneBrandCarStyleVoList, List<CarStyleDto> carStyleDtoList) {
        for (int i = 0; i < carStyleDtoList.size(); i++) {
            CarStyleDto carStyleDto = carStyleDtoList.get(i);
            LiveSceneBrandCarStyleVo liveSceneBrandCarStyleVo = new LiveSceneBrandCarStyleVo();
            liveSceneBrandCarStyleVo.setId(carStyleDto.getCsId());
            liveSceneBrandCarStyleVo.setCarLevel(carStyleDto.getCsLevel());
            liveSceneBrandCarStyleVo.setCarLevelName(carStyleDto.getCsLevelName());
            liveSceneBrandCarStyleVo.setLevel(3);
            liveSceneBrandCarStyleVo.setLogo(carStyleDto.getCsPicUrl());
            liveSceneBrandCarStyleVo.setName(carStyleDto.getCsName());
            liveSceneBrandCarStyleVo.setPinyin(carStyleDto.getCsEnName());
            liveSceneBrandCarStyleVo.setPrice(carStyleDto.getCsReferPrices());
            liveSceneBrandCarStyleVoList.add(liveSceneBrandCarStyleVo);
        }
        liveSceneBrandVo.setList(liveSceneBrandCarStyleVoList);
    }

    /**
     * @param periodsId
     * @param cityId
     * @param excludeBrandId
     * @param size
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author GongBo
     * @Description ???????????? & ?????? & ?????? & ??????????????????ID  ??????????????????????????????
     * @Date 10:23 2020/6/15
     **/
    @Override
    public List<Map<String, Object>> getRandomBrands(Integer periodsId, Integer cityId, Integer excludeBrandId, Integer size) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.getRandomBrandList(periodsId, cityId, excludeBrandId, size);
        for (int i = 0; i < liveSceneDealerBrandList.size(); i++) {
            Map<String, Object> mapObj = new HashMap<>();
            mapObj.put("brandId", liveSceneDealerBrandList.get(i).getBrandId());
            mapObj.put("brandName", liveSceneDealerBrandList.get(i).getBrandName());
            result.add(mapObj);
        }
        return result;
    }

    /**
     * @param periodsId
     * @param cityId
     * @return java.util.Map<java.lang.String, java.util.List < com.tuanche.directselling.vo.LiveSceneSecondBrandListVo>>
     * @Author GongBo
     * @Description ????????????????????????
     * @Date 15:06 2020/7/7
     **/
    @Override
    public List<LiveSceneSecondBrandListVo> getSecondBrandListByPeriodsIdAndCityId(Integer periodsId, Integer cityId) {
        List<LiveSceneSecondBrandListVo> list = new ArrayList<>();
        try {
            List<Integer> cbList = liveSceneDealerBrandReadMapper.getBrandListByPeriodsIdAndCityId(periodsId, cityId);
            CommonLogUtil.addInfo("getSecondBrandListByPeriodsIdAndCityId===", "??????",
                    "periodsId=" + periodsId + " ;cityId=" + cityId + " ;cbList==" + JSON.toJSONString(cbList));
            if (cbList != null && cbList.size() > 0) {
                List<CarBrandDto> carBrandDtos = dubboService.getBrandListByCbIdList(cbList);
                if (carBrandDtos != null) {
                    for (CarBrandDto carBrandDto : carBrandDtos) {
                        LiveSceneSecondBrandListVo liveSceneSecondBrandListVo = new LiveSceneSecondBrandListVo();
                        if (carBrandDto != null) {
                            liveSceneSecondBrandListVo.setCbId(carBrandDto.getCbId());
                            liveSceneSecondBrandListVo.setCbName(carBrandDto.getCbName());
                            liveSceneSecondBrandListVo.setCbLogo(carBrandDto.getCbLogo());
                            if (!list.contains(liveSceneSecondBrandListVo)) {
                                list.add(liveSceneSecondBrandListVo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            CommonLogUtil.addError("getSecondBrandListByPeriodsIdAndCityId", "", e);
        }
        return list;
    }

    /**
     * @param periodsId
     * @param cityId
     * @param secondBrandId
     * @return java.util.List<com.tuanche.directselling.vo.LiveSceneBrandCarStyleVo>
     * @Author GongBo
     * @Description ???????????? & ?????? & ???????????? ??????????????????
     * @Date 15:31 2020/7/7
     **/
    @Override
    public List<LiveSceneCarStyleVo> getLiveSceneCarStyleListBySecondBrandId(Integer periodsId, Integer cityId, Integer secondBrandId) {
        List<LiveSceneCarStyleVo> result = new ArrayList<>();
        try {
            List<Integer> brandIds = new ArrayList<>();
            brandIds.add(secondBrandId);
            // ???????????? ?????? ??????ids  ??????????????????
            List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerBrandReadMapper.getBrandList(periodsId, cityId, brandIds);

            CommonLogUtil.addInfo("LiveSceneDealerBrandServiceImpl-getLiveSceneCarStyleListBySecondBrandId",
                    "???????????? & ?????? & ???????????? ??????????????????",
                    "periodsId=" + periodsId + " ;cityId=" + cityId + " ;cbList==" + JSON.toJSONString(liveSceneDealerBrandList));

            if (liveSceneDealerBrandList != null && liveSceneDealerBrandList.size() > 0) {

                // ????????????ids  ?????????????????????????????????
                List<Integer> carStyleIds = new ArrayList<>();
                for (int i = 0; i < liveSceneDealerBrandList.size(); i++) {
                    LiveSceneDealerBrand liveSceneDealerBrand = liveSceneDealerBrandList.get(i);
                    if (StringUtils.isEmpty(liveSceneDealerBrand.getStyleIds()) || "0".equals(liveSceneDealerBrand.getStyleIds())) {
                        // ?????????????????????????????????  ???????????????????????????????????????
                        List<CarStyleDto> carStyleDtoList = dubboService.getStyleListByBrandId(liveSceneDealerBrand.getBrandId());
                        appendLiveSceneBrandCarStyleVoList(result, carStyleDtoList);
                    } else {
                        List<Integer> listIds = Arrays.asList(liveSceneDealerBrand.getStyleIds().split(",")).stream().
                                map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                        carStyleIds.addAll(listIds);
                    }
                }
                // ?????????????????????????????????  ????????????????????????????????????
                List<CarStyleDto> carStyleDtoList = dubboService.getStyleListByIds(carStyleIds);
                appendLiveSceneBrandCarStyleVoList(result, carStyleDtoList);

            }
        } catch (Exception e) {
            CommonLogUtil.addError("getLiveSceneCarStyleListBySecondBrandId", "???????????? & ?????? & ???????????? ????????????????????????", e);
        }
        return result;
    }

    private void appendLiveSceneBrandCarStyleVoList(List<LiveSceneCarStyleVo> result, List<CarStyleDto> carStyleDtoList) {
        for (int i = 0; i < carStyleDtoList.size(); i++) {
            CarStyleDto carStyleDto = carStyleDtoList.get(i);
            LiveSceneCarStyleVo liveSceneCarStyleVo = new LiveSceneCarStyleVo();
            liveSceneCarStyleVo.setId(carStyleDto.getCsId());
            liveSceneCarStyleVo.setName(carStyleDto.getCsName());
            result.add(liveSceneCarStyleVo);
        }
    }
}

class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
