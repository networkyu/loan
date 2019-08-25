package com.loan.service.dateutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class DateUtilServiceImp implements DateUtilService {
    // 业务逻辑一期最小的天数。--非常重要
    public static Integer MINPERIOD = 28;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static Calendar calendar = Calendar.getInstance();
    public static Logger logger = LogManager.getLogger(DateUtilServiceImp.class);

    // 构造方法。
//
//    public DateUtilServiceImp() {
//        calendar.set(Calendar.HOUR_OF_DAY,0);
//        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.MILLISECOND,0);
//    }

    @Override
    public Date dateAddNumber(char type, Date date, Integer number) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (type){
            case 'y':
                cal.add(Calendar.YEAR,number);
                break;
            case 'm':
                cal.add(Calendar.MONTH,number);
                break;
            case 'd':
                cal.add(Calendar.DATE,number);
                break;
            default:
                cal.add(Calendar.DATE,number);
                break;
        }
        return cal.getTime();
    }
    @Override
    public Integer intervalDate(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = calendar.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
    @Override
    public Integer intervalDay(Date start, Date end) {
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        long time1 = calendar.getTimeInMillis();
        calendar.setTime(end);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        long time2 = calendar.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
    public Date dateFromString(String dateStr){
        Date date = new Date();
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e){
            logger.error(e);
        }
        return date;
    }
    public String fromDate(Date date){
        return simpleDateFormat.format(date);
    }

    /**
     * 设置日期的年月日
     * @param type
     * @param date
     * @param number
     * @return
     */
    public Date setDateWithNumber(char type,Date date,Integer number){
        calendar.setTime(date);
        switch (type){
            case 'y':
                calendar.set(Calendar.YEAR,number);
                break;
            case 'm':
                calendar.set(Calendar.MONTH,number);
                break;
            case 'd':
                calendar.set(Calendar.DAY_OF_MONTH,number);
                break;
            default:
                calendar.set(Calendar.DAY_OF_MONTH,number);

        }
        return calendar.getTime();
    }

    /**
     * 获得日期的日
     * @param date
     * @return
     */
    public Integer getDay(Date date){
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 通过开始日期和期限计算按月分期数
     * @param start
     * @param deadline
     * @return
     * 注意：28是本系统的业务逻辑，指的是当天数大于28天才可以计为一期。借款日是1月
     * 20号，还款日是30号，那么第一期是在2月30号，如果在1月1号那么1月1号与1月30号有30天（包含1号，30号）
     * 所以就是28
     */
    @Override
    public Integer calPeriodBy(Date start, Integer deadline) {
        //最后还款日期
        Date endDate = dateAddNumber('d',start,deadline);
        Integer period = 0;
        // 大于等于28天计算一期
        while (intervalDay(start,endDate) >= MINPERIOD){
            // 自增1
            period++;
            endDate = dateAddNumber('m',endDate,-1);
        }
        return period;
    }

    /**
     * 计算两个日期之间的天数
     * @param start 开始日期
     * @param end 结束日期
     * @return 两个日期之间的间隔天数，如果开始日期在后，转换后的日期为负数。
     *
     * 原来的计算错误。
     */
    public Integer intervalDateSpare(Date start, Date end) {
        calendar.setTime(start);
        long time1 = calendar.getTimeInMillis();
        calendar.setTime(end);
        long time2 = calendar.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}
