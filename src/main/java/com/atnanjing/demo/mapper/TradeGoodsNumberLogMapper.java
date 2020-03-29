package com.atnanjing.demo.mapper;

import com.atnanjing.demo.dao.TradeGoodsNumberLog;
import com.atnanjing.demo.dao.TradeGoodsNumberLogExample;
import com.atnanjing.demo.dao.TradeGoodsNumberLogKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradeGoodsNumberLogMapper {
    long countByExample(TradeGoodsNumberLogExample example);

    int deleteByExample(TradeGoodsNumberLogExample example);

    int deleteByPrimaryKey(TradeGoodsNumberLogKey key);

    int insert(TradeGoodsNumberLog record);

    int insertSelective(TradeGoodsNumberLog record);

    List<TradeGoodsNumberLog> selectByExample(TradeGoodsNumberLogExample example);

    TradeGoodsNumberLog selectByPrimaryKey(TradeGoodsNumberLogKey key);

    int updateByExampleSelective(@Param("record") TradeGoodsNumberLog record, @Param("example") TradeGoodsNumberLogExample example);

    int updateByExample(@Param("record") TradeGoodsNumberLog record, @Param("example") TradeGoodsNumberLogExample example);

    int updateByPrimaryKeySelective(TradeGoodsNumberLog record);

    int updateByPrimaryKey(TradeGoodsNumberLog record);
}