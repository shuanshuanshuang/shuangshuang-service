package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.TradeGoodsDTO;
import com.atguigu.demo.service.IGoodsService;
import com.atnanjing.demo.dao.TradeGoods;
import com.atnanjing.demo.mapper.TradeGoodsMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private TradeGoodsMapper tradeGoodsMapper;
    @Override
    public TradeGoodsDTO findOne(Long goodsId) {
        TradeGoods tradeGoods = tradeGoodsMapper.selectByPrimaryKey(goodsId);
        TradeGoodsDTO tradeGoodsDTO = new TradeGoodsDTO();
        BeanUtils.copyProperties(tradeGoods,tradeGoodsDTO);
        return tradeGoodsDTO;
    }

    @Override
    public void updateByPrimaryKey(TradeGoodsDTO tradeGoodsDTO) {
        TradeGoods tradeGoods = new TradeGoods();
        BeanUtils.copyProperties(tradeGoodsDTO,tradeGoods);
        tradeGoodsMapper.updateByPrimaryKey(tradeGoods);
    }
}
