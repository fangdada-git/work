package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleActivityStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleActivityStatisticsWriteMapper {

    int insertBatch(@Param("list") List<AfterSaleActivityStatistics> list);

    int updateBatch(@Param("list") List<AfterSaleActivityStatistics> list);

}