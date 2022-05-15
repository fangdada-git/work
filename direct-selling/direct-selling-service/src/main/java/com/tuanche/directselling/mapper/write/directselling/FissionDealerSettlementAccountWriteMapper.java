package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import org.apache.ibatis.annotations.Param;
import scala.Int;

import java.util.List;

public interface FissionDealerSettlementAccountWriteMapper {
    /**
     */
    int insert(FissionDealerSettlementAccount record);

    /**
     * 批量插入结算账户
     * @author HuangHao
     * @CreatTime 2021-04-07 15:56
     * @param list
     * @return int
     */
    int batchInsert(List<FissionDealerSettlementAccount> list);

    /**
     */
    int updateById(FissionDealerSettlementAccount record);

    /**
     * 批量标记为已打款且更新结算账户
     * @param ids
     * @return
     */
    int updatePaymentStatusByIds(List<Integer> ids);

    /**
     * 逻辑删除
     * @param fissionId
     * @param dealerId
     * @return
     */
    int deleteDealerLastSettlementAccountByFissionIdAndDealerId(@Param("fissionId") Integer fissionId, @Param("dealerId") Integer dealerId);
}