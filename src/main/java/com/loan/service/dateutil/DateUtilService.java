package com.loan.service.dateutil;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public interface DateUtilService {
    // 将一个日期加上一个天数。
    public Date dateAddNumber(char type, Date date, Integer number);
    //计算两个日期间的间隔天数 type为y，m，d。
    public Integer intervalDate(Date start, Date end);

    public Date dateFromString(String dateStr);
    public String fromDate(Date date);

    /**
     * 设置日期的年月日
     * @param type
     * @param date
     * @param number
     * @return
     */
    public Date setDateWithNumber(char type,Date date,Integer number);

}
