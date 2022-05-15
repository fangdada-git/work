package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleAgent;

public interface AfterSaleAgentWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleAgent record);

    int insertSelective(AfterSaleAgent record);

    int updateByPrimaryKeySelective(AfterSaleAgent record);

    int updateByPrimaryKey(AfterSaleAgent record);
}