package com.tuanche.directselling.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.broadcast.rpc.enums.PlanSourceEnum;
import com.tuanche.broadcast.rpc.service.TcBroadCastPlanService;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.broadcast.rpc.vo.TcPlanCastInfoDto;
import com.tuanche.broadcast.rpc.vo.TcPlanInfoDto;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.enums.CityBrandDataType;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneCityBrandReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneDealerBrandReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneReadMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneCityBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerConfigWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneWriteMapper;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneCityBrand;
import com.tuanche.directselling.service.PeriodsService;
import com.tuanche.directselling.service.impl.kafka.SceneNoticeServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LivePeriodsDataDetailVo;
import com.tuanche.directselling.vo.LiveProgramVo;
import com.tuanche.directselling.vo.LiveSceneBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.input.DistrictPlusInputDto;
import com.tuanche.district.api.dto.input.KeyVaueParam;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.dto.output.DistrictOutputPlusDto;
import com.tuanche.district.api.service.IDistrictApiService;
import com.tuanche.eap.api.bean.manufacturer.CsCompany;
import com.tuanche.eap.api.dto.manufacturer.DirectSellingClueDataReportDto;
import com.tuanche.eap.api.service.project.DirectSellingClueService;
import com.tuanche.manu.api.DealerService;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Author gongbo
 * @Description 团车直卖-场次活动相关服务实现
 * @Date 2020年3月4日17:46:43
 **/
@Service
public class LiveSceneServiceImpl implements LiveSceneService {

    @Autowired
    LiveSceneReadMapper liveSceneReadMapper;
    @Autowired
    LiveSceneWriteMapper liveSceneWriteMapper;
    @Autowired
    LiveSceneDealerBrandReadMapper liveSceneDealerBrandReadMapper;
    @Autowired
    private LiveSceneCityBrandWriteMapper liveSceneCityBrandWriteMapper;
    @Autowired
    SceneNoticeServiceImpl sceneNoticeService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    private PeriodsService periodsService;
    @Reference
    CityBaseService cityBaseService;
    @Reference
    TcBroadCastPlanService tcBroadCastPlanService;
    @Reference
    IDistrictApiService districtApiService;
    @Reference
    DirectSellingClueService directSellingClueService;
    @Autowired
    private LiveSceneCityBrandReadMapper liveSceneCityBrandReadMapper;
    @Autowired
    private LiveSceneService liveSceneService;
    @Reference
    DealerService dealerService;
    @Autowired
    LiveSceneDealerBrandWriteMapper liveSceneDealerBrandWriteMapper;
    @Autowired
    LiveSceneDealerConfigWriteMapper liveSceneDealerConfigWriteMapper;
    @Reference
    IDistrictApiService iDistrictApiService;
    @Reference
    CompanyBaseService companyBaseService;
    /*线程池工具类*/
    ExecutorService executorService = ThreadUtil.newExecutor(2);

    /**
     * @param pageResult
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 团车直卖-查询场次活动数据
     * @Date 14:01 2020/3/5
     **/
    @Override
    public PageResult findSceneList(PageResult<LiveSceneDto> pageResult, LiveSceneVo liveSceneVo) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "up_state desc");
        List<LiveSceneDto> liveSceneDtoList = liveSceneReadMapper.selectLiveSceneDtoList(liveSceneVo);

        // 当前系统时间戳
        long nowMillisecond = System.currentTimeMillis();

        for (int i = 0; i < liveSceneDtoList.size(); i++) {
            LiveSceneDto liveSceneDto = liveSceneDtoList.get(i);
            if (nowMillisecond < liveSceneDto.getBeginTime().getTime()) {
                // 场次活动未开始
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_0);
            } else if (nowMillisecond >= liveSceneDto.getBeginTime().getTime() &&
                    nowMillisecond <= liveSceneDto.getEndTime().getTime()) {
                // 场次活动进行中
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_1);
            } else {
                // 场次活动已结束
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_2);
            }
            try {
                String cityName = cityBaseService.getCityName(liveSceneDto.getCityId());
                liveSceneDto.setCityName(cityName);
            } catch (Exception e) {
                e.printStackTrace();
                CommonLogUtil.addError("团车直卖-查询场次活动数据", "获取城市数据异常", e.fillInStackTrace());
            }
        }
        PageInfo<LiveSceneDto> page = new PageInfo<LiveSceneDto>(liveSceneDtoList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveSceneDtoList);
        return pageResult;
    }

    @Override
    public PageResult findSceneListPage(PageResult<LiveSceneDto> pageResult, LiveSceneVo liveSceneVo) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "up_state desc");
        List<LiveSceneDto> liveSceneDtoList = liveSceneReadMapper.selectLiveSceneDtoPage(liveSceneVo);
        if (liveSceneDtoList.isEmpty()) {
            PageInfo<LiveSceneDto> page = new PageInfo<>(liveSceneDtoList);
            pageResult.setCount(page.getTotal());
            pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
            pageResult.setMsg("");
            pageResult.setData(liveSceneDtoList);
            return pageResult;
        }
        List<Integer> sceneIds = new ArrayList<>();
        for (LiveSceneDto liveSceneDto : liveSceneDtoList) {
            sceneIds.add(liveSceneDto.getId());
        }
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneCityBrandReadMapper.selectLiveSceneCityBrandBySceneIds(sceneIds);
        Map<Integer, List<Integer>> sceneCityIdMap = new HashMap<>();
        Map<Integer, List<String>> sceneCityNameMap = new HashMap<>();
        Map<Integer, List<Integer>> sceneBrandIdMap = new HashMap<>();
        Map<Integer, List<String>> sceneBrandNameMap = new HashMap<>();
        for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
            if (liveSceneCityBrand.getDataType() == CityBrandDataType.CITY.getType()) {
                List<Integer> sceneCityIdList = sceneCityIdMap.get(liveSceneCityBrand.getSceneId());
                if (sceneCityIdList == null) {
                    sceneCityIdMap.put(liveSceneCityBrand.getSceneId(), new ArrayList<>());
                    sceneCityNameMap.put(liveSceneCityBrand.getSceneId(), new ArrayList<>());
                }
                sceneCityIdMap.get(liveSceneCityBrand.getSceneId()).add(liveSceneCityBrand.getDataId());
                sceneCityNameMap.get(liveSceneCityBrand.getSceneId()).add(liveSceneCityBrand.getDataValue());
            } else {
                List<Integer> sceneBrandIdList = sceneBrandIdMap.get(liveSceneCityBrand.getSceneId());
                if (sceneBrandIdList == null) {
                    sceneBrandIdMap.put(liveSceneCityBrand.getSceneId(), new ArrayList<>());
                    sceneBrandNameMap.put(liveSceneCityBrand.getSceneId(), new ArrayList<>());
                }
                sceneBrandIdMap.get(liveSceneCityBrand.getSceneId()).add(liveSceneCityBrand.getDataId());
                sceneBrandNameMap.get(liveSceneCityBrand.getSceneId()).add(liveSceneCityBrand.getDataValue());
            }
        }
        // 当前系统时间戳
        long nowMillisecond = System.currentTimeMillis();

        for (LiveSceneDto liveSceneDto : liveSceneDtoList) {
            if (nowMillisecond < liveSceneDto.getBeginTime().getTime()) {
                // 场次活动未开始
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_0);
            } else if (nowMillisecond >= liveSceneDto.getBeginTime().getTime() &&
                    nowMillisecond <= liveSceneDto.getEndTime().getTime()) {
                // 场次活动进行中
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_1);
            } else {
                // 场次活动已结束
                liveSceneDto.setSceneState(GlobalConstants.Scene.SCENE_STATE_2);
            }
            liveSceneDto.setCityIds(sceneCityIdMap.get(liveSceneDto.getId()));
            liveSceneDto.setCityNames(sceneCityNameMap.get(liveSceneDto.getId()));
            liveSceneDto.setBrandIds(sceneBrandIdMap.get(liveSceneDto.getId()));
            liveSceneDto.setBrandNames(sceneBrandNameMap.get(liveSceneDto.getId()));
        }
        PageInfo<LiveSceneDto> page = new PageInfo<>(liveSceneDtoList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(liveSceneDtoList);
        return pageResult;
    }


    /**
     * @param liveScene
     * @return void
     * @Author GongBo
     * @Description 团车直卖-新增场次活动
     * @Date 18:18 2020/3/5
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void insertByScene(LiveScene liveScene) throws Exception {
        liveScene.setDeleteState(false);
        liveSceneWriteMapper.insertSelective(liveScene);
        // 删除场次活动城市缓存key
        redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        // 调用直播平台-创建直播计划
        syncBroadcastRpc(liveScene);
        // 调用直播平台-创建直播机位
        createHostCode(liveScene);
    }

    /**
     * @param liveScene
     * @return String
     * @Author GongBo
     * @Description 团车直卖-同步直播平台场次活动，创建直播计划
     * @Date 12:53 2020/3/6
     **/
    public String syncBroadcastRpc(LiveScene liveScene) throws Exception {
        String result = "失败";
        // 调用直播平台-创建直播计划
        BroadcastRpcVo vo = new BroadcastRpcVo();
        TcPlanInfoDto a = new TcPlanInfoDto();
        a.setPlanName(liveScene.getSceneTitle());
        a.setPlanDesc("无");
        a.setBroadcastStartTime(liveScene.getBeginTime());
        a.setSource(PlanSourceEnum.OUTSIDE_PLAN.getCode());
        a.setCreateId(liveScene.getCreateUserId());
        a.setCreateDt(liveScene.getCreateDt());
        a.setCityId(liveScene.getCityId());
        a.setCityName(cityBaseService.getCityName(liveScene.getCityId()));
        vo.setSources(BroadcastRpcConstants.SOURCES.PC);
        vo.setBusinessType(BroadcastRpcConstants.BUSINESS_TYPE.OUTSIDE_CREATE_NEW_PLAN);
        vo.setPlatform(BroadcastRpcConstants.PLATFORM.PC);
        vo.setVersion(BroadcastRpcConstants.VERSION);
        vo.setInfo("param", JSONObject.toJSONString(a));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        CommonLogUtil.addInfo("团车直卖-新增场次活动", "开始请求直播平台", vo);

        BroadcastRpcVo broadcastRpcResult = tcBroadCastPlanService.createPlan(vo);

        CommonLogUtil.addInfo("团车直卖-新增场次活动", "请求直播平台结束", broadcastRpcResult);

        if (!ObjectUtils.isEmpty(broadcastRpcResult) && "0000".equals(broadcastRpcResult.getResCode())) {
            HashMap<String, Object> resultMap = (HashMap<String, Object>) broadcastRpcResult.getMap();
            JSONObject resultJson = JSON.parseObject(resultMap.get("result").toString());
            liveScene.setPlanId(resultJson.getString("planId"));
            liveScene.setLiveAddress(resultJson.getString("pushUrl"));
            liveScene.setPreviewAddress(resultJson.getString("viewUrl"));
            liveSceneWriteMapper.updateByPrimaryKeySelective(liveScene);
            result = JSON.toJSONString(broadcastRpcResult);
        } else {
            CommonLogUtil.addInfo("团车直卖-新增场次活动", "创建计划失败", result);
            // 同步失败回滚事务
            throw new Exception("请求直播平台创建直播计划失败回滚事务");
        }
        return result;
    }

    /**
     * @param liveScene
     * @return String
     * @Author GongBo
     * @Description 团车直卖-同步直播平台场次活动，创建机位
     * @Date 12:53 2020/3/6
     **/
    private String createHostCode(LiveScene liveScene) {
        String result = "失败";
        BroadcastRpcVo vo = new BroadcastRpcVo();
        List<TcPlanCastInfoDto> list = new ArrayList<>();
        TcPlanCastInfoDto a = new TcPlanCastInfoDto();
        a.setOutSideId(liveScene.getId());
        a.setPlanId(Integer.parseInt(liveScene.getPlanId()));
        a.setBroadcastName("团车主机位");
        a.setBroadcastCode("MAIN_HOME");
        a.setCreateId(liveScene.getCreateUserId());
        a.setCreateDt(liveScene.getCreateDt());
        list.add(a);
        vo.setSources(BroadcastRpcConstants.SOURCES.PC);
        vo.setBusinessType(BroadcastRpcConstants.BUSINESS_TYPE.OUTSIDE_CREATE_CAST_CODE);
        vo.setPlatform(BroadcastRpcConstants.PLATFORM.PC);
        vo.setVersion(BroadcastRpcConstants.VERSION);
        vo.setInfo("param", JSONObject.toJSONString(list));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));

        CommonLogUtil.addInfo("团车直卖-新增场次活动-创建主机位", "开始请求直播平台", vo);

        try {
            BroadcastRpcVo broadcastRpcResult = tcBroadCastPlanService.createCastCode(vo);

            CommonLogUtil.addInfo("团车直卖-新增场次活动-创建主机位", "请求直播平台结束", broadcastRpcResult);

            if (!ObjectUtils.isEmpty(broadcastRpcResult) && "0000".equals(broadcastRpcResult.getResCode())) {
                HashMap<String, Object> resultMap = (HashMap<String, Object>) broadcastRpcResult.getMap();
                JSONObject resultJson = JSON.parseObject(resultMap.get("result").toString());
                liveScene.setHostCode(resultJson.getString(liveScene.getId().toString()));
                liveSceneWriteMapper.updateByPrimaryKeySelective(liveScene);
                result = JSON.toJSONString(broadcastRpcResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("团车直卖-新增场次活动-创建主机位", "请求直播平台创建主机位异常", e);
        }
        return result;
    }

    /**
     * @param sceneId
     * @return com.tuanche.directselling.model.LiveScene
     * @Author GongBo
     * @Description 团车直卖-根据场次活动ID获取场次活动数据
     * @Date 11:19 2020/3/6
     **/
    @Override
    public LiveScene getLiveSceneById(Integer sceneId) {
        return liveSceneReadMapper.selectByPrimaryKey(sceneId);
    }

    @Override
    public LiveSceneCityBrandDto getLiveSceneCityBrandById(Integer sceneId) {
        LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(sceneId);
        if (liveScene == null) {
            return null;
        }
        LiveSceneCityBrandDto liveSceneCityBrandDto = new LiveSceneCityBrandDto();
        BeanCopyUtil.copyProperties(liveScene, liveSceneCityBrandDto);
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneCityBrandReadMapper.selectLiveSceneCityBrandBySceneId(sceneId);
        List<LiveSceneCityDto> cities = new ArrayList<>();
        List<LiveSceneBrandDto> brands = new ArrayList<>();
        LiveSceneCityDto liveSceneCityDto = null;
        LiveSceneBrandDto liveSceneBrandDto = null;
        for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
            if (liveSceneCityBrand.getDataType() == CityBrandDataType.CITY.getType()) {
                liveSceneCityDto = new LiveSceneCityDto();
                liveSceneCityDto.setId(liveSceneCityBrand.getDataId());
                liveSceneCityDto.setName(liveSceneCityBrand.getDataValue());
                cities.add(liveSceneCityDto);
            } else {
                liveSceneBrandDto = new LiveSceneBrandDto();
                liveSceneBrandDto.setId(liveSceneCityBrand.getDataId());
                liveSceneBrandDto.setName(liveSceneCityBrand.getDataValue());
                brands.add(liveSceneBrandDto);
            }
        }
        liveSceneCityBrandDto.setCities(cities);
        liveSceneCityBrandDto.setBrands(brands);
        return liveSceneCityBrandDto;
    }

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     * @Author GongBo
     * @Description 团车直卖-获取场次活动集合
     * @Date 11:54 2020/3/6
     **/
    @Override
    public List<LiveScene> getLiveSceneList(LiveSceneVo liveSceneVo) {
        return liveSceneReadMapper.selectLiveSceneList(liveSceneVo);
    }

    @Override
    public List<LiveScene> getLiveSceneNotFinished() {
        return liveSceneReadMapper.getLiveSceneNotFinished(new Date());
    }

    /**
     * @param liveScene
     * @return void
     * @Author GongBo
     * @Description 团车直卖-更新场次活动
     * @Date 11:30 2020/3/6
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void updateByScene(LiveScene liveScene) throws Exception {
        liveSceneWriteMapper.updateByPrimaryKeySelective(liveScene);
        // 删除场次活动城市缓存key
        redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
//        executorService.submit(() -> {
//            syncLiveScene(liveScene);
//        });
    }

    /**
     * @param liveSceneVo
     * @return void
     * @Author GongBo
     * @Description 团车直卖-同步直播平台场次活动信息修改
     * @Date 12:53 2020/3/6
     **/
    private void syncLiveScene(LiveScene liveSceneVo) {
        // 获取场次活动原信息
        LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(liveSceneVo.getId());

        BroadcastRpcVo vo = new BroadcastRpcVo();
        TcPlanInfoDto a = new TcPlanInfoDto();
        a.setId(Integer.parseInt(liveScene.getPlanId()));
        a.setPlanName(liveSceneVo.getSceneTitle());
        a.setBroadcastStartTime(liveSceneVo.getBeginTime());
        a.setSource(PlanSourceEnum.OUTSIDE_PLAN.getCode());
        a.setUpdateId(liveSceneVo.getUpdateUserId());
        a.setUpdateDt(liveSceneVo.getUpdateDt());
        a.setCityId(liveSceneVo.getCityId());
        a.setCityName(cityBaseService.getCityName(liveSceneVo.getCityId()));
        vo.setSources(BroadcastRpcConstants.SOURCES.PC);
        vo.setBusinessType(BroadcastRpcConstants.BUSINESS_TYPE.OUTSIDE_MODIFY_PLAN);
        vo.setPlatform(BroadcastRpcConstants.PLATFORM.PC);
        vo.setVersion(BroadcastRpcConstants.VERSION);
        vo.setInfo("param", JSONObject.toJSONString(a));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));

        CommonLogUtil.addInfo("团车直卖-更新场次活动-更新计划信息", "开始请求直播平台", vo);

        try {
            BroadcastRpcVo broadcastRpcResult = tcBroadCastPlanService.modifyPlan(vo);

            CommonLogUtil.addInfo("团车直卖-更新场次活动-更新计划信息", "请求直播平台结束", broadcastRpcResult);

            if (!ObjectUtils.isEmpty(broadcastRpcResult) && "0000".equals(broadcastRpcResult.getResCode())) {
                CommonLogUtil.addInfo("团车直卖-更新场次活动-更新计划信息", "更新成功", broadcastRpcResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("团车直卖-更新场次活动-更新计划信息", "请求直播平台更新场次活动异常", e);
        }
    }

    /**
     * @param msg
     * @return void
     * @Author GongBo
     * @Description 团车直卖-场次活动配置导播
     * @Date 17:43 2020/3/9
     **/
    @Override
    public void setDirectorSendKafkaNotice(String msg) {
        sceneNoticeService.sendDirectorMessage(msg);
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 后门服务-用于场次活动创建同步直播平台-发生异常的数据处理创建计划
     * @Date 17:28 2020/3/12
     **/
    @Override
    public String syncSceneToBroadcast(LiveScene liveScene) {
        String result = "";
        try {
            result = syncBroadcastRpc(liveScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 后门服务-用于场次活动创建同步直播平台-发生异常的数据处理创建机位
     * @Date 17:28 2020/3/12
     **/
    @Override
    public String createSceneHostCode(LiveScene liveScene) {
        return createHostCode(liveScene);
    }

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDto>
     * @Author GongBo
     * @Description 团车直卖api-获取场次活动数据
     * @Date 17:12 2020/3/18
     **/
    @Override
    public List<LiveSceneDto> getLiveSceneDtoList(LiveSceneVo liveSceneVo) {
        if (null == liveSceneVo) {
            return null;
        }
        if (null == liveSceneVo.getUpState()) {
            // 默认获取上架场次活动
            liveSceneVo.setUpState(1);
        }
        return liveSceneReadMapper.selectLiveSceneDtoList(liveSceneVo);
    }

    @Override
    public List<LiveSceneDto> selectLiveSceneTitleList(LiveSceneVo liveSceneVo) {
        if (null == liveSceneVo) {
            return null;
        }
        if (null == liveSceneVo.getUpState()) {
            // 默认获取上架场次活动
            liveSceneVo.setUpState(1);
        }
        return liveSceneReadMapper.selectLiveSceneTitleList(liveSceneVo);
    }

    @Override
    public List<LiveSceneDto> getLiveSceneDtoAllList(LiveSceneVo liveSceneVo) {
        if (null == liveSceneVo) {
            return null;
        }
        return liveSceneReadMapper.selectLiveSceneDtoList(liveSceneVo);
    }

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerDto>
     * @Author GongBo
     * @Description 团车直卖api-获取经销商数据
     * @Date 17:12 2020/3/18
     **/
    @Override
    public List<LiveSceneDealerDto> getLiveSceneDealerList(LiveSceneVo liveSceneVo) {
        if (null == liveSceneVo || (null == liveSceneVo.getSceneIdList() && null == liveSceneVo.getCityId()
                && null == liveSceneVo.getBrandIdList()) && null == liveSceneVo.getSceneId() && null == liveSceneVo.getSceneState()
                && null == liveSceneVo.getPeriodsId()) {
            CommonLogUtil.addInfo("团车直卖api-获取经销商数据", "参数为空",null);
            return null;
        }
        if (liveSceneVo.getSceneId() != null) {
            if (liveSceneVo.getSceneIdList() == null) {
                List<Integer> ids = new ArrayList<>();
                ids.add(liveSceneVo.getSceneId());
                liveSceneVo.setSceneIdList(ids);
            } else {
                liveSceneVo.getSceneIdList().add(liveSceneVo.getSceneId());
            }
        }
        if (liveSceneVo.getCityId() != null) {
            if (liveSceneVo.getCityIdList() == null) {
                List<Integer> ids = new ArrayList<>();
                ids.add(liveSceneVo.getCityId());
                liveSceneVo.setCityIdList(ids);
            } else {
                liveSceneVo.getCityIdList().add(liveSceneVo.getCityId());
            }
        }
        List<LiveSceneDealerDto> dealers = liveSceneDealerBrandReadMapper.findDealerBySceneVo(liveSceneVo);
        //如果是根据大场次ID获取经销商列表，则返回经销商地址(直播间后台使用) add by zhangys 20201214
        if(liveSceneVo.getPeriodsId()!=null){
            for (LiveSceneDealerDto dealer : dealers) {
                CsCompanyDetailDto csCompany = companyBaseService.getCsCompanyDetail(dealer.getDealerId());
                if (csCompany != null) {
                    dealer.setAddress(csCompany.getAddress());
                }
            }
        }
        return dealers;
    }

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneCityDto>
     * @Author GongBo
     * @Description 团车直卖api-获取场次活动关联所有城市
     * @Date 2020年4月2日11:51:00
     **/
    @Override
    public List<LiveSceneCityDto> getLiveSceneCityDtoList(LiveSceneVo liveSceneVo) {
        String redisKey = BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey();
        String hash_key = "";
        if (liveSceneVo == null) {
            hash_key = "all";
        } else if (null != liveSceneVo.getCityIds() && null != liveSceneVo.getBrandId()) {
            hash_key = "cityIds_" + liveSceneVo.getCityIds() + "_brandId_" + liveSceneVo.getBrandId();
        } else if (null != liveSceneVo.getCityIds()) {
            hash_key = "cityIds_" + liveSceneVo.getCityIds();
        } else if (null != liveSceneVo.getPeriodsId() && null != liveSceneVo.getBrandId()) {
            hash_key = "periodId_" + liveSceneVo.getPeriodsId() + "_brandId_" + liveSceneVo.getBrandId();
        } else if (null != liveSceneVo.getCityId() && null != liveSceneVo.getBrandId()) {
            hash_key = "cityId_" + liveSceneVo.getCityIds() + "_brandId_" + liveSceneVo.getBrandId();
        } else if (null != liveSceneVo.getPeriodsId()) {
            hash_key = "periodId_" + liveSceneVo.getPeriodsId();
        } else if (null != liveSceneVo.getBrandId()) {
            hash_key = "brandId_" + liveSceneVo.getBrandId();
        } else if (null != liveSceneVo.getCityId()) {
            hash_key = "city_" + liveSceneVo.getCityId().toString();
        }
        List<LiveSceneCityDto> list = new ArrayList<>();
        try {
            if (redisService.hexists(redisKey, hash_key)) {
                // 获取缓存中的场次活动城市
                String result = redisService.hget(redisKey, hash_key, String.class);
                list = JSON.parseArray(result, LiveSceneCityDto.class);
            } else {
                // 缓存不存在 读库
                list = liveSceneReadMapper.getLiveSceneCityDtoList(liveSceneVo);
                // 获取所有城市
                DistrictPlusInputDto cityParams = new DistrictPlusInputDto();
                List<DistrictOutputPlusDto> districtOutputPlusDtoList = districtApiService.getPlusDistrictList(cityParams);
                Map<Integer, DistrictOutputPlusDto> cityMap = new HashMap<>();
                if (districtOutputPlusDtoList.size() > 0) {
                    cityMap = districtOutputPlusDtoList.stream().collect(Collectors.toMap(DistrictOutputPlusDto::getId, DistrictOutputPlusDto -> DistrictOutputPlusDto));
                }

                for (int i = 0; i < list.size(); i++) {
                    LiveSceneCityDto liveSceneCityDto = list.get(i);
                    // 获取城市相关信息
                    DistrictOutputPlusDto districtOutputPlusDto = cityMap.get(liveSceneCityDto.getId());
                    if (districtOutputPlusDto != null) {
                        liveSceneCityDto.setName(districtOutputPlusDto.getExtendName());
                        liveSceneCityDto.setFirst(districtOutputPlusDto.getFirst());
                        liveSceneCityDto.setPy(districtOutputPlusDto.getDomain());
                    }
                }
                // 清除城市map
                cityMap.clear();
                if (!CollectionUtils.isEmpty(list)) {
                    redisService.hset(redisKey, hash_key, JSON.toJSONString(list), BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getExpire());
                }
            }
        } catch (Exception e) {
            CommonLogUtil.addError("团车直卖api-获取场次活动关联所有城市-异常", JSON.toJSONString(liveSceneVo), e);
        }

        return list;
    }

    @Override
    public Map<String, Object> getLiveInfoByPeriodsId(Integer periodsId) {
        Map<String, Object> map = new HashMap<>();
        if (periodsId != null && periodsId > 0) {
            Map<String, Object> liveSceneDate = liveSceneReadMapper.getLiveInfoByPeriodsId(periodsId);
            if (!CollectionUtils.isEmpty(liveSceneDate)) {
                for (Map.Entry<String, Object> entry : liveSceneDate.entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        map.put(entry.getKey(), ((Date) entry.getValue()).getTime());
                    }
                }
            }
        }
        return map;
    }

    /**
     * @param
     * @description: 取最近一场场次id
     * @return: com.tuanche.directselling.model.LiveScene
     * @author: dudg
     * @date: 2020/5/6 14:06
     */
    @Override
    public LiveScene getLastLiveScenePeriodsId() {
        return liveSceneReadMapper.getLastLiveScenePeriodsId();
    }


    /**
     * @param pageResult
     * @param periodDealerDto OpenPage 默认不分页
     * @description: 获取场次活动经销商列表
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/6/23 17:54
     */
    @Override
    public PageResult<LivePeriodDealerDto> getPeriodDealerList(PageResult<LivePeriodDealerDto> pageResult, LivePeriodDealerDto periodDealerDto) {
        if (periodDealerDto.getOpenPage()) {
            PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "");
        }

        List<LivePeriodDealerDto> periodSceneDealerList = liveSceneReadMapper.getPeriodSceneDealerList(periodDealerDto);
        PageInfo<LivePeriodDealerDto> page = new PageInfo<>(periodSceneDealerList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(periodSceneDealerList);
        return pageResult;
    }
    /**
     * 获取裂变经销商列表
     * @author HuangHao
     * @CreatTime 2021-03-12 15:52
     * @param pageResult
     * @param periodDealerDto
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.LivePeriodDealerDto>
     */
    @Override
    public PageResult<LivePeriodDealerDto> getFissionDealerList(PageResult<LivePeriodDealerDto> pageResult, LivePeriodDealerDto periodDealerDto) {
        if (periodDealerDto.getOpenPage()) {
            PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "");
        }

        List<LivePeriodDealerDto> periodSceneDealerList = liveSceneReadMapper.getFissionDealerList(periodDealerDto);
        PageInfo<LivePeriodDealerDto> page = new PageInfo<>(periodSceneDealerList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(periodSceneDealerList);
        return pageResult;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public ResultDto addScene(Integer createUserId, String createUserName, LiveSceneVo liveSceneVo) throws Exception {
        LiveScene liveScene = new LiveScene();
        liveScene.setCreateUserId(createUserId);
        liveScene.setCreateUserName(createUserName);
        liveScene.setCreateDt(new Date());
        liveScene.setBeginTime(DateUtils.stringToDate(liveSceneVo.getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setEndTime(DateUtils.stringToDate(liveSceneVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setReadyBeginTime(DateUtils.stringToDate(liveSceneVo.getReadyBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setReadyEndTime(DateUtils.stringToDate(liveSceneVo.getReadyEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setFormalBeginTime(DateUtils.stringToDate(liveSceneVo.getFormalBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setFormalEndTime(DateUtils.stringToDate(liveSceneVo.getFormalEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setDirectorName("默认");
        liveScene.setUpState(1);
        liveScene.setPeriodsId(liveSceneVo.getPeriodsId());
        liveScene.setSendFlag(liveSceneVo.getSendFlag());

        // 获取场次信息
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setId(liveSceneVo.getPeriodsId());
        ActivityConfigResponseVo activityConfig = periodsService.queryById(activityConfigRequestVo);
        ResultDto resultDto = new ResultDto();
        // 判断场次时间
        if (liveScene.getBeginTime().before(activityConfig.getBeginTime())) {
            resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
            resultDto.setMsg("活动开始时间不能早于场次开始时间，请修改活动开始时间");
            return resultDto;
        }
        if (liveScene.getEndTime().after(activityConfig.getEndTime())) {
            resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
            resultDto.setMsg("活动开始时间不能早于场次开始时间，请修改活动开始时间");
            return resultDto;
        }


        // 根据选择城市  批量添加场次活动
        String[] cityIds = liveSceneVo.getCityIds().split(",");
        String[] cityNames = liveSceneVo.getCityNames();
        String[] brandIds = liveSceneVo.getBrandIds();
        String[] brandNames = liveSceneVo.getBrandNames();
        LiveSceneCityBrand liveSceneCityBrand = null;
        liveScene.setSceneTitle(liveSceneVo.getSceneTitle());
        liveScene.setDeleteState(false);
        liveSceneWriteMapper.insertSelective(liveScene);
        for (int i = 0; i < cityIds.length; i++) {
            liveSceneCityBrand = new LiveSceneCityBrand();
            liveSceneCityBrand.setSceneId(liveScene.getId());
            liveSceneCityBrand.setDataId(Integer.parseInt(cityIds[i]));
            liveSceneCityBrand.setDataValue(cityNames[i]);
            liveSceneCityBrand.setDataType(CityBrandDataType.CITY.getType());
            liveSceneCityBrandWriteMapper.insertSelective(liveSceneCityBrand);
        }
        for (int i = 0; i < brandIds.length; i++) {
            liveSceneCityBrand = new LiveSceneCityBrand();
            liveSceneCityBrand.setSceneId(liveScene.getId());
            liveSceneCityBrand.setDataId(Integer.parseInt(brandIds[i]));
            liveSceneCityBrand.setDataValue(brandNames[i]);
            liveSceneCityBrand.setDataType(CityBrandDataType.BRAND.getType());
            liveSceneCityBrandWriteMapper.insertSelective(liveSceneCityBrand);
        }
        redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        resultDto.setMsg(StatusCodeEnum.SUCCESS.getText());
        return resultDto;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public ResultDto updateScene(Integer createUserId, String createUserName, LiveSceneVo liveSceneVo) throws Exception {
        liveSceneVo.setExcludeId(liveSceneVo.getSceneId());
//            List<LiveScene> liveSceneList = liveSceneService.getLiveSceneList(liveSceneVo);
//            if(liveSceneList.size() > 0){
//                return dynamicResult("存在时间重叠的场次活动，请修改时间");
//            }
        LiveProgramVo liveProgramVo = new LiveProgramVo();
        liveProgramVo.setSceneId(liveSceneVo.getSceneId());
        liveProgramVo.setSceneBeginTime(DateUtils.stringToDate(liveSceneVo.getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveProgramVo.setSceneEndTime(DateUtils.stringToDate(liveSceneVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
//            List<LiveProgramDto> liveProgramList = liveProgramService.queryList(liveProgramVo);
//            if(liveProgramList.size() > 0){
//                return dynamicResult("时间范围不满足节目时间，请修改时间");
//            }

        // 保存场次活动
        LiveScene liveScene = new LiveScene();
        liveScene.setUpdateUserId(createUserId);
        liveScene.setUpdateUserName(createUserName);
        liveScene.setId(liveSceneVo.getExcludeId());
        liveScene.setUpdateDt(new Date());
        liveScene.setCityId(liveSceneVo.getCityId());
        liveScene.setBeginTime(DateUtils.stringToDate(liveSceneVo.getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setEndTime(DateUtils.stringToDate(liveSceneVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setReadyBeginTime(DateUtils.stringToDate(liveSceneVo.getReadyBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setReadyEndTime(DateUtils.stringToDate(liveSceneVo.getReadyEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setFormalBeginTime(DateUtils.stringToDate(liveSceneVo.getFormalBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setFormalEndTime(DateUtils.stringToDate(liveSceneVo.getFormalEndTime(), "yyyy-MM-dd HH:mm:ss"));
        liveScene.setSceneTitle(liveSceneVo.getSceneTitle());
        liveScene.setUpState(1);
        liveScene.setPeriodsId(liveSceneVo.getPeriodsId());
        liveScene.setSendFlag(liveSceneVo.getSendFlag());

        // 获取场次信息
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setId(liveSceneVo.getPeriodsId());
        ActivityConfigResponseVo activityConfig = periodsService.queryById(activityConfigRequestVo);
        ResultDto resultDto = new ResultDto();
        // 判断场次时间
        if (liveScene.getBeginTime().before(activityConfig.getBeginTime())) {
            resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
            resultDto.setMsg("活动开始时间不能早于场次开始时间，请修改活动开始时间");
            return resultDto;
        }
        if (liveScene.getEndTime().after(activityConfig.getEndTime())) {
            resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
            resultDto.setMsg("活动结束时间不能晚于场次结束时间，请修改活动结束时间");
            return resultDto;
        }
        List<LiveSceneCityBrand> liveSceneCityBrands = liveSceneCityBrandReadMapper.selectLiveSceneCityBrandBySceneId(liveSceneVo.getSceneId());
        List<Integer> cityIdsDB = new ArrayList<>();
        List<Integer> brandIdsDB = new ArrayList<>();
        for (LiveSceneCityBrand liveSceneCityBrand : liveSceneCityBrands) {
            if (liveSceneCityBrand.getDataType() == CityBrandDataType.CITY.getType()) {
                cityIdsDB.add(liveSceneCityBrand.getDataId());
            } else {
                brandIdsDB.add(liveSceneCityBrand.getDataId());
            }
        }
        String[] cityIdsUpdateArray = liveSceneVo.getCityIds().split(",");
        String[] brandIdsUpdateArray = liveSceneVo.getBrandIds();
        String[] cityNamesArray = liveSceneVo.getCityNames();
        String[] brandNamesArray = liveSceneVo.getBrandNames();
        List<Integer> cityIdList = new ArrayList<>();
        List<Integer> brandIdList = new ArrayList<>();
        for (String id : cityIdsUpdateArray) {
            cityIdList.add(Integer.parseInt(id));
        }
        for (String id : brandIdsUpdateArray) {
            brandIdList.add(Integer.parseInt(id));
        }
        //如果城市为4级则取上一级城市
        if (!cityIdList.isEmpty()) {
            DistrictBaseInputDto distDto = new DistrictBaseInputDto();
            distDto.setIds(cityIdList);
            List<DistrictOutputBaseDto> districtOutputBaseDtos = iDistrictApiService.getBaseDistrictList(distDto);
            for (DistrictOutputBaseDto dist : districtOutputBaseDtos) {
                if (ManuBaseConstants.CityLevel.FOUR.equals(dist.getLevel())) {
                    cityIdList.add(dist.getPid());
                }
            }
        }
        //查符合条件的供应商
        List<CsCompany> companyList = dealerService.selectCompanyByNameCityBrand(null, brandIdList, cityIdList);
        List<Integer> dealerIds = new ArrayList<>();
        for (CsCompany csCompany : companyList) {
            dealerIds.add(csCompany.getId());
        }
        //该场次下只保留符合条件的供应商
        Date now = new Date();
        liveSceneDealerBrandWriteMapper.deleteByNotInDealerIds(liveSceneVo.getSceneId(), dealerIds, createUserId, createUserName, now);
        liveSceneDealerBrandWriteMapper.deleteByNotInBrandIds(liveSceneVo.getSceneId(), brandIdList, createUserId, createUserName, now);
        liveSceneDealerBrandWriteMapper.deleteByNotInCityIds(liveSceneVo.getSceneId(), cityIdList, createUserId, createUserName, now);
        liveSceneDealerConfigWriteMapper.deleteByNotInDealerIds(liveSceneVo.getSceneId(), dealerIds, createUserId, createUserName, now);
        liveSceneService.updateCityBrand(cityIdsUpdateArray, cityNamesArray, cityIdsDB, liveScene.getId(), CityBrandDataType.CITY.getType());
        liveSceneService.updateCityBrand(brandIdsUpdateArray, brandNamesArray, brandIdsDB, liveScene.getId(), CityBrandDataType.BRAND.getType());
        liveSceneWriteMapper.updateByPrimaryKeySelective(liveScene);
        redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        resultDto.setMsg(StatusCodeEnum.SUCCESS.getText());
        return resultDto;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void updateCityBrand(String[] ids, String[] names, List<Integer> idsDB, Integer sceneId, Integer dataType) {
        List<Integer> idsUpdate = new ArrayList<>();
        for (String id : ids) {
            idsUpdate.add(Integer.parseInt(id));
        }
        idsDB.retainAll(idsUpdate);
        liveSceneCityBrandWriteMapper.deleteNotInDataIdAndType(sceneId, idsDB, dataType);
        idsUpdate.removeAll(idsDB);
        LiveSceneCityBrand liveSceneCityBrand = null;
        for (Integer id : idsUpdate) {
            liveSceneCityBrand = new LiveSceneCityBrand();
            liveSceneCityBrand.setSceneId(sceneId);
            liveSceneCityBrand.setDataId(id);
            int index = ArrayUtils.indexOf(ids, String.valueOf(id));
            if (index != -1) {
                liveSceneCityBrand.setDataValue(names[index]);
            }
            liveSceneCityBrand.setDataType(dataType);
            liveSceneCityBrandWriteMapper.insertSelective(liveSceneCityBrand);
        }
    }

    /** 
    * @Description: 获取场次活动的城市、品牌
    * @param sceneId
    * @return: java.util.List<com.tuanche.directselling.model.LiveSceneCityBrand>
    * @Author: zhangys
    * @Date: 2021/1/11 16:21
    */
    @Override
    public List<LiveSceneCityBrand> selectLiveSceneCityBrandBySceneId(Integer sceneId) {
        if(sceneId==null || sceneId<=0){
            return null;
        }
        return liveSceneCityBrandReadMapper.selectLiveSceneCityBrandBySceneId(sceneId);
    }

    /**
     * 统计场次下线索情况
     *
     * @return
     */
    @Override
    public PageResult getSceneDataReportPeriodsId(PageResult<LiveSceneDataReportDto> pageResult, LiveSceneVo liveSceneVo) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<LiveSceneDataReportDto> dealerDataReportList = new ArrayList<>();
        if (liveSceneVo == null || liveSceneVo.getPeriodsId() == null) {
            pageResult.setData(dealerDataReportList);
            return pageResult;
        }
        dealerDataReportList = liveSceneReadMapper.getSceneDataReportByPeriodsId(liveSceneVo.getPeriodsId(), liveSceneVo.getCityId(), liveSceneVo.getBrandId());
        if (CollectionUtils.isEmpty(dealerDataReportList)) {
            pageResult.setData(dealerDataReportList);
            return pageResult;
        }
        List<Integer> dealerIds = dealerDataReportList.stream().map(LiveSceneDataReportDto::getDealerId).collect(Collectors.toList());
        List<DirectSellingClueDataReportDto> dealerSellingClueDataReport = directSellingClueService.getDirectSellingClueDataReport(liveSceneVo.getPeriodsId(), dealerIds);
        for (LiveSceneDataReportDto dealerDataReportDto : dealerDataReportList) {
            String cityName = cityBaseService.getCityName(dealerDataReportDto.getCityId());
            dealerDataReportDto.setPeriodsId(liveSceneVo.getPeriodsId());
            dealerDataReportDto.setCityName(cityName);
            dealerDataReportDto.setReachRatio(BigDecimal.ZERO);
            for (DirectSellingClueDataReportDto reportDto : dealerSellingClueDataReport) {
                if (dealerDataReportDto.getDealerId().equals(reportDto.getDealerId())) {
                    BeanUtils.copyProperties(reportDto, dealerDataReportDto);
                    if (reportDto.getAllClueSize() != 0 && dealerDataReportDto.getEnsureSize() != 0) {
                        BigDecimal ratio = new BigDecimal(dealerDataReportDto.getAllClueSize()).divide(new BigDecimal(dealerDataReportDto.getEnsureSize()), 4,
                                BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN));
                        dealerDataReportDto.setReachRatio(ratio);
                    }
                }
            }
        }

        PageInfo<LiveSceneDataReportDto> page = new PageInfo<>(dealerDataReportList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(dealerDataReportList);
        return pageResult;
    }

    /**
     * 场次下城市
     *
     * @param periodsId
     * @return
     */
    @Override
    public List<LiveSceneCityDto> getPeriodsCityList(Integer periodsId) {
        List<LiveSceneCityDto> cityList = liveSceneReadMapper.getPeriodsCityListByPeriodsId(periodsId);
        cityList.forEach(city -> {
            DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
            KeyVaueParam keyVaueParam = new KeyVaueParam();
            keyVaueParam.setKey("first");
            keyVaueParam.setValue("asc");
            districtBaseInputDto.setIds(Lists.newArrayList(city.getId()));
            districtBaseInputDto.setOrders(Lists.newArrayList(keyVaueParam));
            List<DistrictOutputBaseDto> baseDistrictList = districtApiService.getBaseDistrictList(districtBaseInputDto);
            city.setName(CollectionUtils.isEmpty(baseDistrictList) ? "" : baseDistrictList.get(0).getName());
        });
        return cityList;
    }

    /**
     * 场次下品牌
     *
     * @param periodsId
     * @return
     */
    @Override
    public List<LiveSceneBrandVo> getPeriodsBrandList(Integer periodsId) {
        List<LiveSceneBrandVo> brandList = liveSceneReadMapper.getPeriodsBrandListByPeriodsId(periodsId);
        return brandList;
    }

    /**
     * 场次下经销商七日线索统计
     *
     * @param periodsId
     * @param dealerId
     * @return
     */
    @Override
    public List<LivePeriodsDataDetailVo> getPeriodsDealerDataDetail(Integer periodsId, Integer dealerId) {
        List<LivePeriodsDataDetailVo> dealerDataDetail = new ArrayList<>();
        List<Integer> oneDayData;
        LivePeriodsDataDetailVo dataDetailVoAll = new LivePeriodsDataDetailVo();
        dataDetailVoAll.setDayDesc("总计");
        dealerDataDetail.add(dataDetailVoAll);
        LivePeriodsDataDetailVo dataDetailVo;
        for (int i = 0; i < 7; i++) {
            oneDayData = directSellingClueService.getPeriodsDealerDataDetail(periodsId, dealerId, LocalDate.now().plusDays(-i).toString());
            dataDetailVo = new LivePeriodsDataDetailVo();
            dataDetailVo.setDayDesc(LocalDate.now().plusDays(-i).toString());
            dataDetailVo.setAddClueCount(oneDayData.get(0));
            dataDetailVo.setDisClueCount(oneDayData.get(1));
            dataDetailVo.setOrderClueCount(oneDayData.get(2));
            dealerDataDetail.add(dataDetailVo);
            dataDetailVoAll.setAddClueCount(dataDetailVoAll.getAddClueCount() + oneDayData.get(0));
            dataDetailVoAll.setDisClueCount(dataDetailVoAll.getDisClueCount() + oneDayData.get(1));
            dataDetailVoAll.setOrderClueCount(dataDetailVoAll.getOrderClueCount() + oneDayData.get(2));
        }
        return dealerDataDetail;
    }


    /**
     * @Description: 返回小场次列表（仅考虑场次本身数据，不按城市区分）
     * @param liveSceneVo
     * @return: java.util.List<com.tuanche.directselling.model.LiveScene>
     * @Author: zhangys
     * @Date: 2021/1/11 14:52
     */
    @Override
    public List<LiveScene> getLiveSceneListByLiveScene(LiveSceneVo liveSceneVo) {
        if(liveSceneVo==null ||
                liveSceneVo.getPeriodsId() == null &&
                liveSceneVo.getSceneId() == null &&
                liveSceneVo.getCityId() == null &&
                liveSceneVo.getBrandId() == null
                ){
            return null;
        }
        return liveSceneReadMapper.getLiveSceneListByLiveScene(liveSceneVo);
    }

    /**
     * 获取经销商参加的小场次列表
     * @author HuangHao
     * @CreatTime 2021-04-20 17:52
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     */
    @Override
    public List<LiveScene> getDealerSceneList(Integer dealerId){
        if(dealerId == null){
            return null;
        }
        return liveSceneReadMapper.getDealerSceneList(dealerId);
    }
}
