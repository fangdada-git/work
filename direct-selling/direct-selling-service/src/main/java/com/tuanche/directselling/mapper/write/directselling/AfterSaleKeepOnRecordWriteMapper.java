package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-05 10:54
 */
public interface AfterSaleKeepOnRecordWriteMapper {

    /**
     * 批量新增
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchInsert(@Param("list") List<AfterSaleKeepOnRecord> list);

    int updateById(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 根据活动ID删除代理人
     *
     * @param activityId
     * @return int
     * @author HuangHao
     * @CreatTime 2021-09-07 15:41
     */
    int deleteByActivityId(@Param("activityId") Integer activityId);

    int deleteByActivityIdAndUserType(@Param("activityId") Integer activityId, @Param("userType") Integer userType);

    /**
     * 根据ID和经销商ID删除数据
     *
     * @param id
     * @param dealerId
     * @return
     */
    int deleteByIdAndDealerId(@Param("id") Integer id, @Param("dealerId") Integer dealerId);

    /**
     * 去重 删掉流失用户
     *
     * @param activityId
     */
    void duplicateRemoval(@Param("activityId") Integer activityId);

    void syncAfterSaleBrand(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    void syncAfterSaleBrandAndCarSeries(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    void syncAfterCarSeries(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    void updateSyncStatusById(AfterSaleKeepOnRecord updateAfterSaleKeepOnRecord);

    AfterSaleKeepOnRecord selectByBrandAndCarSeries(@Param("originalBrandName") String originalBrandName, @Param("originalCarSeriesName") String originalCarSeriesName);

    void updateByBrandAndCarSeries(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    int insertSelective(AfterSaleKeepOnRecord record);
}
