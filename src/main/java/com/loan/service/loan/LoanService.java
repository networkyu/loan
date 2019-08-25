package com.loan.service.loan;

import com.loan.dataobject.Loan;
import com.loan.dataobject.Repayment;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface LoanService {
    // 通过客户id获取所有的借款
    public List<Loan> getLoansByClientId(Integer clientId);
    public boolean createLoan(LoanModel loanModel);
//    // 通过客户id获取收益。
//    public BigDecimal getEarningsByClientId(Integer clientId);
//    // 筒欧客户id获取收益率。
//    public BigDecimal getRateOfReturnsByClientId(Integer clientId);
//    // 通过客户id获取余额。
//    public BigDecimal getBalanceByClientId(Integer clientId);
    // 获取贷款
    public LoanInfoModel[] getLoansByStartRowAndLimit(Integer start,Integer limit);
    // 获取总条数
    public Integer loanSum();
//     获取客户当前的生效借款
    public List<Loan> getEffectiveLoansByClientId(Integer clientId);
    // 通过一笔借款获取正常情况下应还款项目。
//    public List<Repayment> normalRepayments(Loan loan);
    public Loan getLoan(Integer loanId);
    /**
     * 测试方法
     */
    // 通过客户ID获取应还款。
    public Map<String,Object> getLoanRepayment(Integer loanId);

    /**
     * 通过借款人姓名获取有效贷款
     */
    public LoanModel[] getLoanModelsByBorrowerName(String borrowerName);

    /**
     * 将loan转换为loanInfoModel
     * @param loan
     * @return
     */
    public LoanInfoModel convertFromLoan(Loan loan);

    /**
     * 将loanInfoModel转换为Loan
     * @param loanInfoModel
     * @return
     */
    Loan convertFromLoanInfoModel(LoanInfoModel loanInfoModel);




}
