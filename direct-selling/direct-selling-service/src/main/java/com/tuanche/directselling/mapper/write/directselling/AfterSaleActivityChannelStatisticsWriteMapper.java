package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-10 17:23
 */
public interface AfterSaleActivityChannelStatisticsWriteMapper {

    int insertBatch(List<AfterSaleActivityChannelStatistics> list);
    int updateBatch(List<AfterSaleActivityChannelStatistics> list);
}
