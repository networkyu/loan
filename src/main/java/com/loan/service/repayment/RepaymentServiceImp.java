package com.loan.service.repayment;

import com.loan.dao.RepaymentMapper;
import com.loan.dataobject.Repayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepaymentServiceImp implements RepaymentService{
    @Autowired
    RepaymentMapper repaymentMapper;
    @Override
    public Repayment[] getRepaymentsByLoanId(Integer id) {
        Repayment[] repayments = repaymentMapper.selectRepaymentsByLoanId(id);
        return new Repayment[0];
    }
}
