package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FashionCarMarkeHelperUser;

import java.util.List;

public interface FashionCarMarkeHelperUserReadMapper {

    FashionCarMarkeHelperUser selectByPrimaryKey(Integer id);

    List<FashionCarMarkeHelperUser> getFashionCarMarkeHelperUserList(FashionCarMarkeHelperUser helperUser);

    Integer getFashionCarMarkeHelperUserCount(FashionCarMarkeHelperUser helperUser);
}