package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FashionCarMarkeUser;

import java.util.List;
import java.util.Map;

public interface FashionCarMarkeUserService {

    void addUser(FashionCarMarkeUser fashionCarMarkeUser);

    void updateUser(FashionCarMarkeUser fashionCarMarkeUser);

    List<FashionCarMarkeUser> getUserListToOr(FashionCarMarkeUser fashionCarMarkeUser);
    List<FashionCarMarkeUser> getUserList(FashionCarMarkeUser fashionCarMarkeUser);

    List<FashionCarMarkeUser> getUserListByListToOr(List<FashionCarMarkeUser> list, Integer periodsId);

    /**
      * @description : 根据（userid or unionid） and periodsId 查询用户信息
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/9/18 16:03
      */
    Map<String, FashionCarMarkeUser> getKyeUserIdUnionIdMapByList(List<FashionCarMarkeUser> list, Integer periodsId);

    Map<Integer, FashionCarMarkeUser> getKyeUserIdMapByUserId(List<Integer> list, Integer periodsId);

}
