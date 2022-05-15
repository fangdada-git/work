package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:16
 */
public interface AfterSaleActivityPackageSecondaryLabelWriteMapper {

    /**
     * 删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByIds(@Param("ids") List<Integer> ids);
    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param primaryLabel
     * @return int
     */
    int batchInsert(@Param("list")List<AfterSaleActivityPackageSecondaryLabel> list);

    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-11-24 14:20
     * @param primaryLabel
     * @return int
     */
    int batchUpdate(@Param("list")List<AfterSaleActivityPackageSecondaryLabel> list);
}
