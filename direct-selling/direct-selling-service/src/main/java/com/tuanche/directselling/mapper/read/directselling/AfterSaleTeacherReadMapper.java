package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleTeacher;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleTeacherReadMapper {
    AfterSaleTeacher selectByPrimaryKey(Integer id);

    AfterSaleTeacher selectByTeacherWxUnionId(@Param("teacherWxUnionid") String teacherWxUnionid);

    AfterSaleTeacher selectByPhone(@Param("phone") String phone);
}