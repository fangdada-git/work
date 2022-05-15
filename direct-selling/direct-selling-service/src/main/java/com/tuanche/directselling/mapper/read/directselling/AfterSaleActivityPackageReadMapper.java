package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.dto.AfterSaleActivityPackageDto;
import com.tuanche.directselling.model.AfterSaleActivityPackage;

import java.util.List;

public interface AfterSaleActivityPackageReadMapper {

    AfterSaleActivityPackage selectByPrimaryKey(Integer id);

    List<AfterSaleActivityPackageDto> getAfterSaleActivityPackageByPage(AfterSaleActivityPackage afterSaleActivityPackage);

    List<AfterSaleActivityPackageDto> getAfterSaleActivityPackageList(AfterSaleActivityPackage saleActivityPackage);
}