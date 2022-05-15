package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleUserService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.AfterSaleUserBrowseDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleUserBrowseWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleUserWriteMapper;
import com.tuanche.directselling.model.AfterSaleUser;
import com.tuanche.directselling.model.AfterSaleUserBrowse;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 14:12
 */
@Service(retries = 0,timeout = 20000)
public class AfterSaleUserServiceImpl implements AfterSaleUserService {

    @Autowired
    AfterSaleUserReadMapper afterSaleUserReadMapper;
    @Autowired
    AfterSaleUserWriteMapper afterSaleUserWriteMapper;
    @Autowired
    AfterSaleUserBrowseWriteMapper afterSaleUserBrowseWriteMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;



    /**
     * 新增用户浏览记录
     * @author HuangHao
     * @CreatTime 2021-07-21 17:09
     * @param afterSaleUserBrowseDto
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse userBrowse(AfterSaleUserBrowseDto afterSaleUserBrowseDto){
        if(afterSaleUserBrowseDto == null || afterSaleUserBrowseDto.getActivityId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动ID不能为空");
        }
        if(StringUtils.isEmpty(afterSaleUserBrowseDto.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信UnionId不能为空");
        }
        String keyWord = "售后特卖浏览记录,活动ID:"+afterSaleUserBrowseDto.getActivityId()+"，用户："+afterSaleUserBrowseDto.getUserWxUnionId();
        CommonLogUtil.addInfo(null, keyWord+"，开始");
        String pageUrl = afterSaleUserBrowseDto.getPageUrl();
        if (!StringUtil.isEmpty(pageUrl) && pageUrl.length() > 256) {
            afterSaleUserBrowseDto.setPageUrl(pageUrl.substring(0, 255));
        }
        //新增浏览记录
        AfterSaleUserBrowse afterSaleUserBrowse = new AfterSaleUserBrowse();
        BeanUtils.copyProperties(afterSaleUserBrowseDto, afterSaleUserBrowse);
        afterSaleUserBrowseWriteMapper.insert(afterSaleUserBrowse);
        CommonLogUtil.addInfo(null, keyWord+"，新增浏览记录完成，新增数据ID:"+afterSaleUserBrowse.getId());
        //用户处理
        AfterSaleUser user = getUser(afterSaleUserBrowseDto.getActivityId(),afterSaleUserBrowseDto.getUserWxUnionId());
        //头像，昵称 不相等则修改
        boolean saveUserFlag = (user == null || !afterSaleUserBrowseDto.getNickName().equals(user.getNickName()) || !afterSaleUserBrowseDto.getUserWxHead().equals(user.getUserWxHead()));
        CommonLogUtil.addInfo(null, keyWord+"，用户是否需要保存："+saveUserFlag);
        if (saveUserFlag) {
            if (Objects.isNull(user)) {
                user = new AfterSaleUser();
            }
            BeanUtils.copyProperties(afterSaleUserBrowseDto, user);
            TcResponse tcResponse = saveUser(user);
            CommonLogUtil.addInfo(null, keyWord + "，新增用户返回结果:" + tcResponse.getResponseHeader().getMessage());
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * 新增活动用户
     * @author HuangHao
     * @CreatTime 2021-07-21 15:31
     * @param user
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse saveUser(AfterSaleUser user){
        if(user == null || user.getActivityId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动ID不能为空");
        }
        if(user.getUserWxOpenId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信openId不能为空");
        }
        if(StringUtils.isEmpty(user.getNickName())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信昵称不能为空");
        }
        if(StringUtils.isEmpty(user.getUserWxHead())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信头像不能为空");
        }
        /*String nickName = null;
        if (StringUtils.isEmpty(user.getNickName())) {
            nickName = "匿名";
        } else {
            //微信昵称特殊字符过滤
            nickName = EmojiFilter.emojiConverter(user.getNickName());
        }
        user.setNickName(nickName);*/
        if(Objects.isNull(user.getId())){
            afterSaleUserWriteMapper.insert(user);
        }else{
            user.setUpdateDt(new Date());
            afterSaleUserWriteMapper.update(user);
            String key = getCaheUserKey(user.getActivityId(), user.getUserWxUnionId());
            try {
                redisService.del(key);
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }

        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * 获取用户-先从缓存获取没有再从数据库取
     * @author HuangHao
     * @CreatTime 2021-07-21 17:10
     * @param activityId
     * @param userWxUnionId
     * @return com.tuanche.directselling.model.AfterSaleUser
     */
    public AfterSaleUser getUser(Integer activityId,String userWxUnionId){
        if(activityId == null || StringUtils.isEmpty(activityId)){
            return null;
        }
        String key = getCaheUserKey(activityId, userWxUnionId);
        AfterSaleUser user = null;
        try {
            user = redisService.get(key,AfterSaleUser.class);
            if(user == null){
                AfterSaleUser afterSaleUser = new AfterSaleUser();
                afterSaleUser.setActivityId(activityId);
                afterSaleUser.setUserWxUnionId(userWxUnionId);
                user = afterSaleUserReadMapper.getUser(afterSaleUser);
                if(user != null){
                    caheUser(user);
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Map<String, AfterSaleUser> getUserMapByActivityIdAndUnionId (List<AfterSaleUser> userList) {
        Map<String, AfterSaleUser> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(userList)) {
            List<AfterSaleUser> list = afterSaleUserReadMapper.getUserListByActivityIdAndUnionId(userList);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(v->{
                    if (map.get(v.getActivityId()+":"+v.getUserWxUnionId())==null) map.put(v.getActivityId()+":"+v.getUserWxUnionId(), v);
                });
            }
        }
        return map;
    }

    /**
     * 缓存用户
     * @author HuangHao
     * @CreatTime 2021-07-21 14:39
     * @param user
     * @return void
     */
    private void caheUser(AfterSaleUser user){
        String key = getCaheUserKey(user.getActivityId(), user.getUserWxUnionId());
        try {
            redisService.set(key, user, BaseCacheKeys.AFTER_SALE_ACTIVITYID_USER.getExpire());
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }
    public String getCaheUserKey(Integer activityId,String userWxUnionId) {
        return MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_USER.getKey(), activityId, userWxUnionId);
    }

    /**
     * 获取UV
     * @author HuangHao
     * @CreatTime 2021-08-24 11:42
     * @param
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> getUvMap(List<Integer> activityIds){
        if(CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleUserReadMapper.getUvMap(activityIds);
    }
}
