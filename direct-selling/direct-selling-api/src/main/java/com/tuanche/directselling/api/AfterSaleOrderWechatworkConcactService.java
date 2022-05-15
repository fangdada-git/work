package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;

import java.util.List;
import java.util.Map;

public interface AfterSaleOrderWechatworkConcactService {

    Integer edit (AfterSaleOrderWechatworkConcact orderWechatworkConcact);

    void addList (List<AfterSaleOrderWechatworkConcact> list);

    List<AfterSaleOrderWechatworkConcact> getList (AfterSaleOrderWechatworkConcact orderWechatworkConcact);

    Map<String, AfterSaleOrderWechatworkConcact> getMapByOrderCode(List<String> orderCodes);

    List<AfterSaleOrderWechatworkConcact> getWechatworkConcactList(AfterSaleOrderWechatworkConcact concact);

    List<String> getOrderByMaxWwUserId(String wwUserId, Integer activityId);
}
