package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleUser;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 10:43
 */
public interface AfterSaleUserWriteMapper {

    int insert(AfterSaleUser afterSaleUser);

    int update(AfterSaleUser afterSaleUser);
}
