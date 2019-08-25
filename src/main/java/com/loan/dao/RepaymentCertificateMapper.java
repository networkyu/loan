package com.loan.dao;

import com.loan.dataobject.RepaymentCertificate;

public interface RepaymentCertificateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepaymentCertificate record);

    int insertSelective(RepaymentCertificate record);

    RepaymentCertificate selectByPrimaryKey(Integer id);
    //通过还款id获取还款凭证
    RepaymentCertificate selectByRepaymentId(Integer RId);
    int updateByPrimaryKeySelective(RepaymentCertificate record);

    int updateByPrimaryKey(RepaymentCertificate record);
}