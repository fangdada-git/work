package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.UserRewardReconciliationDto;
import com.tuanche.directselling.model.FissionUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 15:50
 */
@Repository
public interface FissionUserReadMapper {

    /**
     * 用户是否存在于裂变活动中
     * @author HuangHao
     * @CreatTime 2020-09-23 15:51
     * @param userWxOpenId
     * @param fissionId
     * @return java.lang.Integer
     */
    Integer userExistInFissionId(@Param("userWxOpenId")String userWxOpenId, @Param("fissionId")Integer fissionId);
    /**
     * 获取裂变活动用户
     * @author HuangHao
     * @CreatTime 2020-09-23 15:51
     * @param userWxOpenId
     * @param fissionId
     * @return java.lang.Integer
     */
    FissionUser getFissionUser(FissionUser fissionUser);

    List<FissionUser> getUserWxInfo(FissionUser fissionUser);

    /**
     * 取用户昵称
     * @param fissionUser
     * @return
     */
    List<FissionUser> selectNickNameByUnionId(FissionUser fissionUser);

    /**
     * 查询有奖励的用户
     *
     * @param userRewardReconciliationDto 裂变活动ID,提现开始时间,提现结束时间
     * @return
     */
    List<FissionUser> selectFissionRewardUser(UserRewardReconciliationDto userRewardReconciliationDto);
    
}
