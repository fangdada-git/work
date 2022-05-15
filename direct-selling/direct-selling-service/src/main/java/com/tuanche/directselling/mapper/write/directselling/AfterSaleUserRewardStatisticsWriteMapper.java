package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleRewardRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleUserRewardStatisticsWriteMapper {
    /**
     * 批量新增
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchInsert(@Param("list") List<AfterSaleUserRewardStatistics> list);
    /**
     * 批量更新
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchupdate(@Param("list") List<AfterSaleUserRewardStatistics> list);

    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleUserRewardStatistics record);

    int insertSelective(AfterSaleUserRewardStatistics record);

    int updateByPrimaryKeySelective(AfterSaleUserRewardStatistics record);

    int updateByPrimaryKey(AfterSaleUserRewardStatistics record);
}