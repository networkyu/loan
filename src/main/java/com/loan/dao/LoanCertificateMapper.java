package com.loan.dao;

import com.loan.dataobject.LoanCertificate;

public interface LoanCertificateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanCertificate record);

    int insertSelective(LoanCertificate record);

    LoanCertificate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanCertificate record);

    int updateByPrimaryKeyWithBLOBs(LoanCertificate record);

    int updateByPrimaryKey(LoanCertificate record);
}