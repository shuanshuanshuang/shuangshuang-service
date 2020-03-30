package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.*;
import com.atguigu.demo.service.IOrderService;
import com.atguigu.demo.service.Result;
import com.atnanjing.demo.dao.TradeOrder;
import com.atnanjing.demo.exception.CastException;
import com.atnanjing.demo.mapper.TradeOrderMapper;
import com.atnanjing.demo.utils.IDWorker;
import com.atnanjing.demo.utils.ShopCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class OrderServiceImpl implements IOrderService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TradeUserServiceImpl tradeUserService;

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private IDWorker idWorker;

    @Autowired
    private TradeCouponServiceImpl tradeCouponService;

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private TradeGoodsNumberLogServiceImpl tradeGoodsNumberLogService;

    @Autowired
    private TradeUserMoneyLogServiceImpl tradeUserMoneyLogService;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public Result confirmOrder(TradeOrderDTO order) {
        //1.校验订单
        checkOrder(order);
        //2.生成预订单
        Long oderId=savePreOrder(order);
        try {
            //3.扣减库存
            reduceGoodsNum(order);
            //4扣减优惠券
            changeCoponStatus(order);
            //5.使用余额
            reduceMonneyPaid(order);
            //6.生成订单
            updateOrderStatus(order);
            //7.返回结果
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
        }catch (Exception e){
              //8.确认消息失败，发送消息
             MQEntity mqEntity=new MQEntity();
             mqEntity.setCouponId(order.getCouponId());
             mqEntity.setOrderId(order.getOrderId());
             mqEntity.setGoodsId(order.getGoodsId());
             mqEntity.setGoodsNum(order.getGoodsNumber());
             mqEntity.setUserId(order.getUserId());
             mqEntity.setUserMoney(order.getMoneyPaid());
             try {
                 sendMessage(mqEntity);
             }catch (Exception ex){
                 ex.getStackTrace();
                 CastException.cast(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL);
             }
            //9.返回失败状态
            return new Result(ShopCode.SHOP_ORDER_CONFIRM_FAIL.getSuccess(),ShopCode.SHOP_ORDER_CONFIRM_FAIL.getMessage());
        }

    }

    /**
     * 订单失败后，生成者发送消息进行补偿
     * @param mqEntity
     */
    private void sendMessage(MQEntity mqEntity) throws Exception{
        Message message=new Message("DemoTopic","DemoTag",mqEntity.getOrderId().toString(),mqEntity.toString().getBytes());
        SendResult sendResult=defaultMQProducer.send(message);
    }


    /**
     * 生成订单
     * @param order
     */
    private void updateOrderStatus(TradeOrderDTO order) {
        order.setOrderStatus(ShopCode.SHOP_ORDER_CONFIRM.getCode());
        order.setPayStatus(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        order.setConfirmTime(new Date());
        TradeOrder tradeOrder=new TradeOrder();
        BeanUtils.copyProperties(order,tradeOrder);
        int insert=tradeOrderMapper.updateByPrimaryKey(tradeOrder);
        if(insert<=0){
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        logger.info("订单:["+order.getOrderId()+"]状态修改成功");
    }

    /**
     * 使用余额
     * @param order
     */
    private void reduceMonneyPaid(TradeOrderDTO order) {
        //判断订单中使用的余额是否合法
        if (order.getMoneyPaid() != null && order.getMoneyPaid().compareTo(BigDecimal.ZERO) == 1) {
            TradeUserMoneyLogDTO userMoneyLog = new TradeUserMoneyLogDTO();
            userMoneyLog.setOrderId(order.getOrderId());
            userMoneyLog.setUserId(order.getUserId());
            userMoneyLog.setUseMoney(order.getMoneyPaid());
            userMoneyLog.setMoneyLogType(ShopCode.SHOP_USER_MONEY_PAID.getCode());
            Result result = tradeUserService.updateMoneyPaid(userMoneyLog);
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_FAIL);
            }
            logger.info("订单:" + order.getOrderId() + ",扣减余额成功");
        }
    }

    /**
     * 扣减优惠券
     * @param order
     */
    private void changeCoponStatus(TradeOrderDTO order) {
        //判断用户是否有使用优惠券
        if(StringUtils.isNoneEmpty(order.getCouponId().toString())){
            //封装优惠券对象
            TradeCouponDTO tradeCoupon=new TradeCouponDTO();
            tradeCoupon.setCouponId(order.getCouponId());
            tradeCoupon.setIsUsed(ShopCode.SHOP_COUPON_ISUSED.getCode());
            tradeCoupon.setUsedTime(new Date());
            tradeCoupon.setOrderId(order.getOrderId());
            //使用优惠券
            Result result=tradeCouponService.changeCouponStatus(tradeCoupon);
            //使用优惠券失败，抛出异常
            if(result.getSuccess().equals(ShopCode.SHOP_FAIL)){
                CastException.cast(ShopCode.SHOP_COUPON_USE_FAIL);
            }
            logger.info("订单:["+order.getOrderId()+"]使用扣减优惠券["+tradeCoupon.getCouponPrice()+"元]成功");
        }
    }

    /**
     * 扣减库存
     * @param order
     */
    private void reduceGoodsNum(TradeOrderDTO order) {
        TradeGoodsNumberLogDTO tradeGoodsNumberLog=new TradeGoodsNumberLogDTO();
        tradeGoodsNumberLog.setGoodsNumber(order.getGoodsNumber());
        tradeGoodsNumberLog.setLogTime(new Date());
        tradeGoodsNumberLog.setGoodsId(order.getGoodsId());
        tradeGoodsNumberLog.setOrderId(order.getOrderId());
        Result result = tradeGoodsNumberLogService.insertTradeGoodsNumberLog(tradeGoodsNumberLog);
        if(result.getSuccess().equals(ShopCode.SHOP_FAIL)){
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_FAIL);
        }
        logger.info("订单:["+order.getOrderId()+"]扣减库存["+order.getGoodsNumber()+"个]成功");
    }

    /**
     * 生成预订单
     * @param order
     * @return
     */
    private Long savePreOrder(TradeOrderDTO order) {
        //1.设置订单状态为不可见
        order.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        //2.生成订单ID
        order.setOrderId(idWorker.nextId());
        //3.核算运费是否正确
        BigDecimal shippingFee=calculateShippingFee(order.getOrderAmount());
        if(order.getShippingFee().compareTo(shippingFee)!=0){
            CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID);
        }
        //4.核算订单总价是否正确
        BigDecimal orderAmount=order.getGoodsPrice().multiply(new BigDecimal(order.getGoodsNumber()));
        orderAmount.add(shippingFee);
        if(orderAmount.compareTo(order.getOrderAmount())!=0){
           CastException.cast(ShopCode.SHOP_ORDERAMOUNT_INVALID);
        }
        //5.核算优惠券是否合法
        Long couponId=order.getCouponId();
        if(couponId!=null){
            TradeCouponDTO tradeCouponDTO = tradeCouponService.findOne(couponId);
            //优惠券不存在
            if(tradeCouponDTO==null){
                CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
            }
            //优惠券已经使用
            if(ShopCode.SHOP_COUPON_ISUSED.getCode().toString().equals(tradeCouponDTO.getIsUsed().toString())){
                CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
            }
            order.setCouponPaid(tradeCouponDTO.getCouponPrice());
        }else{
            order.setCouponPaid(BigDecimal.ZERO);
        }
        //6.判断余额是否正确
        BigDecimal moneyPaid=order.getMoneyPaid();
        if(moneyPaid!=null){
            //比较余额是否大于0
            int compare = moneyPaid.compareTo(BigDecimal.ZERO);
            //余额小于0
            if(compare==-1){
               CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            //余额大于0
            if(compare==1){
                //查询用户信息
                TradeUserDTO tradeUserDTO = tradeUserService.findOne(order.getUserId());
                if(tradeUserDTO==null){
                    CastException.cast(ShopCode.SHOP_USER_IS_NULL);
                }
                //比较余额是否大于用户账户余额
                if(tradeUserDTO.getUserMoney().compareTo(order.getPayAmount().longValue())==-1){
                    CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALID);
                }
            }
            order.setMoneyPaid(order.getMoneyPaid());
        }else{
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        //7.计算订单总价
        order.setPayAmount(orderAmount.subtract(order.getCouponPaid()));
        //8.设置订单创建时间
        order.setAddTime(new Date());
        TradeOrder tradeOrder=new TradeOrder();
        BeanUtils.copyProperties(order,tradeOrder);
        //9.保存订单
        int insert = tradeOrderMapper.insert(tradeOrder);
        if(ShopCode.SHOP_SUCCESS.getCode()!=insert){
            CastException.cast(ShopCode.SHOP_ORDER_SAVE_ERROR);
        }
        logger.info("订单:["+order.getOrderId()+"]预订单生成成功");
        return tradeOrder.getOrderId();
    }

    private BigDecimal calculateShippingFee(BigDecimal orderAmount) {
        if(orderAmount.compareTo(new BigDecimal(100))==1){
            return BigDecimal.ZERO;
        }else{
            return new BigDecimal(10);
        }

    }

    /**
     * 校验订单
     * @param order
     */
    private void checkOrder(TradeOrderDTO order) {
        //1.校验订单是否存在
        if(order==null){
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //2.校验订单中的商品是否存在
        TradeGoodsDTO tradeGoodsDTO = goodsService.findOne(order.getGoodsId());
        if(tradeGoodsDTO==null){
           CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        //3.检验下单用户是否存在
        TradeUserDTO tradeUserDTO = tradeUserService.findOne(order.getUserId());
        if(tradeGoodsDTO==null){
            CastException.cast(ShopCode.SHOP_USER_IS_NULL);
        }
        //4.检验商品单价是否合法
        if(order.getGoodsPrice().compareTo(tradeGoodsDTO.getGoodsPrice())!=0){
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        //5.检验订单商品数量是否合法
        if(order.getGoodsNumber().compareTo(tradeGoodsDTO.getGoodsNumber())!=0){
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        logger.info("订单校验通过");


    }
}
