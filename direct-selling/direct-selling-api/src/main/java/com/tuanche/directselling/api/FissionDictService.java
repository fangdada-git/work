package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionDict;

import java.util.List;

/**
 * 裂变字典service
 */
public interface FissionDictService {

    /**
     * 任务字典
     * @param type 任务类型
     * @return
     */
    List<FissionDict> getFissionDictByType(short type);
}
