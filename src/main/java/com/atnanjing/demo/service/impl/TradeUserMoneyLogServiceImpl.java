package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.TradeUserMoneyLogDTO;
import com.atguigu.demo.service.ITradeUserMoneyLogService;
import com.atguigu.demo.service.Result;
import com.atnanjing.demo.dao.TradeUserMoneyLog;
import com.atnanjing.demo.dao.TradeUserMoneyLogExample;
import com.atnanjing.demo.mapper.TradeUserMoneyLogMapper;
import com.atnanjing.demo.utils.ShopCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class TradeUserMoneyLogServiceImpl implements ITradeUserMoneyLogService {

    @Autowired
    private TradeUserMoneyLogMapper tradeUserMoneyLogMapper;

    @Override
    public Result updateMoneyPaid(TradeUserMoneyLogDTO userMoneyLog) {
        TradeUserMoneyLog tradeUserMoneyLog = new TradeUserMoneyLog();
        BeanUtils.copyProperties(userMoneyLog, tradeUserMoneyLog);
        tradeUserMoneyLogMapper.updateByPrimaryKey(tradeUserMoneyLog);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
    }

    @Override
    public long selectOne(Long orderId, Long userId) {
        TradeUserMoneyLogExample tradeUserMoneyLogExample = new TradeUserMoneyLogExample();
        TradeUserMoneyLogExample.Criteria criteria = tradeUserMoneyLogExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andOrderIdEqualTo(orderId);
        return tradeUserMoneyLogMapper.countByExample(tradeUserMoneyLogExample);
    }

    @Override
    public int selectPaid(TradeUserMoneyLogDTO userMoneyLog) {
        TradeUserMoneyLogExample userMoneyLogExample2 = new TradeUserMoneyLogExample();
        TradeUserMoneyLogExample.Criteria criteria1 = userMoneyLogExample2.createCriteria();
        criteria1.andOrderIdEqualTo(userMoneyLog.getOrderId());
        criteria1.andUserIdEqualTo(userMoneyLog.getUserId());
        criteria1.andMoneyLogTypeEqualTo(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
        int r2 = (int) tradeUserMoneyLogMapper.countByExample(userMoneyLogExample2);
        return r2;

    }

    @Override
    public void insert(TradeUserMoneyLogDTO userMoneyLogDTO) {
        TradeUserMoneyLog tradeUserMoneyLog=new TradeUserMoneyLog();
        BeanUtils.copyProperties(userMoneyLogDTO,tradeUserMoneyLog);
        tradeUserMoneyLogMapper.insert(tradeUserMoneyLog);
    }
}