package com.loan.service.repayment;

import com.loan.dao.RepaymentCertificateMapper;
import com.loan.dataobject.RepaymentCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yulianpeng
 * @email network_u@163.com
 * Date 2019/7/13
 */
@Service
public class RCertificateServiceImp implements RCertificateService {
    @Autowired
    RepaymentCertificateMapper repaymentCertificateMapper;
    @Override
    public RepaymentCertificate getRCertificatesByRepaymentId(Integer id) {
        return repaymentCertificateMapper.selectByRepaymentId(id);
    }
}
