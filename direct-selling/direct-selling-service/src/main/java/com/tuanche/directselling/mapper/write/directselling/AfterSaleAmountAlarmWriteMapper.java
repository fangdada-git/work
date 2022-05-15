package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleAmountAlarm;

public interface AfterSaleAmountAlarmWriteMapper {

    int insertSelective(AfterSaleAmountAlarm record);

    int updateByPrimaryKeySelective(AfterSaleAmountAlarm record);

}