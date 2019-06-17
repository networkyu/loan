package com.loan.service.loan;

import com.loan.dataobject.Loan;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface LoanService {
    // 通过客户id获取所有的借款
    public List<Loan> getLoansByClientId(Integer clientId);
    public boolean createLoan(LoanModel loanModel);
    // 通过客户id获取收益。
    public BigDecimal getEarningsByClientId(Integer clientId);
    // 筒欧客户id获取收益率。
    public BigDecimal getRateOfReturnsByClientId(Integer clientId);
    // 通过客户id获取余额。
    public BigDecimal getBalanceByClientId(Integer clientId);
    // 获取贷款
    public LoanInfoModel[] getLoansByStartRowAndLimit(Integer start,Integer limit);
    // 获取总条数
    public Integer loanSum();
//     获取客户当前的生效借款
    public List<Loan> getEffectiveLoansByClientId(Integer clientId);

}
