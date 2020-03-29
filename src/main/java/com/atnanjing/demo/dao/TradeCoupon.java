package com.atnanjing.demo.dao;

import java.math.BigDecimal;
import java.util.Date;

public class TradeCoupon {
    //优惠券ID
    private Long couponId;
    //优惠券金额
    private BigDecimal couponPrice;
    //用户ID
    private Long userId;
    //订单ID
    private Long orderId;
    //是否使用 0未使用  1已使用
    private Integer isUsed;
    //使用时间
    private Date usedTime;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

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

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }
}