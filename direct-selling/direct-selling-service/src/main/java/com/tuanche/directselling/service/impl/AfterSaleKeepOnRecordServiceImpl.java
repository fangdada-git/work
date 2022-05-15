package com.tuanche.directselling.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.apply.api.TcClueService;
import com.tuanche.apply.dto.TcApplyRequestDto;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleKeepOnRecordService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleBrandCarSeriesDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordBrandCarSeriesDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordDto;
import com.tuanche.directselling.enums.AfterSaleUserType;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleBrandReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleCarSeriesReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleKeepOnRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleOrderRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleCarSeriesWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleKeepOnRecordWriteMapper;
import com.tuanche.directselling.model.AfterSaleBrand;
import com.tuanche.directselling.model.AfterSaleCarSeries;
import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.bean.manufacturer.CsCompany;
import com.tuanche.eap.api.service.manufacturer.CsCompanyService;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import com.tuanche.storage.api.carstyle.CarStyleService;
import com.tuanche.storage.dto.carstyle.CarStyleDto;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-05 10:58
 */
@Service(retries = 0)
public class AfterSaleKeepOnRecordServiceImpl implements AfterSaleKeepOnRecordService {

    @Reference(timeout = 12000, check = false, interfaceName = "com.tuanche.apply.api.TcClueService")
    public TcClueService tcClueService;
    @Autowired
    AfterSaleKeepOnRecordWriteMapper afterSaleKeepOnRecordWriteMapper;
    @Autowired
    AfterSaleKeepOnRecordReadMapper afterSaleKeepOnRecordRreadMapper;
    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Autowired
    AfterSaleKeepOnRecordReadMapper afterSaleKeepOnRecordReadMapper;
    @Autowired
    AfterSaleBrandReadMapper afterSaleBrandReadMapper;
    @Autowired
    AfterSaleBrandWriteMapper afterSaleBrandWriteMapper;
    @Autowired
    AfterSaleCarSeriesReadMapper afterSaleCarSeriesReadMapper;
    @Autowired
    AfterSaleCarSeriesWriteMapper afterSaleCarSeriesWriteMapper;
    @Autowired
    AfterSaleOrderRecordReadMapper afterSaleOrderRecordReadMapper;
    @Reference
    CarStyleService carStyleService;
    @Reference
    CsCompanyService csCompanyService;
    @Autowired
    @Qualifier("executorService")
    ExecutorService executorService;
    @Autowired
    @Qualifier("writeSqlSessionFactory")
    SqlSessionFactory writeSqlSessionFactory;
    @Autowired
    @Qualifier("readSqlSessionFactory")
    SqlSessionFactory readSqlSessionFactory;
    @Autowired
    @Qualifier("ClusterRedisService")
    RedisService redisService;

    /**
     * 根据ID获取记录
     *
     * @param id
     * @return com.tuanche.directselling.model.AfterSaleKeepOnRecord
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    public AfterSaleKeepOnRecord getById(Integer id) {
        if (id == null) {
            return null;
        }
        return afterSaleKeepOnRecordRreadMapper.getById(id);
    }

    @Override
    public AfterSaleKeepOnRecord getByIdAndDealerId(Integer id, Integer dealerId) {
        return afterSaleKeepOnRecordRreadMapper.getByIdAndDealerId(id, dealerId);
    }

    @Override
    public int deleteByIdAndDealerId(Integer id, Integer dealerId) {
        return afterSaleKeepOnRecordWriteMapper.deleteByIdAndDealerId(id, dealerId);
    }

    /**
     * 获取备案记录列表-分页
     *
     * @param afterSaleKeepOnRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleKeepOnRecord>
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    @Override
    public PageResult<AfterSaleKeepOnRecord> getKeepOnRecordListByPage(PageResult<AfterSaleKeepOnRecord> pageResult, AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleKeepOnRecord> list = afterSaleKeepOnRecordRreadMapper.getKeepOnRecordList(afterSaleKeepOnRecord);
        PageInfo<AfterSaleKeepOnRecord> page = new PageInfo<AfterSaleKeepOnRecord>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    @Override
    public List<AfterSaleKeepOnRecord> getKeepOnRecordList(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        return afterSaleKeepOnRecordRreadMapper.getKeepOnRecordList(afterSaleKeepOnRecord);
    }

    @Override
    public PageResult<AfterSaleKeepOnRecordBrandCarSeriesDto> getKeepOnRecordBrandCarSeriesListByPage(PageResult<AfterSaleKeepOnRecordBrandCarSeriesDto> pageResult, AfterSaleKeepOnRecordBrandCarSeriesDto afterSaleKeepOnRecord) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleKeepOnRecordBrandCarSeriesDto> list = afterSaleKeepOnRecordRreadMapper.getKeepOnRecordBrandCarSeriesList(afterSaleKeepOnRecord);
        PageInfo<AfterSaleKeepOnRecordBrandCarSeriesDto> page = new PageInfo(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 删除
     *
     * @param id
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:01
     */
    public int deleteById(Integer id) {
        if (id == null) {
            return 0;
        }
        AfterSaleKeepOnRecord afterSaleKeepOnRecord = new AfterSaleKeepOnRecord();
        afterSaleKeepOnRecord.setId(id);
        afterSaleKeepOnRecord.setDeleteFlag(true);
        return afterSaleKeepOnRecordWriteMapper.updateById(afterSaleKeepOnRecord);
    }

    /**
     * 更新
     *
     * @param afterSaleKeepOnRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:02
     */
    @Override
    public TcResponse updateById(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        if (afterSaleKeepOnRecord == null || afterSaleKeepOnRecord.getId() == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动ID不能为空");
        }
        AfterSaleKeepOnRecord byId = afterSaleKeepOnRecordReadMapper.getById(afterSaleKeepOnRecord.getId());
        if (byId.getUserType() == 1) {
            if (StringUtils.isEmpty(afterSaleKeepOnRecord.getUserName())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "姓名不能为空");
            }
            if (StringUtils.isEmpty(afterSaleKeepOnRecord.getUserPhone())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "手机号不能为空");
            }
        }
        if (byId.getUserType() == 0) {
            if (StringUtils.isEmpty(afterSaleKeepOnRecord.getLicencePlate()) && StringUtils.isEmpty(afterSaleKeepOnRecord.getUserPhone())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "手机号和车牌号必填其一");
            }
        }

        if (!StringUtils.isEmpty(afterSaleKeepOnRecord.getUserPhone()) && !ManeBaseConsoleConstants.isMobile(afterSaleKeepOnRecord.getUserPhone())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "手机号格式不正确");
        }
        if (!StringUtils.isEmpty(afterSaleKeepOnRecord.getLicencePlate()) && !ManeBaseConsoleConstants.isCarLicensePlate(afterSaleKeepOnRecord.getLicencePlate())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "车牌号格式不正确");
        }

        AfterSaleKeepOnRecord keepOnRecord = afterSaleKeepOnRecordRreadMapper.getByLicencePlateOrPhone(afterSaleKeepOnRecord);
        if (keepOnRecord != null) {
            if (!StringUtils.isEmpty(keepOnRecord.getUserPhone()) && keepOnRecord.getUserPhone().equals(afterSaleKeepOnRecord.getUserPhone())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "手机号已存在");
            }
            if (!StringUtils.isEmpty(keepOnRecord.getLicencePlate()) && keepOnRecord.getLicencePlate().equals(afterSaleKeepOnRecord.getLicencePlate())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "车牌号已存在");
            }
        }
        afterSaleKeepOnRecordWriteMapper.updateById(afterSaleKeepOnRecord);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
    }


    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public TcResponse updateBrandCarSeriesById(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        if ("".equals(afterSaleKeepOnRecord.getBrandName()) || "".equals(afterSaleKeepOnRecord.getCarSeriesName())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), StatusCodeEnum.PARAM_IS_INVALID.getText());
        }
        if (afterSaleKeepOnRecord.getBrandName() == null || afterSaleKeepOnRecord.getCarSeriesName() == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), StatusCodeEnum.PARAM_IS_INVALID.getText());
        }
        Date timeNow = new Date();
        AfterSaleBrand afterSaleBrand = afterSaleBrandReadMapper.selectByOriginalBrandName(afterSaleKeepOnRecord.getOriginalBrandName());
        if (afterSaleBrand == null) {
            AfterSaleBrand brand = new AfterSaleBrand();
            brand.setBrandId(afterSaleKeepOnRecord.getBrandId());
            brand.setBrandName(afterSaleKeepOnRecord.getBrandName());
            brand.setOriginalBrandName(afterSaleKeepOnRecord.getOriginalBrandName());
            brand.setCreateDt(timeNow);
            afterSaleBrandWriteMapper.insertSelective(brand);
        } else {
            AfterSaleBrand brand = new AfterSaleBrand();
            brand.setBrandId(afterSaleKeepOnRecord.getBrandId());
            brand.setBrandName(afterSaleKeepOnRecord.getBrandName());
            brand.setId(afterSaleBrand.getId());
            afterSaleBrandWriteMapper.updateByPrimaryKeySelective(brand);
        }
        AfterSaleCarSeries afterSaleCarSeries = afterSaleCarSeriesReadMapper.selectByBrandIdAndOriginalCarSeriesName(afterSaleKeepOnRecord.getBrandId(), afterSaleKeepOnRecord.getOriginalCarSeriesName());
        if (afterSaleCarSeries == null) {
            AfterSaleCarSeries carSeries = new AfterSaleCarSeries();
            carSeries.setBrandId(afterSaleKeepOnRecord.getBrandId());
            carSeries.setCarSeriesId(afterSaleKeepOnRecord.getCarSeriesId());
            carSeries.setCarSeriesName(afterSaleKeepOnRecord.getCarSeriesName());
            carSeries.setOriginalCarSeriesName(afterSaleKeepOnRecord.getOriginalCarSeriesName());
            carSeries.setCreateDt(timeNow);
            afterSaleCarSeriesWriteMapper.insertSelective(carSeries);
        } else {
            AfterSaleCarSeries carSeries = new AfterSaleCarSeries();
            carSeries.setBrandId(afterSaleKeepOnRecord.getBrandId());
            carSeries.setCarSeriesId(afterSaleKeepOnRecord.getCarSeriesId());
            carSeries.setCarSeriesName(afterSaleKeepOnRecord.getCarSeriesName());
            carSeries.setId(afterSaleCarSeries.getId());
            afterSaleCarSeriesWriteMapper.updateByPrimaryKeySelective(carSeries);
        }
        AfterSaleKeepOnRecord sync = new AfterSaleKeepOnRecord();
        sync.setBrandId(afterSaleKeepOnRecord.getBrandId());
        sync.setBrandName(afterSaleKeepOnRecord.getBrandName());
        sync.setOriginalBrandName(afterSaleKeepOnRecord.getOriginalBrandName());
        sync.setCarSeriesId(afterSaleKeepOnRecord.getCarSeriesId());
        sync.setCarSeriesName(afterSaleKeepOnRecord.getCarSeriesName());
        sync.setOriginalCarSeriesName(afterSaleKeepOnRecord.getOriginalCarSeriesName());
        sync.setMatchingTime(timeNow);
        afterSaleKeepOnRecordWriteMapper.syncAfterSaleBrandAndCarSeries(sync);

        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
    }

    @Override
    public boolean getSyncDataStatus(AfterSaleKeepOnRecord afterSaleKeepOnRecord) throws RedisException {
        return redisService.exists(MessageFormat.format(BaseCacheKeys.AFTER_SALE_SYNC_USER.getKey(), afterSaleKeepOnRecord.getActivityId()));
    }

    @Override
    public TcResponse syncData(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        try {
            redisService.set(MessageFormat.format(BaseCacheKeys.AFTER_SALE_SYNC_USER.getKey(), afterSaleKeepOnRecord.getActivityId()), System.currentTimeMillis(), BaseCacheKeys.AFTER_SALE_SYNC_USER.getExpire());
        } catch (RedisException e) {
            CommonLogUtil.addError("售后特卖", "给线索库同步流失用户redis增加同步状态错误", e);
        }
        executorService.execute(() -> {
            afterSaleKeepOnRecord.setUserType((byte) AfterSaleUserType.LOST_USER.getType());
            afterSaleKeepOnRecord.setSyncStatus((byte) 0);
            List<AfterSaleKeepOnRecord> keepOnRecordList = afterSaleKeepOnRecordReadMapper.getKeepOnRecordList(afterSaleKeepOnRecord);
            Set<Integer> dealerIdsSet = new HashSet<>();
            for (AfterSaleKeepOnRecord record : keepOnRecordList) {
                dealerIdsSet.add(record.getDealerId());
            }
            List<Integer> dealerIds = new ArrayList<>();
            dealerIds.addAll(dealerIdsSet);
            Map<Integer, CsCompany> companyNameMap = null;
            if (!dealerIds.isEmpty()) {
                List<CsCompany> companiesByDealerIds = csCompanyService.getCompanysByDealerIds(dealerIds);
                companyNameMap = new HashMap((int) (companiesByDealerIds.size() / 0.75 + 1));
                for (CsCompany companiesByDealerId : companiesByDealerIds) {
                    companyNameMap.put(companiesByDealerId.getId(), companiesByDealerId);
                }
            }
            List<Future<Integer>> submits = new ArrayList<>();
            Map<Integer, CsCompany> finalCompanyNameMap = companyNameMap;
            for (AfterSaleKeepOnRecord record : keepOnRecordList) {
                Future<Integer> submit = executorService.submit(() -> {
                    SqlSession writeSqlSession = writeSqlSessionFactory.openSession(ExecutorType.BATCH, false);
                    SqlSession readSqlSession = readSqlSessionFactory.openSession(ExecutorType.SIMPLE);
                    AfterSaleOrderRecordReadMapper afterSaleOrderRecordReadMapper = readSqlSession.getMapper(AfterSaleOrderRecordReadMapper.class);
                    AfterSaleKeepOnRecordWriteMapper afterSaleKeepOnRecordWriteMapper = writeSqlSession.getMapper(AfterSaleKeepOnRecordWriteMapper.class);
                    try {
                        TcApplyRequestDto tcApplyRequestDto = null;
                        AfterSaleKeepOnRecord updateAfterSaleKeepOnRecord = null;
                        int rows = afterSaleOrderRecordReadMapper.selectAfterSaleAgentOrderCount(afterSaleKeepOnRecord.getActivityId(), record.getUserPhone(), record.getLicencePlate());
                        if (rows > 0) {
                            updateAfterSaleKeepOnRecord = new AfterSaleKeepOnRecord();
                            updateAfterSaleKeepOnRecord.setDeleteFlag(true);
                            updateAfterSaleKeepOnRecord.setId(record.getId());
                            afterSaleKeepOnRecordWriteMapper.updateById(updateAfterSaleKeepOnRecord);
                            writeSqlSession.flushStatements();
                            writeSqlSession.commit();
                            return 1;
                        }
                        CsCompany company = null;
                        if (finalCompanyNameMap != null && finalCompanyNameMap.get(record.getDealerId()) != null) {
                            company = finalCompanyNameMap.get(record.getDealerId());
                        }
                        //线索同步
                        tcApplyRequestDto = new TcApplyRequestDto();
                        tcApplyRequestDto.setUserCityId(company == null ? null : company.getCityId());
                        if (record.getUserName() == null) {
                            return 1;
                        }
                        tcApplyRequestDto.setUserName(record.getUserName());
                        if (record.getUserPhone() == null) {
                            return 1;
                        }
                        tcApplyRequestDto.setUserPhone(record.getUserPhone());
                        if (record.getBrandId() != null) {
                            tcApplyRequestDto.setUserBrandId(record.getBrandId());
                        }
                        if (record.getCarSeriesId() != null) {
                            tcApplyRequestDto.setUserStyleId(record.getCarSeriesId());
                        }
                        tcApplyRequestDto.setCommonDataField1(String.valueOf(record.getDealerId()));
                        tcApplyRequestDto.setCommonDataField2(company == null ? null : company.getCompanyName());
                        if (record.getLicencePlate() != null) {
                            tcApplyRequestDto.setCommonDataField3(record.getLicencePlate());
                        }
                        if (record.getBuyCarTime() != null) {
                            tcApplyRequestDto.setCommonDataField4(DateUtil.formatDateTime(record.getBuyCarTime()));
                        }
                        if (record.getLatestEnterTime() != null) {
                            tcApplyRequestDto.setCommonDataField5(DateUtil.formatDateTime(record.getLatestEnterTime()));
                        }
                        tcApplyRequestDto.setPeriodsType(27);
                        tcApplyRequestDto.setAccountCode("wg10");
                        tcApplyRequestDto.setCommonDataType("afterSaleClue");
                        tcApplyRequestDto.setCreateId(-1);
                        tcApplyRequestDto.setPeriodsId(String.valueOf(afterSaleKeepOnRecord.getActivityId()));
                        if (record.getDataSource() != null) {
                            tcApplyRequestDto.setReservedField1(String.valueOf(record.getDataSource()));
                        }
                        try {
                            tcClueService.insertProfile(tcApplyRequestDto);
                        } catch (Exception e) {
                            CommonLogUtil.addError("售后特卖", "给线索库同步流失用户错误", e);
                            return 1;
                        }
                        CommonLogUtil.addInfo("售后特卖", "给线索库同步流失用户成功" + JSON.toJSONString(tcApplyRequestDto));
                        //流失客户线索修改同步状态
                        updateAfterSaleKeepOnRecord = new AfterSaleKeepOnRecord();
                        updateAfterSaleKeepOnRecord.setId(record.getId());
                        updateAfterSaleKeepOnRecord.setSyncStatus((byte) 1);
                        updateAfterSaleKeepOnRecord.setSyncTime(new Date());
                        afterSaleKeepOnRecordWriteMapper.updateSyncStatusById(updateAfterSaleKeepOnRecord);
                        writeSqlSession.flushStatements();
                        writeSqlSession.commit();
                    } catch (Exception e) {
                        CommonLogUtil.addError("售后特卖", "给线索库同步流失用户错误", e);
                    } finally {
                        readSqlSession.close();
                        writeSqlSession.close();
                        return 1;
                    }

                });
                submits.add(submit);
            }

            try {
                for (Future<Integer> submit : submits) {
                    submit.get();
                }
                CommonLogUtil.addInfo("售后特卖", "给线索库同步流失用户完成,条数" + keepOnRecordList.size());
                redisService.del(MessageFormat.format(BaseCacheKeys.AFTER_SALE_SYNC_USER.getKey(), afterSaleKeepOnRecord.getActivityId()));
            } catch (Exception e) {
                CommonLogUtil.addError("售后特卖", "给线索库同步流失用户获取同步状态错误", e);
            }
        });
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
    }

    /**
     * 获取活动备案用户的手机号和车牌map
     *
     * @param activityId
     * @return java.util.Map<java.lang.String, java.lang.Byte>
     * @author HuangHao
     * @CreatTime 2021-08-06 10:22
     */
    @Override
    public Map<String, List<AfterSaleKeepOnRecord>> keepOnRecordMap(Integer activityId) {
        AfterSaleKeepOnRecord keepOnRecord = new AfterSaleKeepOnRecord();
        keepOnRecord.setActivityId(activityId);
        List<AfterSaleKeepOnRecord> list = afterSaleKeepOnRecordRreadMapper.getKeepOnRecordList(keepOnRecord);
        Map<String, List<AfterSaleKeepOnRecord>> keepOnRecordMap = null;
        if (!CollectionUtils.isEmpty(list)) {
            keepOnRecordMap = new HashMap<>(list.size() * 3);
            for (AfterSaleKeepOnRecord keep : list) {
                if (!StringUtils.isEmpty(keep.getUserPhone())) {
                    if (keepOnRecordMap.get(keep.getUserPhone()) == null) {
                        keepOnRecordMap.put(keep.getUserPhone(), new ArrayList<>());
                    }
                    keepOnRecordMap.get(keep.getUserPhone()).add(keep);
                }
                if (!StringUtils.isEmpty(keep.getLicencePlate())) {
                    if (keepOnRecordMap.get(keep.getLicencePlate()) == null) {
                        keepOnRecordMap.put(keep.getLicencePlate(), new ArrayList<>());
                    }
                    keepOnRecordMap.get(keep.getLicencePlate()).add(keep);
                }
            }
        }
        return keepOnRecordMap;
    }

    /**
     * 批量新增
     *
     * @param list
     * @return int
     * @author HuangHao
     */
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public int batchInsert(List<AfterSaleKeepOnRecord> list) {
        if (list.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        Integer activityId = list.get(0).getActivityId();
        Map<String, AfterSaleBrandCarSeriesDto> keepOnRecordMap = new HashMap((int) (list.size() / 0.75 + 1));
        //品牌+车系是否查过数据库
        Map<String, Boolean> brandCarDBMap = new HashMap(16);
        //原始品牌是否查过数据库
        Map<String, Boolean> originalBrandNameDBMap = new HashMap(16);
        //原始车系是否查过数据库
        Map<String, Boolean> originalCarSeriesNameDBMap = new HashMap(16);
        //原始品牌是否查过车型库
        Map<String, Boolean> carStyleESMap = new HashMap<>(16);
        //原始品牌查的车型库
        Map<String, CarStyleDto> carStyleMap = new HashMap<>(16);
        long t1 = System.currentTimeMillis();
        for (AfterSaleKeepOnRecord record : list) {
            if (record.getOriginalBrandName() == null || record.getOriginalCarSeriesName() == null) {
                continue;
            }
            String nameKey = record.getOriginalBrandName() + record.getOriginalCarSeriesName();
            AfterSaleBrandCarSeriesDto afterSaleBrandCarSeriesDto = null;
            if (keepOnRecordMap.get(nameKey) == null) {
                //查数据库
                if (brandCarDBMap.get(nameKey) == null) {
                    afterSaleBrandCarSeriesDto = afterSaleBrandReadMapper.selectByOriginalName(record.getOriginalBrandName(), record.getOriginalCarSeriesName());
                    brandCarDBMap.put(nameKey, true);
                }
            } else {
                afterSaleBrandCarSeriesDto = keepOnRecordMap.get(nameKey);
            }
            if (afterSaleBrandCarSeriesDto == null) {
                //查车型库
                if (carStyleESMap.get(record.getOriginalCarSeriesName()) == null) {
                    List<CarStyleDto> carStyleDtos = carStyleService.getListByWildcardQueryCsName(record.getOriginalCarSeriesName());
                    if (carStyleDtos.isEmpty()) {
                        carStyleMap.put(record.getOriginalCarSeriesName(), null);
                    } else {
                        carStyleMap.put(record.getOriginalCarSeriesName(), carStyleDtos.get(0));
                    }
                    carStyleESMap.put(record.getOriginalCarSeriesName(), true);
                }
                CarStyleDto carStyleDto = carStyleMap.get(record.getOriginalCarSeriesName());
                if (carStyleDto != null) {
                    String cbName = carStyleDto.getCbName() + "-(" + carStyleDto.getCmName() + ")";
                    afterSaleBrandCarSeriesDto = new AfterSaleBrandCarSeriesDto();
                    afterSaleBrandCarSeriesDto.setBrandId(carStyleDto.getCbId());
                    afterSaleBrandCarSeriesDto.setBrandName(cbName);
                    afterSaleBrandCarSeriesDto.setCarSeriesId(carStyleDto.getCsId());
                    afterSaleBrandCarSeriesDto.setCarSeriesName(carStyleDto.getCsName());
                    //把车型库的品牌车系数据插入直卖数据库
                    if (originalBrandNameDBMap.get(record.getOriginalBrandName()) == null) {
                        AfterSaleBrand afterSaleBrand = afterSaleBrandWriteMapper.selectByOriginalBrandName(record.getOriginalBrandName());
                        if (afterSaleBrand == null) {
                            AfterSaleBrand brandAdd = new AfterSaleBrand();
                            brandAdd.setBrandId(carStyleDto.getCbId());
                            brandAdd.setBrandName(cbName);
                            brandAdd.setOriginalBrandName(record.getOriginalBrandName());
                            brandAdd.setCreateDt(now);
                            afterSaleBrandWriteMapper.insertSelective(brandAdd);
                        } else {
                            AfterSaleBrand brandUpdate = new AfterSaleBrand();
                            brandUpdate.setBrandId(carStyleDto.getCbId());
                            brandUpdate.setBrandName(cbName);
                            brandUpdate.setId(afterSaleBrand.getId());
                            afterSaleBrandWriteMapper.updateByPrimaryKeySelective(brandUpdate);
                        }
                        originalBrandNameDBMap.put(record.getOriginalBrandName(), true);
                    }
                    if (originalCarSeriesNameDBMap.get(carStyleDto.getCbId() + record.getOriginalCarSeriesName()) == null) {
                        AfterSaleCarSeries afterSaleCarSeries = afterSaleCarSeriesWriteMapper.selectByBrandIdAndOriginalCarSeriesName(carStyleDto.getCbId(), record.getOriginalCarSeriesName());
                        if (afterSaleCarSeries == null) {
                            AfterSaleCarSeries carSeriesAdd = new AfterSaleCarSeries();
                            carSeriesAdd.setBrandId(carStyleDto.getCbId());
                            carSeriesAdd.setCarSeriesId(carStyleDto.getCsId());
                            carSeriesAdd.setCarSeriesName(carStyleDto.getCsName());
                            carSeriesAdd.setOriginalCarSeriesName(record.getOriginalCarSeriesName());
                            carSeriesAdd.setCreateDt(now);
                            afterSaleCarSeriesWriteMapper.insertSelective(carSeriesAdd);
                        } else {
                            AfterSaleCarSeries carSeriesUpdate = new AfterSaleCarSeries();
                            carSeriesUpdate.setBrandId(carStyleDto.getCbId());
                            carSeriesUpdate.setCarSeriesId(carStyleDto.getCsId());
                            carSeriesUpdate.setCarSeriesName(carStyleDto.getCsName());
                            carSeriesUpdate.setId(afterSaleCarSeries.getId());
                            afterSaleCarSeriesWriteMapper.updateByPrimaryKeySelective(carSeriesUpdate);
                        }
                        originalCarSeriesNameDBMap.put(carStyleDto.getCbId() + record.getOriginalCarSeriesName(), true);
                    }
                }
            }
            if (afterSaleBrandCarSeriesDto == null) {
                continue;
            }
            keepOnRecordMap.put(nameKey, afterSaleBrandCarSeriesDto);
            record.setBrandId(afterSaleBrandCarSeriesDto.getBrandId());
            record.setBrandName(afterSaleBrandCarSeriesDto.getBrandName());
            record.setCarSeriesId(afterSaleBrandCarSeriesDto.getCarSeriesId());
            record.setCarSeriesName(afterSaleBrandCarSeriesDto.getCarSeriesName());
            record.setMatchingTime(now);
        }
        int rows = 0;
        List<List<AfterSaleKeepOnRecord>> splitList = split(list, 5000);
        for (List<AfterSaleKeepOnRecord> afterSaleKeepOnRecords : splitList) {
            rows += afterSaleKeepOnRecordWriteMapper.batchInsert(afterSaleKeepOnRecords);
        }
        afterSaleKeepOnRecordWriteMapper.duplicateRemoval(activityId);
        return rows;
    }

    private List<List<AfterSaleKeepOnRecord>> split(List<AfterSaleKeepOnRecord> list, int size) {
        if (list == null || list.size() == 0) {
            return null;
        }
        int count = list.size();
        int pageCount = (count / size) + (count % size == 0 ? 0 : 1);
        List<List<AfterSaleKeepOnRecord>> temp = new ArrayList<>(pageCount);
        for (int i = 0, from = 0, to = 0; i < pageCount; i++) {
            from = i * size;
            to = from + size;
            to = to > count ? count : to;
            List<AfterSaleKeepOnRecord> list1 = list.subList(from, to);
            temp.add(list1);
        }
        return temp;
    }

    /**
     * 根据活动ID删除代理人
     *
     * @param activityId
     * @return int
     * @author HuangHao
     * @CreatTime 2021-09-07 15:41
     */
    public TcResponse deleteByActivityId(Integer activityId) {
        ResultDto resultDto = new ResultDto();
        if (activityId == null || activityId < 1) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动ID不正确");
        }
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getCacheAfterSaleActivityDtoById(activityId);
        if (afterSaleActivityDto == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动不存在");
        }
        if (afterSaleActivityDto.getOnState() == 1) {
            resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动已开启，不能删除备案用户");
        }
        int num = afterSaleKeepOnRecordWriteMapper.deleteByActivityId(activityId);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "删除完成，总共删除：" + num + "个");
    }

    @Override
    public TcResponse deleteByActivityIdAndUserType(Integer activityId, Integer userType) {
        ResultDto resultDto = new ResultDto();
        if (activityId == null || activityId < 1) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动ID不正确");
        }
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getCacheAfterSaleActivityDtoById(activityId);
        if (afterSaleActivityDto == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动不存在");
        }
        if (afterSaleActivityDto.getOnState() == 1 && userType == 0) {
            resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动已开启，不能删除备案用户");
        }
        int num = afterSaleKeepOnRecordWriteMapper.deleteByActivityIdAndUserType(activityId, userType);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "删除完成，总共删除：" + num + "个");
    }

    @Override
    public PageResult<AfterSaleKeepOnRecordDto> getUserSumData(int page, int limit, Integer dealerId, Integer activityId, Integer userType) {
        PageHelper.startPage(page, limit);
        List<AfterSaleKeepOnRecordDto> userSumData = afterSaleKeepOnRecordReadMapper.getUserSumData(dealerId, activityId, userType);
        PageResult<AfterSaleKeepOnRecordDto> pageResult = new PageResult<>();
        PageInfo<AfterSaleKeepOnRecordDto> pageInfo = new PageInfo(userSumData);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(userSumData);
        return pageResult;
    }

    /**
     * 获取用户类型
     *
     * @param afterSaleKeepOnRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     */
    public Integer getUserType(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        if (afterSaleKeepOnRecord == null || afterSaleKeepOnRecord.getActivityId() == null
                || (afterSaleKeepOnRecord.getLicencePlate() == null && afterSaleKeepOnRecord.getUserPhone() == null)) {
            return null;
        }
        return afterSaleKeepOnRecordReadMapper.getUserType(afterSaleKeepOnRecord);
    }

    @Override
    public List<AfterSaleKeepOnRecordBrandCarSeriesDto> getUnmatchedData(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        AfterSaleKeepOnRecordBrandCarSeriesDto afterSaleKeepOnRecordDto = new AfterSaleKeepOnRecordBrandCarSeriesDto();
        afterSaleKeepOnRecordDto.setActivityId(afterSaleKeepOnRecord.getActivityId());
        afterSaleKeepOnRecordDto.setStatus(3);
        afterSaleKeepOnRecordDto.setSyncStatus((byte) 0);
        return afterSaleKeepOnRecordRreadMapper.getKeepOnRecordBrandCarSeriesList(afterSaleKeepOnRecordDto);
    }
}
