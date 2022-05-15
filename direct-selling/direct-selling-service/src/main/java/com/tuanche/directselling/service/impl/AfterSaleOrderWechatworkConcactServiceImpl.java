package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleOrderWechatworkConcactService;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleOrderWechatworkConcactReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleOrderWechatworkConcactWriteMapper;
import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AfterSaleOrderWechatworkConcactServiceImpl implements AfterSaleOrderWechatworkConcactService {

    @Autowired
    private AfterSaleOrderWechatworkConcactReadMapper afterSaleOrderWechatworkConcactReadMapper;
    @Autowired
    private AfterSaleOrderWechatworkConcactWriteMapper afterSaleOrderWechatworkConcactWriteMapper;

    @Override
    public Integer edit (AfterSaleOrderWechatworkConcact orderWechatworkConcact) {
        if (orderWechatworkConcact!=null) {
            if (orderWechatworkConcact.getId()!=null) {
                return afterSaleOrderWechatworkConcactWriteMapper.updateByPrimaryKeySelective(orderWechatworkConcact);
            } else {
                afterSaleOrderWechatworkConcactWriteMapper.insertSelective(orderWechatworkConcact);
                return orderWechatworkConcact.getId()!=null?orderWechatworkConcact.getId():0;
            }
        }
        return 0;
    }

    @Override
    public void addList (List<AfterSaleOrderWechatworkConcact> list) {
        if (!CollectionUtils.isEmpty(list)) {
            afterSaleOrderWechatworkConcactWriteMapper.addList(list);
        }
    }

    @Override
    public List<AfterSaleOrderWechatworkConcact> getList (AfterSaleOrderWechatworkConcact orderWechatworkConcact) {
        return afterSaleOrderWechatworkConcactReadMapper.getList(orderWechatworkConcact);
    }

    @Override
    public List<AfterSaleOrderWechatworkConcact> getWechatworkConcactList (AfterSaleOrderWechatworkConcact orderWechatworkConcact) {
        return afterSaleOrderWechatworkConcactReadMapper.getWechatworkConcactList(orderWechatworkConcact);
    }

    @Override
    public List<String> getOrderByMaxWwUserId(String wwUserId, Integer activityId) {
        return afterSaleOrderWechatworkConcactReadMapper.getOrderByMaxWwUserId(wwUserId, activityId);
    }

    @Override
    public Map<String, AfterSaleOrderWechatworkConcact> getMapByOrderCode(List<String> orderCodes) {
        Map<String, AfterSaleOrderWechatworkConcact> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderCodes)) {
            List<AfterSaleOrderWechatworkConcact> list = afterSaleOrderWechatworkConcactReadMapper.getListByOrderCodes (orderCodes);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(v->{
                    map.put(v.getOrderCode(),v);
                });
            }
        }
        return map;
    }


}
