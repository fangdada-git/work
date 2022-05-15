package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleTeacher;

public interface AfterSaleTeacherService {

    AfterSaleTeacher selectByTeacherWxUnionId(String teacherWxUnionId);

    AfterSaleTeacher selectByPhone(String phone);
}
