package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleAmountRechargeRecordService;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAmountRechargeRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleAmountRechargeRecordWriteMapper;
import com.tuanche.directselling.model.AfterSaleAmountRechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AfterSaleAmountRechargeRecordServiceImpl implements AfterSaleAmountRechargeRecordService {

    @Autowired
    private AfterSaleAmountRechargeRecordReadMapper afterSaleAmountRechargeRecordReadMapper;

    @Autowired
    private AfterSaleAmountRechargeRecordWriteMapper afterSaleAmountRechargeRecordWriteMapper;

    @Override
    public int addAfterSaleAmountRechargeRecord (AfterSaleAmountRechargeRecord afterSaleAmountRechargeRecord) {
        afterSaleAmountRechargeRecordWriteMapper.insertSelective(afterSaleAmountRechargeRecord);
        return afterSaleAmountRechargeRecord.getId()==null?0:afterSaleAmountRechargeRecord.getId();
    }

}
