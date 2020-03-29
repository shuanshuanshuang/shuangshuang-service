package com.atnanjing.demo.mapper;

import com.atnanjing.demo.dao.TradeUserMoneyLog;
import com.atnanjing.demo.dao.TradeUserMoneyLogExample;
import com.atnanjing.demo.dao.TradeUserMoneyLogKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradeUserMoneyLogMapper {
    long countByExample(TradeUserMoneyLogExample example);

    int deleteByExample(TradeUserMoneyLogExample example);

    int deleteByPrimaryKey(TradeUserMoneyLogKey key);

    int insert(TradeUserMoneyLog record);

    int insertSelective(TradeUserMoneyLog record);

    List<TradeUserMoneyLog> selectByExample(TradeUserMoneyLogExample example);

    TradeUserMoneyLog selectByPrimaryKey(TradeUserMoneyLogKey key);

    int updateByExampleSelective(@Param("record") TradeUserMoneyLog record, @Param("example") TradeUserMoneyLogExample example);

    int updateByExample(@Param("record") TradeUserMoneyLog record, @Param("example") TradeUserMoneyLogExample example);

    int updateByPrimaryKeySelective(TradeUserMoneyLog record);

    int updateByPrimaryKey(TradeUserMoneyLog record);
}