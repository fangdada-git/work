package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.AfterSaleActivityPackageDto;
import com.tuanche.directselling.model.AfterSaleActivityPackage;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;

public interface AfterSaleActivityPackageService {
    /**
     * 活动套餐列表
     * @param saleActivityPackage
     * @return
     */
    List<AfterSaleActivityPackageDto> getAfterSaleActivityPackageList(AfterSaleActivityPackage saleActivityPackage);

    /**
     * 获取单个套餐
     * @param packageId
     * @return
     */
    AfterSaleActivityPackage getAfterSaleActivityPackageById(Integer packageId);

    /**
     * 保存套餐
     * @param afterSaleActivityPackage
     * @return
     */
    Integer saveAfterSaleActivityPackage(AfterSaleActivityPackage afterSaleActivityPackage);

    /**
     * 修改套餐
     * @param afterSaleActivityPackage
     * @return
     */
    Integer updateAfterSaleActivityPackage(AfterSaleActivityPackage afterSaleActivityPackage);

    PageResult<AfterSaleActivityPackageDto> getAfterSaleActivityPackageByPage(PageResult<AfterSaleActivityPackageDto> pageResult, AfterSaleActivityPackageDto afterSaleActivityPackage);
}
