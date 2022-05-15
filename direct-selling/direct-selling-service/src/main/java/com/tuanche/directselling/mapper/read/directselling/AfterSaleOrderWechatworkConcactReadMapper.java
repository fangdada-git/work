package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleOrderWechatworkConcactReadMapper {

    AfterSaleOrderWechatworkConcact selectByPrimaryKey(Integer id);

    List<AfterSaleOrderWechatworkConcact> getList(AfterSaleOrderWechatworkConcact orderWechatworkConcact);

    List<AfterSaleOrderWechatworkConcact> getListByOrderCodes(@Param("orderCodes") List<String> orderCodes);

    List<AfterSaleOrderWechatworkConcact> getWechatworkConcactList(AfterSaleOrderWechatworkConcact orderWechatworkConcact);

    List<String> getOrderByMaxWwUserId(@Param("wwUserId")String wwUserId, @Param("activityId")Integer activityId);
}