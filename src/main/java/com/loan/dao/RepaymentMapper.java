package com.loan.dao;

import com.loan.dataobject.Repayment;

public interface RepaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Repayment record);

    int insertSelective(Repayment record);

    Repayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Repayment record);

    int updateByPrimaryKey(Repayment record);
    /// 通过借款id获取该借款的所有还款
    Repayment[] selectRepaymentsByLoanId(Integer loanId);
}