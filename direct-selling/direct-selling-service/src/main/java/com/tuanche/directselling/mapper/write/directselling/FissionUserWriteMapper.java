package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 15:57
 */
@Repository
public interface FissionUserWriteMapper {

    /**
     * 用户是否存在于裂变活动中-主从同步可能没有这么及时，因此从写库查
     * @author HuangHao
     * @CreatTime 2020-09-23 15:51
     * @param userWxOpenId
     * @param fissionId
     * @return java.lang.Integer
     */
    Integer userExistInFissionId(@Param("userWxUnionId")String userWxUnionId, @Param("fissionId")Integer fissionId);
    /**
     * 获取裂变活动用户
     * @author HuangHao
     * @CreatTime 2020-09-23 15:51
     * @param userWxOpenId
     * @param fissionId
     * @return java.lang.Integer
     */
    FissionUser getFissionUser(FissionUser fissionUser);
    /**
     * 新增裂变用户
     * @author HuangHao
     * @CreatTime 2020-09-23 15:57
     * @param fissionUser
     * @return int
     */
    int insertFissionUser(FissionUser fissionUser);
    /**
     * 更新裂变用户
     * @author HuangHao
     * @CreatTime 2020-09-23 15:57
     * @param fissionUser
     * @return int
     */
    int updateFissionUser(FissionUser fissionUser);
}
