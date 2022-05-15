package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionActivityGoodsRecord;

import java.util.List;

public interface FissionActivityGoodsRecordService {

    List<FissionActivityGoodsRecord> getFissionActivityGoodsRecordList (FissionActivityGoodsRecord activityGoodsRecord);

    void editActivityGoodsRecord (Integer fissionId, String goodsIds);

    int delActivityGoodsRecord(Integer fissionId, Integer goodsId);

    int addActivityGoodsRecord(Integer fissionId, String goodsIds);
}
