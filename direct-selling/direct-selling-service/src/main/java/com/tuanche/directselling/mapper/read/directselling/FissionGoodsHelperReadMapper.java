package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionGoodsHelper;

import java.util.List;

public interface FissionGoodsHelperReadMapper extends MyBatisBaseDao<FissionGoodsHelper, Integer> {

    List<FissionGoodsHelper> getFissionGoodsHelperList(FissionGoodsHelper fissionGoodsHelper);
}