package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionGoodsHelperUser;

import java.util.List;

public interface FissionGoodsHelperUserService {

    List<FissionGoodsHelperUser> getFissionGoodsHelperUserList (FissionGoodsHelperUser fissionGoodsHelperUser);

    int getFissionGoodsHelperUserCount (FissionGoodsHelperUser fissionGoodsHelperUser);

    int insertSelective (FissionGoodsHelperUser fissionGoodsHelperUser);

    int updateGoodsHelperUserToBuy(FissionGoodsHelperUser helperUser);
}
