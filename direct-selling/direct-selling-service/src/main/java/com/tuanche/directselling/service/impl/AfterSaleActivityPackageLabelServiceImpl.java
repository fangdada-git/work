package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleActivityPackageLabelService;
import com.tuanche.directselling.dto.AfterSaleActivityPackageLabelDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityPackageLabelReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityPackageLabelWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-25 14:15
 */
@Service
public class AfterSaleActivityPackageLabelServiceImpl implements AfterSaleActivityPackageLabelService {

    @Autowired
    AfterSaleActivityPackageLabelReadMapper afterSaleActivityPackageLabelReadMapper;
    @Autowired
    AfterSaleActivityPackageLabelWriteMapper afterSaleActivityPackageLabelWriteMapper;

    /**
     * 获取套餐标签IDS
     * @author HuangHao
     * @CreatTime 2021-11-25 15:28
     * @param packageId
     * @return java.util.List<java.lang.Integer>
     */
    public List<Integer> getLabelIds(Integer packageId){
        if(packageId==null){
            return null;
        }
        return afterSaleActivityPackageLabelReadMapper.getLabelIds(packageId);
    }
    /**
     * 获取套餐的标签
     * @author HuangHao
     * @CreatTime 2021-11-25 14:13
     * @param packageId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    public List<AfterSaleActivityPackageLabel> getPackageLabels(Integer packageId){
        if(packageId==null){
            return null;
        }
        return afterSaleActivityPackageLabelReadMapper.getPackageLabels(packageId);
    }
    /**
     * 获取套餐的标签-带标签名称
     * @author HuangHao
     * @CreatTime 2021-11-25 14:13
     * @param packageId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    public List<AfterSaleActivityPackageLabel> getPackageLabelNames(Integer packageId){
        if(packageId==null){
            return null;
        }
        return afterSaleActivityPackageLabelReadMapper.getPackageLabelNames(packageId);
    }
    /**
     * 根据套餐IDS获取其标签
     * @author HuangHao
     * @CreatTime 2021-11-26 16:18
     * @param packageIds
     * @return java.util.Map<java.lang.Integer,java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>>
     */
    public Map<Integer,List<AfterSaleActivityPackageLabelDto>> getPackageLabelNamesByPackageIds(List<Integer> packageIds){
        if(CollectionUtils.isEmpty(packageIds)){
            return null;
        }
        List<AfterSaleActivityPackageLabelDto> list = afterSaleActivityPackageLabelReadMapper.getPackageLabelNamesByPackageIds(packageIds);
        Map<Integer,List<AfterSaleActivityPackageLabelDto>> map = null;
        if(!CollectionUtils.isEmpty(list)){
            map = new HashMap<>(packageIds.size()*2);
            for (AfterSaleActivityPackageLabelDto packageLabel : list) {
                Integer packageId = packageLabel.getPackageId();
                List<AfterSaleActivityPackageLabelDto> labels = map.get(packageId);
                if(labels==null){
                    labels=new ArrayList<>();
                }
                labels.add(packageLabel);
                map.put(packageId, labels);
            }
        }
        return map;
    }

    /**
     * 删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    public int delByIds(List<Integer> ids){
        if(CollectionUtils.isEmpty(ids)){
            return 0;
        }
        return afterSaleActivityPackageLabelWriteMapper.delByIds(ids);
    }
    /**
     * 根据一级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    public int delByPrimaryLabelIds(List<Integer> primaryLabelIds){
        if(CollectionUtils.isEmpty(primaryLabelIds)){
            return 0;
        }
        return afterSaleActivityPackageLabelWriteMapper.delByPrimaryLabelIds(primaryLabelIds);
    }
    /**
     * 根据二级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    public int delBySecondaryIds(List<Integer> secondaryIds){
        if(CollectionUtils.isEmpty(secondaryIds)){
            return 0;
        }
        return afterSaleActivityPackageLabelWriteMapper.delBySecondaryIds(secondaryIds);
    }
    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param primaryLabel
     * @return int
     */
    public int batchInsert(List<AfterSaleActivityPackageLabel> list){
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        return afterSaleActivityPackageLabelWriteMapper.batchInsert(list);
    }

    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-11-24 14:20
     * @param primaryLabel
     * @return int
     */
    public int batchUpdate(List<AfterSaleActivityPackageLabel> list){
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        return afterSaleActivityPackageLabelWriteMapper.batchUpdate(list);
    }
}
