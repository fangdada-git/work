package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionActivityIncomeDto;
import com.tuanche.directselling.dto.FissionActivityNameDto;
import com.tuanche.directselling.dto.FissionStatisticBigDecimalDto;
import com.tuanche.directselling.dto.FissionStatisticIntDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordStatDto;
import com.tuanche.directselling.model.FissionActivity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/28 16:39
 **/
public interface FissionStatisticsReadMapper {

    /**
     * 奖金池总金额
     *
     * @return
     */
    FissionStatisticBigDecimalDto selectPrizePool(@Param("ruleType") Integer ruleType, @Param("fissionId") Integer fissionId);
    /**
     * 财务已审核 或 已打款金额
     * @param fissionId
     * @return
     */
    FissionStatisticBigDecimalDto selectPrizeActualByFissionId(@Param("fissionId") Integer fissionId, @Param("withdrawalBeginTime") Date withdrawalBeginTime, @Param("withdrawalEndTime") Date withdrawalEndTime);

    /**
     * 积分总数
     *
     * @return
     */
    FissionStatisticIntDto selectTotalIntegral(@Param("fissionId") Integer fissionId);

    /**
     * 完成任务 or 参与活动 的销售个数
     *
     * @param whetherCompleteTask
     * @return
     */
    FissionStatisticIntDto selectSaleCount(@Param("whetherCompleteTask") Integer whetherCompleteTask, @Param("fissionId") Integer fissionId);

    /**
     * 最低、最高 预计、实际收入
     *
     * @return
     */
    FissionActivityIncomeDto selectMinMaxIncome(@Param("fissionId") Integer fissionId);
    /**
     * 未生成数据概览的活动
     *
     * @return
     */
    List<FissionActivity> selectStatNotGenerate(@Param("fissionId") Integer fissionId, @Param("statGenerate") int statGenerate, @Param("limit") Integer limit);


    /**
     * 活动当前发放金额
     *
     * @param fissionId
     * @return
     */
    FissionStatisticBigDecimalDto selectIssued(@Param("fissionId") Integer fissionId);

    /**
     * 获得奖金用户数量
     *
     * @param fissionId
     * @return
     */
    FissionStatisticIntDto selectCustomerCount(@Param("fissionId") Integer fissionId);

}
