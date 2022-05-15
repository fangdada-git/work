package com.tuanche.directselling.api;


import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.dto.AfterSaleActivityDealerDto;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleRewardSlideshowDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleActivityCoupon;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.AfterSaleActivitySimpleVo;
import com.tuanche.directselling.vo.AfterSaleActivityVo;

import java.util.List;
import java.util.Map;

/**
 * @program: direct-selling
 * @description: 售后服务活动service
 * @author: lvsen
 * @create: 2021-07-20 10:23
 **/
public interface AfterSaleActivityService {

    /**
     * 前端获取售后服务活动接口
     * @param activityVo
     * @return
     */
    TcResponse getAfterSaleActivityInfoForApi(AfterSaleActivityVo activityVo);

    /**
     * 前端获取售后服务经销商列表接口
     * @param activityVo
     * @return
     */
    TcResponse getAfterSaleActivityDealerList(AfterSaleActivityVo activityVo);

    /**
     * 前端获取售后服务活动列表接口
     * @param activityVo
     * @return
     */
    TcResponse getAfterSaleActivityList(AfterSaleActivityVo activityVo);

    /**
     * 根据id获取售后服务活动
     * @param id
     * @return
     */
    AfterSaleActivityDto getAfterSaleActivityDtoById(Integer id);

    /**
     * 根据id获取售后服务活动
     * @param id
     * @return
     */
    AfterSaleActivity getAfterSaleActivityById(Integer id);


    /**
     * 根据orderCodes获取售后服务活动
     * @param orderCodes
     * @return
     */
    List<AfterSaleActivity> getAfterSaleActivityByOrderCodes(List<String> orderCodes);

    /**
     * @return com.tuanche.directselling.utils.PageResult
     * @description 查询售后特卖任务（分页）
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    PageResult findAfterSaleActivityByPage(PageResult<AfterSaleActivityDto> pageResult, AfterSaleActivity afterSaleActivity);

    /**
     * 开启售后特卖活动
     * @param activityDto
     */
    void openAfterSaleActivity(AfterSaleActivityDto activityDto, Integer optUserId);

    /**
     * @description 保存售后特卖活动
     * @param [afterSaleActivity]
     * @return int
     * @date 2021/7/22 14:31
     * @author lvsen
     */
    int saveAfterSaleActivity(AfterSaleActivity afterSaleActivity);

    /**
     * 抵用券列表
     * @param pageResult
     * @param afterSaleActivityCoupon
     * @return
     */
    PageResult findAfterSaleCouponListByPage(PageResult<AfterSaleActivityCoupon> pageResult, AfterSaleActivityCoupon afterSaleActivityCoupon);

    List<AfterSaleActivity> selectActivityList(AfterSaleActivity afterSaleActivity);

    /**
     * 获取经销商的活动列表-已开启
     * @author HuangHao
     * @CreatTime 2021-09-28 15:07
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    List<AfterSaleActivity> getDealerActivityList(Integer dealerId);
    /**
     * 单个抵用券
     * @param id
     * @return
     */
    AfterSaleActivityCoupon findAfterSaleActivityCouponById(Integer id);

    int updateAfterSaleActivityCouponById(Integer id);

    /**
     * 保存抵用券
     * @param activityCoupon
     * @return
     */
    int saveActivityCoupon(AfterSaleActivityCoupon activityCoupon);

    /**
     *  生成海报图
     * @param afterSaleUserShare
     * @return
     */
    String createPostUrl(String shareUrl,String posterUrl);

    Map<Integer, AfterSaleActivity> getAfterSaleActivityMap(List<Integer> activityIds);

    /**
     * 简单版特卖活动列表
     * @param afterSaleActivity
     * @return
     */
    List<AfterSaleActivitySimpleVo> getSimpleActivityList(AfterSaleActivityDto afterSaleActivity);

    /**
     * 获取缓存的活动信息-只缓存基础且是已开启的
     * @author HuangHao
     * @CreatTime 2021-08-11 16:12
     * @param id
     * @return com.tuanche.directselling.dto.AfterSaleActivityDto
     */
    AfterSaleActivityDto getCacheAfterSaleActivityDtoById(Integer id);

    /**
     * 获取进行中的活动ID列表,活动开始时间到线下活动结束时间的数据
     *
     * @param datatime
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2021-08-19 14:30
     */
    List<Integer> getOngoingActivityIds(String datatime);

    /**
     * 获取进行中的活动ID列表,活动开始时间到线下活动结束时间的数据
     *
     * @param datatime
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2021-08-19 14:30
     */
    List<Integer> getOngoingActivityIds(AfterSaleActivityDto afterSaleActivityDto);
    /**
     * 参加售后特卖活动的经销商
     *
     * @return
     */
    List<AfterSaleActivityDealerDto> getAfterSaleDealer();

    /**
     * 根据经销商ID获取活动
     *
     * @param dealerId
     * @return
     */
    List<AfterSaleActivityDto> getAfterSaleActivityByDealerId(Integer dealerId);

    /**
     * 修改活动
     *
     * @param activityDto
     * @return
     */
    int updateAfterSaleActivityById(AfterSaleActivityDto activityDto);

    /**
     * 获取线下兑换正在进行中的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    List<AfterSaleActivityDto> getOfflineOngoingActivityIdsMap(Integer day);

    /**
     * 获取活动结束前day天的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    Map<Integer, ResultMapDto> getEndTimeBeforActivityMap(Integer day);

    /**
     * 奖励轮播数据
     *
     * @param activityId
     * @return
     */
    List<AfterSaleRewardSlideshowDto> getSaleRewardSlideshow(Integer activityId);

    /**
     * 清除活动缓存
     * @author HuangHao
     * @CreatTime 2021-12-02 15:47
     * @param id
     * @return com.tuanche.commons.util.ResultDto
     */
    ResultDto delActivityCache(Integer id);

    /**
     * 开启关闭分账
     * @param activityDto
     *
     */
    void openCloseSubAccount(AfterSaleActivityDto activityDto);
}
