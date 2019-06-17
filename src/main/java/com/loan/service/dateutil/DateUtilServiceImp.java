package com.loan.service.dateutil;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
@Service
public class DateUtilServiceImp implements DateUtilService {
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
}
