package com.tuanche.directselling.api;

import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneCityBrand;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LivePeriodsDataDetailVo;
import com.tuanche.directselling.vo.LiveSceneBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author GongBo
 * @Description 团车直卖-场次活动相关服务
 * @Date 17:01 2020/3/4
 **/
public interface LiveSceneService {

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动数据查询
     * @Date 2020年3月5日09:45:25
     **/
    PageResult findSceneList(PageResult<LiveSceneDto> pageResult, LiveSceneVo liveSceneVo);


    /**
     * @return PageResult
     * @Description 团车直卖-场次活动数据查询
     **/
    PageResult findSceneListPage(PageResult<LiveSceneDto> pageResult, LiveSceneVo liveSceneVo);

    /**
     * @param liveScene
     * @return int
     * @Author GongBo
     * @Description 团车直卖-新增场次活动
     * @Date 18:16 2020/3/5
     **/
    void insertByScene(LiveScene liveScene) throws Exception;

    /**
     * @param sceneId
     * @return com.tuanche.directselling.model.LiveScene
     * @Author GongBo
     * @Description 团车直卖-根据场次活动ID获取场次活动数据
     * @Date 11:19 2020/3/6
     **/
    LiveScene getLiveSceneById(Integer sceneId);

    /**
     * 根据场次id 查场次信息 包括城市和品牌
     * @param sceneId
     * @return
     */
    LiveSceneCityBrandDto getLiveSceneCityBrandById(Integer sceneId);

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     * @Author GongBo
     * @Description 团车直卖-获取场次活动集合
     * @Date 11:53 2020/3/6
     **/
    List<LiveScene> getLiveSceneList(LiveSceneVo liveSceneVo);


    /**
     * 所有场次活动（未开始，进行中）
     * @return
     */
    List<LiveScene> getLiveSceneNotFinished();

    /**
     * @param liveScene
     * @return void
     * @Author GongBo
     * @Description 团车直卖-更新场次活动
     * @Date 11:27 2020/3/6
     **/
    void updateByScene(LiveScene liveScene) throws Exception;

    /**
     * @param msg
     * @return void
     * @Author GongBo
     * @Description 团车直卖-场次活动配置导播
     * @Date 17:43 2020/3/9
     **/
    void setDirectorSendKafkaNotice(String msg);

    /**
     * @param
     * @return java.lang.String
     * @Author GongBo
     * @Description 后门服务-用于场次活动创建同步直播平台-发生异常的数据处理
     * @Date 17:28 2020/3/12
     **/
    String syncSceneToBroadcast(LiveScene liveScene);

    String createSceneHostCode(LiveScene liveScene);

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDto>
     * @Author GongBo
     * @Description 团车直卖对外api-获取场次活动数据
     * @Date 17:12 2020/3/18
     **/
    List<LiveSceneDto> getLiveSceneDtoList(LiveSceneVo liveSceneVo);

    /**
     * 查活动名称
     * @param liveSceneVo
     * @return
     */
    List<LiveSceneDto> selectLiveSceneTitleList(LiveSceneVo liveSceneVo);

    List<LiveSceneDto> getLiveSceneDtoAllList(LiveSceneVo liveSceneVo);

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerDto>
     * @Author GongBo
     * @Description 团车直卖api-根据场次活动获取经销商数据
     * @Date 17:12 2020/3/18
     **/
    List<LiveSceneDealerDto> getLiveSceneDealerList(LiveSceneVo liveSceneVo);

    /**
     * @param liveSceneVo
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     * @Author GongBo
     * @Description 团车直卖api-获取场次活动关联所有城市
     * @Date 2020年4月2日11:51:00
     **/
    List<LiveSceneCityDto> getLiveSceneCityDtoList(LiveSceneVo liveSceneVo);

    /**
     * 根据大场次获取预热正式时间
     * 架构用到
     */
    Map<String, Object> getLiveInfoByPeriodsId(Integer periodsId);

    /**
     * @param
     * @description: 返回最近一场场次id
     * @return: com.tuanche.directselling.model.LiveScene
     * @author: dudg
     * @date: 2020/5/6 14:10
     */
    LiveScene getLastLiveScenePeriodsId();

    /**
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDataReportDto>
     * @description 根据大场次查询场次下线索统计
     * @date 2020/6/30 18:24
     * @author lvsen
     */
    PageResult getSceneDataReportPeriodsId(PageResult<LiveSceneDataReportDto> pageResult, LiveSceneVo liveSceneVo);

    /**
     * 查询场次下的城市
     *
     * @param periodsId
     * @return
     */
    List<LiveSceneCityDto> getPeriodsCityList(Integer periodsId);

    /**
     * 查询场次下的品牌
     *
     * @param periodsId
     * @rrn
     */
    List<LiveSceneBrandVo> getPeriodsBrandList(Integer periodsId);

    /**
     * 查询经销商7天数据
     *
     * @return
     */
    List<LivePeriodsDataDetailVo> getPeriodsDealerDataDetail(Integer periodsId, Integer dealerId);

    /**
     * @param pageResult
     * @param periodDealerDto
     * @description: 获取场次活动经销商列表
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/6/23 17:54
     */
    PageResult<LivePeriodDealerDto> getPeriodDealerList(PageResult<LivePeriodDealerDto> pageResult, LivePeriodDealerDto periodDealerDto);

    /**
     * 添加场次
     *
     * @param liveSceneVo
     */
    ResultDto addScene(Integer createUserId, String createUserName, LiveSceneVo liveSceneVo) throws Exception;


    /**
     * 修改场次
     *
     * @param liveSceneVo
     */
    ResultDto updateScene(Integer createUserId, String createUserName, LiveSceneVo liveSceneVo) throws Exception;

    /**
     * 更新CityBrand
     * @param ids 传入的city ids or brand ids
     * @param names 传入的city names or brand names
     * @param idsDB 数据库中的city ids or brand ids
     * @param sceneId 场次id
     * @param dataType 0 city 1 brand
     */
    void updateCityBrand(String[] ids, String[] names, List<Integer> idsDB, Integer sceneId, Integer dataType);

    /**
     * 获取场次活动的城市、品牌
     *
     * @param sceneId
     * @return
     */
    List<LiveSceneCityBrand> selectLiveSceneCityBrandBySceneId(Integer sceneId);

    /** 
    * @Description: 返回小场次列表（返回数据-仅考虑场次本身数据，不按城市区分）
    * @param liveSceneVo
    * @return: java.util.List<com.tuanche.directselling.model.LiveScene>
    * @Author: zhangys
    * @Date: 2021/1/11 14:52
    */
    List<LiveScene> getLiveSceneListByLiveScene(LiveSceneVo liveSceneVo);

    /**
     * 获取裂变经销商列表
     * @author HuangHao
     * @CreatTime 2021-03-12 15:52
     * @param pageResult
     * @param periodDealerDto
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.LivePeriodDealerDto>
     */
    PageResult<LivePeriodDealerDto> getFissionDealerList(PageResult<LivePeriodDealerDto> pageResult, LivePeriodDealerDto periodDealerDto);

    /**
     * 获取经销商参加的小场次列表
     * @author HuangHao
     * @CreatTime 2021-04-20 17:52
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     */
    List<LiveScene> getDealerSceneList(Integer dealerId);
}
