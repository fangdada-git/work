package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.FashionCarMarkeUserService;
import com.tuanche.directselling.mapper.read.directselling.FashionCarMarkeUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionCarMarkeUserWriteMapper;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FashionCarMarkeUserServiceImpl implements FashionCarMarkeUserService {

    @Autowired
    private FashionCarMarkeUserReadMapper fashionCarMarkeUserReadMapper;
    @Autowired
    private FashionCarMarkeUserWriteMapper fashionCarMarkeUserWriteMapper;

    @Override
    public void addUser(FashionCarMarkeUser fashionCarMarkeUser) {
        fashionCarMarkeUserWriteMapper.insertSelective(fashionCarMarkeUser);
    }

    @Override
    public void updateUser(FashionCarMarkeUser fashionCarMarkeUser) {
        fashionCarMarkeUserWriteMapper.updateByPrimaryKeySelective(fashionCarMarkeUser);
    }

    @Override
    public List<FashionCarMarkeUser> getUserListToOr(FashionCarMarkeUser fashionCarMarkeUser) {
        return fashionCarMarkeUserReadMapper.getUserListToOr(fashionCarMarkeUser);
    }
    @Override
    public List<FashionCarMarkeUser> getUserList(FashionCarMarkeUser fashionCarMarkeUser) {
        return fashionCarMarkeUserReadMapper.getUserList(fashionCarMarkeUser);
    }

    @Override
    public List<FashionCarMarkeUser> getUserListByListToOr(List<FashionCarMarkeUser> list, Integer periodsId) {
        return fashionCarMarkeUserReadMapper.getUserListByListToOr(list, periodsId);
    }

    @Override
    public Map<String, FashionCarMarkeUser> getKyeUserIdUnionIdMapByList(List<FashionCarMarkeUser> list, Integer periodsId) {
        Map<String, FashionCarMarkeUser> map = new HashMap<>();
        List<FashionCarMarkeUser> userList = fashionCarMarkeUserReadMapper.getUserListByListToOr(list, periodsId);
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(v->{
                map.put(v.getUserId() + "", v);
            });
        }
        return map;
    }

    @Override
    public Map<Integer, FashionCarMarkeUser> getKyeUserIdMapByUserId(List<Integer> list, Integer periodsId) {
        Map<Integer, FashionCarMarkeUser> userMap = new HashMap<>();
        List<FashionCarMarkeUser> userList = fashionCarMarkeUserReadMapper.getKyeUserIdMapByUserId(list, periodsId);
        if (CollectionUtils.isNotEmpty(userList)) {
            for (FashionCarMarkeUser markeUser : userList) {
                if (markeUser.getUserId()!=null) userMap.put(markeUser.getUserId(), markeUser);
            }
        }
        return userMap;
    }



}
