package com.tuanche.directselling.api;

import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordBrandCarSeriesDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordDto;
import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-05 14:03
 */
public interface AfterSaleKeepOnRecordService {

    /**
     * 获取用户类型
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     * @param afterSaleKeepOnRecord
     * @return int
     */
    Integer getUserType(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 根据ID获取记录
     *
     * @param id
     * @return com.tuanche.directselling.model.AfterSaleKeepOnRecord
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    AfterSaleKeepOnRecord getById(Integer id);

    /**
     * 根据ID和经销商ID查数据
     *
     * @param id
     * @param dealerId
     * @return
     */
    AfterSaleKeepOnRecord getByIdAndDealerId(Integer id, Integer dealerId);

    /**
     * 获取备案记录列表-分页
     *
     * @param afterSaleKeepOnRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleKeepOnRecord>
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    PageResult<AfterSaleKeepOnRecord> getKeepOnRecordListByPage(PageResult<AfterSaleKeepOnRecord> pageResult, AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 获取备案记录
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    List<AfterSaleKeepOnRecord> getKeepOnRecordList(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 品牌车系匹配列表
     *
     * @param pageResult
     * @param afterSaleKeepOnRecord
     * @return
     */
    PageResult<AfterSaleKeepOnRecordBrandCarSeriesDto> getKeepOnRecordBrandCarSeriesListByPage(PageResult<AfterSaleKeepOnRecordBrandCarSeriesDto> pageResult, AfterSaleKeepOnRecordBrandCarSeriesDto afterSaleKeepOnRecord);

    /**
     * 删除
     *
     * @param id
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:01
     */
    int deleteById(Integer id);

    /**
     * 根据ID和经销商ID删除数据
     *
     * @param id
     * @param dealerId
     * @return
     */
    int deleteByIdAndDealerId(Integer id, Integer dealerId);

    /**
     * 更新
     *
     * @param afterSaleKeepOnRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:02
     */
    TcResponse updateById(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 获取活动备案用户的手机号和车牌map
     *
     * @param id
     * @return
     * @author HuangHao
     * @CreatTime 2021-08-06 10:22
     */
    Map<String, List<AfterSaleKeepOnRecord>> keepOnRecordMap(Integer id);

    /**
     * 批量新增
     * @author HuangHao
     * @param list
     * @return int
     */
    int batchInsert(List<AfterSaleKeepOnRecord> list);

    /**
     * 根据活动ID删除代理人
     *
     * @param activityId
     * @return int
     * @author HuangHao
     * @CreatTime 2021-09-07 15:41
     */
    TcResponse deleteByActivityId(Integer activityId);

    TcResponse deleteByActivityIdAndUserType(Integer activityId, Integer userType);


    /**
     * 备案用户/流失用户 汇总数据
     *
     * @param activityId 活动ID
     * @param userType   用户类型 0:备案用户 1:流失用户
     * @return
     */
    PageResult<AfterSaleKeepOnRecordDto> getUserSumData(int page, int limit, Integer dealerId, Integer activityId, Integer userType);

    /**
     * 编辑品牌 车型
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    TcResponse updateBrandCarSeriesById(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 线索库数据同步
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    TcResponse syncData(AfterSaleKeepOnRecord afterSaleKeepOnRecord);

    /**
     * 线索库数据同步状态
     * @return
     */
    boolean getSyncDataStatus(AfterSaleKeepOnRecord afterSaleKeepOnRecord) throws RedisException;

    /**
     * 车系未匹配的数量
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    List<AfterSaleKeepOnRecordBrandCarSeriesDto> getUnmatchedData(AfterSaleKeepOnRecord afterSaleKeepOnRecord);
}
