package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-24 11:06
 */
public class AfterSaleActivityPackagePrimaryLabelDto extends AfterSaleActivityPackagePrimaryLabel {

    //二级标签列表
    List<AfterSaleActivityPackageSecondaryLabel> secondaryLabels;

    public List<AfterSaleActivityPackageSecondaryLabel> getSecondaryLabels() {
        return secondaryLabels;
    }

    public void setSecondaryLabels(List<AfterSaleActivityPackageSecondaryLabel> secondaryLabels) {
        this.secondaryLabels = secondaryLabels;
    }
}
