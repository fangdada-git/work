package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.broadcast.rpc.service.TcBroadCastPlanService;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.broadcast.rpc.vo.TcPlanCastInfoDto;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveSceneDealerConfigService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.LiveSceneDealerConfigDto;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneDealerConfigReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveSceneReadMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerConfigWriteMapper;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import com.tuanche.directselling.service.CommonService;
import com.tuanche.directselling.service.PeriodsService;
import com.tuanche.directselling.service.impl.kafka.SceneNoticeServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveSceneDealerConfigVo;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: LiveSceneDealerConfigServiceImpl
 * @Description: 团车直卖-场次活动经销商配置service Impl
 * @Author: GongBo
 * @Date: 2020/4/28 10:18
 * @Version 1.0
 **/
@Service
public class LiveSceneDealerConfigServiceImpl implements LiveSceneDealerConfigService {

    @Autowired
    LiveSceneDealerConfigReadMapper liveSceneDealerConfigReadMapper;
    @Autowired
    LiveSceneDealerConfigWriteMapper liveSceneDealerConfigWriteMapper;
    @Autowired
    PeriodsService periodsService;
    @Autowired
    LiveSceneDealerBrandWriteMapper liveSceneDealerBrandWriteMapper;
    @Autowired
    LiveSceneReadMapper liveSceneReadMapper;
    @Autowired
    SceneNoticeServiceImpl sceneNoticeService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    @Reference
    CarBaseService carBaseService;
    @Reference
    TcBroadCastPlanService tcBroadCastPlanService;

    @Autowired
    CommonService commonService;
    @Reference
    CompanyBaseService companyBaseService;
    /**
     * @param id
     * @return com.tuanche.directselling.model.LiveSceneDealerConfig
     * @Author GongBo
     * @Description 团车直卖-获取场次活动经销商配置信息
     * @Date 10:15 2020/4/28
     **/
    @Override
    public LiveSceneDealerConfig getLiveSceneDealerConfigById(Integer id) {
        return liveSceneDealerConfigReadMapper.selectByPrimaryKey(id);
    }

    /**
     * @param liveSceneDealerConfig
     * @return com.tuanche.directselling.model.LiveSceneDealerConfig
     * @Author GongBo
     * @Description 团车直卖-获取场次活动经销商配置信息
     * @Date 10:15 2020/4/28
     **/
    @Override
    public LiveSceneDealerConfig getLiveSceneDealerConfigByVo(LiveSceneDealerConfig liveSceneDealerConfig) {
        return liveSceneDealerConfigReadMapper.selectByLiveSceneDealerConfig(liveSceneDealerConfig);
    }

    @Override
    public int selectByLiveSceneDealerConfigCount(LiveSceneDealerConfig liveSceneDealerConfig) {
        return liveSceneDealerConfigReadMapper.selectByLiveSceneDealerConfigCount(liveSceneDealerConfig);
    }

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-保添加场次活动经销商配置
     * @Date 10:16 2020/4/28
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void insertByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) throws Exception {
        liveSceneDealerConfigWriteMapper.insertSelective(liveSceneDealerConfig);

        // 调用直播平台-创建经销商分机码
//        createExtensionCodeCode(liveSceneDealerConfig);
    }

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-修改场次活动经销商配置
     * @Date 10:16 2020/4/28
     **/
    @Override
    public void updateByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) {
        liveSceneDealerConfigWriteMapper.updateByPrimaryKeySelective(liveSceneDealerConfig);
        // 删除场次活动城市缓存key
        try {
            redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-删除场次活动经销商配置
     * @Date 2020年5月21日12:34:09
     **/
    @Override
    public void deleteByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) {
        //删除 场次活动经销商配置
        LiveSceneDealerConfig oldConfigObj = liveSceneDealerConfigReadMapper.selectByPrimaryKey(liveSceneDealerConfig.getId());
        liveSceneDealerConfigWriteMapper.deleteByLiveSceneDealerConfig(liveSceneDealerConfig);

        //删除 场次活动经销商品牌
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setUpdateUserId(liveSceneDealerConfig.getUpdateUserId());
        liveSceneDealerBrand.setUpdateUserName(liveSceneDealerConfig.getUpdateUserName());
        liveSceneDealerBrand.setUpdateDt(liveSceneDealerConfig.getUpdateDt());
        liveSceneDealerBrand.setDealerId(oldConfigObj.getDealerId());
        liveSceneDealerBrand.setSceneId(oldConfigObj.getSceneId());
        liveSceneDealerBrandWriteMapper.deleteByLiveSceneDealerBrand(liveSceneDealerBrand);

        // 删除场次活动城市缓存key
        try {
            redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        } catch (RedisException e) {
            e.printStackTrace();
        }

        // 发送删除场次活动经销商机位kafka通知
        Map<String, Object> params = new HashMap<>();
        params.put("type", GlobalConstants.Common.KAFKA_LIVE_NOTICE_TYPE_1);
        List<String> extensionCodeList = new ArrayList<>();
        extensionCodeList.add(liveSceneDealerConfig.getExtensionCode());
        params.put("authCodes", extensionCodeList);
        sceneNoticeService.sendDealerDelMessage(JSON.toJSONString(params));
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动经销商数据查询
     * @Date 2020年3月6日17:59:49
     **/
    @Override
    public PageResult findSceneDealerConfigList(PageResult<LiveSceneDealerConfigDto> pageResult, Integer sceneId) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "id desc");
        List<LiveSceneDealerConfigDto> liveSceneDealerConfigDtoList = liveSceneDealerConfigReadMapper.selectSceneDealerConfigList(sceneId);
        for (int i = 0; i < liveSceneDealerConfigDtoList.size(); i++) {
            LiveSceneDealerConfigDto liveSceneDealerConfigDto = liveSceneDealerConfigDtoList.get(i);
            // 获取活动配置品牌
            List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerConfigDto.getDealerBrandList();
            StringBuilder brandNames = new StringBuilder();
            liveSceneDealerBrandList.forEach(LiveSceneDealerBrand -> {
//                String masterBrandName = carBaseService.getBrandById(LiveSceneDealerBrand.getBrandId()).getCmName();
//                brandNames.append(LiveSceneDealerBrand.getBrandName() + "(" + masterBrandName + ")，");
                brandNames.append(LiveSceneDealerBrand.getBrandName() + "，");
            });
            if (!StringUtils.isEmpty(brandNames.toString())) {
                brandNames.deleteCharAt(brandNames.length() - 1);
                liveSceneDealerConfigDto.setBrandNames(brandNames.toString());
            }
        }
        PageInfo<LiveSceneDealerConfigDto> page = new PageInfo<LiveSceneDealerConfigDto>(liveSceneDealerConfigDtoList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveSceneDealerConfigDtoList);
        return pageResult;
    }

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-同步直播平台场次活动，创建经销商分机位
     * @Date 12:53 2020/3/6
     **/
    private void createExtensionCodeCode(LiveSceneDealerConfig liveSceneDealerConfig) throws Exception {
        LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(liveSceneDealerConfig.getSceneId());
        BroadcastRpcVo vo = new BroadcastRpcVo();
        List<TcPlanCastInfoDto> list = new ArrayList<>();
        TcPlanCastInfoDto a = new TcPlanCastInfoDto();
        a.setOutSideId(liveSceneDealerConfig.getId());
        a.setPlanId(Integer.parseInt(liveScene.getPlanId()));
        a.setBroadcastName(liveSceneDealerConfig.getDealerName());
        a.setBroadcastCode("BRANCH_HOME");
        a.setCreateId(liveSceneDealerConfig.getCreateUserId());
        a.setCreateDt(liveSceneDealerConfig.getCreateDt());
        list.add(a);
        vo.setSources(BroadcastRpcConstants.SOURCES.PC);
        vo.setBusinessType(BroadcastRpcConstants.BUSINESS_TYPE.OUTSIDE_CREATE_CAST_CODE);
        vo.setPlatform(BroadcastRpcConstants.PLATFORM.PC);
        vo.setVersion(BroadcastRpcConstants.VERSION);
        vo.setInfo("param", JSONObject.toJSONString(list));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));

        CommonLogUtil.addInfo("团车直卖-新增场次活动-创建经销商分机位", "开始请求直播平台", vo);
        BroadcastRpcVo broadcastRpcResult = tcBroadCastPlanService.createCastCode(vo);
        CommonLogUtil.addInfo("团车直卖-新增场次活动-创建经销商分机位", "请求直播平台结束", broadcastRpcResult);

        if (!ObjectUtils.isEmpty(broadcastRpcResult) && "0000".equals(broadcastRpcResult.getResCode())) {
            HashMap<String, Object> resultMap = (HashMap<String, Object>) broadcastRpcResult.getMap();
            JSONObject resultJson = JSON.parseObject(resultMap.get("result").toString());
            liveSceneDealerConfig.setExtensionCode(resultJson.getString(liveSceneDealerConfig.getId().toString()));
            liveSceneDealerConfigWriteMapper.updateByPrimaryKeySelective(liveSceneDealerConfig);
        } else {
            CommonLogUtil.addInfo("团车直卖-新增场次活动-创建经销商分机位", "请求直播平台创建经销商分机位异常", vo);
            // 同步失败回滚事务
            throw new Exception("请求直播平台创建直播计划失败回滚事务");
        }
    }

    /**
     * 获取场次活动经销商配置列表
     * @author HuangHao
     * @CreatTime 2020-05-29 15:57
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    @Override
    public List<LiveSceneDealerConfigDto> getLiveSceneDealerConfigList(LiveSceneDealerConfigVo liveSceneDealerConfig){
        List<LiveSceneDealerConfigDto> list = liveSceneDealerConfigReadMapper.getLiveSceneDealerConfigList(liveSceneDealerConfig);
        if(list != null && list.size() > 0){
            for(LiveSceneDealerConfigDto dealerConfig:list){
                CsCompany company = companyBaseService.getCsCompanyById(dealerConfig.getDealerId());
                if(company != null){
                    dealerConfig.setCityName(company.getCityName());
                }
            }
        }
        return list;
    }

    /**
     * 根据IDS批量更新经销商赠送油卡上限
     * @author HuangHao
     * @CreatTime 2020-05-29 16:39
     * @param refuelingCardNum  赠送油卡数
     * @param ids   更新对象的ID
     * @return int
     */
    @Override
    public TcResponse batchUpdate(Integer refuelingCardNum,List<Integer> ids,String updateUserName){
        String keyWord = "批量更新经销商赠送油卡上限";
        CommonLogUtil.addInfo(keyWord, "开始，赠送油卡数："+refuelingCardNum+"，修改ID："+ JSON.toJSONString(ids));
        if(refuelingCardNum == null || refuelingCardNum < 0){
            String msg = "赠送油卡数必须大于等于0";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(ids == null || ids.size() < 1){
            String msg = "ids为空";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(updateUserName)){
            String msg = "操作人updateUserName不能为空";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        int updateNum = liveSceneDealerConfigWriteMapper.updateRefuelingCardNumByIds(refuelingCardNum, ids,updateUserName);
        CommonLogUtil.addInfo(keyWord, "更新完成，更新数："+updateNum+"，操作人："+updateUserName);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
    /**
     * 经销商油卡统计-不分页
     * @author HuangHao
     * @CreatTime 2020-06-02 11:32
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerConfigDto>
     */
    @Override
    public List<LiveSceneDealerConfigDto> getDealerRefuelCardStatistics(LiveSceneDealerConfigVo liveSceneDealerConfig){
        return liveSceneDealerConfigReadMapper.getDealerRefuelCardStatistics(liveSceneDealerConfig);
    }

    /**
     * 经销商油卡统计-分页
     * @author HuangHao
     * @CreatTime 2020-06-02 11:32
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerConfigDto>
     */
    @Override
    public PageResult<LiveSceneDealerConfigDto> getDealerRefuelCardStatisticsByPage(LiveSceneDealerConfigVo liveSceneDealerConfig){
        PageHelper.startPage(liveSceneDealerConfig.getPage(), liveSceneDealerConfig.getLimit());
        List<LiveSceneDealerConfigDto> list = liveSceneDealerConfigReadMapper.getDealerRefuelCardStatistics(liveSceneDealerConfig);
        PageInfo<LiveSceneDealerConfigDto> page = new PageInfo<LiveSceneDealerConfigDto>(list);
        PageResult<LiveSceneDealerConfigDto> pageResult = new PageResult<LiveSceneDealerConfigDto>();
        pageResult.setCount(page.getTotal());
        pageResult.setPage(liveSceneDealerConfig.getPage());
        pageResult.setLimit(liveSceneDealerConfig.getLimit());
        pageResult.setCode(String.valueOf(StatusCodeEnum.SUCCESS.getCode()));
        pageResult.setMsg(null);
        //设置场次名称
        setPeriodsName(list);
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 循环列表给每条记录设置大场次名称
     * 注：此方法在list数据过多时会有效率问题
     * @author HuangHao
     * @CreatTime 2020-06-02 15:19
     * @param list
     * @return void
     */
    private void setPeriodsName(List<LiveSceneDealerConfigDto> list){
        if(list != null && list.size() > 0){
            ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
            for(LiveSceneDealerConfigDto configDto:list){
                activityConfigRequestVo.setId(configDto.getPeriodsId());
                ActivityConfigResponseVo activityConfigResponseVo = periodsService.queryById(activityConfigRequestVo);
                if(activityConfigResponseVo != null){
                    configDto.setPeriodsName(activityConfigResponseVo.getName());
                }
            }
        }
    }

    /**
     * 获取经销商参加过直播活动的大场次
     * @author HuangHao
     * @CreatTime 2020-06-02 18:10
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    @Override
    public List<LiveSceneDealerConfigDto> getDealerPeriods(Integer dealerId){
        if(dealerId == null){
            return null;
        }
        List<LiveSceneDealerConfigDto> list = liveSceneDealerConfigReadMapper.getDealerPeriods(dealerId);
        //设置场次名称
        setPeriodsName(list);
        return list;
    }

}
