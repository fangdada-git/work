package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.FissionGoodsHelperUserService;
import com.tuanche.directselling.mapper.read.directselling.FissionGoodsHelperUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionGoodsHelperUserWriteMapper;
import com.tuanche.directselling.model.FissionGoodsHelperUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FissionGoodsHelperUserServiceImpl implements FissionGoodsHelperUserService {
    
    @Autowired
    private FissionGoodsHelperUserReadMapper fissionGoodsHelperUserReadMapper;
    @Autowired
    private FissionGoodsHelperUserWriteMapper fissionGoodsHelperUserWriteMapper;
    
    @Override
    public List<FissionGoodsHelperUser> getFissionGoodsHelperUserList (FissionGoodsHelperUser fissionGoodsHelperUser) {
        return fissionGoodsHelperUserReadMapper.getFissionGoodsHelperUserList(fissionGoodsHelperUser);
    }
    @Override
    public int getFissionGoodsHelperUserCount (FissionGoodsHelperUser fissionGoodsHelperUser) {
        return fissionGoodsHelperUserReadMapper.getFissionGoodsHelperUserCount(fissionGoodsHelperUser);
    }
    @Override
    public int insertSelective (FissionGoodsHelperUser fissionGoodsHelperUser) {
        fissionGoodsHelperUserWriteMapper.insertSelective(fissionGoodsHelperUser);
        return fissionGoodsHelperUser.getId()==null ? 0: fissionGoodsHelperUser.getId();
    }
    @Override
    public int updateGoodsHelperUserToBuy (FissionGoodsHelperUser fissionGoodsHelperUser) {
        return fissionGoodsHelperUserWriteMapper.updateGoodsHelperUserToBuy(fissionGoodsHelperUser);
    }
    
}
