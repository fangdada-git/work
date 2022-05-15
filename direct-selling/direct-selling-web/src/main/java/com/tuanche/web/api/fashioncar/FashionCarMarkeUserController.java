package com.tuanche.web.api.fashioncar;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.po.MemberPo;
import com.tuanche.arch.service.MemberService;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.api.FashionCarMarkeUserService;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.EmojiFilter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/fashioncar/user")
@RestController
public class FashionCarMarkeUserController {
    
    @Reference
    private FashionCarMarkeUserService fashionCarMarkeUserService;
    @Autowired
    private CommonWebService commonWebService;
    @Reference
    private MemberService memberService;

    /**
      * @description : 潮车集添加c端用户
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/9/7 16:05
      */
    @RequestMapping("/addUser")
    @ResponseBody
    public TcResponse addUser (HttpServletRequest request, FashionCarMarkeUser markeUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeUserController","addUser",  "潮车集添加c端用户 start " +st, JSON.toJSONString(markeUser));
        if (markeUser.getUserId()==null) markeUser.setUserId(commonWebService.getMemberPoIdByToken(request));
        List<FashionCarMarkeHelperUser> list = null;
        if (markeUser.getPeriodsId()==null || (markeUser.getUserWxUnionId()==null && markeUser.getUserId()==null)
            ||markeUser.getUserWxHead()==null || markeUser.getNickName()==null
        ) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            if (markeUser.getCityId()==null) {
                Integer cityId = commonWebService.getCityIdByNameOrLongitudeLatitudeOrIp(request, null, null, null);
                markeUser.setCityId(cityId);
            }
            MemberPo memberPo = new MemberPo();
            memberPo.setId(markeUser.getUserId());
            MemberPo userInfo = memberService.queryMemberByParams(memberPo);
            markeUser.setNickName(EmojiFilter.filterEmoji(markeUser.getNickName()));
            markeUser.setUserPhone(userInfo != null ? String.valueOf(userInfo.getPhone()) : null);
            FashionCarMarkeUser user = new FashionCarMarkeUser();
            user.setPeriodsId(markeUser.getPeriodsId());
            user.setUserId(markeUser.getUserId());
            user.setUserWxUnionId(markeUser.getUserWxUnionId());
            List<FashionCarMarkeUser> userList = fashionCarMarkeUserService.getUserListToOr(user);
            if (CollectionUtils.isEmpty(userList)) {
                fashionCarMarkeUserService.addUser(markeUser);
            } else {
                FashionCarMarkeUser oldUser = userList.get(0);
                markeUser.setId(oldUser.getId());
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeUserController","addUser",  "潮车集添加c端用户 用户存在，更新用户信息 " +st, JSON.toJSONString(oldUser));
                fashionCarMarkeUserService.updateUser(markeUser);
                if (oldUser.getUserId()!=null && markeUser.getUserId()!=null && oldUser.getUserWxUnionId()!=null && markeUser.getUserWxUnionId()!=null
                        && (!oldUser.getUserId().equals(markeUser.getUserId()) || !oldUser.getUserWxUnionId().equals(markeUser.getUserWxUnionId()))) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.USER_HAS_EXISTED.getCode(), "您正在用当前微信账号参与此活动，原有微信账号活动数据将被更换", st,
                            "FashionCarMarkeUserController", "addUser", "");
                }
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarMarkeUserController", "潮车集添加c端用户 error", e, st, JSON.toJSONString(markeUser));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeUserController","addUser",  "潮车集添加c端用户 end " +st, JSON.toJSONString(list));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
    }
    
}
