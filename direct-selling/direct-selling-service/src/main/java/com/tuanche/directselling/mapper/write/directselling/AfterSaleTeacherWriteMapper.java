package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleTeacher;

public interface AfterSaleTeacherWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleTeacher record);

    int insertSelective(AfterSaleTeacher record);

    int updateByPrimaryKeySelective(AfterSaleTeacher record);

    int updateByPrimaryKey(AfterSaleTeacher record);
}