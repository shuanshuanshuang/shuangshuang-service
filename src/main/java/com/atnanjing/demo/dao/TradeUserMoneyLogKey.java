package com.atnanjing.demo.dao;

public class TradeUserMoneyLogKey {
    //用户id
    private Long userId;
    //订单id
    private Long orderId;
    //日志类型   1订单付款   2订单退款
    private Integer moneyLogType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getMoneyLogType() {
        return moneyLogType;
    }

    public void setMoneyLogType(Integer moneyLogType) {
        this.moneyLogType = moneyLogType;
    }
}