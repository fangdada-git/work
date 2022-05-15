package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionSalesOrder;

import java.util.List;

public interface FissionSalesOrderWriteMapper extends MyBatisBaseDao<FissionSalesOrder, Integer> {
    List<FissionSalesOrderDto> getFissionSalesOrderListByWrite(FissionSalesOrder salesOrder);
}