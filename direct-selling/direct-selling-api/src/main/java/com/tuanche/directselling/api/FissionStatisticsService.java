package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionDataView;
import com.tuanche.directselling.model.FissionSaleDataView;
import com.tuanche.directselling.model.FissionUserDataView;

import java.util.List;
import java.util.Map;

/**
 * 统计相关service
 *
 * @author Administrator
 */
public interface FissionStatisticsService {

    /**
     * 修改B端统计概览
     */
    void updateSaleDataView();

    /**
     * 修改C端统计概览
     */
    void updateUserDataView();

    /**
     * 修改 用户运营渠道 销售渠道统计数据
     * @param statGenerate 2进制数 从右到左 B端数据;C端数据;不带销售标识;用户运营渠道;销售渠道 是否已完成
     * @param dataType 数据类型 0:B端数据 1:C端数据 2:不带销售标识 3:用户运营渠道 4:销售渠道
     * @param channel 渠道
     */
    void updateDataView(int statGenerate, int dataType, int channel);

    /**
     * 更新FissionSaleDataView
     *
     * @param fissionSaleDataView
     */
    void insertFissionSaleDataView(FissionSaleDataView fissionSaleDataView);

    /**
     * 更新fissionSaleDataView
     *
     * @param fissionSaleDataView
     */
    void updateFissionSaleDataView(Long updateDataViewId, Long updateSaleDataVieId, FissionSaleDataView fissionSaleDataView);


    /**
     * 更新FissionUserDataView
     *
     * @param fissionUserDataView
     */
    void insertFissionUserDataView(FissionUserDataView fissionUserDataView);

    /**
     * 更新FissionUserDataView
     *
     * @param fissionuserDataView
     */
    void updateFissionUserDataView(Long updateDataViewId, Long updateUserDataVieId, FissionUserDataView fissionuserDataView);

    /**
     * 获取FissionDataView的map结构,Map<fissionId, Map<dataType,List<FissionDataView>>>
     *
     * @param fissionId
     * @return
     */
    Map<Integer, List<FissionDataView>> getFissionDataViewMap(Integer fissionId);
}
