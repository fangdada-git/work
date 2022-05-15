package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;

public interface FissionGoodsOrderRecordWriteMapper extends MyBatisBaseDao<FissionGoodsOrderRecord, Integer> {
    int updateFissionGoodsOrderRecordByOrderNo(FissionGoodsOrderRecord fissionGoodsOrderRecord);
}