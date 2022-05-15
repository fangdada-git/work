package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.FashionCarMarkeHelperUserService;
import com.tuanche.directselling.mapper.read.directselling.FashionCarMarkeHelperUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionCarMarkeHelperUserWriteMapper;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FashionCarMarkeHelperUserServiceImpl implements FashionCarMarkeHelperUserService {

    @Autowired
    private FashionCarMarkeHelperUserReadMapper fashionCarMarkeHelperUserReadMapper;
    @Autowired
    private FashionCarMarkeHelperUserWriteMapper fashionCarMarkeHelperUserWriteMapper;

    public List<FashionCarMarkeHelperUser> getFashionCarMarkeHelperUserList (FashionCarMarkeHelperUser helperUser) {
        return fashionCarMarkeHelperUserReadMapper.getFashionCarMarkeHelperUserList(helperUser);
    }

    public Integer getFashionCarMarkeHelperUserCount (FashionCarMarkeHelperUser helperUser) {
        return fashionCarMarkeHelperUserReadMapper.getFashionCarMarkeHelperUserCount(helperUser);
    }

    public Integer addFashionCarMarkeHelperUser (FashionCarMarkeHelperUser helperUser) {
        fashionCarMarkeHelperUserWriteMapper.insertSelective(helperUser);
        return helperUser.getId();
    }

    public Integer updateHelperUserToBuy (FashionCarMarkeHelperUser helperUser) {
        return fashionCarMarkeHelperUserWriteMapper.updateHelperUserToBuy(helperUser);
    }

}
