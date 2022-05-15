package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionActivityGoodsRecord;

import java.util.List;

public interface FissionActivityGoodsRecordReadMapper extends MyBatisBaseDao<FissionActivityGoodsRecord, Integer>  {

    List<FissionActivityGoodsRecord> getFissionActivityGoodsRecordList(FissionActivityGoodsRecord activityGoodsRecord);
}