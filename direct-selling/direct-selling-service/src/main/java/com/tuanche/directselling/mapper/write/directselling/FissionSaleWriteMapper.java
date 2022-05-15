package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionSale;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FissionSaleWriteMapper extends MyBatisBaseDao<FissionSale, Integer> {

    /**
     * 获取单个销售信息
     * @author HuangHao
     * @CreatTime 2021-02-19 16:49
     * @param fissionSale
     * @return com.tuanche.directselling.model.FissionSale
     */
    FissionSale getFissionSale(FissionSale fissionSale);
    /**
     * 审核/打款操作
     * @param fissionSalePayDto
     * @return
     */
    int financialAuditSalePay(FissionSalePayDto fissionSalePayDto);

    /**
     * 冻结/解冻操作
     * @param fissionSalePayDto
     * @return
     */
    int freezeOrUnFreezeFissionSale(FissionSalePayDto fissionSalePayDto);

    /**
     * 销售提现操作
     * @param fissionSalePayDto
     * @return
     */
    int saleWithDrawal(FissionSalePayDto fissionSalePayDto);

    /**
     * 批量更新销售收入
     * @author HuangHao
     * @CreatTime 2020-10-10 10:11
     * @param list
     * @return int
     */
    int batchUpdateSaleIncome(@Param("list")List<FissionSale> list);

    /**
     * 可提现销售列表
     * @param fissionSalePayDto
     * @return
     */
    List<FissionSale> selectPayListByFissionId(FissionSalePayDto fissionSalePayDto);

    /**
     * 是否销售有
     * @author HuangHao
     * @CreatTime 2021-01-20 10:59
     * @param fissionSale
     * @return int
     */
    Integer hasSale(FissionSale fissionSale);
}