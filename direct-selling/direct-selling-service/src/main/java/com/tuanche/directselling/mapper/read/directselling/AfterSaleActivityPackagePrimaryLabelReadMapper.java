package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:15
 */
public interface AfterSaleActivityPackagePrimaryLabelReadMapper {

    /**
     * 根据活动获取一级标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    List<AfterSaleActivityPackagePrimaryLabel> getLabelsByActivityId(Integer activityId);

    /**
     * 活动IDS
     * @author HuangHao
     * @CreatTime 2021-11-24 14:26
     * @param activityId
     * @return java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    List<Integer> getIdsByActivityId(Integer activityId);
}
