package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.broadcast.rpc.vo.TcLiveProgramDto;
import com.tuanche.broadcast.rpc.vo.TcPlanSchedulesInfoDto;
import com.tuanche.directselling.api.LiveProgramService;
import com.tuanche.directselling.dto.LiveProgramDealerBrandDto;
import com.tuanche.directselling.dto.LiveProgramDto;
import com.tuanche.directselling.dto.PosterDto;
import com.tuanche.directselling.mapper.read.directselling.*;
import com.tuanche.directselling.mapper.write.directselling.LiveProgramDealerBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveProgramWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.service.impl.dubbo.DubboService;
import com.tuanche.directselling.service.impl.kafka.SceneNoticeServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveProgramVo;
import com.tuanche.manu.entity.TDealerGoods;
import com.tuanche.manu.entity.TDealerGoodsGift;
import com.tuanche.merchant.pojo.dto.image.CommodityImageDO;
import com.tuanche.merchant.pojo.dto.image.CommodityImageRes;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangxing
 * @Description 节目相关服务实现
 * @date 2020年3月5日上午11:52:46
 */
@Service
public class LiveProgramServiceImpl implements LiveProgramService {
    @Autowired
    private LiveProgramReadMapper liveProgramReadMapper;
    @Autowired
    private LiveProgramWriteMapper liveProgramWriteMapper;
    @Autowired
    private LiveSceneDealerBrandReadMapper liveSceneDealerBrandReadMapper;
    @Autowired
    private LiveSceneReadMapper liveSceneReadMapper;
    @Autowired
    private LiveProgramDealerBrandReadMapper liveProgramDealerBrandReadMapper;
    @Autowired
    private LiveProgramDealerBrandWriteMapper liveProgramDealerBrandWriteMapper;
    @Autowired
    private LiveSceneDealerConfigReadMapper liveSceneDealerConfigReadMapper;
    @Autowired
    private SceneNoticeServiceImpl sceneNoticeService;
    @Autowired
    private DubboService dubboService;


    @Override
    public PageResult findProgramList(PageResult<LiveProgramDto> pageResult, LiveProgramVo liveProgramVo) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<LiveProgramDto> data = liveProgramReadMapper.queryList(liveProgramVo);
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        for (int i = 0, size = data.size(); i < size; i++) {
            liveSceneDealerBrand.setId(data.get(i).getId());
            List<LiveSceneDealerBrand> list = liveSceneDealerBrandReadMapper.searchDealerBrands(liveSceneDealerBrand);
            data.get(i).setLiveSceneDealerBrands(list);
        }
        PageInfo<LiveProgramDto> page = new PageInfo<LiveProgramDto>(data);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(data);
        return pageResult;
    }

    @Override
    public List<LiveProgramDto> queryList(LiveProgramVo liveProgramVo) {
        List<LiveProgramDto> data = liveProgramReadMapper.queryList(liveProgramVo);
        return data;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void save(LiveProgramVo liveProgramVo) throws Exception {
        String reqMessageId = String.valueOf(System.currentTimeMillis());
        LiveProgram liveProgram = new LiveProgram();
        BeanUtils.copyProperties(liveProgramVo, liveProgram);
        liveProgramWriteMapper.insertSelective(liveProgram);
        CommonLogUtil.addInfo(reqMessageId, "保存节目结束", JSONObject.toJSONString(liveProgram));


        LiveProgramDealerBrand record = new LiveProgramDealerBrand();
        record.setCreateDt(liveProgram.getCreateDt());
        record.setCreateUserId(liveProgram.getCreateUserId());
        record.setCreateUserName(liveProgram.getCreateUserName());
        record.setDeleteState(0);
        record.setProgramType(1);
        record.setProgramId(liveProgram.getId());
        record.setSceneId(liveProgram.getSceneId());
        liveProgramDealerBrandWriteMapper.insertSelective(record);
        CommonLogUtil.addInfo(reqMessageId, "保存主机位节目结束", JSONObject.toJSONString(record));


        LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(liveProgramVo.getSceneId());
        CommonLogUtil.addInfo(reqMessageId, "查询场次活动信息结束", JSONObject.toJSONString(liveScene));


        List<TcPlanSchedulesInfoDto> broadcasts = Lists.newArrayList();
        TcPlanSchedulesInfoDto broadcastDto = new TcPlanSchedulesInfoDto();
        broadcastDto.setOutSideId(record.getId());
        broadcastDto.setPlanId(Integer.valueOf(liveScene.getPlanId()));
        broadcastDto.setAuthCode(liveScene.getHostCode());
        broadcastDto.setStartTime(liveProgram.getBeginTime());
        broadcastDto.setEndTime(liveProgram.getEndTime());
        broadcastDto.setCreateDt(liveProgramVo.getCreateDt());
        broadcastDto.setCreateId(liveProgramVo.getCreateUserId());
        broadcasts.add(broadcastDto);
        addBroadcast(broadcasts, reqMessageId);
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void update(LiveProgramVo liveProgramVo) throws Exception {
        String reqMessageId = String.valueOf(System.currentTimeMillis());
        LiveProgram liveProgram = new LiveProgram();
        BeanUtils.copyProperties(liveProgramVo, liveProgram);
        liveProgramWriteMapper.updateByPrimaryKeySelective(liveProgram);
        CommonLogUtil.addInfo(reqMessageId, "更新节目信息结束", JSONObject.toJSONString(liveProgram));


        LiveProgramDealerBrand liveProgramDealerBrand = new LiveProgramDealerBrand();
        liveProgramDealerBrand.setProgramId(liveProgramVo.getId());
        liveProgramDealerBrand.setDeleteState(0);
        switch (liveProgramVo.getDeleteState()) {
            case 1:
                delProgramDealerBrand(liveProgramDealerBrand, reqMessageId);
                break;
            case 0:
                if (liveProgram.getBeginTime() == null || liveProgram.getEndTime() == null) {
                    return;
                }

                List<Integer> programIds = Lists.newArrayList();
                List<LiveProgramDealerBrand> list = liveProgramDealerBrandReadMapper.queryList(liveProgramDealerBrand);
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                CommonLogUtil.addInfo(reqMessageId, "查询品牌经销商信息结束", JSONObject.toJSONString(liveProgramDealerBrand));


                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                LiveScene liveScene = liveSceneReadMapper.selectByPrimaryKey(liveProgramVo.getSceneId());
                CommonLogUtil.addInfo(reqMessageId, "查询场次活动信息结束", JSONObject.toJSONString(liveScene));

                List<TcPlanSchedulesInfoDto> broadcasts = Lists.newArrayList();
                LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
                LiveSceneDealerConfig liveSceneDealerConfig = new LiveSceneDealerConfig();
                for (LiveProgramDealerBrand dto : list) {
                    programIds.add(dto.getBroadcastId());
                    TcPlanSchedulesInfoDto broadcastDto = new TcPlanSchedulesInfoDto();
                    broadcastDto.setOutSideId(dto.getId());
                    broadcastDto.setPlanId(Integer.valueOf(liveScene.getPlanId()));
                    if (dto.getProgramType() == 1) {
                        broadcastDto.setAuthCode(liveScene.getHostCode());
                    } else {
                        // 获取节目配置品牌
                        liveSceneDealerBrand = liveSceneDealerBrandReadMapper.selectByPrimaryKey(dto.getDealerBrandId());
                        LiveSceneDealerConfig params = new LiveSceneDealerConfig();
                        params.setDealerId(liveSceneDealerBrand.getDealerId());
                        params.setSceneId(liveSceneDealerBrand.getSceneId());
                        // 获取节目配置品牌所属经销商配置
                        liveSceneDealerConfig = liveSceneDealerConfigReadMapper.selectByLiveSceneDealerConfig(params);
                        broadcastDto.setAuthCode(liveSceneDealerConfig.getExtensionCode());
                    }
                    broadcastDto.setStartTime(liveProgram.getBeginTime());
                    broadcastDto.setEndTime(liveProgram.getEndTime());
                    broadcastDto.setCreateDt(liveProgramVo.getUpdateDt());
                    broadcastDto.setCreateId(liveProgramVo.getUpdateUserId());
                    broadcasts.add(broadcastDto);
                }

                TcLiveProgramDto msg = new TcLiveProgramDto();
                msg.setType(2);
                msg.setProgramIds(programIds);
                sceneNoticeService.sendSceneMessage(JSONObject.toJSONString(msg), reqMessageId);

                addBroadcast(broadcasts, reqMessageId);
                break;

            default:
                break;
        }
    }

    private void addBroadcast(List<TcPlanSchedulesInfoDto> broadcasts, String reqMessageId) throws Exception {
        BroadcastRpcVo vo = new BroadcastRpcVo(BroadcastRpcConstants.SOURCES.PC, BroadcastRpcConstants.BUSINESS_TYPE.OUTSIDE_CREATE_CAST_SCHEDULES, BroadcastRpcConstants.PLATFORM.PC);
        vo.setInfo("param", JSONObject.toJSONString(broadcasts));
        vo.setInfo("reqMessageId", reqMessageId);
        vo = dubboService.broadCastPlanService(vo);
        JSONObject json = JSONObject.parseObject(vo.getString("result"));
        Iterator<String> keys = json.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            LiveProgramDealerBrand entity = new LiveProgramDealerBrand();
            entity.setBroadcastId(json.getInteger(key));
            entity.setId(Integer.valueOf(key));
            liveProgramDealerBrandWriteMapper.updateByPrimaryKeySelective(entity);
        }
        CommonLogUtil.addInfo(reqMessageId, "更新品牌经销商直播id结束");
    }

    private void delProgramDealerBrand(LiveProgramDealerBrand liveProgramDealerBrand, String reqMessageId) {
        List<Integer> programIds = Lists.newArrayList();
        List<LiveProgramDealerBrand> list = liveProgramDealerBrandReadMapper.queryList(liveProgramDealerBrand);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        CommonLogUtil.addInfo(reqMessageId, "查询品牌经销商信息结束", JSONObject.toJSONString(liveProgramDealerBrand));

        for (LiveProgramDealerBrand entiry : list) {
            programIds.add(entiry.getBroadcastId());
            liveProgramDealerBrand.setDeleteState(1);
            liveProgramDealerBrand.setId(entiry.getId());
            liveProgramDealerBrandWriteMapper.updateByPrimaryKeySelective(liveProgramDealerBrand);
        }
        CommonLogUtil.addInfo(reqMessageId, "删除品牌经销商信息结束", JSONObject.toJSONString(list));

        if (!CollectionUtils.isEmpty(programIds)) {
            TcLiveProgramDto msg = new TcLiveProgramDto();
            msg.setType(2);
            msg.setProgramIds(programIds);
            sceneNoticeService.sendSceneMessage(JSONObject.toJSONString(msg), reqMessageId);
        }
    }

    @Override
    public List<LiveSceneDealerBrand> searchBrands(LiveProgramVo liveProgramVo) {
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setSceneId(liveProgramVo.getSceneId());
        liveSceneDealerBrand.setId(liveProgramVo.getId());
        return liveSceneDealerBrandReadMapper.searchBrands(liveSceneDealerBrand);
    }

    @Override
    public List<LiveSceneDealerBrand> searchDealers(LiveProgramVo liveProgramVo) {
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setSceneId(liveProgramVo.getSceneId());
        liveSceneDealerBrand.setBrandId(liveProgramVo.getBrandId());
        liveSceneDealerBrand.setId(liveProgramVo.getId());
        return liveSceneDealerBrandReadMapper.searchDealers(liveSceneDealerBrand);
    }

    @Override
    public List<LiveSceneDealerBrand> searchDealerBrands(LiveProgramVo liveProgramVo) {
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setId(liveProgramVo.getId());
        return liveSceneDealerBrandReadMapper.searchDealerBrands(liveSceneDealerBrand);
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void updateProgramDealer(LiveProgramVo liveProgramVo) throws Exception {
        String reqMessageId = String.valueOf(System.currentTimeMillis());
        LiveProgramDealerBrand liveProgramDealerBrand = new LiveProgramDealerBrand();
        liveProgramDealerBrand.setProgramId(liveProgramVo.getId());
        liveProgramDealerBrand.setDeleteState(0);
        liveProgramDealerBrand.setProgramType(0);
        delProgramDealerBrand(liveProgramDealerBrand, reqMessageId);

        List<LiveProgramDealerBrandDto> programDealerBrands = liveProgramVo.getProgramDealerBrands();
        if (CollectionUtils.isEmpty(programDealerBrands)) {
            return;
        }
        LiveScene liveScene = null;
        LiveProgram liveProgram = null;
        List<TcPlanSchedulesInfoDto> broadcasts = Lists.newArrayList();
        LiveSceneDealerBrand liveSceneDealerBrand = new LiveSceneDealerBrand();
        liveSceneDealerBrand.setSceneId(liveProgramVo.getSceneId());
        for (LiveProgramDealerBrandDto dto : programDealerBrands) {
            LiveProgramDealerBrand record = new LiveProgramDealerBrand();
            record.setCreateDt(liveProgramVo.getCreateDt());
            record.setCreateUserId(liveProgramVo.getCreateUserId());
            record.setCreateUserName(liveProgramVo.getCreateUserName());
            liveSceneDealerBrand.setBrandId(dto.getBrandId());
            liveSceneDealerBrand.setDealerId(dto.getDealerId());
            liveSceneDealerBrand = liveSceneDealerBrandReadMapper.searchDealerBrand(liveSceneDealerBrand);
            record.setDealerBrandId(liveSceneDealerBrand.getId());
            record.setDeleteState(0);
            record.setProgramId(liveProgramVo.getId());
            record.setSceneId(liveProgramVo.getSceneId());
            liveProgramDealerBrandWriteMapper.insertSelective(record);
            CommonLogUtil.addInfo(reqMessageId, "保存品牌以及经销商结束", JSONObject.toJSONString(record));


            liveScene = liveSceneReadMapper.selectByPrimaryKey(liveSceneDealerBrand.getSceneId());
            CommonLogUtil.addInfo(reqMessageId, "查询场次活动信息结束", JSONObject.toJSONString(liveScene));

            liveProgram = liveProgramReadMapper.selectByPrimaryKey(liveProgramVo.getId());
            CommonLogUtil.addInfo(reqMessageId, "查询节目信息结束", JSONObject.toJSONString(liveScene));


            // 获取节目配置品牌所属经销商配置
            LiveSceneDealerConfig params = new LiveSceneDealerConfig();
            params.setDealerId(dto.getDealerId());
            params.setSceneId(liveProgramVo.getSceneId());
            LiveSceneDealerConfig liveSceneDealerConfig = liveSceneDealerConfigReadMapper.selectByLiveSceneDealerConfig(params);

            TcPlanSchedulesInfoDto broadcastDto = new TcPlanSchedulesInfoDto();
            broadcastDto.setOutSideId(record.getId());
            broadcastDto.setPlanId(Integer.valueOf(liveScene.getPlanId()));
            broadcastDto.setAuthCode(liveSceneDealerConfig.getExtensionCode());
            broadcastDto.setStartTime(liveProgram.getBeginTime());
            broadcastDto.setEndTime(liveProgram.getEndTime());
            broadcastDto.setCreateDt(liveProgramVo.getCreateDt());
            broadcastDto.setCreateId(liveProgramVo.getCreateUserId());
            broadcasts.add(broadcastDto);
        }
        addBroadcast(broadcasts, reqMessageId);
    }

    /**
     * @param dealerBrandId
     * @return java.util.List<com.tuanche.directselling.model.LiveProgramDealerBrand>
     * @Author GongBo
     * @Description 根据场次活动经销商品牌ID  获取节目关联的经销商品牌数据
     * @Date 16:28 2020/3/9
     **/
    @Override
    public List<LiveProgramDealerBrand> findDealerBrandList(Integer dealerBrandId) {
        return liveProgramDealerBrandReadMapper.selectDealerBrandList(dealerBrandId);
    }

    /**
     * 海报实体
    * */
    @Override
    public List<PosterModel> selectPosterByProgramId(Integer id) {
        List<PosterModel> posterModelList = new ArrayList<>();
        List<PosterDto> posterDtos = liveProgramReadMapper.selectPosterByProgramId(id);
        if (posterDtos != null && posterDtos.size() > 0) {
            List<CommodityImageDO> commodityImageResList = new ArrayList<>();
            for (PosterDto v : posterDtos){
                CommodityImageDO commodityImageDO = new CommodityImageDO();
                BeanUtils.copyProperties(v, commodityImageDO);
                //图片模板类型 6 海报预热 7 海报正式
                if (commodityImageDO.getType().equals(ConstantsUtil.PROGRAM_TYPE_B_1)) {
                    commodityImageDO.setType(ConstantsUtil.POSTER_TYPE_6);
                    String goodGiftName = getGoodGiftName(v.getSceneId(), v.getMerchantId(), ConstantsUtil.GOODS_TYPE_B_2, ConstantsUtil.GOODS_STATUS_B_2);
                    commodityImageDO.setPresent(goodGiftName);
                } else if (commodityImageDO.getType().equals(ConstantsUtil.PROGRAM_TYPE_B_2)) {
                    commodityImageDO.setType(ConstantsUtil.POSTER_TYPE_7);
                }
                String dateStr = getDateStr(v.getBeginTime());
                String dateStr1 = getDateStr(v.getEndTime());
                if (!dateStr.isEmpty() && !dateStr1.isEmpty()) {
                    commodityImageDO.setActiveTime(dateStr + "-" + dateStr1);
                }
                if (commodityImageDO.getType() != null) {
                    commodityImageResList.add(commodityImageDO);
                }
                String activeUrl=commodityImageDO.getActiveUrl();
                if (activeUrl!=null && activeUrl.indexOf("http:")>-1){
                    commodityImageDO.setActiveUrl(commodityImageDO.getActiveUrl().replace("http:","https:"));
                }
                if (activeUrl!=null && activeUrl.indexOf("http:")<0 && activeUrl.indexOf("https:")<0){
                    commodityImageDO.setActiveUrl("https://" + activeUrl);
                }
                commodityImageDO.setCityName(dubboService.getCityName(v.getCityId()));
            }
            CommonLogUtil.addInfo("rpc调用生成海报时间","开始", System.currentTimeMillis());
            List<CommodityImageRes> commodityImageResList1 = dubboService.generateTmImagesBatch(commodityImageResList);
            CommonLogUtil.addInfo("rpc调用生成海报时间","结束", System.currentTimeMillis());
            if (commodityImageResList1 != null && commodityImageResList1.size() > 0) {
                for (CommodityImageRes v : commodityImageResList1){
                    PosterModel posterModel = new PosterModel();
                    BeanUtils.copyProperties(v, posterModel);
                    posterModel.setDealerName(dubboService.getDealerName(v.getMerchantId()));
                    posterModelList.add(posterModel);
                }
            }
        }
        return posterModelList;
    }
    @Override
    public Integer getPosterModelNumb(Integer id){
        List<PosterDto> posterDtos = liveProgramReadMapper.selectPosterByProgramId(id);
        return posterDtos.size();
    }

    private String getDateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm");
        return format.format(date);
    }
    /*
    * goodsType 商品类型 1 车款 2 到店礼品; status 状态：0 草稿 1 待审核  2 审核通过
    * */
    private String getGoodGiftName(Integer activityId, Integer dealerId, Integer goodsType, Integer status){
        TDealerGoods goods = new TDealerGoods();
        goods.setDealerId(dealerId);
        goods.setGoodsType(goodsType);
        goods.setActivityId(activityId);
        goods.setStatus(status);
        String goodGiftName = "";
        TDealerGoodsGift goodsGiftByAIdAndDIdAndGTypeAndStatus =
                dubboService.getGoodsGiftByAIdAndDIdAndGTypeAndStatus(goods);
        if (goodsGiftByAIdAndDIdAndGTypeAndStatus != null){
            goodGiftName = goodsGiftByAIdAndDIdAndGTypeAndStatus.getGiftName();
        }
        return goodGiftName;
    }

}
