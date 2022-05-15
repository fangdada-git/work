package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionGoodsHelperUser;

public interface FissionGoodsHelperUserWriteMapper extends MyBatisBaseDao<FissionGoodsHelperUser, Integer> {

    int updateGoodsHelperUserToBuy(FissionGoodsHelperUser fissionGoodsHelperUser);
}