package com.tuanche.directselling.api;

import java.util.List;

import com.tuanche.directselling.dto.LiveProgramDto;
import com.tuanche.directselling.model.LiveProgramDealerBrand;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.PosterModel;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveProgramVo;

/**
 *
 * @Description 节目相关服务
 * @author zhangxing
 * @version
 * @date 2020年3月5日上午11:50:49
 */
public interface LiveProgramService {

	/**
	 * 节目数据查询
	 * @param liveProgramDto
	 * @return
	 */
	PageResult findProgramList(PageResult<LiveProgramDto> pageResult,LiveProgramVo liveProgramVo);

	List<LiveProgramDto> queryList(LiveProgramVo liveProgramVo);

	List<LiveSceneDealerBrand> searchBrands(LiveProgramVo liveProgramVo);

	List<LiveSceneDealerBrand> searchDealers(LiveProgramVo liveProgramVo);

	List<LiveSceneDealerBrand> searchDealerBrands(LiveProgramVo liveProgramVo);
	void save(LiveProgramVo liveProgramVo) throws Exception;

	void updateProgramDealer(LiveProgramVo liveProgramVo) throws Exception;
	void update(LiveProgramVo liveProgramVo) throws Exception;

    /**
     * @param dealerBrandId
     * @return java.util.List<com.tuanche.directselling.model.LiveProgramDealerBrand>
     * @Author GongBo
     * @Description 根据场次活动经销商品牌ID  获取节目关联的经销商品牌数据
     * @Date 16:28 2020/3/9
     **/
	List<LiveProgramDealerBrand> findDealerBrandList(Integer dealerBrandId);

    List<PosterModel> selectPosterByProgramId(Integer id);

    Integer getPosterModelNumb(Integer id);
}
