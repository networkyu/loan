package com.loan.dao;

import com.loan.dataobject.Repayment;

public interface RepaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Repayment record);

    int insertSelective(Repayment record);

    Repayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Repayment record);

    int updateByPrimaryKey(Repayment record);
}