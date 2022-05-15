package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.directselling.api.FissionSalesOrderService;
import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.enums.FissionActivityStatus;
import com.tuanche.directselling.enums.FissionOnStatus;
import com.tuanche.directselling.mapper.read.directselling.FissionSalesOrderReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionSalesOrderWriteMapper;
import com.tuanche.directselling.model.FissionSalesOrder;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionSalesOrderVo;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-25 15:54
 **/
@Service
public class FissionSalesOrdeServiceImpl implements FissionSalesOrderService {

    @Autowired
    private FissionSalesOrderReadMapper fissionSalesOrderReadMapper;
    @Autowired
    private FissionSalesOrderWriteMapper fissionSalesOrderWriteMapper;
    @Autowired
    private FissionTradeRecordServiceImpl fissionTradeRecordServiceImpl;
    @Reference
    private UserBaseService userBaseService;
    @Reference
    private CityBaseService cityBaseService;
    @Reference
    private CompanyBaseService companyBaseService;

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public FissionSalesOrder insertSelective (FissionSalesOrder fissionSalesOrder)  throws Exception {
        int n = fissionSalesOrderWriteMapper.insertSelective(fissionSalesOrder);
        if (n==0) {
            return null;
        }
        String orderNo = FuncUtil.createOrderNo("",fissionSalesOrder.getId(),12);
        FissionSalesOrder salesOrder = new FissionSalesOrder();
        salesOrder.setId(fissionSalesOrder.getId());
        salesOrder.setOrderNo(orderNo);
        int nun = updateByPrimaryKeySelective(salesOrder);
        if (nun==0) throw new Exception("生成订单编号失败");
        fissionSalesOrder.setOrderNo(orderNo);
        return fissionSalesOrder;
    }
    @Override
    public Integer updateByPrimaryKeySelective (FissionSalesOrder fissionSalesOrder) {
        int n = fissionSalesOrderWriteMapper.updateByPrimaryKeySelective(fissionSalesOrder);
        return n==1?fissionSalesOrder.getId():0;
    }

    @Override
    public List<FissionSalesOrderDto> getFissionSalesOrderList (FissionSalesOrder salesOrder) {
        return fissionSalesOrderReadMapper.getFissionSalesOrderList(salesOrder);
    }
    @Override
    public List<FissionSalesOrderDto> getFissionSalesOrderListByWrite (FissionSalesOrder salesOrder) {
        return fissionSalesOrderWriteMapper.getFissionSalesOrderListByWrite(salesOrder);
    }

    @Override
    public PageResult getSaleOrderListByPage(PageResult<FissionSalesOrderDto> pageResult, FissionSalesOrderDto fissionSalesOrder) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<FissionSalesOrderDto> fissionGoodsListByPage = fissionSalesOrderReadMapper.getSaleOrderListByPage(fissionSalesOrder);
        if (!CollectionUtils.isEmpty(fissionGoodsListByPage)) {
            List<String> orderNoList = new ArrayList<>();
            List<Integer> saleIds = new ArrayList<>();
            fissionGoodsListByPage.forEach(v->{
                orderNoList.add(v.getOrderNo());
                saleIds.add(v.getSaleId());
            });
            FissionTradeRecord fissionTradeRecord = new FissionTradeRecord();
            fissionTradeRecord.setOrderNoList(orderNoList);
            fissionTradeRecord.setTradeStatus(GlobalConstants.FissionTrade.TRADE_STATUS_PAY_SUCCESS);
            fissionTradeRecord.setTradeType(GlobalConstants.FissionTradeType.ORDER_PAY.getCode());
            Map<String, FissionTradeRecord> recordMap = fissionTradeRecordServiceImpl.getFissionTradeRecordMap(fissionTradeRecord);
            Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleIds);
            for (FissionSalesOrderDto fissionSalesOrderDto : fissionGoodsListByPage) {
                if (recordMap.get(fissionSalesOrderDto.getOrderNo()) !=null)  fissionSalesOrderDto.setTradeNo(recordMap.get(fissionSalesOrderDto.getOrderNo()).getTradeNo());
                if (csUserMap.get(fissionSalesOrderDto.getSaleId()) !=null) {
                    CsUser user = csUserMap.get(fissionSalesOrderDto.getSaleId());
                    fissionSalesOrderDto.setSaleName(user.getUname());
                    fissionSalesOrderDto.setSalePhone(user.getUphone());
                    fissionSalesOrderDto.setCityName(cityBaseService.getCityName(user.getCityId()));
                    fissionSalesOrderDto.setDealerName(companyBaseService.getCsCompanyName(user.getDealerId()));
                } 
            }
        }
        PageInfo<FissionSalesOrderDto> page = new PageInfo<>(fissionGoodsListByPage);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(fissionGoodsListByPage);
        return pageResult;
    }
    
    

    @Override
    public int getFissionSalesOrderCount (FissionSalesOrder salesOrder) {
        return fissionSalesOrderReadMapper.getFissionSalesOrderCount(salesOrder);
    }

    @Override
    public PageResult selectSaleOrder(int page, int limit, Integer saleId) {
        PageResult<FissionSalesOrderVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionSalesOrderDto> fissionSalesOrderDtos = fissionSalesOrderReadMapper.selectSaleOrder(saleId);
        List<FissionSalesOrderVo> fissionSalesOrderVos = new ArrayList<>();
        FissionSalesOrderVo orderVo = null;
        Date now = new Date();
        //增加ActivityStatus 0：未开始 1：进行中 2：已结束
        for (FissionSalesOrderDto salesOrderDto : fissionSalesOrderDtos) {
            orderVo = new FissionSalesOrderVo();
            BeanUtils.copyProperties(salesOrderDto, orderVo);
            orderVo.setActivityStatus(FissionActivityStatus.IS_OVER.getStatus());
            if (salesOrderDto.getStartTime().before(now) && salesOrderDto.getEndTime().after(now)) {
                if (FissionOnStatus.OPEN.getStatus() == salesOrderDto.getOnState()) {
                    orderVo.setActivityStatus(FissionActivityStatus.IN_PROGRESS.getStatus());
                } else {
                    orderVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
                }
            }
            if (salesOrderDto.getStartTime().after(now)) {
                orderVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
            }
            fissionSalesOrderVos.add(orderVo);
        }
        PageInfo<FissionSalesOrderDto> pageInfo = new PageInfo(fissionSalesOrderDtos);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(fissionSalesOrderVos);
        return pageResult;
    }

    @Override
    public List<FissionSalesOrder> getFissionSalesOrderListByFissionId(Integer fissionId) {
        if (fissionId == null) {
            return new ArrayList<>();
        }
        return fissionSalesOrderReadMapper.getFissionSalesOrderListByFissionId(fissionId);
    }
}
