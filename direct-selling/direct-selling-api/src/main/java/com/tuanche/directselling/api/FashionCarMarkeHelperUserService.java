package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FashionCarMarkeHelperUser;

import java.util.List;

public interface FashionCarMarkeHelperUserService {

    List<FashionCarMarkeHelperUser> getFashionCarMarkeHelperUserList (FashionCarMarkeHelperUser helperUser);

    Integer getFashionCarMarkeHelperUserCount (FashionCarMarkeHelperUser helperUser);

    Integer addFashionCarMarkeHelperUser (FashionCarMarkeHelperUser helperUser);

    Integer updateHelperUserToBuy (FashionCarMarkeHelperUser helperUser);

}
