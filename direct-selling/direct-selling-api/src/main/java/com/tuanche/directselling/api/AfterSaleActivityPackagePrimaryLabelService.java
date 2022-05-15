package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.AfterSaleActivityPackagePrimaryLabelDto;
import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-24 16:05
 */
public interface AfterSaleActivityPackagePrimaryLabelService {

    /**
     * 根据活动ID获取标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    List<AfterSaleActivityPackagePrimaryLabel> getLabelsByActivityId(Integer activityId);

    /**
     * 保存标签
     * @author HuangHao
     * @CreatTime 2021-11-24 16:05
     * @param labelList
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse save(List<AfterSaleActivityPackagePrimaryLabelDto> labelList);
}
