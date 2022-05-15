package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:14
 */
public interface AfterSaleActivityPackagePrimaryLabelWriteMapper {

    /**
     * 删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByIds(@Param("ids")List<Integer> ids);
    /**
     * 新增
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param primaryLabel
     * @return int
     */
    int insert(AfterSaleActivityPackagePrimaryLabel primaryLabel);

    /**
     * 更新
     * @author HuangHao
     * @CreatTime 2021-11-24 14:20
     * @param primaryLabel
     * @return int
     */
    int updateById(AfterSaleActivityPackagePrimaryLabel primaryLabel);
}
