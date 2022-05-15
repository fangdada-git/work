package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.GiftRefuelingcardPeriodsService;
import com.tuanche.directselling.dto.EnumStrDto;
import com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto;
import com.tuanche.directselling.mapper.read.directselling.GiftRefuelingcardPeriodsReadMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardPeriodsCommodityWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardPeriodsGiftNumWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardPeriodsWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.LiveSceneDealerConfigWriteMapper;
import com.tuanche.directselling.model.GiftRefuelingcardPeriods;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity;
import com.tuanche.directselling.model.GiftRefuelingcardPeriodsGiftNum;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.GiftRefuelingcardPeriodsVo;
import com.tuanche.merchant.pojo.dto.enums.CommodityTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-27 16:46
 */
@Service(retries = 0)
public class GiftRefuelingcardPeriodsServiceImpl implements GiftRefuelingcardPeriodsService {

    @Autowired
    GiftRefuelingcardPeriodsReadMapper giftRefuelingcardPeriodsReadMapper;
    @Autowired
    GiftRefuelingcardPeriodsWriteMapper giftRefuelingcardPeriodsWriteMapper;
    @Autowired
    GiftRefuelingcardPeriodsCommodityWriteMapper giftRefuelingcardPeriodsCommodityWriteMapper;
    @Autowired
    GiftRefuelingcardPeriodsGiftNumWriteMapper giftRefuelingcardPeriodsGiftNumWriteMapper;
    @Autowired
    LiveSceneDealerConfigWriteMapper liveSceneDealerConfigWriteMapper;
    List<EnumStrDto> commodityList;

    /**
     * 根据ID获取油卡场次配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    @Override
    public GiftRefuelingcardPeriodsDto getById(Integer id){
        if (id == null){
            return null;
        }
        return giftRefuelingcardPeriodsReadMapper.getById(id);
    }
    /**
     * 根据大场次ID获取油卡场次配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param periodsId
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    private GiftRefuelingcardPeriodsDto getByPeriodsId(Integer periodsId){
        if (periodsId == null){
            return null;
        }
        return giftRefuelingcardPeriodsReadMapper.getByPeriodsId(periodsId);
    }
    /**
     * 根据ID获取油卡场次配置详细新（商品类型列表，油卡配置列表）
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    @Override
    public GiftRefuelingcardPeriodsDto getDetailsById(Integer id){
        if (id == null){
            return null;
        }
        return giftRefuelingcardPeriodsReadMapper.getDetailsById(id);
    }
    /**
     * 获取油卡场次配置列表-带格式化的商品类型和油卡规则
     * @author HuangHao
     * @CreatTime 2020-05-27 16:45
     * @param
     * @return PageResult<GiftRefuelingcardPeriodsDto>
     */
    @Override
    public PageResult<GiftRefuelingcardPeriodsDto> getFuelCardPeriodsConfigListByPage(GiftRefuelingcardPeriodsVo refuelingcardPeriodsVo){
        PageHelper.startPage(refuelingcardPeriodsVo.getPage(), refuelingcardPeriodsVo.getLimit());
        List<GiftRefuelingcardPeriodsDto> list = giftRefuelingcardPeriodsReadMapper.getFuelCardPeriodsConfigList(refuelingcardPeriodsVo);
        PageInfo<GiftRefuelingcardPeriodsDto> page = new PageInfo<GiftRefuelingcardPeriodsDto>(list);
        PageResult<GiftRefuelingcardPeriodsDto> pageResult = new PageResult<GiftRefuelingcardPeriodsDto>();
        pageResult.setCount(page.getTotal());
        pageResult.setPage(refuelingcardPeriodsVo.getPage());
        pageResult.setLimit(refuelingcardPeriodsVo.getLimit());
        pageResult.setCode(String.valueOf(StatusCodeEnum.SUCCESS.getCode()));
        pageResult.setMsg(null);
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 获取直播商品类型列表
     * @author HuangHao
     * @CreatTime 2020-05-27 17:52
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.EnumStrDto>
     */
    @Override
    public List<EnumStrDto> getCommodityList(){
        if(commodityList == null){
            commodityList = new ArrayList<>(CommodityTypeEnum.values().length);
            for (CommodityTypeEnum commodityType:CommodityTypeEnum.values()) {
                //去除一口价
                if(!CommodityTypeEnum.FIXED_PRICE.value().equals(commodityType.value()) ){
                    EnumStrDto enumStrDto = new EnumStrDto();
                    enumStrDto.setText(commodityType.tag());
                    enumStrDto.setValue(commodityType.value());
                    commodityList.add(enumStrDto);
                }
            }
        }
        return commodityList;
    }
    
    /**
     *  新增油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 14:42
     * @param refuelcardPeriodrs
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public TcResponse inster(GiftRefuelingcardPeriodsDto refuelcardPeriodrs){
        String keyWord = "新增油卡场次配置,场次ID:"+(refuelcardPeriodrs != null?refuelcardPeriodrs.getPeriodsId()+"，操作人："+refuelcardPeriodrs.getUpdateName():"null");
        CommonLogUtil.addInfo(keyWord, "新增开始，数据："+JSON.toJSONString(refuelcardPeriodrs));
        String msg = StatusCodeEnum.SUCCESS.getText();
        if(refuelcardPeriodrs == null || refuelcardPeriodrs.getPeriodsId() == null){
            msg = "场次不能ID为空"; 
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(refuelcardPeriodrs.getPeriodsName())){
            msg = "场次名称不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(refuelcardPeriodrs.getBeginTime() == null){
            msg = "场次开始时间不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(refuelcardPeriodrs.getEndTime() == null){
            msg = "场次结束时间不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(CollectionUtils.isEmpty(refuelcardPeriodrs.getCommodityList())){
            msg = "商品类型不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(CollectionUtils.isEmpty(refuelcardPeriodrs.getFuelCardRuleList())){
            msg = "油卡规则不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(refuelcardPeriodrs.getCreateName())){
            msg = "操作人createName不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        GiftRefuelingcardPeriodsDto periodsInfo = getByPeriodsId(refuelcardPeriodrs.getPeriodsId());
        if(periodsInfo != null){
            msg = "该场次已经配置，不能重复配置";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        //新增油卡规则配置
        giftRefuelingcardPeriodsWriteMapper.insertGiftRefuelingcardPeriods(refuelcardPeriodrs);
        CommonLogUtil.addInfo(keyWord, "新增油卡规则配置完成");

        List<GiftRefuelingcardPeriodsCommodity> commodityList = refuelcardPeriodrs.getCommodityList();
        List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList = refuelcardPeriodrs.getFuelCardRuleList();
        //新增油卡配置详情
        addPeriodsDetails(keyWord,refuelcardPeriodrs.getId(), commodityList, fuelCardRuleList);

        CommonLogUtil.addInfo(keyWord, "新增油卡规则详情完成");
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
    /**
     *  更新油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 14:42
     * @param refuelcardPeriodrs
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public TcResponse update(GiftRefuelingcardPeriodsDto refuelcardPeriodrs){
        String keyWord = "更新油卡场次配置,ID:"+(refuelcardPeriodrs != null?(refuelcardPeriodrs.getId()+"，操作人："+refuelcardPeriodrs.getUpdateName()):"null");
        CommonLogUtil.addInfo(keyWord, "更新开始，数据："+JSON.toJSONString(refuelcardPeriodrs));
        String msg = StatusCodeEnum.SUCCESS.getText();
        if(refuelcardPeriodrs == null || refuelcardPeriodrs.getId() == null){
            msg = "id不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(refuelcardPeriodrs == null || refuelcardPeriodrs.getPeriodsId() == null){
            msg = "场次不能ID为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(refuelcardPeriodrs.getPeriodsName())){
            msg = "场次名称不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(refuelcardPeriodrs.getBeginTime() == null){
            msg = "场次开始时间不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(refuelcardPeriodrs.getEndTime() == null){
            msg = "场次结束时间不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(CollectionUtils.isEmpty(refuelcardPeriodrs.getCommodityList())){
            msg = "商品类型不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(CollectionUtils.isEmpty(refuelcardPeriodrs.getFuelCardRuleList())){
            msg = "油卡规则不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }else if(StringUtils.isEmpty(refuelcardPeriodrs.getUpdateName())){
            msg = "操作人updateName不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        //新增油卡规则配置
        giftRefuelingcardPeriodsWriteMapper.updateGiftRefuelingcardPeriods(refuelcardPeriodrs);
        CommonLogUtil.addInfo(keyWord, "新增油卡规则配置完成");

        List<GiftRefuelingcardPeriodsCommodity> commodityList = refuelcardPeriodrs.getCommodityList();
        List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList = refuelcardPeriodrs.getFuelCardRuleList();
        //删除之前的油卡商品类型
        int deleteNum1 = giftRefuelingcardPeriodsCommodityWriteMapper.deleteByRcPeriodsId(refuelcardPeriodrs.getId());
        CommonLogUtil.addInfo(keyWord, "删除油卡商品类型，删除条数："+ deleteNum1);
        //删除之前的油卡配置规则
        int deleteNum2 = giftRefuelingcardPeriodsGiftNumWriteMapper.deleteByRcPeriodsId(refuelcardPeriodrs.getId());
        CommonLogUtil.addInfo(keyWord, "油卡配置规则，删除条数："+ deleteNum2);
        //插入新的
        addPeriodsDetails(keyWord,refuelcardPeriodrs.getId(), commodityList, fuelCardRuleList);

        CommonLogUtil.addInfo(keyWord, "更新完成");
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * 新增油卡配置详情
     * @author HuangHao
     * @CreatTime 2020-05-28 14:30
     * @param rcPeriodsId
     * @param commodityList
     * @param fuelCardRuleList
     * @return void
     */
    private void addPeriodsDetails(String keyWord,Integer rcPeriodsId,List<GiftRefuelingcardPeriodsCommodity> commodityList,
                                   List<GiftRefuelingcardPeriodsGiftNum> fuelCardRuleList){
        List<GiftRefuelingcardPeriodsCommodity> newCommodityList = new ArrayList<>();
        for (GiftRefuelingcardPeriodsCommodity commodity:commodityList){
            if(!StringUtils.isEmpty(commodity.getCommodityType()) && !StringUtils.isEmpty(commodity.getCommodityName())) {
                commodity.setRcPeriodsId(rcPeriodsId);
                newCommodityList.add(commodity);
            }
        }
        //批量新增油卡商品类型
        if (newCommodityList.size() >0){
            giftRefuelingcardPeriodsCommodityWriteMapper.batchInsert(newCommodityList);
        }
        CommonLogUtil.addInfo(keyWord, "批量新增赠送油卡规则，新增数据："+ JSON.toJSONString(commodityList));
        List<GiftRefuelingcardPeriodsGiftNum> newFuelCardRuleList = new ArrayList<>();
        for (GiftRefuelingcardPeriodsGiftNum fuelCardRule:fuelCardRuleList){
            if(fuelCardRule.getCommodityPrice() != null && fuelCardRule.getGiftNum() != null && fuelCardRule.getGiftNum() >0){
                fuelCardRule.setRcPeriodsId(rcPeriodsId);
                newFuelCardRuleList.add(fuelCardRule);
            }
        }
        //批量新增赠送油卡规则
        if (newFuelCardRuleList.size() > 0){
            giftRefuelingcardPeriodsGiftNumWriteMapper.batchInsert(newFuelCardRuleList);
        }
        CommonLogUtil.addInfo(keyWord, "批量新增赠送油卡规则，新增数据："+ JSON.toJSONString(fuelCardRuleList));
    }

    /**
     * 删除油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-29 9:50
     * @param giftRefuelingcardPeriods
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Override
    public TcResponse delete(GiftRefuelingcardPeriods giftRefuelingcardPeriods){
        Integer id = giftRefuelingcardPeriods==null?null:giftRefuelingcardPeriods.getId();
        String keyWord= "删除油卡配置，ID:"+id;
        if(id==null){
            String msg = "id不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        if(giftRefuelingcardPeriods.getUpdateName()==null){
            String msg = "操作人updateName不能为空";
            CommonLogUtil.addInfo(keyWord, "失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        giftRefuelingcardPeriods.setDeleteFlag(true);
        GiftRefuelingcardPeriodsDto periodsDto = giftRefuelingcardPeriodsReadMapper.getById(id);
        giftRefuelingcardPeriodsWriteMapper.updateGiftRefuelingcardPeriods(giftRefuelingcardPeriods);
        int updateNum = liveSceneDealerConfigWriteMapper.updateRefuelingCardNumByPeriodsId(0,periodsDto.getPeriodsId(),giftRefuelingcardPeriods.getUpdateName());
        CommonLogUtil.addInfo(keyWord, "删除成功，操作人："+giftRefuelingcardPeriods.getUpdateName()+"，重置该场次配置的经销商油卡赠送上限"+updateNum+"家");
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
}
