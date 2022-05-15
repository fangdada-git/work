package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.model.LiveScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: BackDoorController
 * @Description: 后门接口
 * @Author: GongBo
 * @Date: 2020年3月12日17:23:23
 * @Version 1.0
 **/
@Controller
@RequestMapping("/backDoor")
public class BackDoorController {

    @Reference
    LiveSceneService liveSceneService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 创建场次活动失败  重新创建直播计划
     * @Date 2020年3月9日11:58:04
     **/
    @RequestMapping("/syncBroadcastRpc")
    @ResponseBody
    public String syncBroadcastRpc(Integer sceneId) {
        // 获取场次活动信息
        LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
        return liveSceneService.syncSceneToBroadcast(liveScene);
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 创建场次活动失败  重新创建主机位
     * @Date 2020年3月9日11:58:04
     **/
    @RequestMapping("/createSceneHostCode")
    @ResponseBody
    public String createSceneHostCode(Integer sceneId) {
        // 获取场次活动信息
        LiveScene liveScene = liveSceneService.getLiveSceneById(sceneId);
        return liveSceneService.createSceneHostCode(liveScene);
    }

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 手动删除售后特卖活动缓存
     * @Date 2020年3月9日11:58:04
     **/
    @RequestMapping("/deleteAfterSaleActivityRedis")
    @ResponseBody
    public void deleteAfterSaleActivityRedis(Integer activityId) throws RedisException {
        if (activityId != null && activityId > 0) {
            System.out.println("手动删除售后特卖活动缓冲");
            redisService.del("after:sale:activity:config:" + activityId);
        }
    }

}
