package com.loan.dao;

import com.loan.dataobject.Loan;

public interface LoanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Loan record);

    int insertSelective(Loan record);

    Loan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Loan record);

    int updateByPrimaryKey(Loan record);
}