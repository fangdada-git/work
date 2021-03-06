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
 * @Description: ????????????-???????????????????????????service Impl
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
     * @Description ????????????-???????????????????????????????????????
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
     * @Description ????????????-???????????????????????????????????????
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
     * @Description ????????????-????????????????????????????????????
     * @Date 10:16 2020/4/28
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void insertByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) throws Exception {
        liveSceneDealerConfigWriteMapper.insertSelective(liveSceneDealerConfig);

        // ??????????????????-????????????????????????
//        createExtensionCodeCode(liveSceneDealerConfig);
    }

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description ????????????-?????????????????????????????????
     * @Date 10:16 2020/4/28
     **/
    @Override
    public void updateByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) {
        liveSceneDealerConfigWriteMapper.updateByPrimaryKeySelective(liveSceneDealerConfig);
        // ??????????????????????????????key
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
     * @Description ????????????-?????????????????????????????????
     * @Date 2020???5???21???12:34:09
     **/
    @Override
    public void deleteByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) {
        //?????? ???????????????????????????
        LiveSceneDealerConfig oldConfigObj = liveSceneDealerConfigReadMapper.selectByPrimaryKey(liveSceneDealerConfig.getId());
        liveSceneDealerConfigWriteMapper.deleteByLiveSceneDealerConfig(liveSceneDealerConfig);

        //?????? ???????????????????????????
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setUpdateUserId(liveSceneDealerConfig.getUpdateUserId());
        liveSceneDealerBrand.setUpdateUserName(liveSceneDealerConfig.getUpdateUserName());
        liveSceneDealerBrand.setUpdateDt(liveSceneDealerConfig.getUpdateDt());
        liveSceneDealerBrand.setDealerId(oldConfigObj.getDealerId());
        liveSceneDealerBrand.setSceneId(oldConfigObj.getSceneId());
        liveSceneDealerBrandWriteMapper.deleteByLiveSceneDealerBrand(liveSceneDealerBrand);

        // ??????????????????????????????key
        try {
            redisService.del(BaseCacheKeys.NOT_END_SCENE_CITY_CACHE.getKey());
        } catch (RedisException e) {
            e.printStackTrace();
        }

        // ???????????????????????????????????????kafka??????
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
     * @Description ????????????-?????????????????????????????????
     * @Date 2020???3???6???17:59:49
     **/
    @Override
    public PageResult findSceneDealerConfigList(PageResult<LiveSceneDealerConfigDto> pageResult, Integer sceneId) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "id desc");
        List<LiveSceneDealerConfigDto> liveSceneDealerConfigDtoList = liveSceneDealerConfigReadMapper.selectSceneDealerConfigList(sceneId);
        for (int i = 0; i < liveSceneDealerConfigDtoList.size(); i++) {
            LiveSceneDealerConfigDto liveSceneDealerConfigDto = liveSceneDealerConfigDtoList.get(i);
            // ????????????????????????
            List<LiveSceneDealerBrand> liveSceneDealerBrandList = liveSceneDealerConfigDto.getDealerBrandList();
            StringBuilder brandNames = new StringBuilder();
            liveSceneDealerBrandList.forEach(LiveSceneDealerBrand -> {
//                String masterBrandName = carBaseService.getBrandById(LiveSceneDealerBrand.getBrandId()).getCmName();
//                brandNames.append(LiveSceneDealerBrand.getBrandName() + "(" + masterBrandName + ")???");
                brandNames.append(LiveSceneDealerBrand.getBrandName() + "???");
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
     * @Description ????????????-?????????????????????????????????????????????????????????
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

        CommonLogUtil.addInfo("????????????-??????????????????-????????????????????????", "????????????????????????", vo);
        BroadcastRpcVo broadcastRpcResult = tcBroadCastPlanService.createCastCode(vo);
        CommonLogUtil.addInfo("????????????-??????????????????-????????????????????????", "????????????????????????", broadcastRpcResult);

        if (!ObjectUtils.isEmpty(broadcastRpcResult) && "0000".equals(broadcastRpcResult.getResCode())) {
            HashMap<String, Object> resultMap = (HashMap<String, Object>) broadcastRpcResult.getMap();
            JSONObject resultJson = JSON.parseObject(resultMap.get("result").toString());
            liveSceneDealerConfig.setExtensionCode(resultJson.getString(liveSceneDealerConfig.getId().toString()));
            liveSceneDealerConfigWriteMapper.updateByPrimaryKeySelective(liveSceneDealerConfig);
        } else {
            CommonLogUtil.addInfo("????????????-??????????????????-????????????????????????", "????????????????????????????????????????????????", vo);
            // ????????????????????????
            throw new Exception("??????????????????????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????????????????
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
     * ??????IDS???????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2020-05-29 16:39
     * @param refuelingCardNum  ???????????????
     * @param ids   ???????????????ID
     * @return int
     */
    @Override
    public TcResponse batchUpdate(Integer refuelingCardNum,List<Integer> ids,String updateUserName){
        String keyWord = "???????????????????????????????????????";
        CommonLogUtil.addInfo(keyWord, "???????????????????????????"+refuelingCardNum+"?????????ID???"+ JSON.toJSONString(ids));
        if(refuelingCardNum == null || refuelingCardNum < 0){
            String msg = "?????????????????????????????????0";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(ids == null || ids.size() < 1){
            String msg = "ids??????";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(updateUserName)){
            String msg = "?????????updateUserName????????????";
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        int updateNum = liveSceneDealerConfigWriteMapper.updateRefuelingCardNumByIds(refuelingCardNum, ids,updateUserName);
        CommonLogUtil.addInfo(keyWord, "???????????????????????????"+updateNum+"???????????????"+updateUserName);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
    /**
     * ?????????????????????-?????????
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
     * ?????????????????????-??????
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
        //??????????????????
        setPeriodsName(list);
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ????????????????????????????????????????????????
     * ??????????????????list?????????????????????????????????
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
     * ????????????????????????????????????????????????
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
        //??????????????????
        setPeriodsName(list);
        return list;
    }

}
