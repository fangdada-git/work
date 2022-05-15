package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.broadcast.rpc.enums.BroadcastRoomMethodEnum;
import com.tuanche.broadcast.rpc.service.BroadcastRoomBusinessFacadeService;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomRequestDto;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomResponseDto;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FissionCommonServiceImpl {

    @Reference
    BroadcastRoomBusinessFacadeService broadcastRoomBusinessFacadeService;

    /**
     * 获取直播间详情
     * @param broadcastRoomId
     * @return
     */
    public BroadcastRoomResponseDto getBroadcastRoomInfo(Integer broadcastRoomId){
        String source = "1";
        String platform = "4";

        BroadcastRoomRequestDto param = new BroadcastRoomRequestDto();
        param.setBroadcastRoomId(broadcastRoomId);

        BroadcastRpcVo vo = new BroadcastRpcVo(source, BroadcastRoomMethodEnum.API_QUERY_SIMPLE_BY_FISSION, platform);
        vo.setInfo("param", JSONObject.toJSONString(param));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        BroadcastRpcVo resultVo = broadcastRoomBusinessFacadeService.service(vo);
        if (resultVo != null) {
            if (resultVo.getResCode().equals(BroadcastRpcConstants.RESULT_SUCCESS)) {
                // 结果值
                List<BroadcastRoomResponseDto> list = JSONObject.parseArray(resultVo.getString("data"), BroadcastRoomResponseDto.class);
                return list.get(0);
            }
        }

        return null;
    }
    
}
