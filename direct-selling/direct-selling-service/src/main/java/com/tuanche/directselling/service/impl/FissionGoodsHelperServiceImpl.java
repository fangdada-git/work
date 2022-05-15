package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.FissionGoodsHelperService;
import com.tuanche.directselling.mapper.read.directselling.FissionGoodsHelperReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionGoodsHelperWriteMapper;
import com.tuanche.directselling.model.FissionGoodsHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FissionGoodsHelperServiceImpl implements FissionGoodsHelperService {
    
    @Autowired
    private FissionGoodsHelperReadMapper fissionGoodsHelperReadMapper;
    @Autowired
    private FissionGoodsHelperWriteMapper fissionGoodsHelperWriteMapper;
    
    @Override
    public List<FissionGoodsHelper> getFissionGoodsHelperList (FissionGoodsHelper fissionGoodsHelper) {
        return fissionGoodsHelperReadMapper.getFissionGoodsHelperList(fissionGoodsHelper);
    }
    
    @Override
    public int editFissionGoodsHelper (FissionGoodsHelper fissionGoodsHelper) {
        int num = 0;
        if (fissionGoodsHelper!=null) {
            //编辑
            if (fissionGoodsHelper.getId()!=null && fissionGoodsHelper.getId()>0) {
                return fissionGoodsHelperWriteMapper.updateByPrimaryKeySelective(fissionGoodsHelper);
                //添加
            } else {
                int n = fissionGoodsHelperWriteMapper.insertSelective(fissionGoodsHelper);
                if (n==1) num=fissionGoodsHelper.getId();
            }
        }
        return num;
    }
    
}
