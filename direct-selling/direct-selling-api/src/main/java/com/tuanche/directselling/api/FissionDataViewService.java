package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionDataView;
import com.tuanche.directselling.model.FissionSaleDataView;
import com.tuanche.directselling.model.FissionUserDataView;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.*;

import java.util.List;

/**
 * 裂变活动B/C数据概览service
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 12:03
 **/
public interface FissionDataViewService {

    /**
     * 根据裂变活动ID返回B端数据概览
     *
     * @param fissionId 裂变活动id
     * @return FissionSaleDataView
     * @author ZhangYxin
     * @date 2020/9/23 12:03
     */
    FissionSaleDataView getBusinessFissionDataViewByFissionId(Integer fissionId);

    /**
     * 根据裂变活动ID返回C端数据概览
     *
     * @param fissionId 裂变活动id
     * @return FissionUserDataView
     * @author ZhangYxin
     * @date 2020/9/23 12:03
     */
    FissionUserDataView getCustomerFissionDataViewByFissionId(Integer fissionId);

    /**
     * 根据裂变活动ID返回C端数据概览
     *
     * @param fissionId 裂变活动id
     * @param dataType 数据类型 0:B端数据 1:C端数据 2:不带销售标识 3:用户运营渠道 4:销售渠道
     * @return FissionUserDataView
     * @author ZhangYxin
     * @date 2020/9/23 12:03
     */
    List<FissionDataView> getFissionDataViewByFissionIdAndTypeAndIsShare(Integer fissionId, Integer dataType, Boolean isShare);

    /**
     * 销售排行
     *
     * @param fissionId 裂变活动id
     * @return
     */
    List<FissionActivityRankVo> getBusinessRankSale(Integer fissionId);

    /**
     * 所有销售排行-按经销商名称查询
     *
     * @param fissionId 裂变活动id
     * @return
     */
    PageResult getBusinessRankAllSale(int page, int limit, Integer fissionId, String companyName);
    /**
     * 所有销售排行-按经销商ID查询
     *
     * @param fissionId 裂变活动id
     * @return
     */
    PageResult getBusinessRankAllSale(int page, int limit, Integer fissionId, List<Integer> csDealerIds, List<Integer> saleIds);

    List<FissionAllSaleRankVo> getBusinessRankAllSale(Integer fissionId, String companyName);

    /**
     * 经销商排行 limit 10
     *
     * @param fissionId
     * @return
     */
    List<FissionActivityRankVo> getBusinessRankDealer(Integer fissionId);

    /**
     * 所有经销商排行
     *
     * @param fissionId
     * @return
     */
    PageResult getBusinessRankAllDealer(int page, int limit, Integer fissionId, String companyName);

    List<FissionAllDealerRankVo> getBusinessRankAllDealer(Integer fissionId, String companyName);


    /**
     * 销售顾问奖金发放统计
     *
     * @param fissionId
     * @return
     */
    PageResult getBusinessRewardList(int page, int limit, Integer fissionId);

    List<FissionSaleRewardVo> getBusinessRewardList(Integer fissionId);

    /**
     * C端裂变奖金排行
     *
     * @param fissionId
     * @return
     */
    List<FissionUserRewardRankVo> getUserRewardRank(Integer fissionId, Integer channelId);
}
