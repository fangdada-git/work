package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.AfterSaleActivity;

public interface AfterSaleActivityWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleActivity record);

    int insertSelective(AfterSaleActivity record);

    AfterSaleActivity selectByPrimaryKey(Integer id);

    /**
     * @description 不是原始方法，已对品牌，礼品券字段做了修改
     * @param [record]
     * @return int
     * @date 2021/11/11 11:29
     * @author lvsen
     */
    int updateByPrimaryKeySelective(AfterSaleActivity record);

    /**
     * 开启关闭分账功能
     * @param record
     * @return
     */
    int openCloseSubAccountById(AfterSaleActivity record);

}