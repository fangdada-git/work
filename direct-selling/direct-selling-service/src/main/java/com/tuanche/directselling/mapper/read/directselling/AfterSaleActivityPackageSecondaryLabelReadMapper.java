package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:17
 */
public interface AfterSaleActivityPackageSecondaryLabelReadMapper {

    /**
     * 根据主标签ID获取二级标签
     * @author HuangHao
     * @CreatTime 2021-11-24 11:21
     * @param primaryLabelId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel>
     */
    List<AfterSaleActivityPackageSecondaryLabel> getLabelsByPrimaryLabelId(Integer primaryLabelId);

    /**
     * 获取ID列表
     * @author HuangHao
     * @CreatTime 2021-11-24 15:28
     * @param primaryLabelId
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getIdsByPrimaryLabelId(Integer primaryLabelId);
}
