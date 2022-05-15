package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionTradeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FissionTradeRecordMapper继承基类
 */
@Repository
public interface FissionTradeRecordReadMapper extends MyBatisBaseDao<FissionTradeRecord, Long> {
    
    List<FissionTradeRecord> getFissionTradeRecordList(FissionTradeRecord record);

    FissionTradeRecord getFissionTradeRecordByInfo(FissionTradeRecord record);

    /**
     * 根据ID批量查
     * @param ids
     * @return
     */
    List<FissionTradeRecord> selectFissionUserPaymentByIds(List<Integer> ids);
}