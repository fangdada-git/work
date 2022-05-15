package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.AfterSaleTeacherService;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleTeacherReadMapper;
import com.tuanche.directselling.model.AfterSaleTeacher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/25 15:58
 **/
@Service
public class AfterSaleTeacherServiceImpl implements AfterSaleTeacherService {

    @Autowired
    AfterSaleTeacherReadMapper afterSaleTeacherReadMapper;

    @Override
    public AfterSaleTeacher selectByTeacherWxUnionId(String teacherWxUnionId) {
        return afterSaleTeacherReadMapper.selectByTeacherWxUnionId(teacherWxUnionId);
    }

    @Override
    public AfterSaleTeacher selectByPhone(String phone) {
        return afterSaleTeacherReadMapper.selectByPhone(phone);
    }
}
