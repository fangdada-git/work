package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.LiveProgramDto;
import com.tuanche.directselling.dto.PosterDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveProgram;
import com.tuanche.directselling.vo.LiveProgramVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LiveProgramDAO继承基类
 */
@Repository
public interface LiveProgramReadMapper extends MyBatisBaseDao<LiveProgram, Integer> {
	/**
     * 查询一个对象列表
     */
    public List<LiveProgramDto> queryList(LiveProgramVo liveProgramVo);

    List<PosterDto> selectPosterByProgramId(@Param("id") Integer id);
}