package com.tuanche.directselling.api;

import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.dto.AfterSaleActivityAccountDto;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleActivityAccount;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @program: direct-selling
 * @description: 分账信息服务
 * @author: lvsen
 * @create: 2021-12-15 13:40
 **/
public interface AfterSaleActivityAccountService {

    /**
     * 获取活动经销商分账信息
     * @param activityId
     * @return
     */
    AfterSaleActivityAccount getAfterSaleActivityAccountByActivityId(Integer activityId);

    /**
     * 获取活动经销商分账信息
     * @param activityId
     * @return
     */
    AfterSaleActivityAccount getAfterSaleActivityAccountById(Integer id);

    /**
     * 保存并新建分账账号
     * @param afterSaleActivityAccount
     * @param optUserId
     * @return
     */
    ResultDto saveAndVerifyAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId);

    AfterSaleActivityAccount saveAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId);

    /**
     * 查询经销商分账信息
     * @param activityAccount
     * @return
     */
    List<AfterSaleActivityAccount> getAfterSaleActivityAccount(AfterSaleActivityAccount activityAccount);
    /**
     * 获取账号列表
     * @author HuangHao
     * @CreatTime 2021-12-21 10:26
     * @param accountDto
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityAccount>
     */
    List<AfterSaleActivityAccount> getActivityAccountList(AfterSaleActivityAccountDto accountDto);
    /**
     * 根据活动ID获取获取账号信息
     * @author HuangHao
     * @CreatTime 2021-12-21 10:51
     * @param accountDto
     * @return java.util.Map<java.lang.Integer,com.tuanche.directselling.model.AfterSaleActivityAccount>
     */
    Map<Integer,AfterSaleActivityAccount> getActivityAccountMapByActivityIds(Set<Integer> activityIds);

    /**
     * 更新经销商分账信息
     * @param activityAccount
     * @return
     */
    AfterSaleActivityAccount updateAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId);

    /**
     * 验证分账
     * @param afterSaleActivityAccount
     * @param afterSaleActivity
     * @param optUser
     * @return
     */
    ResultDto verfiyProfitsharing(AfterSaleActivityAccount afterSaleActivityAccount, AfterSaleActivity afterSaleActivity, Integer optUser);
}
