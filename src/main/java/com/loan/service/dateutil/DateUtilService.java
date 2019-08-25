package com.loan.service.dateutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public interface DateUtilService {
//    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//    public static Calendar calendar = Calendar.getInstance(Locale.CHINA);
//    public static Logger logger = LogManager.getLogger(DateUtilServiceImp.class);
    // 将一个日期加上一个天数。
    public Date dateAddNumber(char type, Date date, Integer number);
    //计算两个日期间的间隔天数 type为y，m，d,不对传入的日期处理。
    public Integer intervalDate(Date start, Date end);
    // 两个日期的间隔天数，函数里面会将日期的时分秒，微秒清为零。
    public Integer intervalDay(Date start, Date end);

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
    public Integer getDay(Date date);
    public Integer calPeriodBy(Date start,Integer deadline);

    public Integer intervalDateSpare(Date start, Date end);


}
