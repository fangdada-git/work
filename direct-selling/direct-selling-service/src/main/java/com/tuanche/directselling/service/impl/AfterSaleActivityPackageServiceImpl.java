package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.directselling.api.AfterSaleActivityPackageLabelService;
import com.tuanche.directselling.api.AfterSaleActivityPackageService;
import com.tuanche.directselling.dto.AfterSaleActivityPackageDto;
import com.tuanche.directselling.dto.AfterSaleActivityPackageLabelDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityPackageReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityCouponWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityPackageWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivityCoupon;
import com.tuanche.directselling.model.AfterSaleActivityPackage;
import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/8/4
 **/
@Service
public class AfterSaleActivityPackageServiceImpl implements AfterSaleActivityPackageService {

    @Autowired
    AfterSaleActivityPackageReadMapper packageReadMapper;
    @Autowired
    AfterSaleActivityPackageWriteMapper packageWriteMapper;
    @Autowired
    AfterSaleActivityCouponWriteMapper couponWriteMapper;
    @Reference
    AfterSaleActivityPackageLabelService afterSaleActivityPackageLabelService;

    @Override
    public List<AfterSaleActivityPackageDto> getAfterSaleActivityPackageList(AfterSaleActivityPackage saleActivityPackage) {
        return packageReadMapper.getAfterSaleActivityPackageList(saleActivityPackage);
    }

    @Override
    public AfterSaleActivityPackage getAfterSaleActivityPackageById(Integer packageId) {
        return null;
    }

    @Override
    public Integer saveAfterSaleActivityPackage(AfterSaleActivityPackage afterSaleActivityPackage) {
        return packageWriteMapper.insertSelective(afterSaleActivityPackage);
    }

    @Override
    public Integer updateAfterSaleActivityPackage(AfterSaleActivityPackage afterSaleActivityPackage) {
        int flag = packageWriteMapper.updateByPrimaryKeySelective(afterSaleActivityPackage);
        if (flag > 0) {
            //删除套餐关联的卡券
            AfterSaleActivityCoupon coupon = new AfterSaleActivityCoupon();
            coupon.setPackageId(afterSaleActivityPackage.getId());
            coupon.setRelStatus(AfterSaleConstants.ActivityCoupon.REL_STATUS_NO.getCode());
            coupon.setOperateUser(afterSaleActivityPackage.getOperateUser());
            coupon.setRelTime(new Date());
            couponWriteMapper.deleteByPackageId(coupon);
        }
        return packageWriteMapper.updateByPrimaryKeySelective(afterSaleActivityPackage);
    }

    @Override
    public PageResult<AfterSaleActivityPackageDto> getAfterSaleActivityPackageByPage(PageResult<AfterSaleActivityPackageDto> pageResult, AfterSaleActivityPackageDto afterSaleActivityPackage) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleActivityPackageDto> list = packageReadMapper.getAfterSaleActivityPackageByPage(afterSaleActivityPackage);
        if(!CollectionUtils.isEmpty(list)){
            List<Integer> packageIds = new ArrayList<>(list.size()*2);
            for (AfterSaleActivityPackageDto packageDto : list) {
                packageIds.add(packageDto.getId());
            }
            Map<Integer,List<AfterSaleActivityPackageLabelDto>> map = afterSaleActivityPackageLabelService.getPackageLabelNamesByPackageIds(packageIds);
            if(map != null){
                for (AfterSaleActivityPackageDto packageDto : list) {
                    List<AfterSaleActivityPackageLabelDto> packageLabels =map.get(packageDto.getId());
                    packageDto.setLabels(packageLabels);
                }
            }
        }
        PageInfo<AfterSaleActivityPackageDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }
}
