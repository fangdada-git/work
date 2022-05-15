package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.FissionSaleExistDto;
import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.dto.FissionSaleRewardReconciliationDto;
import com.tuanche.directselling.dto.FissionStatisticBigDecimalDto;
import com.tuanche.directselling.dto.SaleRewardReconciliationDto;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.FissionActivityDetailVo;
import com.tuanche.directselling.vo.FissionSaleScoreVo;
import com.tuanche.directselling.vo.FissionSaleWalletVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 裂变活动销售相关service
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:15
 **/
public interface FissionSaleService {

    /**
     * 获取单个销售
     *
     * @param fissionId 活动ID
     * @return
     */
    FissionSale getFissionSale(FissionSale fissionSale);

    /**
     * 根据FissionId和WxUnionId获取单个销售-走缓存
     *
     * @param fissionId
     * @param wxUnionId
     * @return void
     * @author HuangHao
     * @CreatTime 2021-02-19 16:52
     */
    FissionSale getFissionSaleByFissionIdAndWxUnionId(Integer fissionId, String wxUnionId);

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
     * 根据销售id查询总积分
     *
     * @param saleId 销售ID
     * @return 积分
     */
    Integer getSaleIntegralBySaleId(int saleId);

    /**
     * B端前台个人中心积分页数据
     *
     * @param page   页
     * @param limit  每页条数
     * @param saleId 经销商ID
     * @param saleId 销售ID
     * @return 积分详细列表数据
     */
    PageResult getSaleIntegralListByBySaleId(int page, int limit, int saleId);

    /**
     * 判断销售是否存在于列表活动中
     *
     * @param fissionId
     * @param saleWxOpenId
     * @return -1 参数错误 0：不存在 1：存在
     * @author HuangHao
     * @CreatTime 2020-09-27 10:08
     */
    int saleExistInFission(Integer fissionId, String saleWxOpenId);

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
     * @param pageResult
     * @param fissionSale
     * @return
     */
    PageResult selectSalePayListByFissionId(PageResult<FissionSalePayDto> pageResult, FissionSale fissionSale);

    /**
     * @param fissionSale
     * @description: 销售打款审核列表总数
     * @return: java.lang.Integer
     * @author: dudg
     * @date: 2020/10/9 16:01
     */
    Integer selectSalePayListCountByFissionId(FissionSale fissionSale);

    /**
     * 提现金额
     *
     * @param saleId          销售ID
     * @param withdrawalState 提现状态 0：未提现 1：已提现
     * @return
     */
    BigDecimal selectFissionSaleWithdrawal(Integer saleId, Integer withdrawalState);

    /**
     * 我的钱包提现列表
     *
     * @param page
     * @param limit
     * @param saleId
     * @return
     */
    PageResult selectFissionSaleList(int page, int limit, Integer saleId);

    /**
     * 我的钱包
     *
     * @param page
     * @param limit
     * @param saleId 销售ID
     * @return
     */
    FissionSaleWalletVo selectFissionSaleWallet(int page, int limit, Integer saleId);

    /**
     * B端根据活动ID查详情
     *
     * @param dealerId  经销商ID
     * @param fissionId 活动ID
     * @param saleId    销售ID
     * @return 活动详情
     */
    FissionActivityDetailVo getActivitySaleDetail(int dealerId, int fissionId, int saleId);

    /**
     * 我的成绩
     *
     * @param saleId 销售ID
     * @return 销售最近一场活动的成绩
     */
    FissionSaleScoreVo getPersonalIntegralVo(int dealerId, int saleId, Integer fissionId);

    /**
     * @param fissionSale
     * @description: 更新裂变销售表
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 16:18
     */
    int updateByPrimaryKeySelective(FissionSale fissionSale);

    /**
     * @param fissionSalePayDto
     * @description: 销售奖励审核/打款
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 18:25
     */
    int financialAuditSalePay(FissionSalePayDto fissionSalePayDto);

    /**
     * @param fissionSalePayDto
     * @description: 冻结/解冻裂变活动销售
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 18:31
     */
    int freezeOrUnFreezeFissionSale(FissionSalePayDto fissionSalePayDto);

    /**
     * @param fissionSalePayDto
     * @description: 销售提现
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/9/28 11:36
     */
    TcResponse saleWithdrawal(FissionSalePayDto fissionSalePayDto);

    /**
     * 新增裂变销售
     *
     * @param fissionSale
     * @return int
     * @author HuangHao
     * @CreatTime 2020-10-09 10:27
     */
    int insertFissionSale(FissionSale fissionSale);

    /**
     * 销售预计收入计算
     *
     * @param fissionIds 裂变活动IDS
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 10:23
     */
    TcResponse estimatedIncome(List<Integer> fissionIds);

    /**
     * 销售实际收入计算
     *
     * @param fissionIds 裂变活动IDS
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 10:23
     */
    TcResponse realIncome(List<Integer> fissionIds);

    /**
     * @param fissionId
     * @description: 获取单个裂变活动下B销售总奖金池
     * @return: java.math.BigDecimal
     * @author: dudg
     * @date: 2020/10/10 15:42
     */
    BigDecimal getSalePrizePoolByFissionId(Integer fissionId);

    /**
     * 销售最终奖励金对账数据 for ebs-console
     *
     * @param fissionId 裂变活动ID
     * @return List<FissionSaleRewardReconciliationDto>
     */
    List<FissionSaleRewardReconciliationDto> getSaleRewardReconciliationDtoList(SaleRewardReconciliationDto saleRewardReconciliationDto);

    /**
     * 销售最终奖励金对账数据 for ebs-console
     *
     * @param page      页
     * @param limit     每页条数
     * @param fissionId 裂变活动ID
     * @return PageResult
     */
    PageResult getSaleRewardReconciliationDtoPage(SaleRewardReconciliationDto saleRewardReconciliationDto);


    /**
     * 裂变活动的实发奖金 for ebs-console
     *
     * @param fissionId 裂变活动ID
     * @return
     */
    FissionStatisticBigDecimalDto selectPrizeActualByFissionId(Integer fissionId, Date withdrawalBeginTime, Date withdrawalEndTime);
}
