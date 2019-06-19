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
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static Calendar calendar = Calendar.getInstance(Locale.CHINA);
    public static Logger logger = LogManager.getLogger(DateUtilServiceImp.class);


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
        long time2 = cal.getTimeInMillis();
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
        SimpleDateFormat printFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return printFormat.format(date);
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
}
