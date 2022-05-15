package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.dto.AfterSaleActivityAccountDto;
import com.tuanche.directselling.model.AfterSaleActivityAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleActivityAccountReadMapper {

    AfterSaleActivityAccount selectByPrimaryKey(Integer id);

    /**
     * 获取账号列表
     * @author HuangHao
     * @CreatTime 2021-12-21 10:26
     * @param accountDto
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityAccount>
     */
    List<AfterSaleActivityAccount> getActivityAccountList(AfterSaleActivityAccountDto accountDto);

    /**
     * 根据活动，经销商id查询
     * @param dealerId
     * @param activityId
     * @return
     */
    AfterSaleActivityAccount selectByActivityId(@Param("activityId") Integer activityId, @Param("dealerId") Integer dealerId);


    List<AfterSaleActivityAccount> selectListByAccount(AfterSaleActivityAccount activityAccount);

}