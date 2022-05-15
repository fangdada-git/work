package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-28 13:58
 */
public interface GiftRefuelingcardPeriodsCommodityWriteMapper {

    /**
     * 根据油卡配置表ID删除
     * @author HuangHao
     * @CreatTime 2020-05-28 14:00
     * @param rcPeriodsId
     * @return int
     */
    int deleteByRcPeriodsId(Integer rcPeriodsId);
    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2020-05-28 14:01
     * @param rcPeriodsId
     * @return int
     */
    int batchInsert(@Param("commodityList") List<GiftRefuelingcardPeriodsCommodity> commodityList);
}
