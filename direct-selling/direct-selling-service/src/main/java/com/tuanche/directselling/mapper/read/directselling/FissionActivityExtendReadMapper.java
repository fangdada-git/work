package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionActivityExtendDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionActivityExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 裂变任务扩展mapper
 * @date 2020/9/22 16:55
 * @author lvsen
 */
public interface FissionActivityExtendReadMapper {

    List<FissionActivityExtend> selectActivityExtendByFissionId(@Param("fissionId") Integer fissionId);

    List<FissionActivityExtend> selectActivityExtendByTypeAndId(@Param("dateId") Integer dateId, @Param("fissionId") Integer fissionId,
                                                                @Param("dataType") Byte dataType);

    /**
     * 获取经销商参与的活动列表
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     */
    List<FissionActivity> getDealerActivityList(FissionActivityExtend fissionActivityExtend);
    /**
     * 根据扩展类型获取数据
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     */
    List<FissionActivityExtendDto> getActivityExtendByType(FissionActivityExtend fissionActivityExtend);
    /**
     * 获取经销商的结算状态列表
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     */
    List<FissionActivityExtendDto> getFissionDealerSettlementAccountList(@Param("fissionId") Integer fissionId);

}