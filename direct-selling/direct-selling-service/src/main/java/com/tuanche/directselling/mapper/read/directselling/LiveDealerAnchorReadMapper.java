package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.directselling.vo.LiveDealerAnchorVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveDealerAnchorReadMapper extends MyBatisBaseDao<LiveDealerAnchor, Integer> {

    List<LiveDealerAnchor> getLiveDealerAnchorByPv(LiveDealerAnchorVo liveDealerAnchorVo);

    List<LiveDealerAnchor> getLiveDealerAnchorByLiveTime(LiveDealerAnchorVo liveDealerAnchorVo);

    List<LiveDealerAnchor> getAnchorLiveDataByCityId(LiveDealerAnchorVo liveDealerAnchorVo);

    List<LiveDealerAnchor> getAnchorCityList();

    LiveDealerAnchor getLiveDealerAnchorByInfo(LiveDealerAnchor liveDealerAnchor);

    List<LiveDealerAnchor> searchAnchorList(LiveDealerAnchor liveDealerAnchor);

    List<LiveDealerAnchor> searchAnchorSceneList(LiveDealerAnchor liveDealerAnchor);

    List<Integer> transformDealerIds(@Param("anchorIds")List<Long> anchorIds);
}