package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.AfterSaleUserBrowseDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleUser;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 17:11
 */
public interface AfterSaleUserService {

    /**
     * 新增用户浏览记录
     * @author HuangHao
     * @CreatTime 2021-07-21 17:09
     * @param afterSaleUserBrowseDto
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse userBrowse(AfterSaleUserBrowseDto afterSaleUserBrowseDto);

    /**
     * 获取用户-先从缓存获取没有再从数据库取
     * @author HuangHao
     * @CreatTime 2021-07-21 17:10
     * @param activityId
     * @param userWxUnionId
     * @return com.tuanche.directselling.model.AfterSaleUser
     */
    AfterSaleUser getUser(Integer activityId, String userWxUnionId);

    //key=activityId:userWxUnionId
    Map<String, AfterSaleUser> getUserMapByActivityIdAndUnionId (List<AfterSaleUser> userList);

    /**
     * 获取UV
     * @author HuangHao
     * @CreatTime 2021-08-24 11:42
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getUvMap(List<Integer> activityIds);
}
