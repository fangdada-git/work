package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.arch.web.model.TcPageInfo;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.StringUtils;
import com.tuanche.consol.dubbo.ActivityConfigService;
import com.tuanche.consol.dubbo.BroadcastBusinessFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.MethodEnum;
import com.tuanche.consol.dubbo.enums.SourcesEnum;
import com.tuanche.consol.dubbo.enums.VersionEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigFormEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigTypeEnum;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.consol.dubbo.vo.base.ConsoleServiceParamVo;
import com.tuanche.directselling.api.LiveHomeService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveDealerBroadcastDto;
import com.tuanche.directselling.dto.LiveInfoDto;
import com.tuanche.directselling.dto.LiveSceneCityDto;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveParameterVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.district.api.dto.input.LocateCityInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputPlusDto;
import com.tuanche.district.api.service.ILocateCityService;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @class: LiveHomeController
 * @description: ??????????????????api
 * @author: dudg
 * @create: 2020-04-29 11:10
 */

@RestController
@RequestMapping("/api/livehome")
public class LiveHomeController extends BaseController{

	@Reference
	LiveSceneService liveSceneService;
	@Autowired
	CommonWebService commonWebService;
    @Reference
    LiveHomeService liveHomeService;
    @Reference
	BroadcastBusinessFacadeService broadcastBusinessFacadeService;
    @Autowired
    PeriodsService periodsService;
    @Reference
	ILocateCityService locateCityService;

	/**
	 * ?????????????????????????????????
	 */
	@Value("${livehome_default_city}")
    private String livehome_default_city;
	@Value("${live_changan_periods_config}")
	private String live_changan_periods_config;
	@Value("${live_changan_pic_config}")
	private String live_changan_pic_config;

	@Reference
	public ActivityConfigService activityConfigService;

	/**
	 *
	 * getLiveBroadcastInprogressList(???????????????????????????????????????)
	 * @param  @param liveDealerBroadcastDto
	 * @param  @return    ????????????
	 * @return List<LiveDealerBroadcastDto>
	 * @Exception ????????????
	 * @since  CodingExample???Ver(??????????????????) 1.1
	 */
	@RequestMapping("/getLiveBroadcastInprogressList")
	public TcResponse getLiveBroadcastInprogressList (LiveDealerBroadcastDto liveDealerBroadcastDto) {
		CommonLogUtil.addInfo("???????????????????????????????????????","???????????????",liveDealerBroadcastDto);
		long st = System.currentTimeMillis();
		if(liveDealerBroadcastDto.getPeriodsId() == null || liveDealerBroadcastDto.getCityId() == null){
		    return paramInvalid(st);
        }

		reSetParamterVo(liveDealerBroadcastDto);
		List<LiveDealerBroadcastDto> list = liveHomeService.getLiveBroadcastInprogressList(liveDealerBroadcastDto);
		return response(StatusCodeEnum.SUCCESS, list,st);
	}
	/**
	 *
	 * getLiveBroadcastPlaybackList(???????????????????????????)
	 * @param  @param pageResult
	 * @param  @param liveDealerBroadcastDto
	 * @param  @return    ????????????
	 * @return PageResult<LiveDealerBroadcastDto>
	 * @Exception ????????????
	 * @since  CodingExample???Ver(??????????????????) 1.1
	 */
	@RequestMapping("/getLiveBroadcastPlaybackList")
	public TcResponse getLiveBroadcastPlaybackList (PageResult<LiveDealerBroadcastDto> pageParam, LiveDealerBroadcastDto liveDealerBroadcastDto) {
		CommonLogUtil.addInfo("???????????????????????????","???????????????",liveDealerBroadcastDto);
		long st = System.currentTimeMillis();
		if(pageParam.getLimit() ==0){
			pageParam.setLimit(10);
		}

        if(liveDealerBroadcastDto.getPeriodsId() == null || liveDealerBroadcastDto.getCityId() == null){
            return paramInvalid(st);
        }

        reSetParamterVo(liveDealerBroadcastDto);
		PageResult<LiveDealerBroadcastDto> pageResult = liveHomeService.getLiveBroadcastPlaybackList(pageParam, liveDealerBroadcastDto);
		TcPageInfo tcPageInfo = new TcPageInfo();
		tcPageInfo.setSize(pageResult.getData() == null ? 0:pageResult.getData().size());
		tcPageInfo.setPageNum(pageResult.getPage());
		tcPageInfo.setPageSize(pageResult.getLimit());
		return response(StatusCodeEnum.SUCCESS, pageResult.getData(), st, tcPageInfo);
	}

	private void reSetParamterVo(LiveDealerBroadcastDto liveDealerBroadcastDto){
        if(liveDealerBroadcastDto.getCityId() == 0){
            // ????????????????????????
            LiveSceneVo liveSceneVo = new LiveSceneVo();
            liveSceneVo.setPeriodsId(liveDealerBroadcastDto.getPeriodsId());
            List<LiveSceneCityDto> liveSceneCityDtoList = liveSceneService.getLiveSceneCityDtoList(liveSceneVo);
            liveDealerBroadcastDto.setCityIds(liveSceneCityDtoList.stream().map(n->n.getId()).collect(Collectors.toList()));
        }else{
            liveDealerBroadcastDto.setCityIds(Lists.newArrayList(liveDealerBroadcastDto.getCityId()));
        }
    }

	/**
	 *
	 * getLiveBroadcastForBrandList(???????????????????????????,????????????????????????)
	 * @param  @param pageResult
	 * @param  @param liveDealerBroadcastDto
	 * @param  @return    ????????????
	 * @return PageResult<LiveDealerBroadcastDto>
	 * @Exception ????????????
	 * @since  CodingExample???Ver(??????????????????) 1.1
	 */
	@RequestMapping("/getLiveBroadcastForBrandList")
	public TcResponse getLiveBroadcastForBrandList (PageResult<LiveDealerBroadcastDto> pageParam, LiveDealerBroadcastDto liveDealerBroadcastDto) {
		CommonLogUtil.addInfo("???????????????????????????,????????????????????????","???????????????",liveDealerBroadcastDto);
		long st = System.currentTimeMillis();
		if(pageParam.getLimit() ==0){
			pageParam.setLimit(10);
		}
		PageResult<LiveDealerBroadcastDto> pageResult = liveHomeService.getLiveBroadcastForBrandList(pageParam, liveDealerBroadcastDto);
		TcPageInfo tcPageInfo = new TcPageInfo();
		tcPageInfo.setSize(pageResult.getData() == null ? 0:pageResult.getData().size());
		tcPageInfo.setPageNum(pageResult.getPage());
		tcPageInfo.setPageSize(pageResult.getLimit());
		return response(StatusCodeEnum.SUCCESS, pageResult.getData(), st, tcPageInfo);
	}

    /***
     * @description: ????????????????????????????????????
     * @param request
     * @param periodsId
     * @param beginTime
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/4/29 19:29
    */
    @RequestMapping("/getrealtimerollnotices")
    public TcResponse getRealTimeRollNotices(LiveParameterVo liveParamVo) {
        long st = System.currentTimeMillis();
        Object result = new ArrayList<>();
        if (liveParamVo == null || liveParamVo.getPeriodsId() == null || liveParamVo.getStartTimeStamp() == null) {
            return response(StatusCodeEnum.PARAM_IS_INVALID, result,st);
        }
        else{
            result = liveHomeService.pullRealTimeRollNotices(liveParamVo.getPeriodsId(), DateUtils.LongToDate(liveParamVo.getStartTimeStamp()));
        }

        return response(StatusCodeEnum.SUCCESS, result,st);
    }

    /**
     * @description: ???????????????????????????????????????
     * @param periodsId
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/4/30 13:39
    */
    @RequestMapping("/getrecdealerliveinfos")
    public TcResponse getRecDealerLiveInfos(LiveParameterVo liveParamVo){
        long st = System.currentTimeMillis();
        List<LiveInfoDto> data = new ArrayList<>();

        ConsolParametersVo parametersVo = new ConsolParametersVo("1","QUERY_ACTIVITY_DEALER_LIST");
        JSONObject param = new JSONObject();
        param.put("activityId", liveParamVo.getPeriodsId());
        param.put("moduleType", 1);
        param.put("cityId", liveParamVo.getCityId());
        parametersVo.setInfo("param", JSONObject.toJSONString(param));
        parametersVo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        ConsolParametersVo resultVo = broadcastBusinessFacadeService.service(parametersVo);
        CommonLogUtil.addInfo("???????????????????????????????????????","??????BroadcastBusinessFacadeService.service???????????????",resultVo);
        if (null != resultVo && resultVo.getResCode().equalsIgnoreCase("0000") && !StringUtils.isEmpty(resultVo.getString("data"))) {
            List<Long> anchorIdList = new ArrayList<>();
            JSONArray anchorIdArr = JSONObject.parseObject(resultVo.getString("data")).getJSONArray("list");
            if(anchorIdArr.size()>0){
                for (int i = 0; i < anchorIdArr.size(); i++) {
                    System.out.println(anchorIdArr.getJSONObject(i).get("dealerUid"));
                    anchorIdList.add(Long.parseLong(anchorIdArr.getJSONObject(i).getString("dealerUid")));
                }
            }
            liveParamVo.setAnchorIds(anchorIdList);
            // ???????????????
            data = liveHomeService.getLivingOrPlayBackListByAnchorAndPeriods(liveParamVo);
        }

        if(CollectionUtils.isEmpty(data)){
            return response(StatusCodeEnum.RESULE_DATA_NONE,null,st);
        }

        return response(StatusCodeEnum.SUCCESS, data, st);
    }

    /**
     * @description: ??????????????????????????????
     * @param request
	 * @param liveParamVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/5/7 17:47
    */
    @RequestMapping("/getperiodscitylist")
    public TcResponse getPeriodsCityList(HttpServletRequest request, LiveParameterVo liveParamVo){
    	CommonLogUtil.addInfo("??????????????????????????????","???????????????",liveParamVo);
		long st = System.currentTimeMillis();
		TcResponse result = new TcResponse();
		// ????????????id
		try {
			Integer locaCityId = 0;
			// ???????????????????????????
			ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
			activityConfigRequestVo.setActivityFormEnum(ActivityConfigFormEnum.ONLINE);
			activityConfigRequestVo.setActivityTypeEnum(ActivityConfigTypeEnum.ONLINE_ACTIVITY);
			ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo = ConsoleServiceParamVo.builder()
					.sources(SourcesEnum.H5)
					.method(MethodEnum.ACTIVITY_CONFIG_QUERY_CURRENT_INFO)
					.version(VersionEnum.v1_0)
					.requestData(activityConfigRequestVo)
					.build();

			ConsoleServiceParamVo serviceParamVo = activityConfigService.service(consoleServiceParamVo);
			ActivityConfigResponseVo responseVo = (ActivityConfigResponseVo) serviceParamVo.getResponseData();
			// ???????????????????????????/????????? ???????????????
			if (responseVo == null || responseVo.getId() == 0 || responseVo.getSingleFlag() == 0) {
				LocateCityInputDto locateCityInputDto = new LocateCityInputDto();
				locateCityInputDto.setIp(getIpAddress(request));
				if (liveParamVo.getLongitude() != null && liveParamVo.getLatitude() != null) {
					locateCityInputDto.setLatitude(liveParamVo.getLatitude());
					locateCityInputDto.setLongitude(liveParamVo.getLongitude());
				}
				CommonLogUtil.addInfo("com.tuanche.district.api.service.ILocateCityService.findCity","???????????????????????????????????????",locateCityInputDto);
				DistrictOutputPlusDto locationCity = locateCityService.findCity(locateCityInputDto);
				CommonLogUtil.addInfo("com.tuanche.district.api.service.ILocateCityService.findCity","???????????????????????????????????????",locationCity);
				if (locationCity != null && locationCity.getId() != null) {
					locaCityId = locationCity.getId();
				}
			}

			// ????????????????????????
			LiveSceneVo liveSceneVo = new LiveSceneVo();
			liveSceneVo.setPeriodsId(liveParamVo.getPeriodsId());
			List<LiveSceneCityDto> liveSceneCityDtoList = liveSceneService.getLiveSceneCityDtoList(liveSceneVo);
			if (null == liveSceneCityDtoList) {
				result = noData(st);
			} else {
				// ??????????????????
				Map<String,List<LiveSceneCityDto>> cityMap = commonWebService.groupByPinYinHeadCharByLambda(liveSceneCityDtoList);
				Map<String, Object> data = Maps.newHashMap();
				// ???????????????
				final Integer tmpCityId = locaCityId;
				Optional<LiveSceneCityDto> liveSceneCityDto = liveSceneCityDtoList.stream().filter(n -> n.getId().equals(tmpCityId)).findFirst();
				if(liveSceneCityDto.isPresent()){
					data.put("location", liveSceneCityDto.get());
				}
				else{
				    //?????? 0 ??????
					LiveSceneCityDto cityDto = new LiveSceneCityDto();
                    cityDto.setId(0);
					data.put("location", cityDto);
				}
				data.put("list",cityMap);
				result = success(data,st);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommonLogUtil.addError("????????????????????????????????????????????? ????????????",e.getMessage(),e);
			result = systemError(st);
		}
		return result;
	}

    public String getIpAddress(HttpServletRequest request) {
        // ??????????????????IP??????,?????????????????????????????????????????????????????????IP??????
        String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
	 * @description: ????????????????????????????????????????????????????????????&????????????????????????????????????????????????????????????????????????
	 * @param request
	 * @param liveDealerBroadcastDto
	 * @return: com.tuanche.arch.web.model.TcResponse
	 * @author: dudg
	 * @date: 2020/5/10 16:18
	*/
	@RequestMapping("/getpreheatlivingandplaybacklist")
	public TcResponse getPreheatLivingAndPlayBackList(PageResult<LiveDealerBroadcastDto> pageParam, LiveDealerBroadcastDto liveDealerBroadcastDto){
		CommonLogUtil.addInfo("??????/????????????????????????????????????","???????????????",liveDealerBroadcastDto);
		long st = System.currentTimeMillis();

		if(pageParam.getLimit() ==0){
			pageParam.setLimit(10);
		}
		Integer cityId = liveDealerBroadcastDto.getCityId();
		liveDealerBroadcastDto.setCityId(0);
		reSetParamterVo(liveDealerBroadcastDto);
		liveDealerBroadcastDto.setCityId(cityId);
        PageResult<LiveDealerBroadcastDto> pageResult = liveHomeService.getPreheatLivingAndPlayBackList(pageParam, liveDealerBroadcastDto);
		TcPageInfo tcPageInfo = new TcPageInfo();
		tcPageInfo.setSize(pageResult.getData() == null ? 0:pageResult.getData().size());
		tcPageInfo.setPageNum(pageResult.getPage());
		tcPageInfo.setPageSize(pageResult.getLimit());
		return response(StatusCodeEnum.SUCCESS, pageResult.getData(), st, tcPageInfo);
	}

	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@GetMapping("/getTcLivinginfo")
	public TcResponse getTcAnchorLivingBroadcast(){
		long st = System.currentTimeMillis();
		TcResponse result = new TcResponse();
		LiveParameterVo liveParameterVo = JSON.parseObject(live_changan_periods_config,LiveParameterVo.class);
		try {
			LiveDealerBroadcastDto livingBroadcast = liveHomeService.getTcAnchorLivingBroadcast(liveParameterVo);
			if (livingBroadcast == null) {
				result = noData(st);
			}
			else{
				result = success(livingBroadcast,st);
			}
			CommonLogUtil.addInfo("??????????????????????????????????????????", "????????????", result);
		} catch (Exception e) {
			CommonLogUtil.addError("??????????????????????????????????????????error","???????????????"+ JSON.toJSONString(liveParameterVo),e);
			result = systemError(st);
		}
		return result;
	}

	/**
	 * ??????????????????????????????
	 * @return
	 */
	@GetMapping("/getTcLivePlaybackList")
	public TcResponse getTcAnchorLivePlayBackList(LiveParameterVo liveParameterVo){
		long st = System.currentTimeMillis();
		TcResponse result = new TcResponse();
		LiveParameterVo livePeriodsConfig = JSON.parseObject(live_changan_periods_config,LiveParameterVo.class);
		try {
			livePeriodsConfig.setExcludeLiveId(liveParameterVo.getExcludeLiveId());
			List<LiveDealerBroadcastDto> tcAnchorLivePlaybackList = liveHomeService.getTcAnchorLivePlaybackList(livePeriodsConfig);
			if (CollectionUtils.isEmpty(tcAnchorLivePlaybackList)) {
				result = noData(st);
			}
			else{
				result = success(tcAnchorLivePlaybackList,st);
			}
			CommonLogUtil.addInfo("???????????????????????????????????????", "????????????", result);
		} catch (Exception e) {
			CommonLogUtil.addError("???????????????????????????????????????error","???????????????"+ JSON.toJSONString(liveParameterVo),e);
			result = systemError(st);
		}
		return result;
	}


	/**
	 * ???????????????Banner&??????????????????
	 * @return
	 */
	@GetMapping("/getChangAnBannerAndCarFinance")
	public TcResponse getTcAnchorLivePlayBackList(){
		long st = System.currentTimeMillis();
		return success(JSON.parseObject(live_changan_pic_config, Map.class),st);
	}
}
