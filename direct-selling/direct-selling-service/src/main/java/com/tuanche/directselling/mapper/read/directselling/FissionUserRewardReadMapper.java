package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionUserReward;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * FissionUserRewardMapper继承基类
 */
public interface FissionUserRewardReadMapper extends MyBatisBaseDao<FissionUserReward, Integer> {

    /**
     * C端用户可打款数据
     *
     * @return
     */
    List<FissionUserReward> selectUserPayableList();

    /**
     * C端用户奖励列表
     *
     * @param fissionUserReward
     * @return
     */
    List<FissionUserRewardDto> selectUserRewardListByFissionId(FissionUserReward fissionUserReward);

    /**
     * 获取裂变用户某一任务奖励及裂变任务已分发的总奖金额
     *
     * @param fissionUserReward
     * @return com.tuanche.directselling.dto.FissionUserRewardDto
     * @author HuangHao
     * @CreatTime 2020-10-10 17:09
     */
    FissionUserRewardDto getRewardAmountAndTotalRewarByFissionId(FissionUserReward fissionUserReward);

    /**
     * 当前活动下奖励金总额
     *
     * @param fissionUserReward
     * @return
     */
    BigDecimal selectTotalAmountByFissionId(FissionUserReward fissionUserReward);

    /**
     * 活动的奖励金额汇总数据
     *
     * @param fissionId      活动ID
     * @param userWxUnionIds 用户wxUnionIds
     * @return List<FissionUserRewardDto>
     */
    List<FissionUserRewardDto> selectFissionUserRewardByWxUnionIds(@Param("fissionId") Integer fissionId, @Param("userWxUnionIds") List<String> userWxUnionIds,
                                                                   @Param("payBeginTime") Date payBeginTime, @Param("payEndTime") Date payEndTime);

}