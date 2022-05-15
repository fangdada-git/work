package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionSaleDto;
import com.tuanche.directselling.dto.FissionSaleExistDto;
import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.dto.FissionSaleRewardReconciliationDto;
import com.tuanche.directselling.model.FissionSale;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface FissionSaleReadMapper {

    /**
     * 获取单个销售
     *
     * @param fissionId 活动ID
     * @return
     */
    FissionSale getFissionSale(FissionSale fissionSale);

    /**
     * 获取裂变活动销售列表
     *
     * @param fissionSale
     * @return java.util.List<com.tuanche.directselling.model.FissionSale>
     * @author HuangHao
     * @CreatTime 2020-10-09 17:03
     */
    List<FissionSale> getFissionSaleList(FissionSale fissionSale);

    /**
     * 经销商排名
     *
     * @param fissionId 活动ID
     * @return
     */
    List<FissionSale> selectFissionDealerRank(@Param("fissionId") Integer fissionId);


    /**
     * 销售排名 前10
     *
     * @param fissionId 活动ID
     * @return
     */
    List<FissionSale> selectFissionSaleRank(@Param("fissionId") Integer fissionId);


    /**
     * 所有销售排名
     *
     * @param fissionId 活动ID
     * @return
     */
    List<FissionSale> selectFissionAllSaleRank(@Param("fissionId") Integer fissionId, @Param("dealerIds") List<Integer> dealerIds, @Param("saleIds") List<Integer> saleIds);

    /**
     * 所有经销商排行
     *
     * @param fissionId
     * @return
     */
    List<FissionSaleDto> selectFissionAllDealerRank(@Param("fissionId") Integer fissionId, @Param("dealerIds") List<Integer> dealerIds);

    /**
     * 店内销售排名 前10
     *
     * @param fissionId 活动ID
     * @return
     */
    List<FissionSale> selectFissionDealerSaleRank(@Param("fissionId") Integer fissionId, @Param("dealerId") Integer dealerId);

    /**
     * 判断销售是否存在于列表活动中
     *
     * @param fissionId
     * @param saleWxOpenId
     * @return null:不存在 1：存在
     * @author HuangHao
     * @CreatTime 2020-09-27 10:08
     */
    Integer saleExistInFission(@Param("fissionId") Integer fissionId, @Param("saleWxOpenId") String saleWxOpenId);

    /**
     * 检测用户和分享人是否是销售
     *
     * @param fissionSaleExistDto
     * @return java.lang.Integer
     * @author HuangHao
     * @CreatTime 2020-10-14 11:42
     */
    FissionSaleExistDto userAndShareUserIsSalesman(FissionSaleExistDto fissionSaleExistDto);

    /**
     * 销售打款审核列表
     *
     * @param fissionSale
     * @return
     */
    List<FissionSalePayDto> selectSalePayListByFissionId(FissionSale fissionSale);

    /**
     * 销售打款审核列表总数
     *
     * @param fissionSale
     * @return
     */
    Integer selectSalePayListCountByFissionId(FissionSale fissionSale);

    /**
     * 根据活动ID和销售ID查提现金额
     *
     * @param saleId          销售ID
     * @param withdrawalState 提现状态
     * @return 已提现金额
     */
    BigDecimal selectFissionSaleWithdrawal(@Param("saleId") Integer saleId, @Param("state") Integer withdrawalState);


    /**
     * 根据销售ID查询提现 待提现列表数据
     *
     * @param saleId 销售ID
     * @return
     */
    List<FissionSaleDto> selectFissionSaleList(@Param("saleId") Integer saleId);

    /**
     * 根据销售ID查最新参加的活动ID
     *
     * @param saleId 销售ID
     * @return
     */
    Integer selectNewestFissionIdBySaleId(@Param("saleId") Integer saleId);

    /**
     * 根据活动ID和销售ID查
     *
     * @param fissionId 活动ID
     * @param saleId    销售ID
     * @return FissionSale
     */
    FissionSale selectFissionSaleByFissionAndIdSaleId(@Param("fissionId") Integer fissionId, @Param("saleId") Integer saleId);

    /**
     * 根据销售id获取待提现列表
     *
     * @param saleId 销售id
     * @return
     */
    List<FissionSale> selectWithdrawalFissionListBySaleId(@Param("saleId") Integer saleId);

    /**
     * 获取裂变活动销售支付的对赌总金额
     *
     * @param
     * @return java.util.Map<java.lang.Integer, com.tuanche.directselling.model.FissionSale>
     * @author HuangHao
     * @CreatTime 2020-10-09 16:40
     */
    @MapKey("fissionId")
    Map<Integer, FissionSale> getTotalAmountByFissionIds(@Param("fissionIds") List<Integer> fissionIds);

    /**
     * 销售奖金发放
     *
     * @param fissionId
     * @return
     */
    List<FissionSale> selectFissionSaleReward(@Param("fissionId") Integer fissionId);

    /**
     * 销售最终奖励金对账数据 for ebs-console
     *
     * @param fissionId 活动id
     * @return List<FissionSaleRewardReconciliationDto>
     */
    List<FissionSaleRewardReconciliationDto> getSaleRewardReconciliationDtoList(@Param("fissionId") Integer fissionId, @Param("withdrawalBeginTime") Date withdrawalBeginTime, @Param("withdrawalEndTime") Date withdrawalEndTime);

    /**
     * 每个销售在活动中获得的奖励
     *
     * @param fissionId 活动id
     * @return List<FissionSale>
     */
    List<FissionSale> selectFissionRewardGroupSale(@Param("fissionId") Integer fissionId);

    /**
     * 获取裂变活动下的所有经销商真实收入，返回MAP<dealer_id,real_income>
     * @author HuangHao
     * @CreatTime 2021-04-14 10:57
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.math.BigDecimal>
     */
    @MapKey("dealerId")
    Map<Integer,FissionSale> getDealerRealIncomeByFissionId(@Param("fissionId") Integer fissionId);
}