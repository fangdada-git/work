package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.AfterSaleActivityPackageLabelDto;
import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-25 14:17
 */
public interface AfterSaleActivityPackageLabelService {

    /**
     * 获取套餐标签IDS
     * @author HuangHao
     * @CreatTime 2021-11-25 15:28
     * @param packageId
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getLabelIds(Integer packageId);
    /**
     * 获取套餐的标签
     * @author HuangHao
     * @CreatTime 2021-11-25 14:13
     * @param packageId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    List<AfterSaleActivityPackageLabel> getPackageLabels(Integer packageId);
    /**
     * 获取套餐的标签-带标签名称
     * @author HuangHao
     * @CreatTime 2021-11-25 14:13
     * @param packageId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    List<AfterSaleActivityPackageLabel> getPackageLabelNames(Integer packageId);

    /**
     * 根据套餐IDS获取其标签
     * @author HuangHao
     * @CreatTime 2021-11-26 16:18
     * @param packageIds
     * @return java.util.Map<java.lang.Integer,java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>>
     */
    public Map<Integer,List<AfterSaleActivityPackageLabelDto>> getPackageLabelNamesByPackageIds(List<Integer> packageIds);
    /**
     * 删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByIds(List<Integer> ids);
    /**
     * 根据一级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByPrimaryLabelIds(List<Integer> primaryLabelIds);
    /**
     * 根据二级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delBySecondaryIds(List<Integer> secondaryIds);
    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param primaryLabel
     * @return int
     */
    int batchInsert(List<AfterSaleActivityPackageLabel> list);

    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-11-24 14:20
     * @param primaryLabel
     * @return int
     */
    int batchUpdate(List<AfterSaleActivityPackageLabel> list);
}
