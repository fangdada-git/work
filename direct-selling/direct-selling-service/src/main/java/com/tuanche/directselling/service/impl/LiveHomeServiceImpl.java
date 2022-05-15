package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.LiveHomeService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.LiveDealerBroadcastDto;
import com.tuanche.directselling.dto.LiveInfoDto;
import com.tuanche.directselling.mapper.read.directselling.LiveDealerBroadcastReadMapper;
import com.tuanche.directselling.model.LiveDealerBroadcast;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveParameterVo;
import com.tuanche.framework.base.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * @class: LiveHomeServiceImpl
 * @description: 直播会场接口实现类
 * @author: dudg
 * @create: 2020-04-29 11:31
 */
@Service
public class LiveHomeServiceImpl implements LiveHomeService {

    /**
     * 预热播报固定数据
     */
    @Value("${prehot_broadcast_fixed_info}")
    private String prehot_broadcast_fixed_info;

    /**
     * 开始播报固定数据
     */
    @Value("${begin_broadcast_fixed_info}")
    private String begin_broadcast_fixed_info;

    @Autowired
    private LiveDealerBroadcastReadMapper liveDealerBroadcastReadMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    RedisService redisService;


    /**
     * @description: 拉取实时播报数据接口   固定数据+ 直播中数据
     * @param periodsId 场次id
     * @param beginTime 场次开始时间
     * @return: java.util.List<java.lang.String> 播报文案列表
     * @author: dudg
     * @date: 2020/4/29 11:23
    */
    @Override
    public List<String> pullRealTimeRollNotices(Integer periodsId, Date beginTime){
        if(periodsId == null || beginTime.getTime() > System.currentTimeMillis()){
            return getFixedRollNoticeByType(1);
        }

        //固定数据
        List<String> fixedRollNotices = getFixedRollNoticeByType(2);
        String cacheInfo = null;
        try {
            cacheInfo = FuncUtil.redisGetOrSetData(redisService, BaseCacheKeys.ROLL_NOTICE_DYNAMIC_PERIODS.setParamValue(periodsId),String.class,()->{
                //拉取直播中数据
                List<LiveInfoDto> livingBroadcasts = liveDealerBroadcastReadMapper.getLivingRollNoticeByPeriodsId(periodsId);
                return JsonUtil.Object2Json(livingBroadcasts);
            },BaseCacheKeys.ROLL_NOTICE_DYNAMIC_PERIODS.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("获取实时播报动态直播数据，发生异常",e.getMessage(),e);
        }

        List<LiveInfoDto> livingBroadcasts = StringUtil.isNotEmpty(cacheInfo)?JsonUtil.json2List(cacheInfo,LiveInfoDto.class):Lists.newArrayList();
        livingBroadcasts.forEach(item->{
            fixedRollNotices.add(MessageFormat.format("【{0}】正在直播【{1}】",item.getAnchorNick(),item.getLiveTitle()));
        });
        return fixedRollNotices;
    }

    /**
     * @description: 获取实时播报固定数据（预热/开始阶段）
     * @param type  1 预热 2 开始
     * @return: java.util.List<java.lang.String>
     * @author: dudg
     * @date: 2020/4/29 13:40
    */
    private List<String> getFixedRollNoticeByType(Integer type) {
        List<String> fixedInfoLst = null;
        if (type == 1) {
            fixedInfoLst = Lists.newArrayList(prehot_broadcast_fixed_info.split("\n"));

        } else {
            fixedInfoLst = Lists.newArrayList(begin_broadcast_fixed_info.split("\n"));
        }

        return CollectionUtils.isEmpty(fixedInfoLst) ? new ArrayList<>() : fixedInfoLst;
    }

    /**
     * @description: 获取指定主播ids直播中/回放数据 （如果优先直播中其次观看量最大） - 好店推荐接口
     * @param parameterVo
     * @return: java.util.List<com.tuanche.directselling.dto.LiveInfoDto>
     * @author: dudg
     * @date: 2020/4/30 14:08
    */
    @Override
    public List<LiveInfoDto> getLivingOrPlayBackListByAnchorAndPeriods(LiveParameterVo parameterVo){
        String cacheInfo = null;
        try {
            cacheInfo = FuncUtil.redisGetOrSetData(redisService, BaseCacheKeys.REC_DEALER_LIVE_PERIODS_CITY.setParamValue(parameterVo.getPeriodsId(),parameterVo.getCityId()),String.class,()->{
                List<LiveInfoDto> liveInfoDtos = liveDealerBroadcastReadMapper.getLivingOrPlayBackInfosByAnchorAndPeriods(parameterVo);
                return JsonUtil.Object2Json(liveInfoDtos);
            },BaseCacheKeys.REC_DEALER_LIVE_PERIODS_CITY.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("获取好店推荐直播数据，发生异常",e.getMessage(),e);
        }

        return StringUtils.isNotEmpty(cacheInfo)?JsonUtil.json2List(cacheInfo,LiveInfoDto.class):Lists.newArrayList();
    }

	@Override
	public List<LiveDealerBroadcastDto> getLiveBroadcastInprogressList(LiveDealerBroadcastDto liveDealerBroadcastDto) {
        String cacheInfo = null;
        try {
            cacheInfo = FuncUtil.redisGetOrSetData(redisService, BaseCacheKeys.LIVING_PERIODS_CITY.setParamValue(liveDealerBroadcastDto.getPeriodsId(),liveDealerBroadcastDto.getCityId()),String.class,()->{
                List<LiveDealerBroadcastDto> liveBroadcastInprogress = liveDealerBroadcastReadMapper.getLiveBroadcastInprogressList(liveDealerBroadcastDto);
                CommonLogUtil.addInfo("获取正在直播的数据条数为", (CollectionUtils.isEmpty(liveBroadcastInprogress)?0:liveBroadcastInprogress.size())+"");
                if (!CollectionUtils.isEmpty(liveBroadcastInprogress) && liveBroadcastInprogress.size() % 2 != 0) {
                	
                	CommonLogUtil.addInfo("获取正在直播的数据条数为开始补位", liveBroadcastInprogress.size() + "");
                    LiveDealerBroadcastDto maxLivePlayBack = liveDealerBroadcastReadMapper.getMaxViewCountPlayBackByPeriodsTime(liveDealerBroadcastDto);
                    CommonLogUtil.addInfo("获取正在直播的数据条数为开始补位结果", maxLivePlayBack == null ? "0" : "1");
                    if (maxLivePlayBack != null) {
                        liveBroadcastInprogress.add(maxLivePlayBack);
                    }
                }
                
                CommonLogUtil.addInfo("获取正在直播的数据条数结果为", (CollectionUtils.isEmpty(liveBroadcastInprogress)?0:liveBroadcastInprogress.size()) + "");
                return JsonUtil.Object2Json(liveBroadcastInprogress);
            },BaseCacheKeys.LIVING_PERIODS_CITY.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("获取正在直播数据列表，发生异常",e.getMessage(),e);
        }

        return StringUtil.isNotEmpty(cacheInfo)?JsonUtil.json2List(cacheInfo,LiveDealerBroadcastDto.class):Lists.newArrayList();
	}

	@Override
	public PageResult<LiveDealerBroadcastDto> getLiveBroadcastPlaybackList(PageResult<LiveDealerBroadcastDto> pageResult,
			LiveDealerBroadcastDto liveDealerBroadcastDto) {
		PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), false);

		List<LiveDealerBroadcastDto> list = liveDealerBroadcastReadMapper.getLiveBroadcastPlaybackList(liveDealerBroadcastDto);
        if (!CollectionUtils.isEmpty(list) && list.size() % 2 != 0) {
            CommonLogUtil.addInfo("获取精彩回放的数据条数为开始补位", list.size() + "");
            LiveDealerBroadcastDto maxLivePlayBack = liveDealerBroadcastReadMapper.getMaxViewCountPlayBackByPeriodsTime(liveDealerBroadcastDto);
            CommonLogUtil.addInfo("获取精彩回放的数据条数为开始补位结果", maxLivePlayBack == null ? "0" : "1");
            if (maxLivePlayBack != null) {
                list.add(maxLivePlayBack);
            }
        }
		pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
		pageResult.setMsg("");
		pageResult.setData(list);

		return pageResult;
	}

	@Override
	public PageResult<LiveDealerBroadcastDto> getLiveBroadcastForBrandList(PageResult<LiveDealerBroadcastDto> pageResult,
			LiveDealerBroadcastDto liveDealerBroadcastDto) {
		PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), false);
		List<LiveDealerBroadcastDto> list = liveDealerBroadcastReadMapper.getLiveBroadcastForBrandList(liveDealerBroadcastDto);

		pageResult.setCode("0");
		pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
		pageResult.setMsg("");
		pageResult.setData(list);

		return pageResult;
	}


    @Override
    public PageResult<LiveDealerBroadcastDto> getPreheatLivingAndPlayBackList(PageResult<LiveDealerBroadcastDto> pageResult,LiveDealerBroadcastDto liveDealerBroadcastDto) {
        CommonLogUtil.addInfo("预热/全国获取直播列表数据接口","请求参数：",liveDealerBroadcastDto);
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), false);
        List<LiveDealerBroadcastDto> list = liveDealerBroadcastReadMapper.getPreheatLivingAndPlayBackList(liveDealerBroadcastDto);

        pageResult.setCode("0");
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);

        return pageResult;
    }

    /**
     * 【长安云车展】根据条件取团车主直播号正在直播数据
     * @param liveParameterVo
     * @return
     */
    @Override
    public LiveDealerBroadcastDto getTcAnchorLivingBroadcast(LiveParameterVo liveParameterVo){
        /**
         * 直播状态 0 预告/结束 1 直播 2 回放
         */
        LiveDealerBroadcastDto result = null;
        try {
             result = FuncUtil.redisGetOrSetData(redisService, BaseCacheKeys.CHANGAN_PERIODS_LIVING_CACHE.getKey(), LiveDealerBroadcastDto.class, () -> {
                List<LiveDealerBroadcastDto> tcAnchorLiveList = liveDealerBroadcastReadMapper.getTcAnchorLiveList(liveParameterVo);
                if (CollectionUtils.isEmpty(tcAnchorLiveList)) {
                    return null;
                }

                // 直播中
                Optional<LiveDealerBroadcastDto> livingOpt = tcAnchorLiveList.stream().filter(n -> n.getLiveState().intValue() == 1).findFirst();
                if (!livingOpt.isPresent()) {
                    livingOpt = tcAnchorLiveList.stream().filter(n -> n.getLiveState().intValue() == 1).sorted(Comparator.comparing(LiveDealerBroadcast::getLiveStartTime)).findFirst();
                }

                // 预告
                if (!livingOpt.isPresent()) {
                    livingOpt = tcAnchorLiveList.stream().filter(n -> n.getLiveState().intValue() == 0).sorted(Comparator.comparing(LiveDealerBroadcast::getLiveStartTime)).findFirst();
                }

                // 回放
                if (!livingOpt.isPresent()) {
                    livingOpt = tcAnchorLiveList.stream().filter(n -> n.getLiveState().intValue() == 2).sorted(Comparator.comparing(LiveDealerBroadcast::getViewCount).reversed()).findFirst();
                }

                return livingOpt.get();
             }, BaseCacheKeys.CHANGAN_PERIODS_LIVING_CACHE.getExpire());
        } catch (RedisException e) {
            e.printStackTrace();
            CommonLogUtil.addError("根据条件取团车主直播号正在直播数据，发生异常",JSON.toJSONString(liveParameterVo),e);
        }

        return result;
    }

    /**
     * 【长安云车展】根据条件取团车主直播号回放列表
     * @param liveParameterVo
     * @return
     */
    @Override
    public List<LiveDealerBroadcastDto> getTcAnchorLivePlaybackList(LiveParameterVo liveParameterVo){
        List<LiveDealerBroadcastDto> result = null;
        try {
            String cacheInfo = FuncUtil.redisGetOrSetData(redisService, BaseCacheKeys.CHANGAN_PERIODS_PLAYBACK_CACHE.getKey(), String.class, () -> {
                liveParameterVo.setLiveState(2);
                List<LiveDealerBroadcastDto> tcAnchorLiveList = liveDealerBroadcastReadMapper.getTcAnchorLiveList(liveParameterVo);

                return JsonUtil.Object2Json(tcAnchorLiveList);
            }, BaseCacheKeys.CHANGAN_PERIODS_PLAYBACK_CACHE.getExpire());

            result = JSON.parseArray(cacheInfo,LiveDealerBroadcastDto.class);
        } catch (RedisException e) {
            e.printStackTrace();
            CommonLogUtil.addError("根据条件取团车主直播号回放列表，发生异常",JSON.toJSONString(liveParameterVo),e);
        }
        return result;
    }
}
