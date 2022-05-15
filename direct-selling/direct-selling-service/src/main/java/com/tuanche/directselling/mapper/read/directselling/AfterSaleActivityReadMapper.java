package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.vo.AfterSaleActivitySimpleVo;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AfterSaleActivityReadMapper {

    AfterSaleActivity selectByPrimaryKey(Integer id);

    /**
     * 查询活动列表
     * @param afterSaleActivity
     * @return
     */
    List<AfterSaleActivity> selectActivityList(AfterSaleActivityDto afterSaleActivity);

    List<AfterSaleActivity> selectActivityDealerList(AfterSaleActivityVo activityVo);

    List<AfterSaleActivity> selectActivityListByIds(@Param("ids") List<Integer> ids);

    List<AfterSaleActivitySimpleVo> selectActivitySimpleList(AfterSaleActivityDto afterSaleActivity);

    List<AfterSaleActivity> getAfterSaleActivityByOrderCodes(@Param("orderCodes") List<String> orderCodes);

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
     * 获取线下兑换正在进行中的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    List<AfterSaleActivityDto> getOfflineOngoingActivityIdsMap(@Param("day") Integer day);
    /**
     * 获取活动结束前day天的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    @MapKey("mapKey")
    Map<Integer, ResultMapDto> getEndTimeBeforActivityMap(@Param("day") Integer day);

    /**
     * 参与活动的经销商
     *
     * @return
     */
    List<Integer> getAfterSaleDealerIds();

    List<AfterSaleActivityDto> getAfterSaleActivityByDealerId(@Param("dealerId") Integer dealerId);

    /**
     * 开启中的活动
     * @return
     */
    List<AfterSaleActivity> getOngoingActivities(@Param("dateTime") Date dateTime);
}