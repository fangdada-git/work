package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleUserBrowseService;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserBrowseReadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-29 17:28
 */
@Service
public class AfterSaleUserBrowseServiceImpl implements AfterSaleUserBrowseService {

    @Autowired
    AfterSaleUserBrowseReadMapper afterSaleUserBrowseReadMapper;

    /**
     * 活动的浏览数量统计
     * @author HuangHao
     * @CreatTime 2021-09-29 17:24
     * @param activityIds
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> activityBrowseTotalMap(List<Integer> activityIds){
        if(CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleUserBrowseReadMapper.activityBrowseTotalMap(activityIds);
    }
}
