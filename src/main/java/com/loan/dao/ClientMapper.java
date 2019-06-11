package com.loan.dao;

import com.loan.dataobject.Client;

import java.util.List;

public interface ClientMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);

    List<Integer>selectId();
    // 通过id查找姓名。
    String selectName(Integer id);

    List<Client> selectByMaxIdAndLimit(Integer maxId,Integer limit);
    // 通过select count(id)查询总条数。
    Integer selectCountId();
}