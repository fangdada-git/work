package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.model.FissionUserReward;
import com.tuanche.directselling.utils.PageResult;

import java.math.BigDecimal;

public interface FissionUserRewardService {

    /**
     * @description: C端用户奖励打款(24h以内的奖励数据)
     * @param
     * @return: boolean
     * @author: dudg
     * @date: 2020/9/29 17:40
     */
    boolean rewardPayToUser();

    /**
     * @description: C端用户奖励列表
     * @param pageResult
     * @param userRewardDto
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/9/29 18:21
     */
    PageResult selectUserRewardListByFissionId(PageResult<FissionUserRewardDto> pageResult, FissionUserRewardDto userRewardDto);

    /**
     * @description: 获取当前活动C端奖励金总额
     * @param fissionUserReward
     * @return: java.math.BigDecimal
     * @author: dudg
     * @date: 2020/10/10 12:11
    */
    BigDecimal selectTotalAmountByFissionId(FissionUserReward fissionUserReward);
}
