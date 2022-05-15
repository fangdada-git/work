package com.tuanche.directselling.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.Resources;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleAgentService;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityCouponReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleRewardRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityCouponWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ImageUtil;
import com.tuanche.directselling.util.QRCodeUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.ApiBaseCacheKeys;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.AfterSaleActivitySimpleVo;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import com.tuanche.eap.api.dto.manufacturer.CsDealerFinancialInfoDto;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialInvoiceService;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.simpleInfo.CsCompanySimpleInfo;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.ImageResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.upload.FtpUtil;
import com.tuanche.upload.UploadProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/20
 **/
@Service
public class AfterSaleActivityServiceImpl implements AfterSaleActivityService {
    private static String tmpFilePath = UploadProperties.getString("temfilePath");
    @Autowired
    AfterSaleActivityReadMapper afterSaleActivityReadMapper;
    @Autowired
    AfterSaleActivityWriteMapper afterSaleActivityWriteMapper;
    @Autowired
    AfterSaleActivityCouponReadMapper couponReadMapper;
    @Autowired
    AfterSaleActivityCouponWriteMapper couponWriteMapper;
    @Autowired
    AfterSaleRewardRecordReadMapper afterSaleRewardRecordReadMapper;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private CsDealerFinancialService dealerFinancialService;
    @Reference
    private CsDealerFinancialInvoiceService csDealerFinancialInvoiceService;
    @Autowired
    private AfterSaleOrderRecordServiceImpl afterSaleOrderRecordServiceImpl;
    @Reference
    private AfterSaleAgentService afterSaleAgentService;
    @Reference
    CarBaseService carBaseService;
    @Autowired
    AfterSaleUserReadMapper saleUserReadMapper;

    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    @Override
    public TcResponse getAfterSaleActivityInfoForApi(AfterSaleActivityVo activityVo) {
        long st = System.currentTimeMillis();
        String keyword = "index_"+activityVo.getUserWxUnionId();
        CommonLogUtil.addInfo(keyword+",售后服务活动首页-获取活动数据接口 start", "入参：", JSON.toJSONString(activityVo));
        AfterSaleActivityFontDto afterSaleActivityFontDto;
        try {
            AfterSaleActivityFontDto redisActivityDto = redisService.get(ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_CONFIG_CACHE.getKey() + activityVo.getActivityId(), AfterSaleActivityFontDto.class);
            AfterSaleOrderRecord query = new AfterSaleOrderRecord();
            query.setActivityId(activityVo.getActivityId());
            query.setUserWxUnionId(activityVo.getUserWxUnionId());
            query.setOrderType(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode());
            AfterSaleAgent afterSaleAgent = afterSaleAgentService.getDealerOrTelesalesAgent(activityVo.getActivityId(), activityVo.getUserWxUnionId());
            if (!Objects.isNull(redisActivityDto)) {
                redisActivityDto.setAgent(Objects.isNull(afterSaleAgent) ? false : true);
                redisActivityDto.setAgentType(Objects.isNull(afterSaleAgent) ? null: afterSaleAgent.getType());
                query.setGoodsId(redisActivityDto.getGoodsId());
                judgeOnState(redisActivityDto);
                judgeBuyFlag(redisActivityDto, query);
                getShareUserInfo(redisActivityDto, activityVo.getShareWxUnionId());
                CommonLogUtil.addInfo(keyword+"售后服务活动首页-获取活动redis数据接口 end", "返参：", JSON.toJSONString(redisActivityDto));
                return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), redisActivityDto);
            }
            AfterSaleActivityDto afterSaleActivityDto = getAfterSaleActivityDtoById(activityVo.getActivityId());
            if (afterSaleActivityDto == null) {
                return new TcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), System.currentTimeMillis() - st, "活动无效", afterSaleActivityDto);
            }
            afterSaleActivityFontDto = new AfterSaleActivityFontDto();
            BeanUtils.copyProperties(afterSaleActivityDto, afterSaleActivityFontDto);
            query.setGoodsId(afterSaleActivityDto.getGoodsId());
            judgeBuyFlag(afterSaleActivityFontDto, query);
            afterSaleActivityFontDto.setActivityId(afterSaleActivityDto.getId());
            afterSaleActivityFontDto.setAgent(afterSaleAgent == null ? false : true);
            afterSaleActivityFontDto.setAgentType(Objects.isNull(afterSaleAgent) ? null: afterSaleAgent.getType());
            afterSaleActivityFontDto.setOfflineConvertStartTimeMils(afterSaleActivityFontDto.getOfflineConvertStartTime().getTime());
            afterSaleActivityFontDto.setOfflineConvertEndTimeMils(afterSaleActivityFontDto.getOfflineConvertEndTime().getTime());
            CarBrandDto brandById = carBaseService.getBrandById(afterSaleActivityDto.getBrandId());
            afterSaleActivityFontDto.setMasterBrandId(brandById != null ? brandById.getCmId() : null);
            BaseResponseDto<CommodityDetailBusinessResponseDto> goodsDetail = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(afterSaleActivityDto.getGoodsId());
            CommonLogUtil.addInfo(keyword+"售后服务活动首页-获取对应商品信息", "返参：", JSON.toJSONString(goodsDetail));
            if (ResultEnum.SUCCESS.getCode().equals(goodsDetail.getCode()) && !Objects.isNull(goodsDetail.getData())) {
                //商品价格
                afterSaleActivityFontDto.setCommodityPrice(goodsDetail.getData().getCommodityPrice());
                afterSaleActivityFontDto.setCommodityName(goodsDetail.getData().getCommodityName());
                afterSaleActivityFontDto.setOriginalPrice(goodsDetail.getData().getOriginalPrice());
                afterSaleActivityFontDto.setDescription(goodsDetail.getData().getDescription());
                List<FissionGoodsImageDto> headImages = new ArrayList<>();
                FissionGoodsImageDto goodsImageDto;
                if (!CollectionUtils.isEmpty(goodsDetail.getData().getHeadImages())) {
                    for (ImageResponseDto headImage : goodsDetail.getData().getHeadImages()) {
                        goodsImageDto = new FissionGoodsImageDto();
                        BeanUtils.copyProperties(headImage, goodsImageDto);
                        headImages.add(goodsImageDto);
                    }
                    afterSaleActivityFontDto.setHeadImages(headImages);
                }
                if (!CollectionUtils.isEmpty(goodsDetail.getData().getDetailImages())) {
                    List<FissionGoodsImageDto> detailImages = new ArrayList<>();
                    for (ImageResponseDto detailImage : goodsDetail.getData().getDetailImages()) {
                        goodsImageDto = new FissionGoodsImageDto();
                        BeanUtils.copyProperties(detailImage, goodsImageDto);
                        detailImages.add(goodsImageDto);
                    }
                    afterSaleActivityFontDto.setDetailImages(detailImages);
                }
                redisService.set(ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_CONFIG_CACHE.getKey() + activityVo.getActivityId(), afterSaleActivityFontDto, 1000 * 60 * 60 * 24);
                judgeOnState(afterSaleActivityFontDto);
                getShareUserInfo(afterSaleActivityFontDto, activityVo.getShareWxUnionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError(keyword+"裂变活动首页-获取活动数据接口 error", "出错：", e);
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
        CommonLogUtil.addInfo(keyword+"售后服务活动首页-获取活动数据接口 end", "返参：", JSON.toJSONString(afterSaleActivityFontDto));
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), afterSaleActivityFontDto);
    }

    /**
     *  分享人信息 昵称 头像
     * @param activityDto
     */
    private void getShareUserInfo(AfterSaleActivityFontDto activityDto,String shareWxUnionId) {
        if (!StringUtils.isEmpty(shareWxUnionId)) {
            AfterSaleUser afterSaleUser = new AfterSaleUser();
            afterSaleUser.setActivityId(activityDto.getActivityId());
            afterSaleUser.setUserWxUnionId(shareWxUnionId);
            AfterSaleUser shareUser = saleUserReadMapper.getUser(afterSaleUser);
            if (!Objects.isNull(shareUser)) {
                activityDto.setShareWxNickName(shareUser.getNickName());
                activityDto.setShareWxHead(shareUser.getUserWxHead());
            }
        }
    }


    /**
     * 判断活动是否开启
     * 0 未开启，1 进行中，2 已结束
     * @param saleActivityDto
     */
    private void judgeOnState(AfterSaleActivityFontDto saleActivityDto) {
        Date now = new Date();
        if (saleActivityDto.getOnState() == 1 && saleActivityDto.getSaleStartTime().before(now) && saleActivityDto.getSaleEndTime().after(now)) {
            saleActivityDto.setOnState(1);
            saleActivityDto.setCountDown(DateUtil.between(new Date(), saleActivityDto.getSaleEndTime(), DateUnit.MS));
        } else if (saleActivityDto.getOnState() == 1 && saleActivityDto.getSaleEndTime().before(now)) {
            saleActivityDto.setOnState(2);
            saleActivityDto.setCountDown(0);
        } else {
            saleActivityDto.setOnState(0);
            saleActivityDto.setCountDown(DateUtil.between(new Date(), saleActivityDto.getSaleEndTime(), DateUnit.MS));
        }
    }

   /**
     * 判断是否购买
     *
     * @param saleActivityDto
     * @param query
     */
    private void judgeBuyFlag(AfterSaleActivityFontDto saleActivityDto, AfterSaleOrderRecord query) {
        AfterSaleOrderRecord userBuyFlag = afterSaleOrderRecordServiceImpl.getUserBuyFlag(query);
        if (!Objects.isNull(userBuyFlag) && userBuyFlag.getOrderStatus() >= AfterSaleConstants.OrderStatus.PAID.getCode()) {
            // 1 备案用户 2 不是备案用户（及普通用户） 3备案用户已完成
            if (userBuyFlag.getKeepOnRecordUser() && (!Objects.isNull(userBuyFlag.getFinishFissionTask()) && userBuyFlag.getFinishFissionTask())) {
                saleActivityDto.setIsBuyed(3);
            } else if (userBuyFlag.getKeepOnRecordUser()) {
                saleActivityDto.setIsBuyed(1);
            } else {
                saleActivityDto.setIsBuyed(2);
            }
        } else {
            //0 未购买
            saleActivityDto.setIsBuyed(0);
        }
    }


    @Override
    public TcResponse getAfterSaleActivityDealerList(AfterSaleActivityVo activityVo) {
        CommonLogUtil.addInfo("售后服务活动首页-获取经销商列表接口 start", "入参：", JSON.toJSONString(activityVo));
        long st = System.currentTimeMillis();
        List<AfterSaleActivity> activityList = afterSaleActivityReadMapper.selectActivityDealerList(activityVo);
        List<CsCompanySimpleInfo> dealerList = new ArrayList<>();
        CsCompanySimpleInfo dealer;
        for (AfterSaleActivity activity : activityList) {
            dealer = new CsCompanySimpleInfo();
            dealer.setId(activity.getDealerId());
            dealer.setCompanyName(companyBaseService.getCsCompanyName(activity.getDealerId()));
            dealerList.add(dealer);
        }
        CommonLogUtil.addInfo("售后服务活动首页-获取经销商列表接口 end", "返参：", JSON.toJSONString(dealerList));
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), dealerList);
    }

    @Override
    public TcResponse getAfterSaleActivityList(AfterSaleActivityVo activityVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("售后服务活动首页-获取活动列表接口 start", "入参：", JSON.toJSONString(activityVo));
        AfterSaleActivityDto activity = new AfterSaleActivityDto();
        if (!Objects.isNull(activityVo)) {
            BeanUtils.copyProperties(activityVo, activity);
        }
        activity.setOnState(1);
        List<AfterSaleActivity> afterSaleActivityList = afterSaleActivityReadMapper.selectActivityList(activity);
        List<AfterSaleActivitySimpleVo> simpleVoList = new ArrayList<>();
        AfterSaleActivitySimpleVo simpleVo;
        for (AfterSaleActivity afterSaleActivity : afterSaleActivityList) {
            simpleVo = new AfterSaleActivitySimpleVo();
            simpleVo.setActivityId(afterSaleActivity.getId());
            simpleVo.setActivityName(afterSaleActivity.getActivityName());
            simpleVoList.add(simpleVo);
        }
        CommonLogUtil.addInfo("售后服务活动首页-获取活动列表接口 end", "返参：", JSON.toJSONString(afterSaleActivityList));
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), simpleVoList);
    }

    @Override
    public AfterSaleActivityDto getAfterSaleActivityDtoById(Integer id) {
        AfterSaleActivity afterSaleActivity = afterSaleActivityReadMapper.selectByPrimaryKey(id);
        if (Objects.isNull(afterSaleActivity)) {
            return null;
        }
        AfterSaleActivityDto saleActivityDto = new AfterSaleActivityDto();
        BeanUtils.copyProperties(afterSaleActivity, saleActivityDto);
        CsCompanyDetailDto detail = companyBaseService.getCsCompanyDetail(afterSaleActivity.getDealerId());
        if (!Objects.isNull(detail)) {
            saleActivityDto.setDealerName(detail.getCompanyName());
            saleActivityDto.setDealerAddress(detail.getAddress());
            saleActivityDto.setDealerTel(detail.getTel());
            saleActivityDto.setPosition(detail.getPosition());
            saleActivityDto.setDealerImg(detail.getDealerImg());
            saleActivityDto.setShortName(detail.getShortName());
        }
        return saleActivityDto;
    }
    /**
     * 获取缓存的活动信息-只缓存基础且是已开启的
     * @author HuangHao
     * @CreatTime 2021-08-11 16:12
     * @param id
     * @return com.tuanche.directselling.dto.AfterSaleActivityDto
     */
    public AfterSaleActivityDto getCacheAfterSaleActivityDtoById(Integer id) {
        if(id == null){
            return null;
        }
        String key = ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_BASE_CACHE.getKey()+id;
        AfterSaleActivityDto activityDto = null;
        try {
            activityDto = redisService.get(key, AfterSaleActivityDto.class);
            if(activityDto == null){
                activityDto = getAfterSaleActivityDtoById(id);
                //不等于空且已开启就缓存
                if(activityDto != null && AfterSaleConstants.ActivityOnState.ONSTATE_1.equals(activityDto.getOnState()) ){
                    redisService.set(key, activityDto, ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_BASE_CACHE.getExpire());
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return activityDto;
    }


    @Override
    public AfterSaleActivity getAfterSaleActivityById(Integer id) {
        return afterSaleActivityReadMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AfterSaleActivity> getAfterSaleActivityByOrderCodes(List<String> orderCodes) {
        return afterSaleActivityReadMapper.getAfterSaleActivityByOrderCodes(orderCodes);
    }

    @Override
    public PageResult findAfterSaleActivityByPage(PageResult<AfterSaleActivityDto> pageResult, AfterSaleActivity afterSaleActivity) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        AfterSaleActivityDto activity = new AfterSaleActivityDto();
        if (!Objects.isNull(afterSaleActivity)) {
            BeanUtils.copyProperties(afterSaleActivity, activity);
        }
        List<AfterSaleActivity> liveAfterSaleActivityList = afterSaleActivityReadMapper.selectActivityList(activity);
        List<AfterSaleActivityDto> liveAfterSaleActivityDtoList = new ArrayList<>();
        AfterSaleActivityDto afterSaleActivityDto;
        CsCompany companyDetail;
        for (AfterSaleActivity saleActivity : liveAfterSaleActivityList) {
            afterSaleActivityDto = new AfterSaleActivityDto();
            BeanUtils.copyProperties(saleActivity, afterSaleActivityDto);
            companyDetail = new CsCompany();
            companyDetail.setId(saleActivity.getDealerId());
            List<CsCompanyDetailDto> list = companyBaseService.getCsCompanyDetailList(companyDetail, 1, 1);
            if (!CollectionUtils.isEmpty(list)) {
                afterSaleActivityDto.setDealerName(list.get(0).getCompanyName());
                afterSaleActivityDto.setDealerAddress(list.get(0).getAddress());
                afterSaleActivityDto.setDealerTel(list.get(0).getTel());
                afterSaleActivityDto.setManagerName(list.get(0).getUname());
            }
            List<CsDealerFinancialInfoDto> dealerFinancial = csDealerFinancialInvoiceService.getCsDealerFinancialInfo(saleActivity.getDealerId());
            afterSaleActivityDto.setIsSetBaseCarNumber(1);
            if (CollectionUtils.isEmpty(dealerFinancial)) {
                afterSaleActivityDto.setIsSetBaseCarNumber(0);
            }
            liveAfterSaleActivityDtoList.add(afterSaleActivityDto);
        }
        pageResult.setCount(new PageInfo<>(liveAfterSaleActivityList).getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveAfterSaleActivityDtoList);
        return pageResult;
    }

    @Override
    public void openAfterSaleActivity(AfterSaleActivityDto activityDto, Integer optUserId) {
        AfterSaleActivity afterSaleActivity = new AfterSaleActivity();
        afterSaleActivity.setId(activityDto.getId());
        afterSaleActivity.setBrandId(activityDto.getBrandId());
        afterSaleActivity.setGiftCouponId(activityDto.getGiftCouponId());
        afterSaleActivity.setGiftCouponName(activityDto.getGiftCouponName());
        afterSaleActivity.setGiftFissionCount(activityDto.getGiftFissionCount());
        afterSaleActivity.setOnState(1);
        afterSaleActivity.setUpdateBy(optUserId);
        afterSaleActivity.setUpdateDt(new Date());
        afterSaleActivityWriteMapper.updateByPrimaryKeySelective(afterSaleActivity);
        delActivityCache(afterSaleActivity.getId());
    }

    @Override
    public int saveAfterSaleActivity(AfterSaleActivity afterSaleActivity) {
        if (Objects.isNull(afterSaleActivity)) {
            return 0;
        }
        if (afterSaleActivity.getId() == null) {
            afterSaleActivity.setOnState(0);
            afterSaleActivity.setDeleteFlag(false);
            return afterSaleActivityWriteMapper.insertSelective(afterSaleActivity);
        } else {
            int flag = afterSaleActivityWriteMapper.updateByPrimaryKeySelective(afterSaleActivity);
            delActivityCache(afterSaleActivity.getId());
            return flag;
        }
    }

    @Override
    public PageResult findAfterSaleCouponListByPage(PageResult<AfterSaleActivityCoupon> pageResult, AfterSaleActivityCoupon afterSaleActivityCoupon) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<AfterSaleActivityCoupon> liveAfterSaleActivityList = couponReadMapper.selectCouponList(afterSaleActivityCoupon);
        PageInfo<AfterSaleActivityCoupon> page = new PageInfo<>(liveAfterSaleActivityList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveAfterSaleActivityList);
        return pageResult;
    }

    @Override
    public List<AfterSaleActivity> selectActivityList(AfterSaleActivity afterSaleActivity) {
        AfterSaleActivityDto activity = new AfterSaleActivityDto();
        BeanUtils.copyProperties(afterSaleActivity, activity);
        return afterSaleActivityReadMapper.selectActivityList(activity);
    }

    /**
     * 获取经销商的活动列表-已开启
     * @author HuangHao
     * @CreatTime 2021-09-28 15:07
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    public List<AfterSaleActivity> getDealerActivityList(Integer dealerId){
        if(dealerId == null){
            return null;
        }
        AfterSaleActivityDto activity = new AfterSaleActivityDto();
        activity.setDealerId(dealerId);
        activity.setOnState(1);
        return afterSaleActivityReadMapper.selectActivityList(activity);
    }

    @Override
    public int saveActivityCoupon(AfterSaleActivityCoupon activityCoupon) {
        if (Objects.isNull(activityCoupon)) {
            return 0;
        }
        if (activityCoupon.getId() == null) {
            activityCoupon.setRelStatus(1);
            return couponWriteMapper.insertSelective(activityCoupon);
        } else {
            int flag = couponWriteMapper.updateByPrimaryKeySelective(activityCoupon);
            return flag;
        }
    }

    @Override
    public AfterSaleActivityCoupon findAfterSaleActivityCouponById(Integer id) {
        if (Objects.isNull(id)) {
            return new AfterSaleActivityCoupon();
        }
        return couponReadMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateAfterSaleActivityCouponById(Integer id) {
        AfterSaleActivityCoupon activityCoupon = new AfterSaleActivityCoupon();
        activityCoupon.setId(id);
        activityCoupon.setRelStatus(2);
        return couponWriteMapper.updateByPrimaryKeySelective(activityCoupon);
    }

    @Override
    public String createPostUrl(String shareUrl, String posterUrl) {
        return createSharePic(shareUrl, posterUrl);
    }

    /**
     * 生成海报图
     *
     * @param shareUrl
     * @param posterUrl
     */
    private String createSharePic(String shareUrl, String posterUrl) {
        try {
            File file = new File("");
            String serverPath = file.getCanonicalPath() + File.separator + tmpFilePath + File.separator;

            File dirFile = new File(serverPath);
            if (!dirFile.exists() && !dirFile.isDirectory()) {
                dirFile.mkdirs();
            }
            // template
            String templatePicPath = serverPath + UUID.randomUUID().toString() + ".jpg";
            File templageFile = new File(templatePicPath);
            ImageUtil.getUrlImageFile(posterUrl, templageFile);
            // target
            String fileName = System.currentTimeMillis() + "" + new Random().nextInt(10000) + ".jpg";
            String targetPicPath = serverPath + fileName;
            File targetPicFile = new File(targetPicPath);
            // qr
            String tt = UUID.randomUUID().toString() + ".png";
            String qrPicPath = serverPath + tt;
            File qrPicFile = new File(qrPicPath);
            QRCodeUtil.createCodeToFile(shareUrl,  new File(serverPath), tt);
            //QRCodeGenerator.encode(shareUrl, 110, qrPicPath, true);

            // 合并图片
            ImageUtil.mergeImage(templageFile.getAbsolutePath(), qrPicPath, targetPicFile.getAbsolutePath(), 290, 175);
            String tmpPath = "afterSale/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            // 上传图片
            FtpUtil.ftpUpload(Resources.getString("ftp.host"), Resources.getString("ftp.username"),
                    Resources.getString("ftp.password"), serverPath, tmpPath, fileName);

            String httpUrl = Resources.getString("pic.url") + "/zixun/" + tmpPath + "/" + fileName;
            if (templageFile.exists()) {
                templageFile.delete();
            }
            if (targetPicFile.exists()) {
                targetPicFile.delete();
            }
            if (qrPicFile.exists()) {
                qrPicFile.delete();
            }
            return httpUrl;
        } catch (Exception e) {
            CommonLogUtil.addError("创建分享海报图，error", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<Integer, AfterSaleActivity> getAfterSaleActivityMap(List<Integer> activityIds) {
        Map<Integer, AfterSaleActivity> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(activityIds)) {
            List<AfterSaleActivity> list = afterSaleActivityReadMapper.selectActivityListByIds(activityIds);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(v -> {
                    map.put(v.getId(), v);
                });
            }
        }
        return map;
    }

    @Override
    public List<AfterSaleActivitySimpleVo> getSimpleActivityList(AfterSaleActivityDto afterSaleActivity) {
        List<AfterSaleActivitySimpleVo> activitySimpleVos = afterSaleActivityReadMapper.selectActivitySimpleList(afterSaleActivity);
        for (AfterSaleActivitySimpleVo activitySimpleVo : activitySimpleVos) {
            activitySimpleVo.setDealerName(companyBaseService.getCsCompanyName(activitySimpleVo.getDealerId()));
        }
        return activitySimpleVos;
    }

    /**
     * 获取进行中的活动ID列表,活动开始时间到线下活动结束时间的数据
     *
     * @param datatime
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2021-08-19 14:30
     */
    public List<Integer> getOngoingActivityIds(String datatime) {
        if (StringUtils.isEmpty(datatime)) {
            return null;
        }
        AfterSaleActivityDto afterSaleActivityDto = new AfterSaleActivityDto();
        afterSaleActivityDto.setSaleStartTimeStr(datatime);
        return getOngoingActivityIds(afterSaleActivityDto);
    }
    /**
     * 获取进行中的活动ID列表,活动开始时间到线下活动结束时间的数据
     *
     * @param datatime
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2021-08-19 14:30
     */
    public List<Integer> getOngoingActivityIds(AfterSaleActivityDto afterSaleActivityDto) {
        if (afterSaleActivityDto==null || afterSaleActivityDto.getSaleStartTimeStr()==null) {
            return null;
        }
        return afterSaleActivityReadMapper.getOngoingActivityIds(afterSaleActivityDto);
    }

    @Override
    public List<AfterSaleActivityDealerDto> getAfterSaleDealer() {
        List<Integer> afterSaleDealerIds = afterSaleActivityReadMapper.getAfterSaleDealerIds();
        List<CsCompany> companyList = companyBaseService.getCompanyListByDealerIds(afterSaleDealerIds);
        List<AfterSaleActivityDealerDto> result = new ArrayList<>();
        AfterSaleActivityDealerDto dto = null;
        for (CsCompany company : companyList) {
            dto = new AfterSaleActivityDealerDto();
            dto.setId(company.getId());
            dto.setDealerName(company.getCompanyName());
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<AfterSaleActivityDto> getAfterSaleActivityByDealerId(Integer dealerId) {
        return afterSaleActivityReadMapper.getAfterSaleActivityByDealerId(dealerId);
    }

    /**
     * 修改二维码
     * @param activityDto
     * @return
     */
    @Override
    public int updateAfterSaleActivityById(AfterSaleActivityDto activityDto) {
        AfterSaleActivity afterSaleActivity = afterSaleActivityReadMapper.selectByPrimaryKey(activityDto.getId());
        afterSaleActivity.setPublicQrCode(activityDto.getPublicQrCode());
        afterSaleActivity.setUpdateBy(activityDto.getUpdateBy());
        afterSaleActivity.setUpdateDt(new Date());
        int updateFlag = afterSaleActivityWriteMapper.updateByPrimaryKeySelective(afterSaleActivity);
        delActivityCache(afterSaleActivity.getId());
        return updateFlag;
    }
    /**
     * 获取线下兑换正在进行中的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    public List<AfterSaleActivityDto> getOfflineOngoingActivityIdsMap(Integer day){
        if (day==null){
            day=0;
        }
        return afterSaleActivityReadMapper.getOfflineOngoingActivityIdsMap(day);
    }
    /**
     * 获取活动结束前day天的活动
     * @author HuangHao
     * @CreatTime 2021-09-14 11:01
     * @param day
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivity>
     */
    public Map<Integer, ResultMapDto> getEndTimeBeforActivityMap(Integer day){
        if (day==null){
            day=0;
        }
        return afterSaleActivityReadMapper.getEndTimeBeforActivityMap(day);
    }

    @Override
    public List<AfterSaleRewardSlideshowDto> getSaleRewardSlideshow(Integer activityId) {
        if (Objects.isNull(activityId)) {
            return new ArrayList<>();
        }
        AfterSaleRewardRecord afterSaleRewardRecord = new AfterSaleRewardRecord();
        afterSaleRewardRecord.setActivityId(activityId);
        return afterSaleRewardRecordReadMapper.selectSlideshowReward(afterSaleRewardRecord);
    }
    /**
     * 清除活动缓存
     * @author HuangHao
     * @CreatTime 2021-12-02 15:47
     * @param id
     * @return com.tuanche.commons.util.ResultDto
     */
    @Override
    public ResultDto delActivityCache(Integer id){
        String base_chche_key = ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_BASE_CACHE.getKey()+id;
        String config_chche_key = ApiBaseCacheKeys.AFTER_SALE_ACTIVITY_CONFIG_CACHE.getKey() +id;
        try {
            redisService.del(base_chche_key);
            redisService.del(config_chche_key);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMsg("成功");
        return resultDto;
    }

    /**
     * 开启关闭分账
     * @param activityDto
     *
     */
    @Override
    public void openCloseSubAccount(AfterSaleActivityDto activityDto) {
        int updateFlag = afterSaleActivityWriteMapper.openCloseSubAccountById(activityDto);
    }
}
