package com.loan.dao;

import com.loan.dataobject.RepaymentIntegral;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RepaymentIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepaymentIntegral record);

    int insertSelective(RepaymentIntegral record);

    RepaymentIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RepaymentIntegral record);

    int updateByPrimaryKey(RepaymentIntegral record);

    // 通过客户统计积分总和。
    Map<String,BigDecimal> selectValueMapByClient(Integer client);

    Integer selectOutValueByClient(Integer client);

    Integer selectEnterValueByClient(Integer client);
}