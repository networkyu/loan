package com.loan.dao;

import com.loan.dataobject.Credits;

public interface CreditsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Credits record);

    int insertSelective(Credits record);

    Credits selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Credits record);

    int updateByPrimaryKey(Credits record);
}