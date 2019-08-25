package com.loan.dao;

import com.loan.dataobject.Loan;

import java.util.List;

public interface LoanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Loan record);

    int insertSelective(Loan record);

    Loan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Loan record);

    int updateByPrimaryKey(Loan record);
    // 查询出客户的所有借入借出借款记录。
    List<Loan> selectLoansByClientId(Integer clientId);

    // 查询出客户的有效借入借出借款记录。
    List<Loan> selectEffectiveLoansByClientId(Integer clientId);
    // 查询出所有借款记录。
    List<Loan> selectLoansByStartRowAndLimit(Integer start,Integer limit);
    // 查询出借款总条数
    // 通过select count(id)查询总条数。
    Integer selectCountId();
    //通过借款人姓名返回有效的借款
    List<Loan> selectLoansByBorrower(String borrowerName);

}