package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionGoodsHelper;

import java.util.List;

public interface FissionGoodsHelperService {

    List<FissionGoodsHelper> getFissionGoodsHelperList (FissionGoodsHelper fissionGoodsHelper);

    int editFissionGoodsHelper (FissionGoodsHelper fissionGoodsHelper);
    
    
}
