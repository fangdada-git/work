package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.directselling.api.FissionDictService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.enums.FissionDictType;
import com.tuanche.directselling.mapper.read.directselling.FissionDictReadMapper;
import com.tuanche.directselling.model.FissionDict;
import com.tuanche.directselling.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 15:56
 **/
@Service
public class FissionDictServiceImpl implements FissionDictService {

    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    private FissionDictReadMapper fissionDictReadMapper;

    @Override
    public List<FissionDict> getFissionDictByType(short type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String data = redisService.get(BaseCacheKeys.FISSION_DICT_CONFIG_CACHE.getKey()+type, String.class);
            if (data == null) {
                List<FissionDict> fissionDictList = fissionDictReadMapper.selectByType(type);
                String jsonList = mapper.writeValueAsString(fissionDictList);
                redisService.set(BaseCacheKeys.FISSION_DICT_CONFIG_CACHE.getKey()+type, jsonList, BaseCacheKeys.FISSION_DICT_CONFIG_CACHE.getExpire());
                return fissionDictList;
            } else {
                return mapper.readValue(data, mapper.getTypeFactory().constructParametricType(List.class, FissionDict.class));
            }
        } catch (Exception e) {
            CommonLogUtil.addError("getFissionDictByType", "get FissionDict cache error", e);
            return fissionDictReadMapper.selectByType(FissionDictType.LB.getType());
        }
    }
}
