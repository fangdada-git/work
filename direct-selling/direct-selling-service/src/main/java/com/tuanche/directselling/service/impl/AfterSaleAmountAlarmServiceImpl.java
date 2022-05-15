package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleAmountAlarmService;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAmountAlarmReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleAmountAlarmWriteMapper;
import com.tuanche.directselling.model.AfterSaleAmountAlarm;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AfterSaleAmountAlarmServiceImpl implements AfterSaleAmountAlarmService {

    @Autowired
    private AfterSaleAmountAlarmReadMapper afterSaleAmountAlarmReadMapper;

    @Autowired
    private AfterSaleAmountAlarmWriteMapper afterSaleAmountAlarmWriteMapper;

    @Override
    public AfterSaleAmountAlarm getAfterSaleAmountAlarm () {
        return afterSaleAmountAlarmReadMapper.getAfterSaleAmountAlarm();
    }

    @Override
    public int addAfterSaleAmountAlarm (AfterSaleAmountAlarm afterSaleAmountAlarm) {
        afterSaleAmountAlarmWriteMapper.insertSelective(afterSaleAmountAlarm);
        return afterSaleAmountAlarm.getId()==null?0:afterSaleAmountAlarm.getId();
    }

    @Override
    public int updateAfterSaleAmountAlarm (AfterSaleAmountAlarm afterSaleAmountAlarm) {
        return afterSaleAmountAlarmWriteMapper.updateByPrimaryKeySelective(afterSaleAmountAlarm);
    }



}