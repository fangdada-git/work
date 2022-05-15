package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleKeepOnRecordBrandCarSeriesDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordDto;
import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 18:02
 */
public interface AfterSaleKeepOnRecordReadMapper {

    /**
     * 获取用户类型
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     * @param afterSaleKeepOnRecord
     * @return int
     */
    Integer getUserType(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 是否是活动的备案用户
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     * @param afterSaleKeepOnRecord
     * @return int
     */
    AfterSaleKeepOnRecord getByLicencePlateOrPhone(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 根据ID获取记录
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     * @param id
     * @return com.tuanche.directselling.model.AfterSaleKeepOnRecord
     */
    AfterSaleKeepOnRecord getById(@Param("id") Integer id);

    /**
     * 获取备案记录列表
     *
     * @param afterSaleKeepOnRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleKeepOnRecord>
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    List<AfterSaleKeepOnRecord> getKeepOnRecordList(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 备案用户/流失用户 汇总数据
     *
     * @param activityId 活动ID
     * @param userType   用户类型 0:备案用户 1:流失用户
     * @return
     */
    List<AfterSaleKeepOnRecordDto> getUserSumData(@Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId, @Param("userType") Integer userType);

    /**
     * 根据ID和经销商ID查数据
     *
     * @param id
     * @param dealerId
     * @return
     */
    AfterSaleKeepOnRecord getByIdAndDealerId(@Param("id") Integer id, @Param("dealerId") Integer dealerId);

    /**
     * 品牌车系匹配
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    List<AfterSaleKeepOnRecordBrandCarSeriesDto> getKeepOnRecordBrandCarSeriesList(AfterSaleKeepOnRecordBrandCarSeriesDto afterSaleKeepOnRecord);
}
