package com.loan.dao;

import com.loan.dataobject.AdminPassword;

public interface AdminPasswordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminPassword record);

    int insertSelective(AdminPassword record);

    AdminPassword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminPassword record);

    int updateByPrimaryKey(AdminPassword record);
    //  通过adminId查询AdminPassword
    AdminPassword selectByAdminId(Integer adminId);

    int updateCiphertextByAdminId(String ciphertext,Integer adminId);


}