package com.loan.service.repayment;

import com.loan.dataobject.Loan;
import com.loan.dataobject.Repayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface RepaymentService {
    // 获得某笔借款的所有已还款
    public Repayment[] getRepaymentsByLoanId(Integer id);
    // 通过一个日期和借款id查询这笔还款的状态。
    //计算待还款
    List<Repayment> calNormalRepayments(Loan loan);
    // 创建一笔还款

    /**
     *
     * @param loanId 贷款id
     * @param borrowerId 借款人id
     * @param lenderId 出借人id
     * @param time 还款时间
     * @param money 还款金额
     * @return 操作是否成功。
     */
    public boolean createRepayment(Integer loanId,Integer borrowerId, Integer lenderId, String time, BigDecimal money);
}
