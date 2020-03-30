package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.TradeUserDTO;
import com.atguigu.demo.dto.TradeUserMoneyLogDTO;
import com.atguigu.demo.service.ITradeUserService;
import com.atguigu.demo.service.Result;
import com.atnanjing.demo.dao.TradeUser;
import com.atnanjing.demo.dao.TradeUserMoneyLogExample;
import com.atnanjing.demo.exception.CastException;
import com.atnanjing.demo.mapper.TradeUserMapper;
import com.atnanjing.demo.utils.ShopCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class TradeUserServiceImpl implements ITradeUserService {
    @Autowired
    private TradeUserMapper tradeUserMapper;
    @Autowired
    private TradeUserMoneyLogServiceImpl tradeUserMoneyLogService;
    @Override
    public TradeUserDTO findOne(Long userId) {
        TradeUser tradeUser = tradeUserMapper.selectByPrimaryKey(userId);
        TradeUserDTO tradeUserDTO=new TradeUserDTO();
        BeanUtils.copyProperties(tradeUser,tradeUserDTO);
        return tradeUserDTO;
    }

    @Override
    public Result updateMoneyPaid(TradeUserMoneyLogDTO userMoneyLog) {
        //判断请求参数是否合法
        if(userMoneyLog==null || userMoneyLog.getUseMoney()==null || userMoneyLog.getOrderId()==null
                ||userMoneyLog.getUserId()==null ||userMoneyLog.getMoneyLogType()==null){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //查询该订单是否存在付款记录
        long order=tradeUserMoneyLogService.selectOne(userMoneyLog.getOrderId(),userMoneyLog.getUserId());
        //判断余额操作行为
        //如果已经付款，抛出异常
        if(userMoneyLog.getMoneyLogType().equals(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY.getCode())){
            CastException.cast(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY);
        }
        TradeUser tradeUser = tradeUserMapper.selectByPrimaryKey(userMoneyLog.getUserId());
        //正常付款  用户账户扣减余额
        tradeUser.setUserMoney(new BigDecimal(tradeUser.getUserMoney()).subtract(userMoneyLog.getUseMoney()).longValue());
        tradeUserMapper.updateByPrimaryKey(tradeUser);
        //退款操作
        //如果用户未支付，不能退款，抛出异常
        if(userMoneyLog.getMoneyLogType().intValue()==ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()){
                //如果没有支付,则不能回退余额
            CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY);
        }
        //防止多次退款
        int r2 =tradeUserMoneyLogService.selectPaid(userMoneyLog);

        if(r2>0){
            CastException.cast(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY);
        }
        //用户账户添加余额
        tradeUser.setUserMoney(new BigDecimal(tradeUser.getUserMoney()).add(userMoneyLog.getUseMoney()).longValue());
        tradeUserMapper.updateByPrimaryKey(tradeUser);
        //记录用户余额日志
        userMoneyLog.setCreateTime(new Date());
        tradeUserMoneyLogService.insert(userMoneyLog);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());

    }


}
