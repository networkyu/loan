package com.loan.service.repayment;

import com.loan.dao.RepaymentMapper;
import com.loan.dataobject.Repayment;
import com.loan.service.loan.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepaymentServiceImp implements RepaymentService{
    @Autowired
    RepaymentMapper repaymentMapper;
    @Autowired
    LoanService loanService;
    @Override
    public Repayment[] getRepaymentsByLoanId(Integer id) {
        Repayment[] repayments = repaymentMapper.selectRepaymentsByLoanId(id);
        return repayments;
    }

    /**
     * 判断还款是逾期，提前还款，正常还款，转借。
     * @param loanId
     */
    public void repaymentDetail(Integer loanId){
        // 首先通过借款id查出所有还款记录。
        Repayment[] repayments = this.getRepaymentsByLoanId(loanId);
        List<Repayment> waitingRepayment = loanService.normalRepayments(loanService.getLoan(loanId));
        for (Repayment repayment:repayments){

        }
    }
}
