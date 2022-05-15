package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityGoodsRecordService;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityGoodsRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionActivityGoodsRecordWriteMapper;
import com.tuanche.directselling.model.FissionActivityGoodsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FissionActivityGoodsRecordServiceImpl implements FissionActivityGoodsRecordService {

    @Autowired
    private FissionActivityGoodsRecordReadMapper FissionActivityGoodsRecordReadMapper;
    @Autowired
    private FissionActivityGoodsRecordWriteMapper FissionActivityGoodsRecordWriteMapper;

    @Override
    public List<FissionActivityGoodsRecord> getFissionActivityGoodsRecordList (FissionActivityGoodsRecord activityGoodsRecord) {
        return FissionActivityGoodsRecordReadMapper.getFissionActivityGoodsRecordList(activityGoodsRecord);
    }
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void editActivityGoodsRecord (Integer fissionId, String goodsIds) {
        if (fissionId !=null) {
            FissionActivityGoodsRecordWriteMapper.deleteActivityGoodsRecordByFissionIdAndGoodsId(fissionId, null);
            if (!StringUtil.isEmpty(goodsIds)) {
                List<FissionActivityGoodsRecord> activityGoodsRecordList =Arrays.asList(goodsIds.split(",")).stream().map(v->{
                    FissionActivityGoodsRecord dto = new FissionActivityGoodsRecord();
                    dto.setFissionId(fissionId);
                    dto.setGoodsId(Integer.valueOf(v));
                    return dto;
                }).collect(Collectors.toList());
                FissionActivityGoodsRecordWriteMapper.insertSelectiveList(activityGoodsRecordList);
            }
        }
    }
    @Override
    public int addActivityGoodsRecord (Integer fissionId, String goodsIds) {
        int n=0;
        if (fissionId !=null) {
            if (!StringUtil.isEmpty(goodsIds)) {
                List<FissionActivityGoodsRecord> activityGoodsRecordList =Arrays.asList(goodsIds.split(",")).stream().map(v->{
                    FissionActivityGoodsRecord dto = new FissionActivityGoodsRecord();
                    dto.setFissionId(fissionId);
                    dto.setGoodsId(Integer.valueOf(v));
                    return dto;
                }).collect(Collectors.toList());
                n = FissionActivityGoodsRecordWriteMapper.insertSelectiveList(activityGoodsRecordList);
            }
        }
        return n;
    }
    @Override
    public int delActivityGoodsRecord (Integer fissionId, Integer goodsId) {
        int n=0;
        if (fissionId !=null) {
            n = FissionActivityGoodsRecordWriteMapper.deleteActivityGoodsRecordByFissionIdAndGoodsId(fissionId, goodsId);
        }
        return n;
    }




}
