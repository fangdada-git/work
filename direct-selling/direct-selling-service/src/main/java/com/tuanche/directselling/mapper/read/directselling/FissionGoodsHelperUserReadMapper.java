package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionGoodsHelperUser;

import java.util.List;

public interface FissionGoodsHelperUserReadMapper extends MyBatisBaseDao<FissionGoodsHelperUser, Integer> {

    List<FissionGoodsHelperUser> getFissionGoodsHelperUserList(FissionGoodsHelperUser fissionGoodsHelperUser);
    Integer getFissionGoodsHelperUserCount(FissionGoodsHelperUser fissionGoodsHelperUser);
}