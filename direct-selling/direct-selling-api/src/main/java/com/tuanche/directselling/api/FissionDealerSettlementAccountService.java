package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-03-09 17:31
 */
public interface FissionDealerSettlementAccountService {

    /**
     * 通过主键获取单个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    FissionDealerSettlementAccount getDealerSettlementAccountById(Integer id);
    /**
     * 经销商获取结算账号列表-分页
     * @author HuangHao
     * @CreatTime 2021-03-09 11:41
     * @param settlementAccount
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.FissionDealerSettlementAccountDto>
     */
    PageResult<FissionDealerSettlementAccountDto> getDealerListByPage(FissionDealerSettlementAccountDto settlementAccount);

    /**
     * 获取结算账户列表
     * @author HuangHao
     * @CreatTime 2021-04-19 16:06
     * @param settlementAccount
     * @return java.util.List<com.tuanche.directselling.dto.FissionDealerSettlementAccountDto>
     */
    List<FissionDealerSettlementAccountDto> getSettlementAccountList(FissionDealerSettlementAccountDto settlementAccount);
    /**
     * 获取经销商的结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:41
     * @param settlementAccount
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.FissionDealerSettlementAccountDto>
     */
    FissionDealerSettlementAccount getDealerSettlementAccount(FissionDealerSettlementAccount settlementAccount);

    /**
     * 获取经销商最后一个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-12 10:53
     * @param dealerId
     * @return com.tuanche.directselling.model.FissionDealerSettlementAccount
     */
    FissionDealerSettlementAccount getDealerLastSettlementAccount(Integer dealerId);
    /**
     * 保存结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 15:25
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse save(FissionDealerSettlementAccount settlementAccount);

    /**
     * 新增或更新结算账户
     * @author HuangHao
     * @CreatTime 2021-04-22 14:05
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse addOrUpdate(FissionDealerSettlementAccount settlementAccount);

    /**
     * 只新增
     * @param settlementAccount
     * @return
     */
    TcResponse addDealerSettlementAccount(FissionDealerSettlementAccount settlementAccount);
    /**
     * 获取经销商结算账户ID
     * @author HuangHao
     * @CreatTime 2021-03-12 11:42
     * @param fissionId
     * @param dealerId
     * @return java.lang.Integer
     */
    Integer getDealerSettlementAccountId(Integer fissionId,Integer dealerId);

    PageResult<FissionDealerSettlementAccountDto> getFissionDealerSettlementAccountList(FissionDealerSettlementAccountDto dealerSettlementAccountDto);

    void remit(Integer id);

    /**
     * 通过裂变ID获取结算账户信息，返回Map<dealerId,id>
     * @author HuangHao
     * @CreatTime 2021-04-07 15:21
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    Map<Integer,Integer> getMapByFissionId(Integer fissionId);

    /**
     * 批量插入结算账户
     * @author HuangHao
     * @CreatTime 2021-04-07 15:56
     * @param list
     * @return int
     */
    int batchInsert(List<FissionDealerSettlementAccount> list);

    /**
     * 批量标记为已打款
     * @param ids
     */
    void remitBatch(List<Integer> ids);

    /**
     * 逻辑删除fission_dealer_settlement_account
     *
     * @param fissionId
     * @param dealerId
     * @return
     */
    Integer deleteDealerSettlement(Integer fissionId, Integer dealerId);


    /**
     * 同步结算帐号
     * @param settlementAccount
     * @return
     */
    TcResponse syncDealerSettlement(FissionDealerSettlementAccount settlementAccount);
}
