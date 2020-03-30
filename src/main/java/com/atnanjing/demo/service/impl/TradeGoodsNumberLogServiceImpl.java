package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.TradeGoodsDTO;
import com.atguigu.demo.dto.TradeGoodsNumberLogDTO;
import com.atguigu.demo.service.ITradeGoodsNumberLogService;
import com.atguigu.demo.service.Result;
import com.atnanjing.demo.dao.TradeGoodsNumberLog;
import com.atnanjing.demo.dao.TradeGoodsNumberLogExample;
import com.atnanjing.demo.exception.CastException;
import com.atnanjing.demo.mapper.TradeGoodsNumberLogMapper;
import com.atnanjing.demo.utils.ShopCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class TradeGoodsNumberLogServiceImpl implements ITradeGoodsNumberLogService {
    @Autowired
    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;

    @Autowired
    private GoodsServiceImpl goodsService;
    @Override
    public Result insertTradeGoodsNumberLog(TradeGoodsNumberLogDTO tradeGoodsNumberLogDTO) {
        if(tradeGoodsNumberLogDTO==null || tradeGoodsNumberLogDTO.getGoodsNumber()==null
                || tradeGoodsNumberLogDTO.getGoodsId()==null || tradeGoodsNumberLogDTO.getOrderId()==null
                || tradeGoodsNumberLogDTO.getGoodsNumber().intValue()<0){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        TradeGoodsDTO tradeGoodsDTO=goodsService.findOne(tradeGoodsNumberLogDTO.getGoodsId());
        //库存不足
        if(tradeGoodsDTO.getGoodsNumber()<tradeGoodsNumberLogDTO.getGoodsNumber()){
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        //减库存
        tradeGoodsDTO.setGoodsNumber(tradeGoodsDTO.getGoodsNumber()-tradeGoodsNumberLogDTO.getGoodsNumber());
        goodsService.updateByPrimaryKey(tradeGoodsDTO);
        //记录操作日志
        TradeGoodsNumberLog tradeGoodsNumberLog=new TradeGoodsNumberLog();
        BeanUtils.copyProperties(tradeGoodsNumberLogDTO,tradeGoodsNumberLog);
        tradeGoodsNumberLogMapper.insert(tradeGoodsNumberLog);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
