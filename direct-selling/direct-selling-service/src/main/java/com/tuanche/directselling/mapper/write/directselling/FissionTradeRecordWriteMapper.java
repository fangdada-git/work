package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionTradeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FissionTradeRecordMapper继承基类
 */
@Repository
public interface FissionTradeRecordWriteMapper extends MyBatisBaseDao<FissionTradeRecord, Long> {
    List<FissionTradeRecord> getFissionTradeRecordListByWrite(FissionTradeRecord record);
}