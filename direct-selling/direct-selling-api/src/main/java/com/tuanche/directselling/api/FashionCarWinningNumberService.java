package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.FashionCarWinningNumberDto;
import com.tuanche.directselling.exception.WinningNumberException;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;

import java.util.List;

/**
 * 潮车集-半价车-中奖号码
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/3 10:58
 **/
public interface FashionCarWinningNumberService {


    /**
     * 初始化中奖码
     *
     * @return
     */
    void initWinningNumber(Integer periodsId, long expire);

    /**
     * 生成中奖码
     *
     * @param periodsId  场次ID
     * @param userId     被助力用户id、中奖码所属人
     * @param helpUserId fashion_car_marke_helper_user表id,为null则表示是转盘抽中的中奖码
     * @param userType   com.tuanche.directselling.enums.FashionCarUserType
     * @return 中奖码
     */
    String createWinningNumber(Integer periodsId, Integer userId, Integer helpUserId, Integer userType) throws WinningNumberException;

    /**
     * 获取用户中奖码
     *
     * @param periodsId
     * @param userId
     * @return
     */
    List<FashionCarWinningNumberDto> getWinningNumbers(Integer periodsId, Integer userId);
    /**
     * 获取用户中奖码数量
     *
     * @param periodsId
     * @param userId
     * @return
     */
    Integer getWinningNumberCount(Integer periodsId, Integer userId);

    List<FashionCarMarkeWinNum> getFashionCarMarkeWinNumList(FashionCarMarkeWinNum markeWinNum);


    /**
     * 开奖(定时任务)
     */
    void chooseWinningNumber();


    void processWinningNumber();

    Integer changeWinNum(Integer winNum);
}
