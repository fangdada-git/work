package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.dto.FissionStatisticIntDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionSalesOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FissionSalesOrderReadMapper extends MyBatisBaseDao<FissionSalesOrder, Integer> {

    List<FissionSalesOrderDto> getFissionSalesOrderList(FissionSalesOrder salesOrder);

    int getFissionSalesOrderCount(FissionSalesOrder salesOrder);

    /**
     * 查询一次活动有多少人参加活动
     *
     * @param fissionId 活动ID
     * @return 人数
     */
    int selectSaleJoinCount(@Param("fissionId") Integer fissionId);

    FissionStatisticIntDto selectSaleJoinCounts(@Param("fissionId") Integer fissionId);

    /**
     * 根据销售id查订单
     *
     * @param saleId 销售ID
     * @return 订单
     */
    List<FissionSalesOrderDto> selectSaleOrder(@Param("saleId") Integer saleId);

    /**
     * 根据裂变id查销售支付成功的对赌金订单
     * @param fissionId
     * @return
     */
    List<FissionSalesOrder> getFissionSalesOrderListByFissionId(@Param("fissionId") Integer fissionId);
    List<FissionSalesOrderDto> getSaleOrderListByPage(FissionSalesOrderDto fissionSalesOrder);
}