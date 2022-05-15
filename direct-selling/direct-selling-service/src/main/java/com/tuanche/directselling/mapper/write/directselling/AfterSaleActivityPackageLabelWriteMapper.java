package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2021-11-22 18:09
 */
public interface AfterSaleActivityPackageLabelWriteMapper {

    /**
     * 根据主键删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByIds(@Param("ids") List<Integer> ids);
    /**
     * 根据一级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delByPrimaryLabelIds(@Param("primaryLabelIds") List<Integer> primaryLabelIds);
    /**
     * 根据二级标签ID删除
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param id
     * @return int
     */
    int delBySecondaryIds(@Param("secondaryIds") List<Integer> secondaryIds);
    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2021-11-24 14:19
     * @param primaryLabel
     * @return int
     */
    int batchInsert(@Param("list")List<AfterSaleActivityPackageLabel> list);

    /**
     * 批量更新
     * @author HuangHao
     * @CreatTime 2021-11-24 14:20
     * @param primaryLabel
     * @return int
     */
    int batchUpdate(@Param("list")List<AfterSaleActivityPackageLabel> list);
}
