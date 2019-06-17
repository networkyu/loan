package com.loan.service.dateutil;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface DateUtilService {
    // 将一个日期加上一个天数。
    public Date dateAddNumber(char type, Date date, Integer number);
    //计算两个日期间的间隔天数 type为y，m，d。
    public Integer intervalDate(Date start, Date end);
}
