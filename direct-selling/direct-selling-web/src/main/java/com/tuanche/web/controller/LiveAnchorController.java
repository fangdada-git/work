package com.tuanche.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.directselling.api.LiveDealerAnchorService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveSceneDto;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.web.service.LiveAnchorService;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: czy
 * @Date: 2020/4/30 13:37
 * @Version 1.0
 */
@Controller
@RequestMapping("/anchor")
public class LiveAnchorController extends BaseController {

    @Reference
    private LiveDealerAnchorService liveDealerAnchorService;
    @Reference
    private LiveSceneService liveSceneService;
    @Reference
    private CityBaseService cityBaseService;
    @Autowired
    private PeriodsService periodsService;
    @Autowired
    private LiveAnchorService liveAnchorService;

    /**
     * @param modelMap
     * @return: java.lang.String
     * @description:
     * @author: czy
     * @create: 2020/4/30 13:39
     **/
    @RequestMapping("/list")
    public String list(ModelMap modelMap) {
        List<DistrictOutputBaseDto> provinceList = cityBaseService.getProvinceList();
        modelMap.addAttribute("provinceList", provinceList);
        modelMap.addAttribute("cityList", getAllCity());
        return "live/anchor-list";
    }

    /**
     * @param modelMap
     * @param id
     * @return java.lang.String
     * @Author GongBo
     * @Description 编辑账号页面
     * @Date 15:11 2020/6/11
     **/
    @RequestMapping("/toEditAnchor")
    public String toEditAnchor(ModelMap modelMap, Integer id) {
        LiveDealerAnchor liveDealerAnchor = liveDealerAnchorService.getLiveDealerAnchorById(id);
        List<DistrictOutputBaseDto> provinceList = cityBaseService.getProvinceList();
        modelMap.addAttribute("liveDealerAnchor", liveDealerAnchor);
        modelMap.addAttribute("provinceList", provinceList);
        modelMap.addAttribute("cityList", getAllCity());
        return "live/edit-anchor";
    }

    /**
     * @param pageResult
     * @param liveSceneVo
     * @return: com.tuanche.directselling.utils.PageResult
     * @description: 搜索
     * @author: czy
     * @create: 2020/5/4 19:32
     **/
    @RequestMapping("/list/search")
    @ResponseBody
    public PageResult searchAnchorList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveSceneVo) {
        PageResult PageList = liveDealerAnchorService.findAnchorList(pageResult, liveSceneVo);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @param dealerId
     * @return: java.util.List<com.tuanche.directselling.dto.LiveSceneDto>
     * @description: 获取经销商下活动
     * @author: czy
     * @create: 2020/5/4 19:33
     **/
    @RequestMapping("/list/scene")
    @ResponseBody
    public List<LiveSceneDto> getLiveSceneDtoList(Integer dealerId) {
        LiveSceneVo liveSceneVo = new LiveSceneVo();
        List<Integer> dealerIds = new ArrayList<>();
        dealerIds.add(dealerId);
        liveSceneVo.setDealerIdList(dealerIds);
        List<LiveSceneDto> liveSceneDtoList = liveSceneService.getLiveSceneDtoList(liveSceneVo);
        return liveSceneDtoList;
    }

    /**
     * @param liveSceneVo
     * @return: com.tuanche.commons.util.ResultDto
     * @description:编辑
     * @author: czy
     * @create: 2020/5/4 19:36
     **/
    @RequestMapping("/list/edit")
    @ResponseBody
    public ResultDto save(HttpServletRequest request, @RequestBody LiveDealerAnchor liveSceneVo) {
        XxlUser xxlUser = getLoginUser(request);
        if (xxlUser == null || xxlUser.getId() == null) {
            return loginError();
        }
        try {
            liveAnchorService.doLiveDealerAnchor(liveSceneVo, xxlUser);
        } catch (Exception e) {
            return systemError();
        }
        return success();
    }

    /**
     * @param liveSceneVo
     * @return: com.tuanche.commons.util.ResultDto
     * @description:编辑
     * @author: czy
     * @create: 2020/5/4 19:36
     **/
    @RequestMapping("/list/status")
    @ResponseBody
    public ResultDto editStatus(HttpServletRequest request, @RequestBody LiveDealerAnchor liveSceneVo) {
        XxlUser xxlUser = getLoginUser(request);
        if (xxlUser == null || xxlUser.getId() == null) {
            return loginError();
        }
        try {
            liveSceneVo.setUpdateDt(new Date());
            liveSceneVo.setOperatorUserId(xxlUser.getId());
            liveDealerAnchorService.updateLiveDealerAnchor(liveSceneVo);
        } catch (Exception e) {
            return systemError();
        }
        return success();
    }

    /**
     * @description: 直播账号导出
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/5/20 18:12
     */
    @RequestMapping("/list/export")
    public void editStatus(HttpServletRequest request, HttpServletResponse response, LiveDealerAnchor liveSceneVo) {
        try {
            List<LiveDealerAnchor> list = liveDealerAnchorService.findAnchorListAll(liveSceneVo);
            if (CollectionUtil.isNotEmpty(list)) {
                Map<String, String> titleValueMap = new LinkedHashMap<String, String>();
                titleValueMap.put("ProvinceName", "省份");
                titleValueMap.put("CityName", "城市");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("BrandName", "品牌");
                titleValueMap.put("Address", "门店地址");
                titleValueMap.put("ChargeName", "负责人姓名");
                titleValueMap.put("ChargePhone", "负责人手机号 ");
                titleValueMap.put("EnterpriseAlipay", "企业支付宝");
                titleValueMap.put("EnterpriseAlipayName", "支付宝企业主体");
                titleValueMap.put("StoreName", "经销商淘宝店铺名称");
                titleValueMap.put("TbNick", "门店直播账号");
                titleValueMap.put("AnchorId", "直播User ID");
                titleValueMap.put("HeadImg", "签约状态");
                list.forEach(v -> {
                    if (v.getStatus() == 1) {
                        v.setHeadImg("已签约");
                    } else {
                        v.setHeadImg("未签约");
                    }
                });
                ExportExcel.exportExcel("直播账号.xls", titleValueMap, list, request, response);
            }
        } catch (Exception e) {
            CommonLogUtil.addError("LiveAnchorController", "直播账号导出error", e);
        }
    }

    /**
     * @param modelMap
     * @return java.lang.String
     * @Author GongBo
     * @Description 账号活动列表
     * @Date 18:39 2020/6/10
     **/
    @RequestMapping("/toSceneList")
    public String toSceneList(ModelMap modelMap) {
        LiveSceneVo liveSceneVo = new LiveSceneVo();
        liveSceneVo.setPeriodsGroup(1);
        List<LiveScene> liveSceneList = liveSceneService.getLiveSceneList(liveSceneVo);
        List<LiveSceneDto> liveSceneDtos = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(liveSceneList)) {
            String liveSceneStr = JSON.toJSONString(liveSceneList);
            liveSceneDtos = JSON.parseArray(liveSceneStr, LiveSceneDto.class);
            List<Integer> periodsIds = new ArrayList<>();
            liveSceneList.forEach(v -> {
                periodsIds.add(v.getPeriodsId());
            });
            Map<Integer, ActivityConfigResponseVo> map = periodsService.getActivityMap(periodsIds);
            if (CollectionUtil.isNotEmpty(map)) {
                liveSceneDtos.forEach(v -> {
                    if (map.get(v.getPeriodsId()) != null) {
                        v.setPeriodsName(map.get(v.getPeriodsId()).getName());
                    }
                });
            }
        }
        modelMap.addAttribute("cityList", getAllCity());
        if (liveSceneList != null) {
            modelMap.addAttribute("liveSceneList", liveSceneDtos);
        }
        return "live/scene-list";
    }

    /**
     * @param pageResult
     * @param liveSceneVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 账号活动搜索
     * @Date 10:23 2020/6/11
     **/
    @RequestMapping("/sceneList/search")
    @ResponseBody
    public PageResult searchSceneList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveSceneVo) {
        PageResult PageList = liveDealerAnchorService.findAnchorSceneList(pageResult, liveSceneVo);
        if (PageList != null && CollectionUtil.isNotEmpty(PageList.getData())) {
            List<LiveDealerAnchor> list = PageList.getData();
            List<Integer> periodsIds = new ArrayList<>();
            list.forEach(v -> {
                periodsIds.add(v.getPeriodsId());
            });
            Map<Integer, ActivityConfigResponseVo> map = periodsService.getActivityMap(periodsIds);
            if (CollectionUtil.isNotEmpty(map)) {
                list.forEach(v -> {
                    if (map.get(v.getPeriodsId()) != null) {
                        v.setPeriodsName(map.get(v.getPeriodsId()).getName());
                    }
                });
            }
        }
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @param request
     * @param response
     * @param liveSceneVo
     * @return void
     * @Author GongBo
     * @Description 直播账号活动导出
     * @Date 10:24 2020/6/11
     **/
    @RequestMapping("/sceneList/export")
    public void sceneListExport(HttpServletRequest request, HttpServletResponse response, LiveDealerAnchor liveSceneVo) {
        try {
            List<LiveDealerAnchor> list = liveDealerAnchorService.findAnchorSceneListAll(liveSceneVo);
            if (CollectionUtil.isNotEmpty(list)) {
                Map<String, String> titleValueMap = new LinkedHashMap<String, String>();
                titleValueMap.put("PeriodsName", "直播场次");
                titleValueMap.put("ProvinceName", "省份");
                titleValueMap.put("CityName", "城市");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("BrandName", "品牌");
                titleValueMap.put("Address", "门店地址");
                titleValueMap.put("ChargeName", "负责人姓名");
                titleValueMap.put("ChargePhone", "负责人手机号 ");
                titleValueMap.put("EnterpriseAlipay", "企业支付宝");
                titleValueMap.put("EnterpriseAlipayName", "支付宝企业主体");
                titleValueMap.put("StoreName", "经销商淘宝店铺名称");
                titleValueMap.put("TbNick", "门店直播账号");
                titleValueMap.put("AnchorId", "直播User ID");
                titleValueMap.put("FeedId", "Feed ID");
                titleValueMap.put("HeadImg", "签约状态");

                List<Integer> periodsIds = new ArrayList<>();
                list.forEach(v -> {
                    periodsIds.add(v.getPeriodsId());
                    if (v.getStatus() == 1) {
                        v.setHeadImg("已签约");
                    } else {
                        v.setHeadImg("未签约");
                    }
                });
                Map<Integer, ActivityConfigResponseVo> periodsMap = periodsService.getActivityMap(periodsIds);
                if (CollectionUtil.isNotEmpty(periodsMap)) {
                    list.forEach(v -> {
                        if (periodsMap.get(v.getPeriodsId()) != null)
                            v.setPeriodsName(periodsMap.get(v.getPeriodsId()).getName());
                    });
                }
                ExportExcel.exportExcel("活动账号.xls", titleValueMap, list, request, response);
            }
        } catch (Exception e) {
            CommonLogUtil.addError("LiveAnchorController", "活动账号导出error", e);
        }
    }
}
