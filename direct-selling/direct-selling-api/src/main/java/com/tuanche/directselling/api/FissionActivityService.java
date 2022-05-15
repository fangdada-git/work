package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionActivityExtendDto;
import com.tuanche.directselling.dto.FissionDealerDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionActivityExtend;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.*;
import com.tuanche.manubasecenter.dto.WxUserInfoDto;

import java.util.Date;
import java.util.List;

/**
 * @program: direct-selling
 * @description: 裂变活动sevice
 * @author: lvsen
 * @create: 2020-09-22 17:08
 **/
public interface FissionActivityService {
    /**
     * @return com.tuanche.directselling.utils.PageResult
     * @description 查询裂变任务（分页）
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    PageResult findFissionActivityByPage(PageResult<FissionActivity> pageResult, FissionActivity fissionActivity);

    /**
     * @return com.tuanche.directselling.utils.PageResult
     * @description 查询裂变任务列表
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    List<FissionActivityVo> findFissionActivityList(FissionActivity fissionActivity);

    /**
     * @description 新增裂变活动
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    Integer saveFissionActivity(FissionActivity fissionActivity);

    /**
     * @description 新增裂变活动配置
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    Integer saveFissionActivityConfig(FissionActivityExtendVo fissionActivityExtendVo);

    /**
     * @description 保存裂变活动奖励规则
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    Integer saveFissionActivityAwardRule(FissionActivityAwardVo fissionActivityAwardVo);


    /**
     * @param fissionId
     * @return com.tuanche.directselling.model.FissionActivity
     * @description 查询裂变活动
     * @date 2020/9/22 17:11
     * @author lvsen
     */
    FissionActivity getFissionActivityById(Integer fissionId);

    /**
     * @return com.tuanche.directselling.model.FissionActivity
     * @description 查询裂变活动所有配置信息
     * @date 2020/9/22 17:11
     * @author lvsen
     */
    FissionActivityDto getFissionActivityDtoById(Integer fissionId);

    /**
     * 裂变活动奖励规则(1B端 2C端)
     * @param fissionId
     * @param type
     * @return
     */
    List<FissionAwardRule> getAwardRuleListByFissionId(Integer fissionId,Integer type);

    /**
     * @return java.lang.Integer
     * @description 开启裂变任务
     * @date 2020/9/24 17:25
     * @author lvsen
     */
    Integer openFissionActivity(Integer fissionId, Date startTime, Integer optUserId);

    /**
     * @return java.lang.Integer
     * @description 经销商参与/取消参与裂变活动
     * @date 2020/9/24 17:25
     * @author lvsen
     */
    Integer dealerJoinFission(Integer fissionId, Integer dealerId, Integer optUserId, Integer joinFlag);

    /**
     * 首页-活动页数据，根据供应商查活动信息和销售信息
     *
     * @param page     页
     * @param limit    每页条数
     * @param dealerId 经销商ID
     * @param saleId   销售ID
     * @return 活动信息和销售信息分页
     */
    PageResult getActivitySaleByDealerId(int page, int limit, int dealerId, int saleId);

    /**
     * @param fissionActivityDto （FissionActivity+FissionAwardRule）
     * @description: 根据条件返回裂变活动列表
     * @return: java.util.List<com.tuanche.directselling.dto.FissionActivityDto>
     * @author: dudg
     * @date: 2020/9/27 15:35
     */
    List<FissionActivityDto> selectActivityDtoList(FissionActivityDto fissionActivityDto);

    /**
     * 销售最近参加的活动
     *
     * @param saleId 销售ID
     * @return
     */
    List<FissionActivityNameTimeVo> getFissionActivityList(Integer saleId);

    /**
     * 获取正在进行中且是确认(开启)状态的裂变活动ID列表
     * @author HuangHao
     * @CreatTime 2020-09-28 17:07
     * @param
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getOngoingAndConfirmIds();
    /**
     * 获取前一天结束且是确认(开启)状态的裂变活动ID列表
     * @author HuangHao
     * @CreatTime 2020-10-13 17:44
     * @param
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getEndedYesterdayAndConfirmIds();
    /**
     *  获取指定时间startTime到现在结束且是开启状态的活动ID
     * @param name 开始时间
     * @return
     */
    List<Integer> getEndAndConfirmActivityIds(String startTime);

    /**
     * @description: 裂变活动页-详情数据接口
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/12 15:47
     */
    TcResponse getActivityInfoForApi(FissionActivityApiVo activityApiVo);

    /**
     * @description: 裂变活动页-预约直播开播提醒
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/13 17:58
     */
    TcResponse appointLiveForApi(FissionActivityApiVo activityApiVo);

    /**
     * @description: 分享页分享数据接口
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/13 12:05
     */
    TcResponse getShareInfoForApi(FissionActivityApiVo activityApiVo);

    /**
     * @description: 取/递增 裂变活动效果数
     * @param fissionId
     * @param effectNumType 效果数类型 1 浏览数 2 预约数 3 分享数
     * @param isGet
     * @return: java.lang.Integer  null 不存在/异常
     * @author: dudg
     * @date: 2020/10/12 13:40
     */
    Integer getOrIncrEffectNum(Integer fissionId, GlobalConstants.FissionEffectTypeEnum effectNumType, Boolean isGet);

    /**
     * @description: 同步裂变活动氛围真实数据任务
     * @param
     * @return: void
     * @author: dudg
     * @date: 2020/10/14 11:14
     */
    void syncActivityEffectNum();

    /**
     * @param liveId
     * @description: 向预约用户发送微信订阅消息
     * @return: void
     * @author: dudg
     * @date: 2020/10/14 16:08
     */
    void liveStartRemind(Integer liveId);

    /**
     * @description: 根据授权码返回OpenId
     * @param code
     * @return: com.tuanche.manubasecenter.dto.WxUserInfoDto
     * @author: dudg
     * @date: 2020/10/16 18:32
     */
    WxUserInfoDto getWxUserInfoByCode(String code);

    /**
     * 分页查询活动列表
     * @param page 页码
     * @param limit 每页条数
     * @return PageResult
     */
    PageResult findFissionActivityByPage(int page, int limit, String activityName);

    /**
     * 获取经销商参与的活动列表
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     */
    List<FissionActivity> getDealerActivityList(FissionActivityExtend fissionActivityExtend);

    TcResponse activityH5OrMiniProgram();

    /**
     * 获取裂变活动的经销商-分页
     * @author HuangHao
     * @CreatTime 2021-04-14 13:59
     * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     */
    PageResult<FissionActivityExtendDto> getActivityDealerListByPage(FissionActivityExtendDto settlementAccount);
    /**
     * 获取裂变活动的经销商
     * @author HuangHao
     * @CreatTime 2021-04-14 13:59
            * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     */
    List<FissionActivityExtendDto> getActivityDealerList(Integer fissionId);

    List<FissionDealerDto> selectActivityDealerList(Integer fissionId, Integer cityId, Integer dealerId);

    /**
     * 获取经销商的结算状态列表-分页
     * @author HuangHao
     * @CreatTime 2021-04-21 16:00
     * @param fissionId
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     */
    PageResult<FissionActivityExtendDto> getFissionDealerSettlementAccountListByPage(FissionActivityExtendDto settlementAccount);
    /**
     * 获取经销商的结算状态列表-分页
     * @author HuangHao
     * @CreatTime 2021-04-21 16:00
     * @param fissionId
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     */
    List<FissionActivityExtendDto> getFissionDealerSettlementAccountList(Integer fissionId);
}
