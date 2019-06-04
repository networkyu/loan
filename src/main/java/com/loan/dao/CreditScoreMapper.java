package com.loan.dao;

import com.loan.dataobject.CreditScore;

public interface CreditScoreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditScore record);

    int insertSelective(CreditScore record);

    CreditScore selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditScore record);

    int updateByPrimaryKey(CreditScore record);
}