package com.loan.service.repayment;

import com.loan.dataobject.Repayment;
import org.springframework.stereotype.Service;

@Service
public interface RepaymentService {
    // 获得某笔借款的所有还款
    public Repayment[] getRepaymentsByLoanId(Integer id);
}
