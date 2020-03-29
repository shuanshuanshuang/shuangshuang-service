package com.atnanjing.demo.dao;

import java.math.BigDecimal;
import java.util.Date;

public class TradeOrder {
    //订单ID
    private Long orderId;
    //用户ID
    private Long userId;
    //订单状态 0未确认 1已确认  2已取消  3无效 4退款
    private Integer orderStatus;
    //支付状态 0未支付 1支付中  2已支付
    private Integer payStatus;
    //发货状态 0未发货  1已发货 2已收货
    private Integer shippingStatus;
    //收获地址
    private String address;
    //收货人
    private String consignee;
    //商品ID
    private Long goodsId;
    //商品数量
    private Integer goodsNumber;
    //商品价格
    private BigDecimal goodsPrice;
    //商品总价
    private Long goodsAmount;
    //运费
    private BigDecimal shippingFee;
    //订单价格
    private BigDecimal orderAmount;
    //优惠券ID
    private Long couponId;
    //优惠券
    private BigDecimal couponPaid;
    //已付金额
    private BigDecimal moneyPaid;
    //支付金额
    private BigDecimal payAmount;
    //创建时间
    private Date addTime;
    //订单确认时间
    private Date confirmTime;
    //支付时间
    private Date payTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Long goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getCouponPaid() {
        return couponPaid;
    }

    public void setCouponPaid(BigDecimal couponPaid) {
        this.couponPaid = couponPaid;
    }

    public BigDecimal getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(BigDecimal moneyPaid) {
        this.moneyPaid = moneyPaid;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}