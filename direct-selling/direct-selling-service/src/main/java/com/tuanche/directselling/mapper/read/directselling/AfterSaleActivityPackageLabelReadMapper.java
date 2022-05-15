package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleActivityPackageLabelDto;
import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:09
 */
public interface AfterSaleActivityPackageLabelReadMapper {

    /**
     * 获取套餐的标签
     * @author HuangHao
     * @CreatTime 2021-11-25 14:13
     * @param packageId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    List<AfterSaleActivityPackageLabel> getPackageLabels(Integer packageId);
    /**
     * 获取套餐标签IDS
     * @author HuangHao
     * @CreatTime 2021-11-25 15:28
     * @param packageId
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getLabelIds(Integer packageId);
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
     * @CreatTime 2021-11-26 16:07
     * @param packageIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageLabel>
     */
    List<AfterSaleActivityPackageLabelDto> getPackageLabelNamesByPackageIds(@Param("packageIds")List<Integer> packageIds);
}
