package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleUserShareService;
import com.tuanche.directselling.model.AfterSaleUserShare;
import com.tuanche.framework.util.IPUtil;
import com.tuanche.web.api.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/api/v2/aftersale/share")
@RestController
public class AfterSaleUserShare2Controller extends BaseController {

    @Reference
    AfterSaleUserShareService afterSaleUserShareService;

    /**
     * 分享记录
     *
     * @param agentRegister
     * @return
     */
    @PostMapping("/record")
    public TcResponse share(HttpServletRequest request, AfterSaleUserShare afterSaleUserShare) {
        long st = System.currentTimeMillis();
        String referer = request.getHeader("Referer");
        afterSaleUserShare.setPageUrl(referer);
        String ip = IPUtil.getRequestIp(request);
        afterSaleUserShare.setIp(ip);
        afterSaleUserShareService.insert(afterSaleUserShare);
        return success(0, st);
    }

}
