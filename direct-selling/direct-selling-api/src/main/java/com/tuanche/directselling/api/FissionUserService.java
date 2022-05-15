package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.FissionUserRewardReconciliationDto;
import com.tuanche.directselling.dto.UserRewardReconciliationDto;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 16:11
 */
public interface FissionUserService {

    /**
     * 新增裂变活动用户
     * @author HuangHao
     * @CreatTime 2020-09-24 18:22
     * @param fissionUser
     * @return int
     */
    int addFissionUser(FissionUser fissionUser);
    /**
     * 先从缓存取，没有再从写库取
     * @author HuangHao
     * @CreatTime 2020-12-28 17:22
     * @param fissionId
     * @param userWxOpenId
     * @return com.tuanche.directselling.model.FissionUser
     */
    FissionUser getFissionUser(FissionUser fissionUser);

    List<FissionUser> getUserWxInfo (FissionUser fissionUser);

    /**
     * C端用户奖励金对账数据 for ebs-console
     * @param fissionId 裂变活动ID
     * @return List<FissionUserRewardReconciliationDto>
     */
    List<FissionUserRewardReconciliationDto> getUserRewardReconciliationDtoList(UserRewardReconciliationDto userRewardReconciliationDto);

    /**
     * C端用户奖励金对账数据 for ebs-console
     * @param page 页
     * @param limit 每页条数
     * @param fissionId 裂变活动ID
     * @return PageResult
     */
    PageResult getUserRewardReconciliationDtoPage(UserRewardReconciliationDto userRewardReconciliationDto);
}
