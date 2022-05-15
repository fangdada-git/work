package com.tuanche.directselling.web;

import com.alibaba.fastjson.JSON;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import com.tuanche.directselling.service.impl.FashionCarMarkeHelperUserServiceImpl;
import com.tuanche.directselling.service.impl.FashionCarMarkeUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private FashionCarMarkeHelperUserServiceImpl fashionCarMarkeHelperUserServiceImpl;
    @Autowired
    private FashionCarMarkeUserServiceImpl fashionCarMarkeUserService;

    @RequestMapping("test")
    public void test () {
//        FashionCarMarkeHelperUser helperUser = new FashionCarMarkeHelperUser();
//        Map<Integer, FashionCarMarkeUser> kyeUserIdMapByUserId = fashionCarMarkeUserService.getKyeUserIdMapByUserId(Arrays.asList(41), null);
//        System.out.println(JSON.toJSONString(kyeUserIdMapByUserId));
    }

}
