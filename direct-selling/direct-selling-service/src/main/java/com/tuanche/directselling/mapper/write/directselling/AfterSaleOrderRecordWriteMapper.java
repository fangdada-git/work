package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleOrderRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleOrderRecordWriteMapper {


    /**
     * 获取购买人的单个订单信息-已支付以上状态
     * @author HuangHao
     * @CreatTime 2021-07-25 10:58
     * @param activityId
     * @param shareWxUnionId
     * @param orderType
     * @return com.tuanche.directselling.model.AfterSaleOrderRecord
     */
    AfterSaleOrderRecord getBuyerOrder(@Param("activityId")Integer activityId, @Param("userWxUnionId")String userWxUnionId, @Param("orderType")Integer orderType);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(AfterSaleOrderRecord record);

    int updateByPrimaryKeySelective(AfterSaleOrderRecord record);

    int updateByOrderCode(AfterSaleOrderRecord record);

    /**
     * 获取非备案的裂变总人数
     * @author HuangHao
     * @CreatTime 2021-07-22 18:24
     * @param afterSaleOrderRecord
     * @return int
     */
    int getNonFilingFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取裂变的人数
     * @author HuangHao
     * @CreatTime 2021-07-22 18:24
     * @param afterSaleOrderRecord
     * @return int
     */
    int getFissionTotal(AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 获取非备案的裂变信息列表
     * @author HuangHao
     * @CreatTime 2021-07-28 22:47
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getNonFilingFissionList(AfterSaleOrderRecord afterSaleOrderRecord);
    /**
     * 获取裂变人的裂变列表
     * @author HuangHao
     * @CreatTime 2021-10-22 17:40
     * @param afterSaleOrderRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleOrderRecord>
     */
    List<AfterSaleOrderRecord> getFissionList(AfterSaleOrderRecord afterSaleOrderRecord);

    /**
     * 同一个活动用户是否已购买（含退款）
     * @param activityId
     * @param userWxUnionId
     * @param orderType
     * @return
     */
    AfterSaleOrderRecord getOrderByUserUnionId(AfterSaleOrderRecord record);

    /**
     * 根据order_code修改车牌
     *
     * @param orderCodes
     * @param licencePlate
     * @return
     */
    int updateLicencePlateByOrderCode(@Param("orderCodes") List<String> orderCodes, @Param("licencePlate") String licencePlate);

    /**
     * 根据order_code查车牌数量(是否重复)
     *
     * @param orderCodes
     * @param licencePlate
     * @return
     */
    int getPlateCountByOrderCode(@Param("orderCodes") List<String> orderCodes, @Param("licencePlate") String licencePlate);
    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-12-20 17:27
     * @param list
     * @return int
     */
    int updateBatchByIds(@Param("list") List<AfterSaleOrderRecord> list);
}