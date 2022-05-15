package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleActivityPackageLabelService;
import com.tuanche.directselling.api.AfterSaleActivityPackagePrimaryLabelService;
import com.tuanche.directselling.dto.AfterSaleActivityPackagePrimaryLabelDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityPackagePrimaryLabelReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityPackageSecondaryLabelReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityPackagePrimaryLabelWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityPackageSecondaryLabelWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-24 11:25
 */
@Service
public class AfterSaleActivityPackagePrimaryLabelServiceImpl implements AfterSaleActivityPackagePrimaryLabelService {

    @Autowired
    AfterSaleActivityPackagePrimaryLabelReadMapper afterSaleActivityPackagePrimaryLabelReadMapper;
    @Autowired
    AfterSaleActivityPackagePrimaryLabelWriteMapper afterSaleActivityPackagePrimaryLabelWriteMapper;
    @Autowired
    AfterSaleActivityPackageSecondaryLabelReadMapper afterSaleActivityPackageSecondaryLabelReadMapper;
    @Autowired
    AfterSaleActivityPackageSecondaryLabelWriteMapper afterSaleActivityPackageSecondaryLabelWriteMapper;
    @Autowired
    AfterSaleActivityPackageLabelService afterSaleActivityPackageLabelService;

    /**
     * 根据活动ID获取标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    public List<AfterSaleActivityPackagePrimaryLabel> getLabelsByActivityId(Integer activityId){
        if(activityId == null){
            return null;
        }
        return afterSaleActivityPackagePrimaryLabelReadMapper.getLabelsByActivityId(activityId);
    }

    /**
     * 保存标签
     * @author HuangHao
     * @CreatTime 2021-11-24 16:05
     * @param labelList
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse save(List<AfterSaleActivityPackagePrimaryLabelDto> labelList){
        if(CollectionUtils.isEmpty(labelList)){
            return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), StatusCodeEnum.PARAM_IS_BLANK.getText());
        }
        List<Integer> pIds = afterSaleActivityPackagePrimaryLabelReadMapper.getIdsByActivityId(labelList.get(0).getActivityId());
        List<Integer> secondaryDelIds = new ArrayList<>();
        List<AfterSaleActivityPackageSecondaryLabel> secondaryAddList = new ArrayList<>();
        List<AfterSaleActivityPackageSecondaryLabel> secondaryUpdateList = new ArrayList<>();
        for (AfterSaleActivityPackagePrimaryLabelDto label : labelList) {
            if(StringUtils.isEmpty(label.getLabelName())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "一级标签名称为空");
            }
            List<Integer> sIds = null;
            if(label.getId()==null || label.getId() < 1){
                afterSaleActivityPackagePrimaryLabelWriteMapper.insert(label);
            }else{
                if(pIds != null && pIds.size()>0){
                    pIds.remove(label.getId());
                }
                sIds = afterSaleActivityPackageSecondaryLabelReadMapper.getIdsByPrimaryLabelId(label.getId());
                afterSaleActivityPackagePrimaryLabelWriteMapper.updateById(label);
            }
            if(label.getSecondaryLabels() != null){
                List<AfterSaleActivityPackageSecondaryLabel> secondaryLabels = label.getSecondaryLabels();

                for (AfterSaleActivityPackageSecondaryLabel secondaryLabel : secondaryLabels) {
                    if(StringUtils.isEmpty(secondaryLabel.getLabelName())){
                        return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "二级标签名称为空");
                    }
                    secondaryLabel.setPrimaryLabelId(label.getId());
                    if(secondaryLabel.getId()==null || secondaryLabel.getId() < 1){
                        secondaryAddList.add(secondaryLabel);
                    }else{
                        if(sIds != null && sIds.size()>0){
                            sIds.remove(secondaryLabel.getId());
                        }
                        secondaryUpdateList.add(secondaryLabel);
                    }
                }
            }
            if(sIds != null && sIds.size()>0){
                secondaryDelIds.addAll(sIds);
            }
        }
        if(pIds != null && pIds.size()>0 ){
            afterSaleActivityPackagePrimaryLabelWriteMapper.delByIds(pIds);
            afterSaleActivityPackageLabelService.delByPrimaryLabelIds(pIds);
        }
        if(secondaryDelIds.size()>0){
            afterSaleActivityPackageSecondaryLabelWriteMapper.delByIds(secondaryDelIds);
            afterSaleActivityPackageLabelService.delBySecondaryIds(secondaryDelIds);
        }
        if(secondaryAddList.size()>0){
            afterSaleActivityPackageSecondaryLabelWriteMapper.batchInsert(secondaryAddList);
        }
        if(secondaryUpdateList.size()>0){
            afterSaleActivityPackageSecondaryLabelWriteMapper.batchUpdate(secondaryUpdateList);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText());
    }

}
