package com.loan.dao;

import com.loan.dataobject.RepaymentIntegral;

public interface RepaymentIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepaymentIntegral record);

    int insertSelective(RepaymentIntegral record);

    RepaymentIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RepaymentIntegral record);

    int updateByPrimaryKey(RepaymentIntegral record);
}