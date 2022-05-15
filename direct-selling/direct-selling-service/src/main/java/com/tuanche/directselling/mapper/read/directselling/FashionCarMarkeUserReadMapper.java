package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FashionCarMarkeUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FashionCarMarkeUserReadMapper {

    FashionCarMarkeUser selectByPrimaryKey(Integer id);

    List<FashionCarMarkeUser> getUserList(FashionCarMarkeUser fashionCarMarkeUser);

    List<FashionCarMarkeUser> getUserListToOr(FashionCarMarkeUser fashionCarMarkeUser);

    List<FashionCarMarkeUser> getUserListByListToOr(@Param("list") List<FashionCarMarkeUser> list, @Param("periodsId") Integer periodsId);

    List<FashionCarMarkeUser> getKyeUserIdMapByUserId(@Param("list") List<Integer> list, @Param("periodsId") Integer periodsId);
}