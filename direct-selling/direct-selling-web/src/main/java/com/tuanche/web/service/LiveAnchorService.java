package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.api.LiveDealerAnchorService;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.inner.sso.core.user.XxlUser;
import org.springframework.stereotype.Service;

/**
 * @Author: czy
 * @Date: 2020/5/25 13:45
 * @Version 1.0
 */
@Service
public class LiveAnchorService {

    @Reference
    private LiveDealerAnchorService liveDealerAnchorService;

    /**
     * @param liveDealerAnchor
     * @param xxlUser
     * @return: java.lang.Integer
     * @description: 直播账号操作
     * @author: czy
     * @create: 2020/5/25 14:47
     **/
    public Integer doLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor, XxlUser xxlUser){
        liveDealerAnchor.setOperatorUserId(xxlUser.getId());
        return liveDealerAnchorService.saveAndUpdateLiveDealerAnchor(liveDealerAnchor);
    }
}
