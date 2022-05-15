package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.api.AfterSaleOrderWechatworkConcactService;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/afterSale/wechatwork")
public class AfterSaleOrderWechatworkConcactController {

    @Reference
    AfterSaleOrderWechatworkConcactService afterSaleOrderWechatworkConcactService;

    @RequestMapping("/getWechatworkConcactList")
    @ResponseBody
    public List<AfterSaleOrderWechatworkConcact> getWechatworkConcactList(AfterSaleOrderWechatworkConcact concact) {
        List<AfterSaleOrderWechatworkConcact> list = afterSaleOrderWechatworkConcactService.getWechatworkConcactList(concact);
        return list;
    }

}
