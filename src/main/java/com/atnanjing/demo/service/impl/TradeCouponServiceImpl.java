package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.TradeCouponDTO;
import com.atguigu.demo.service.ITradeCouponService;
import com.atguigu.demo.service.Result;
import com.atnanjing.demo.dao.TradeCoupon;
import com.atnanjing.demo.exception.CastException;
import com.atnanjing.demo.mapper.TradeCouponMapper;
import com.atnanjing.demo.utils.ShopCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@org.apache.dubbo.config.annotation.Service(version = "${demo.service.version}")
public class TradeCouponServiceImpl implements ITradeCouponService {
    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    @Override
    public TradeCouponDTO findOne(Long couponId) {
        TradeCoupon tradeCoupon = tradeCouponMapper.selectByPrimaryKey(couponId);
        TradeCouponDTO tradeCouponDTO=new TradeCouponDTO();
        BeanUtils.copyProperties(tradeCoupon,tradeCouponDTO);
        return tradeCouponDTO;
    }

    @Override
    public Result changeCouponStatus(TradeCouponDTO coupon) {
        if(coupon==null || coupon.getCouponId()==null){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        try {
            TradeCoupon tradeCoupon=new TradeCoupon();
            BeanUtils.copyProperties(coupon,tradeCoupon);
            tradeCouponMapper.updateByPrimaryKeySelective(tradeCoupon);
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
        }catch (Exception e){
            return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_FAIL.getMessage());
        }

    }
}
