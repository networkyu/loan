package com.loan.service.loan;

import com.loan.dataobject.LoanCertificate;
import com.loan.dataobject.RepaymentCertificate;
import org.springframework.stereotype.Service;

/**
 * @author yulianpeng
 * @email network_u@163.com
 * Date 2019/7/13
 */
@Service
public interface LCertificateService {
    // 根据借款id获取借款凭证
    public LoanCertificate getLCertificatesByLoanId(Integer id);
}
