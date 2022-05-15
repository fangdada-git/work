package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.GiftRefuelingCardActivityDto;
import com.tuanche.directselling.dto.GiftRefuelingcardGiftrecordDto;
import com.tuanche.directselling.model.GiftRefuelingcardGiftrecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.GiftRefuelingcardGiftrecordVo;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-13 10:19
 */
public interface GiftRefuelingcardGiftrecordService {

    /**
     * 获取油卡赠送记录列表-分页
     * @author HuangHao
     * @CreatTime 2020-05-12 17:57
     * @param
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardGiftrecord>
     */
    PageResult<GiftRefuelingcardGiftrecordDto> getGiftRefuelingcardGiftrecordByPage(GiftRefuelingcardGiftrecordVo giftrecord);

    /**
     * 经销商获取油卡赠送活动信息
     * @author HuangHao
     * @CreatTime 2020-05-13 16:19
     * @param dealerId
     * @return com.tuanche.directselling.dto.GiftRefuelingCardActivityDto
     */
    GiftRefuelingCardActivityDto getActinityInfo(Integer dealerId,Integer periodsId);

    /**
     * 赠送油卡
     * @author HuangHao
     * @CreatTime 2020-05-14 11:26
     * @param giftrecord
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse giveRefuelingcard(GiftRefuelingcardGiftrecordVo giftrecord);

    /**
     * 用户到店核销
     * @author HuangHao
     * @CreatTime 2020-05-14 14:24
     * @param phone
     * @param dealerId
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse writeOff(String phone,Integer dealerId,Integer periodsId);

    /**
     * 重发短信
     * @author HuangHao
     * @CreatTime 2020-05-14 18:12
     * @param id
     * @param cityId
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse reSendMsg(Integer id,Integer cityId);
}
