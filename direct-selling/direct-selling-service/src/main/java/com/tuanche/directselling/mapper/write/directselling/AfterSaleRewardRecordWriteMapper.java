package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleRewardRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-23 15:59
 */
public interface AfterSaleRewardRecordWriteMapper {

    /**
     * 检测是否发放了某一类型的奖励
     * @author HuangHao
     * @CreatTime 2021-07-23 16:02
     * @param afterSaleRewardRecord
     * @return int
     */
    AfterSaleRewardRecord getRewardRecord(AfterSaleRewardRecord afterSaleRewardRecord);

    /**
     * 根据ID获取数据
     * @author HuangHao
     * @CreatTime 2021-08-02 16:19
     * @param id
     * @return com.tuanche.directselling.model.AfterSaleRewardRecord
     */
    AfterSaleRewardRecord getRewardRecordById(@Param("id") Integer id);

    /**
     * 新增
     * @author HuangHao
     * @param list
     * @return int
     */
    int insert(AfterSaleRewardRecord afterSaleRewardRecord);
    /**
     * 更新
     * @author HuangHao
     * @param list
     * @return int
     */
    int updateById(AfterSaleRewardRecord afterSaleRewardRecord);
    /**
     * 批量新增
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchInsert(@Param("list") List<AfterSaleRewardRecord> list);
    /**
     * 批量更新
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchUpdate(@Param("list") List<AfterSaleRewardRecord> list);
}
