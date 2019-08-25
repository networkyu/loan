package com.loan.service.repayment;

import com.loan.dataobject.RepaymentCertificate;
import org.springframework.stereotype.Service;

/**
 * @author yulianpeng
 * @email network_u@163.com
 * Date 2019/7/13
 */
@Service
public interface RCertificateService {
    // 根据还款id获取还款凭证
    public RepaymentCertificate getRCertificatesByRepaymentId(Integer id);
}
