package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.model.FissionSalesOrder;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-25 15:54
 **/
public interface FissionSalesOrderService {

    FissionSalesOrder insertSelective(FissionSalesOrder fissionSalesOrder) throws Exception;

    Integer updateByPrimaryKeySelective(FissionSalesOrder fissionSalesOrder);

    List<FissionSalesOrderDto> getFissionSalesOrderList(FissionSalesOrder salesOrder);

    int getFissionSalesOrderCount(FissionSalesOrder salesOrder);

    /**
     * 销售订单数据
     *
     * @param page
     * @param limit
     * @param saleId
     * @return
     */
    PageResult selectSaleOrder(int page, int limit, Integer saleId);


    PageResult getSaleOrderListByPage(PageResult<FissionSalesOrderDto> pageResult, FissionSalesOrderDto fissionSalesOrder);

    List<FissionSalesOrderDto> getFissionSalesOrderListByWrite(FissionSalesOrder salesOrder);

    List<FissionSalesOrder> getFissionSalesOrderListByFissionId(Integer fissionId);
}
