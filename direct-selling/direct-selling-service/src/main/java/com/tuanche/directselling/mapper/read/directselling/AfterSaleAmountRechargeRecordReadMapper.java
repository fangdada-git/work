package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleAmountRechargeRecord;

public interface AfterSaleAmountRechargeRecordReadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleAmountRechargeRecord record);

    int insertSelective(AfterSaleAmountRechargeRecord record);

    AfterSaleAmountRechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterSaleAmountRechargeRecord record);

    int updateByPrimaryKey(AfterSaleAmountRechargeRecord record);
}