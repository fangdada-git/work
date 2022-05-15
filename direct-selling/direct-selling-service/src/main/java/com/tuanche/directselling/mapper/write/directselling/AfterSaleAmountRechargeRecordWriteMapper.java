package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleAmountRechargeRecord;

public interface AfterSaleAmountRechargeRecordWriteMapper {

    int insertSelective(AfterSaleAmountRechargeRecord record);

    int updateByPrimaryKeySelective(AfterSaleAmountRechargeRecord record);

}