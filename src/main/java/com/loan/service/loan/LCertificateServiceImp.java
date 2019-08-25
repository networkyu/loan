package com.loan.service.loan;

import com.loan.dao.LoanCertificateMapper;
import com.loan.dataobject.LoanCertificate;
import com.loan.dataobject.RepaymentCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yulianpeng
 * @email network_u@163.com
 * Date 2019/7/13
 */
@Service
public class LCertificateServiceImp implements LCertificateService {
    @Autowired
    LoanCertificateMapper loanCertificateMapper;
    @Override
    public LoanCertificate getLCertificatesByLoanId(Integer id) {
        LoanCertificate loanCertificate = loanCertificateMapper.selectByLoanId(id);
        return loanCertificate;
    }
}
