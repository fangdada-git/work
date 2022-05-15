package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-29 17:03
 */
@Repository
public interface FissionSaleTaskStatisticsWriteMapper {

    /**
     * 统计表里存在的销售(key=fissionId_saleWxOpenId)-主从同步有延时，所以从从库查
     * @author HuangHao
     * @CreatTime 2020-09-28 17:50
     * @param fissionIds
     * @return java.util.List<java.lang.String>
     */
    @MapKey("mapKey")
    Map<String,String> existingSales(@Param("fissionIds")List<Integer> fissionIds);

    /**
     * 批量新增销售统计
     * @author HuangHao
     * @CreatTime 2020-09-29 17:03
     * @param list
     * @return int
     */
    int batchInsert(@Param("list") List<FissionSaleTaskStatistics> list);
    /**
     * 批量更新裂变活动销售的积分
     * @author HuangHao
     * @CreatTime 2020-09-29 17:03
     * @param list
     * @return int
     */
    int updateBatchTaskIntegral(@Param("list")List<FissionSaleTaskStatistics> list);
}
