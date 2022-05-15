package com.tuanche.web.api.aftersale;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleActivityPackagePrimaryLabelService;
import com.tuanche.directselling.dto.AfterSaleActivityPackagePrimaryLabelDto;
import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.api.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 给测试用来测试售后特卖奖励的controller
 *
 * @param
 * @author HuangHao
 * @CreatTime 2021-08-02 16:40
 * @return
 */
@Controller
@RequestMapping("/api/afterSale/packageLabel")
public class AfterSaleActivityPackageLabelApiController  {

    @Reference
    AfterSaleActivityPackagePrimaryLabelService afterSaleActivityPackagePrimaryLabelService;

    /**
     * 根据活动ID获取标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    @RequestMapping("/labellist")
    @ResponseBody
    public TcResponse getLabelsByActivityId(Integer activityId){
        List<AfterSaleActivityPackagePrimaryLabel> list = null;
        try {
            list = afterSaleActivityPackagePrimaryLabelService.getLabelsByActivityId(activityId);
        }catch (Exception e){
            return new TcResponse(StatusCodeEnum.ERROR.getCode(),"系统繁忙，请稍后再试", list);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText(), list);
    }

}
