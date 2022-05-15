package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 13:55
 */
public interface AfterSaleUserReadMapper {

    /**
     * 获取单个用户
     * @author HuangHao
     * @CreatTime 2021-07-21 14:12
     * @param afterSaleUser
     * @return com.tuanche.directselling.model.AfterSaleUser
     */
    AfterSaleUser getUser(AfterSaleUser afterSaleUser);

    /**
     * 获取UV
     * @author HuangHao
     * @CreatTime 2021-08-24 11:42
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getUvMap(@Param("activityIds") List<Integer> activityIds);

    List<AfterSaleUser> getUserListByActivityIdAndUnionId(@Param("userList") List<AfterSaleUser> userList);
}
