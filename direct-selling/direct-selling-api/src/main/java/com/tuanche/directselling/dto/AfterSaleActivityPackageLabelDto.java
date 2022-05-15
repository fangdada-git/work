package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-25 10:52
 */
public class AfterSaleActivityPackageLabelDto extends AfterSaleActivityPackageLabel {

    /**
     * 一级标签名称
     */
    private String primaryLabelName;
    /**
     * 二级标签名称
     */
    private String secondaryLabelName;

    public String getPrimaryLabelName() {
        return primaryLabelName;
    }

    public void setPrimaryLabelName(String primaryLabelName) {
        this.primaryLabelName = primaryLabelName;
    }

    public String getSecondaryLabelName() {
        return secondaryLabelName;
    }

    public void setSecondaryLabelName(String secondaryLabelName) {
        this.secondaryLabelName = secondaryLabelName;
    }
}
