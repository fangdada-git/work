package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FissionDealerSettlementAccountReadMapper {

    /**
     * 通过主键获取单个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    FissionDealerSettlementAccount getDealerSettlementAccountById(Integer id);
    /**
     * 获取单个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    FissionDealerSettlementAccount getDealerSettlementAccount(FissionDealerSettlementAccount fissionDealerSettlementAccount);
    /**
     * 获取经销商最后一个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    FissionDealerSettlementAccount getDealerLastSettlementAccount(Integer dealerId);
    /**
     * 获取结算账户列表
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    List<FissionDealerSettlementAccountDto> getSettlementAccountList(FissionDealerSettlementAccount fissionDealerSettlementAccount);

    /**
     * 获取经销商结算账户ID
     * @author HuangHao
     * @CreatTime 2021-03-12 11:38
     * @param fissionDealerSettlementAccount
     * @return java.lang.Integer
     */
    Integer getDealerSettlementAccountId(FissionDealerSettlementAccount fissionDealerSettlementAccount);

    /**
     * 通过裂变ID获取结算账户信息，返回Map<dealerId,id>
     * @author HuangHao
     * @CreatTime 2021-04-07 15:21
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    @MapKey("dealerId")
    Map<Integer,Integer> getMapByFissionId(Integer fissionId);
}