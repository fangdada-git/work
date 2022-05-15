package com.tuanche.directselling.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.LiveDealerAnchorService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.LiveDealerPlaybackDto;
import com.tuanche.directselling.mapper.read.directselling.LiveDealerAnchorReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveDealerBroadcastReadMapper;
import com.tuanche.directselling.mapper.read.directselling.LiveDealerPlaybackReadMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveDealerAnchorWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveDealerPlaybackWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveTbloginCookieWriteMapper;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.directselling.model.LiveDealerBroadcast;
import com.tuanche.directselling.model.LiveDealerPlayback;
import com.tuanche.directselling.model.LiveTbloginCookie;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveDealerAnchorVo;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.framework.base.util.DateUtil;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-27 13:02
 **/
@Service
public class LiveDealerAnchorServiceImpl implements LiveDealerAnchorService {

    @Autowired
    private LiveDealerAnchorReadMapper liveDealerAnchorReadMapper;
    @Autowired
    private LiveTbloginCookieWriteMapper tbloginCookieWriteMapper;
    @Autowired
    private LiveDealerBroadcastReadMapper liveDealerBroadcastReadMapper;
    @Autowired
    private LiveDealerAnchorWriteMapper liveDealerAnchorWriteMapper;
    @Autowired
    private LiveDealerPlaybackReadMapper liveDealerPlaybackReadMapper;
    @Autowired
    private LiveDealerPlaybackWriteMapper liveDealerPlaybackWriteMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    /**
     * 百城主播排行榜比例系数 销冠-人气-活力   eg:1-1-1
     */
    @Value("${dealer_anchor_ranking_ratio}")
    private String dealer_anchor_ranking_ratio;
    /**
     * 百城主播排行榜热度都为0时最大热度 销冠-人气-活力   eg:100-200-150
     */
    @Value("${dealer_anchor_ranking_max_heat}")
    private String dealer_anchor_ranking_max_heat;
    /**
     * 百城主播排行榜热度都为0时最小热度 销冠-人气-活力   eg:100-200-150
     */
    @Value("${dealer_anchor_ranking_min_heat}")
    private String dealer_anchor_ranking_min_heat;
    /**
     * 百城主播排行榜热度为0时是否需要补充非0， 1：补充 0：不补充  销冠-人气-活力   eg:0-0-0
     */
    @Value("${dealer_anchor_ranking_zero_heat}")
    private String dealer_anchor_ranking_zero_heat;
    /**
     * 百城主播排行榜默认头像地址
     */
    @Value("${dealer_anchor_head_img}")
    private String dealer_anchor_head_img;
    /**
     * 百城主播排行榜问题头像 主播id
     */
    @Value("${dealer_anchor_head_img_id}")
    private String dealer_anchor_head_img_id;
    /**
     * 淘宝数据跑完时间点 eg：yyyy-MM-dd 10:15:00
     */
    @Value("${dealer_anchor_date_time}")
    private String dealer_anchor_date_time;
    /**
     * 主播排行榜展示日期
     */
    @Value("${dealer_anchor_ranking_display_date: }")
    private String dealer_anchor_ranking_display_date;
    @Reference
    CityBaseService cityBaseService;

    /**
     * @description: 获取百城主播排行榜
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/3/27 13:08
     */
    @Override
    public List<LiveDealerAnchor> LiveDealerAnchorRanking(LiveDealerAnchorVo liveDealerAnchorVo) {
        String key = BaseCacheKeys.DEALER_ANCHOR_RANKING.setParamValue(liveDealerAnchorVo.getType());
        List<LiveDealerAnchor> list = new ArrayList<>();
        try {
            if (redisService.exists(key)) {
                String result = redisService.get(key, String.class);
                list = JSON.parseArray(result, LiveDealerAnchor.class);
            } else {
                liveDealerAnchorVo.setRatio(Integer.valueOf(dealer_anchor_ranking_ratio.split("-")[liveDealerAnchorVo.getInt_type() - 1]));

                /**
                 * 取排行日期逻辑：如果当前场次已经结束取场次结束日期作为排行日期，反之取前一天日期
                 * 优先级：配置显示日期 -》 场次结束时间 -》 前一天
                 */

                // 如果配置了展示日期
                Date displayDate = DateUtils.stringToDate(DateUtils.befoDay(), DateUtils.YYYY_MM_DD);
                //判断当前场次是否进行中，如果正在进行中正常取前一天，反之取最后end_time
                if (liveDealerAnchorVo.getEndTime().getTime() < System.currentTimeMillis()) {
                    displayDate = liveDealerAnchorVo.getEndTime();
                }

                if (!StringUtil.isEmpty(dealer_anchor_ranking_display_date)) {
                    displayDate = DateUtils.stringToDate(dealer_anchor_ranking_display_date, DateUtils.YYYY_MM_DD);
                }

                for (int i = 0; i < 7; i++) {
                    liveDealerAnchorVo.setDate(DateUtil.addTime(displayDate, -24 * i));
                    list = getLiveDealerAnchorList(liveDealerAnchorVo);
                    if (CollectionUtil.isNotEmpty(list)) break;
                }
                if (CollectionUtil.isNotEmpty(list)) {
                    if ((dealer_anchor_ranking_zero_heat.split("-")[liveDealerAnchorVo.getInt_type() - 1]).equals("1")) {
                        list = disposeZeroHeat(list, liveDealerAnchorVo);
                    }
                    //获取最新直播间
                    list = getLiveDealerBroadByAnchorIds(list);
                    long stopTime = getRedisExistTime();
                    redisService.set(key, JSON.toJSONString(list), stopTime);
                }
            }
        } catch (Exception e) {
            CommonLogUtil.addError("获取百城主播排行榜error", JSON.toJSONString(liveDealerAnchorVo), e);
        }
        return list;
    }

    //
    private List<LiveDealerAnchor> getLiveDealerAnchorList(LiveDealerAnchorVo liveDealerAnchorVo) {
        List<LiveDealerAnchor> list = new ArrayList<>();
        if (GlobalConstants.dealer_anchor_ranking.anchor_ranking_type1.equals(liveDealerAnchorVo.getInt_type())
                || GlobalConstants.dealer_anchor_ranking.anchor_ranking_type2.equals(liveDealerAnchorVo.getInt_type())) {
            list = liveDealerAnchorReadMapper.getLiveDealerAnchorByPv(liveDealerAnchorVo);
            //3:活力主播
        } else if (GlobalConstants.dealer_anchor_ranking.anchor_ranking_type3.equals(liveDealerAnchorVo.getInt_type())) {
            list = liveDealerAnchorReadMapper.getLiveDealerAnchorByLiveTime(liveDealerAnchorVo);
        }
        return list;
    }

    //主播热度为0时，随机赋值热度
    private List<LiveDealerAnchor> disposeZeroHeat(List<LiveDealerAnchor> list, LiveDealerAnchorVo liveDealerAnchorVo) {
        //0热度主播
        List<LiveDealerAnchor> zeroHeatList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list) && (list.get(list.size() - 1).getAnchorHeat() == 0 || list.get(list.size() - 1).getAnchorHeat() == null)) {
            for (Iterator<LiveDealerAnchor> it = list.iterator(); it.hasNext(); ) {
                LiveDealerAnchor dealerAnchor = it.next();
                if (dealerAnchor.getAnchorHeat() == 0 || dealerAnchor.getAnchorHeat() == null) {
                    zeroHeatList.add(dealerAnchor);
                    it.remove();
                }
            }
            int max_heat = Integer.valueOf(dealer_anchor_ranking_max_heat.split("-")[liveDealerAnchorVo.getInt_type() - 1]);
            int min_heat = Integer.valueOf(dealer_anchor_ranking_min_heat.split("-")[liveDealerAnchorVo.getInt_type() - 1]);
            Integer minHeat = CollectionUtil.isEmpty(list) ? (max_heat - min_heat) : list.get(list.size() - 1).getAnchorHeat() - min_heat;
            List<Integer> heatList = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < zeroHeatList.size(); i++) {
                heatList.add(random.nextInt(minHeat) + min_heat);
            }
            heatList.sort(Comparator.comparingInt(Integer::intValue).reversed());
            for (int i = 0; i < zeroHeatList.size(); i++) {
                zeroHeatList.get(i).setAnchorHeat(heatList.get(i));
            }
            list.addAll(zeroHeatList);
        }
        return list;
    }

    private List<LiveDealerAnchor> getLiveDealerBroadByAnchorIds(List<LiveDealerAnchor> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            //问题主播头像
            List<Long> anchorIdList = new ArrayList<>();
            if (StringUtils.isNotEmpty(dealer_anchor_head_img_id) && !dealer_anchor_head_img_id.equals("0")) {
                for (String s : (dealer_anchor_head_img_id.split(","))) {
                    anchorIdList.add(Long.valueOf(s));
                }
            }
            List<Long> anchorIds = new ArrayList<>();
            list.forEach(liveDealerAnchor -> {
                if (liveDealerAnchor.getAnchorId() != null) {
                    if (CollectionUtil.isNotEmpty(anchorIdList) && anchorIdList.contains(liveDealerAnchor.getAnchorId())) {
                        liveDealerAnchor.setHeadImg(dealer_anchor_head_img);
                    }
                    anchorIds.add(liveDealerAnchor.getAnchorId());
                }
            });
            //昨天观看数最大直播间
            List<LiveDealerBroadcast> broadcastList = liveDealerBroadcastReadMapper.getLiveDealerBroadViewByAnchorIds(anchorIds);
            if (CollectionUtil.isNotEmpty(broadcastList)) {
                anchorIds.clear();
                Map<Long, String> map = new HashMap<>();
                broadcastList.forEach(liveDealerBroadcast -> {
                    map.put(liveDealerBroadcast.getAnchorId(), liveDealerBroadcast.getLiveUrl());
                });
                list.forEach(liveDealerAnchor -> {
                    if (map.get(liveDealerAnchor.getAnchorId()) != null) {
                        liveDealerAnchor.setAnchorUrl(map.get(liveDealerAnchor.getAnchorId()));
                    } else {
                        anchorIds.add(liveDealerAnchor.getAnchorId());
                    }
                });
            }
            //最新直播间
            if (CollectionUtil.isNotEmpty(anchorIds)) {
                List<LiveDealerBroadcast> broadcasts = liveDealerBroadcastReadMapper.getLiveDealerBroadByAnchorIds(anchorIds);
                if (CollectionUtil.isNotEmpty(broadcasts)) {
                    Map<Long, String> map = new HashMap<>();
                    broadcasts.forEach(liveDealerBroadcast -> {
                        map.put(liveDealerBroadcast.getAnchorId(), liveDealerBroadcast.getLiveUrl());
                    });
                    list.forEach(liveDealerAnchor -> {
                        if (map.get(liveDealerAnchor.getAnchorId()) != null) {
                            liveDealerAnchor.setAnchorUrl(map.get(liveDealerAnchor.getAnchorId()));
                        }
                    });
                }
            }
        }
        return list;
    }

    /***
     * @description: 清空并插入登录cookie
     * @param cookieStr
     * @return: int
     * @author: dudg
     * @date: 2020/3/31 11:04
     */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public int insertLoginCookie(String cookieStr) {
        tbloginCookieWriteMapper.delAll();
        LiveTbloginCookie tbloginCookie = new LiveTbloginCookie();
        tbloginCookie.setCookies(cookieStr);
        tbloginCookie.setCreateDt(new Date());
        return tbloginCookieWriteMapper.insertSelective(tbloginCookie);
    }

    /***
     * @description: 根据城市取每个主播的最新直播数据
     * @param liveDealerAnchorVo
     * @return: java.util.List<com.tuanche.directselling.model.LiveDealerAnchor>
     * @author: dudg
     * @date: 2020/4/7 16:02
     */
    @Override
    public List<LiveDealerAnchor> getAnchorLiveDataByCityId(LiveDealerAnchorVo liveDealerAnchorVo) {
        String key = BaseCacheKeys.ANCHOR_LIVE_CITY.setParamValue(liveDealerAnchorVo.getCityId());
        List<LiveDealerAnchor> list = new ArrayList<>();
        try {
            if (redisService.exists(key)) {
                String result = redisService.get(key, String.class);
                list = JSON.parseArray(result, LiveDealerAnchor.class);
            } else {
                list = liveDealerAnchorReadMapper.getAnchorLiveDataByCityId(liveDealerAnchorVo);
                if (CollectionUtil.isNotEmpty(list)) {
                    redisService.set(key, JSON.toJSONString(list), BaseCacheKeys.ANCHOR_LIVE_CITY.getExpire());
                }
            }
        } catch (Exception e) {
            CommonLogUtil.addError("获取百城直播会场城市直播数据error", JSON.toJSONString(liveDealerAnchorVo), e);
        }
        return list;

    }

    /***
     * @description: 获取主播城市列表
     * @param
     * @return: java.util.List<com.tuanche.district.api.dto.output.DistrictOutputBaseDto>
     * @author: dudg
     * @date: 2020/4/7 17:02
     */
    public List<DistrictOutputBaseDto> getAnchorCityList() {

        List<DistrictOutputBaseDto> cityList = new ArrayList<>();
        try {
            String key = BaseCacheKeys.ANCHOR_CITY_LIST.getKey();
            if (redisService.exists(key)) {
                String result = redisService.get(key, String.class);
                cityList = JSON.parseArray(result, DistrictOutputBaseDto.class);
            } else {
                List<LiveDealerAnchor> anchorCityList = liveDealerAnchorReadMapper.getAnchorCityList();
                List<Integer> cityIdList = anchorCityList.stream().map(LiveDealerAnchor::getCityId).collect(Collectors.toList());
                cityList = cityBaseService.getCityListByIds(cityIdList);
                if (CollectionUtil.isNotEmpty(cityList)) {
                    redisService.set(key, JSON.toJSONString(cityList), BaseCacheKeys.ANCHOR_CITY_LIST.getExpire());
                }
            }
        } catch (Exception e) {
            CommonLogUtil.addError("获取百城直播会场城市列表数据error", null, e);
        }

        return cityList;
    }

    public long getRedisExistTime() {
        Date date = new Date();
        Long nowTime = date.getTime();
        //redis过期时间
        Long stopTime = nowTime > DateUtil.formatDateToLong(DateUtil.date2Str(date, dealer_anchor_date_time))
                //十点后访问  DateUtil.formatTimestampToLong(DateUtil.date2Str(DateUtil.addTime(date, 24), dealer_anchor_date_time)) - nowTime
                ? 3600000
                //十点前访问
                : DateUtil.formatTimestampToLong(DateUtil.date2Str(date, dealer_anchor_date_time)) - nowTime;
        return stopTime;
    }

    /**
     * @param dealerId
     * @return com.tuanche.directselling.model.LiveDealerAnchor
     * @Author GongBo
     * @Description 根据经销商ID获取 直播账号信息
     * @Date 18:50 2020/4/28
     **/
    @Override
    public LiveDealerAnchor getLiveDealerAnchorByDealerId(Integer dealerId) {
        LiveDealerAnchor liveDealerAnchor = new LiveDealerAnchor();
        liveDealerAnchor.setDealerId(dealerId);
        return getLiveDealerAnchorByInfo(liveDealerAnchor);
    }
    @Override
    public LiveDealerAnchor getLiveDealerAnchorByInfo(LiveDealerAnchor liveDealerAnchor) {
        LiveDealerAnchor obj = liveDealerAnchorReadMapper.getLiveDealerAnchorByInfo(liveDealerAnchor);
        return obj;
    }

    /**
     * @param liveDealerAnchor
     * @return com.tuanche.directselling.model.LiveDealerAnchor
     * @Author GongBo
     * @Description 保存主播信息
     * @Date 18:50 2020/4/28
     **/
    @Override
    public int saveLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor) {
        liveDealerAnchorWriteMapper.insertSelective(liveDealerAnchor);
        return liveDealerAnchor.getId();
    }

    /**
     * @param liveDealerAnchor
     * @return com.tuanche.directselling.model.LiveDealerAnchor
     * @Author GongBo
     * @Description 修改主播信息
     * @Date 18:50 2020/4/28
     **/
    @Override
    public int updateLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor) {
        return liveDealerAnchorWriteMapper.updateByPrimaryKeySelective(liveDealerAnchor);
    }

    /**
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveDealerPlayback>
     * @Author GongBo
     * @Description 根据经销商ID 获取直播回放
     * @Date 13:34 2020/4/29
     **/
    @Override
    public List<LiveDealerPlaybackDto> getLiveDealerPlayback(Integer dealerId) {
        return liveDealerPlaybackReadMapper.getLiveDealerPlaybackByDealerId(dealerId);
    }

    /**
     * @param liveDealerPlayback
     * @return java.util.List<com.tuanche.directselling.model.LiveDealerPlayback>
     * @Author GongBo
     * @Description 保存经销商直播回放
     * @Date 13:34 2020/4/29
     **/
    @Override
    public int saveLiveDealerPlayback(LiveDealerPlayback liveDealerPlayback) {
        return liveDealerPlaybackWriteMapper.insertSelective(liveDealerPlayback);
    }

    /**
     * @param liveDealerAnchor
     * @return: com.tuanche.directselling.utils.PageResult
     * @description:
     * @author: czy
     * @create: 2020/4/30 16:10
     **/
    @Override
    public PageResult findAnchorList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveDealerAnchor) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "id desc");
        List<LiveDealerAnchor> liveDealerAnchors = liveDealerAnchorReadMapper.searchAnchorList(liveDealerAnchor);
        PageInfo<LiveDealerAnchor> page = new PageInfo<LiveDealerAnchor>(liveDealerAnchors);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveDealerAnchors);
        return pageResult;
    }

    /**
     * @param pageResult
     * @param liveDealerAnchor
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 账号活动列表
     * @Date 11:18 2020/6/11
     **/
    @Override
    public PageResult findAnchorSceneList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveDealerAnchor) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "t1.id desc");
        List<LiveDealerAnchor> liveDealerAnchors = liveDealerAnchorReadMapper.searchAnchorSceneList(liveDealerAnchor);
        if (liveDealerAnchors != null) {
            liveDealerAnchors.forEach(v -> {
                LiveDealerPlaybackDto info =
                        liveDealerPlaybackReadMapper.getInfoByDealerIdAndActivityId(v.getDealerId(), v.getActivityId());
                if (info != null) {
                    v.setFeedIdKeyId(info.getId());
                }
            });
        }
        PageInfo<LiveDealerAnchor> page = new PageInfo<LiveDealerAnchor>(liveDealerAnchors);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveDealerAnchors);
        return pageResult;
    }

    @Override
    public List<LiveDealerAnchor> findAnchorListAll(LiveDealerAnchor liveDealerAnchor) {
        List<LiveDealerAnchor> liveDealerAnchors = liveDealerAnchorReadMapper.searchAnchorList(liveDealerAnchor);
        return liveDealerAnchors;
    }

    /**
     * @param liveDealerAnchor
     * @return java.util.List<com.tuanche.directselling.model.LiveDealerAnchor>
     * @Author GongBo
     * @Description 查询账号活动集合
     * @Date 14:50 2020/6/12
     **/
    @Override
    public List<LiveDealerAnchor> findAnchorSceneListAll(LiveDealerAnchor liveDealerAnchor) {
        List<LiveDealerAnchor> liveDealerAnchors = liveDealerAnchorReadMapper.searchAnchorSceneList(liveDealerAnchor);
        return liveDealerAnchors;
    }

    @Override
    public LiveDealerPlaybackDto getInfoByDealerIdAndActivityId(Integer dealerId, Integer activityId) {
        return liveDealerPlaybackReadMapper.getInfoByDealerIdAndActivityId(dealerId, activityId);
    }

    @Override
    public int updateLiveDealerPlayback(LiveDealerPlayback liveDealerPlayback) {
        return liveDealerPlaybackWriteMapper.updateByPrimaryKeySelective(liveDealerPlayback);
    }

    @Override
    public LiveDealerAnchor getLiveDealerAnchorById(Integer id) {
        return liveDealerAnchorReadMapper.selectByPrimaryKey(id);
    }

    /**
     * @param liveDealerAnchor
     * @return: int
     * @description: 直播账号操作
     * @author: czy
     * @create: 2020/5/25 14:49
     **/
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public int saveAndUpdateLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor) {

        // 废除原账号数据
        LiveDealerAnchor liveDealerAnchorOld = new LiveDealerAnchor();
        liveDealerAnchorOld.setId(liveDealerAnchor.getId());
        liveDealerAnchorOld.setUpdateDt(new Date());
        liveDealerAnchorOld.setDeleteFlag(true);
        liveDealerAnchorOld.setOperatorUserId(liveDealerAnchor.getOperatorUserId());
        int i = liveDealerAnchorWriteMapper.updateByPrimaryKeySelective(liveDealerAnchorOld);

        // 新增一条账号数据
        LiveDealerAnchor liveDealerAnchorNew = liveDealerAnchorReadMapper.selectByPrimaryKey(liveDealerAnchor.getId());
        liveDealerAnchorNew.setTbNick(liveDealerAnchor.getTbNick());
        liveDealerAnchorNew.setStoreName(liveDealerAnchor.getStoreName());
        liveDealerAnchorNew.setProvinceId(liveDealerAnchor.getProvinceId());
        liveDealerAnchorNew.setProvinceName(liveDealerAnchor.getProvinceName());
        liveDealerAnchorNew.setCityId(liveDealerAnchor.getCityId());
        liveDealerAnchorNew.setCityName(liveDealerAnchor.getCityName());
        liveDealerAnchorNew.setAddress(liveDealerAnchor.getAddress());
        liveDealerAnchorNew.setDealerLandline(liveDealerAnchor.getDealerLandline());
        liveDealerAnchorNew.setAnchorId(liveDealerAnchor.getAnchorId());
        liveDealerAnchorNew.setChargeName(liveDealerAnchor.getChargeName());
        liveDealerAnchorNew.setChargePhone(liveDealerAnchor.getChargePhone());
        liveDealerAnchorNew.setEnterpriseAlipay(liveDealerAnchor.getEnterpriseAlipay());
        liveDealerAnchorNew.setEnterpriseAlipayName(liveDealerAnchor.getEnterpriseAlipayName());
        liveDealerAnchorNew.setDeleteFlag(false);
        liveDealerAnchorNew.setCreateDt(new Date());
        liveDealerAnchorNew.setOperatorUserId(liveDealerAnchor.getOperatorUserId());
        liveDealerAnchorNew.setId(null);
        liveDealerAnchorNew.setUpdateDt(null);
        int i1 = liveDealerAnchorWriteMapper.insertSelective(liveDealerAnchorNew);
        return i + i1;
    }

    @Override
    public void updateByPrimaryKeySelective (LiveDealerAnchor liveDealerAnchor) {
        if (liveDealerAnchor==null || liveDealerAnchor.getId()==null || ManeBaseConsoleConstants.checkObjAllFieldsIsNull(liveDealerAnchor, "id,anchorHeat")) return;
        liveDealerAnchorWriteMapper.updateByPrimaryKeySelective(liveDealerAnchor);
    }

    private String getAnchorName(String name) {
        String anchorName = "";
        if (name != null) {
            if (name.indexOf("-") > -1) {
                String[] split = name.split("-");
                if (split.length > 2) {
                    Integer numb = Integer.valueOf(split[2]) + 1;
                    anchorName = split[0] + "-" + split[1] + "-" + numb;
                } else if (split.length == 2) {
                    anchorName = split[0] + "-" + split[1] + "-" + 1;
                } else {
                    anchorName = name;
                }
            }
        }
        return anchorName;
    }

    /**
     * @description: 主播ids转化团车经销商ids
     * @param anchorIds
     * @return: java.util.List<java.lang.Integer>
     * @author: dudg
     * @date: 2020/6/13 10:40
    */
    @Override
    public List<Integer> transformDealerIds(List<Long> anchorIds){
        if(CollectionUtils.isNotEmpty(anchorIds)){
            return liveDealerAnchorReadMapper.transformDealerIds(anchorIds);
        }

        return Lists.newArrayList();
    }
}
