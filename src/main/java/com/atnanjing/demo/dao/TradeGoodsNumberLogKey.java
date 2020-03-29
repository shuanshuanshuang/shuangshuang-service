package com.atnanjing.demo.dao;

public class TradeGoodsNumberLogKey {
    //商品id
    private Long goodsId;
    //订单id
    private Long orderId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}