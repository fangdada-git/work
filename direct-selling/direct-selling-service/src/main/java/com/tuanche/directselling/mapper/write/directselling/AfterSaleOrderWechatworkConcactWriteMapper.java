package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AfterSaleOrderWechatworkConcactWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(AfterSaleOrderWechatworkConcact record);

    int updateByPrimaryKeySelective(AfterSaleOrderWechatworkConcact record);

    void addList(@Param("list") List<AfterSaleOrderWechatworkConcact> list);
}