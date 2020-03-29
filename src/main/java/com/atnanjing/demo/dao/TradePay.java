package com.atnanjing.demo.dao;

import java.math.BigDecimal;

public class TradePay {
    //支付编号
    private Long payId;
    //订单编号
    private Long orderId;
    //支付金额
    private BigDecimal payAmount;
    ///是否已支付  1否   2是
    private Integer isPaid;

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }
}