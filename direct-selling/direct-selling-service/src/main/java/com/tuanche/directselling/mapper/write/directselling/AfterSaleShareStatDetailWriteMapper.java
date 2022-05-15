package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleShareStatDetail;

public interface AfterSaleShareStatDetailWriteMapper {
    int insert(AfterSaleShareStatDetail record);

    int insertSelective(AfterSaleShareStatDetail record);

    int updateById(AfterSaleShareStatDetail record);
}