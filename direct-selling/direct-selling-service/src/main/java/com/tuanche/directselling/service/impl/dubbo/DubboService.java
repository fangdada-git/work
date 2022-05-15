package com.tuanche.directselling.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.broadcast.rpc.service.TcBroadCastPlanService;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.manu.api.TDealerGoodsGiftService;
import com.tuanche.manu.entity.TDealerGoods;
import com.tuanche.manu.entity.TDealerGoodsGift;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.merchant.api.image.CommodityImageRpcService;
import com.tuanche.merchant.pojo.dto.image.CommodityImageDO;
import com.tuanche.merchant.pojo.dto.image.CommodityImageRes;
import com.tuanche.storage.api.brand.CarBrandService;
import com.tuanche.storage.api.carstyle.CarStyleService;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.storage.dto.carstyle.CarStyleDto;
import com.tuanche.storage.dto.common.ApiDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Descriptionrpc 调用公共接口
 * @author zhangxing
 * @version
 * @date 2019年12月10日下午3:34:05
 */
@Service
public class DubboService {
	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.broadcast.rpc.service.TcBroadCastPlanService")
	public TcBroadCastPlanService tcBroadCastPlanService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.merchant.api.image.CommodityImageRpcService")
	private CommodityImageRpcService commodityImageRpcService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.manu.api.TDealerGoodsGiftService")
	private TDealerGoodsGiftService tDealerGoodsGiftService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.manubasecenter.api.CityBaseService")
	private CityBaseService cityBaseService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.manubasecenter.api.CompanyBaseService")
	private CompanyBaseService companyBaseService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.storage.api.brand.CarBrandService")
	private CarBrandService carBrandService;

    @Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.manubasecenter.api.CarBaseService")
    private CarBaseService carBaseService;

	@Reference(timeout = 9000,check = false,interfaceName = "com.tuanche.storage.api.carstyle.CarStyleService")
	private CarStyleService carStyleService;
	/**
	 * ms后台配置服务
	 * @param vo
	 * @return
	 */
	public BroadcastRpcVo broadCastPlanService(BroadcastRpcVo vo) throws Exception{
		BroadcastRpcVo resultVo = null;
		try {
			CommonLogUtil.addInfo("rpc调用直播系统","开始", vo.toString());
			resultVo = tcBroadCastPlanService.createCastSchedules(vo);
		} catch (Exception e) {
            CommonLogUtil.addError("rpc调用直播系统异常",vo.toString(), e);
            throw new Exception("rpc调用直播系统异常",e);
		}
		if (resultVo == null) {
			CommonLogUtil.addInfo("rpc调用直播系统","失败", vo.toString());
			throw new Exception("rpc调用直播系统失败，返回对象为空");
		}
		CommonLogUtil.addInfo("rpc调用直播系统","结束", resultVo.toString());
		if (BroadcastRpcConstants.RESULT_FAIL.equals(resultVo.getResCode()) || StringUtils.isEmpty(resultVo.getString("result"))) {
			throw new Exception("rpc调用直播系统业务处理失败");
		}
		return resultVo;
	}

	/**
	 * @program:  * @param brandIdList
	 * @return: java.util.List<com.tuanche.directselling.vo.LiveSceneBrandListVo>
	 * @description: 通过品牌ID集合  获取品牌数据集合
	 * @author: czy
	 * @create: 2020/4/22 16:39
	 **/
	public List<CarBrandDto> getBrandListByCbIdList(List<Integer> brandIdList){
		List<CarBrandDto> carBrandDtos = new ArrayList<>();
		try {
			CommonLogUtil.addInfo("rpc调用carBrandService==","开始", JSON.toJSONString(brandIdList));
			carBrandDtos = carBrandService.listByCbIds(new ApiDto(brandIdList, 0));
		} catch (Exception e) {
			carBrandDtos = null;
			CommonLogUtil.addError("rpc调用carBrandService异常",JSON.toJSONString(brandIdList), e);
		}

		CommonLogUtil.addInfo("rpc调用carBrandService==","结束", JSON.toJSONString(carBrandDtos));
		return carBrandDtos;
	}
	/**
	 * 生成海报
	 */
	public List<CommodityImageRes> generateTmImagesBatch(List<CommodityImageDO> list){
		try {
			CommonLogUtil.addInfo("rpc调用生成海报","开始", JSON.toJSONString(list));
			return commodityImageRpcService.generateTmImagesBatch(list);
		} catch (Exception e) {
			CommonLogUtil.addError("rpc调用生成海报异常","generateTmImagesBatch", e);
			return null;
		}
	}

	/**
	 * 预热海报需要到店礼品
	 */
	public TDealerGoodsGift getGoodsGiftByAIdAndDIdAndGTypeAndStatus(TDealerGoods goods){
		try{
			CommonLogUtil.addInfo("预热海报需要到店礼品","开始", JSON.toJSONString(goods));
			return tDealerGoodsGiftService.getGoodsGiftByAIdAndDIdAndGTypeAndStatus(goods);
		} catch (Exception e) {
			CommonLogUtil.addError("预热海报需要到店礼品","getGoodsGiftByAIdAndDIdAndGTypeAndStatus==", e);
			return null;
		}
	}

	public String getCityName(Integer cityId){
		try {
			CommonLogUtil.addInfo("rpc调用CityBaseService","开始== ", cityId);
			return cityBaseService.getCityName(cityId);
		} catch (Exception e) {
			CommonLogUtil.addError("rpc调用CityBaseService异常","getCityName", e);
			return null;
		}
	}

	public String getDealerName(Integer dealerId){
		try {
			CommonLogUtil.addInfo("rpc调用CompanyBaseService","开始== ", dealerId);
			return companyBaseService.getCsCompanyName(dealerId);
		} catch (Exception e) {
			CommonLogUtil.addError("rpc调用CompanyBaseService异常","getCityName", e);
			return null;
		}
	}

    public String getBrandName(Integer brandId){
        try {
            CommonLogUtil.addInfo("rpc调用carBaseService","开始== ", brandId);
            return carBaseService.getBrandName(brandId);
        } catch (Exception e) {
            CommonLogUtil.addError("rpc调用carBaseService异常","getCityName", e);
            return null;
        }
    }

	/**
	 * @program:  * @param masterBrandId
	 * @return: java.util.List<CarBrandDto>
	 * @description: 根据主品牌id获取在售的品牌列表
	 * @author: gongbo
	 * @create: 2020年6月5日17:31:01
	 **/
	public List<CarBrandDto> listByMasterBrandId(Integer masterBrandId){
		List<CarBrandDto> carBrandDtos = new ArrayList<>();
		try {
			CommonLogUtil.addInfo("rpc调用carBrandService==","开始", masterBrandId);
			carBrandDtos = carBrandService.listByMasterBrandId(masterBrandId);
		} catch (Exception e) {
			carBrandDtos = null;
			CommonLogUtil.addError("rpc调用carBrandService异常", masterBrandId.toString(), e);
		}

		CommonLogUtil.addInfo("rpc调用carBrandService==","结束", JSON.toJSONString(carBrandDtos));
		return carBrandDtos;
	}

	/**
	 * @param carStyleIds
	 * @return java.util.List<com.tuanche.storage.dto.carstyle.CarStyleDto>
	 * @Author GongBo
	 * @Description 根据车型ids 获取车型集合
	 * @Date 18:46 2020/6/5
	 **/
	public List<CarStyleDto> getStyleListByIds(List<Integer> carStyleIds){
		List<CarStyleDto> carStyleDtoList = new ArrayList<>();
		try {
			CommonLogUtil.addInfo("rpc调用carBaseService==","开始", carStyleIds);
			carStyleDtoList = carStyleService.listByCsIds(carStyleIds);
		} catch (Exception e) {
			carStyleDtoList = null;
			CommonLogUtil.addError("rpc调用carBaseService异常", carStyleIds.toString(), e);
		}

		CommonLogUtil.addInfo("rpc调用carBaseService==","结束", JSON.toJSONString(carStyleDtoList));
		return carStyleDtoList;
	}

	/**
	 * @param brandId
	 * @return java.util.List<com.tuanche.storage.dto.carstyle.CarStyleDto>
	 * @Author GongBo
	 * @Description 根据品牌ID 获取车型集合
	 * @Date 18:46 2020/6/5
	 **/
	public List<CarStyleDto> getStyleListByBrandId(Integer brandId){
		List<CarStyleDto> carStyleDtoList = new ArrayList<>();
		try {
			CommonLogUtil.addInfo("rpc调用carBaseService==getStyleListByBrandId","开始", brandId);
			carStyleDtoList = carBaseService.getNewStyleListByBrandId(brandId);
		} catch (Exception e) {
			carStyleDtoList = null;
			CommonLogUtil.addError("rpc调用carBaseService==getStyleListByBrandId异常", brandId.toString(), e);
		}

		CommonLogUtil.addInfo("rpc调用carBaseService==getStyleListByBrandId","结束", JSON.toJSONString(carStyleDtoList));
		return carStyleDtoList;
	}
}
