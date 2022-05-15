package com.tuanche.directselling.api;

import java.util.List;

import com.tuanche.directselling.dto.LiveProgramDealerBrandDto;


public interface LiveProgramDealerBrandService {

	List<LiveProgramDealerBrandDto> queryList(LiveProgramDealerBrandDto liveProgramDealerBrandDto);
}
