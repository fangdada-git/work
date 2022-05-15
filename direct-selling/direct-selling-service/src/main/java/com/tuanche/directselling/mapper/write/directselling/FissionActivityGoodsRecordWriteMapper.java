package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionActivityGoodsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FissionActivityGoodsRecordWriteMapper extends MyBatisBaseDao<FissionActivityGoodsRecord, Integer>  {

    int insertSelectiveList(@Param("activityGoodsRecordList") List<FissionActivityGoodsRecord> activityGoodsRecordList);

    int deleteActivityGoodsRecordByFissionIdAndGoodsId(@Param("fissionId")Integer fissionId, @Param("goodsId")Integer goodsId);
}