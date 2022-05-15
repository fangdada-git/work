package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionUserReward;
import com.tuanche.directselling.pojo.UserRewardPayStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FissionUserRewardMapper继承基类
 */
public interface FissionUserRewardWriteMapper extends MyBatisBaseDao<FissionUserReward, Integer> {

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
     * 更新用户奖励打款标识
     * @param payList
     * @return
     */
    int updateUserPayStatus(@Param("payList") List<UserRewardPayStatusVo> payList);
}