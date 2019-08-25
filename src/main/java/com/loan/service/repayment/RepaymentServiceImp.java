package com.loan.service.repayment;

import com.loan.dao.RepaymentMapper;
import com.loan.dataobject.Loan;
import com.loan.dataobject.Repayment;
import com.loan.service.dateutil.DateUtilService;
import com.loan.service.loan.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class RepaymentServiceImp implements RepaymentService{
    @Autowired
    RepaymentMapper repaymentMapper;
    @Autowired
    LoanService loanService;
    @Autowired
    DateUtilService dateUtilService;

    @Override
    public Repayment[] getRepaymentsByLoanId(Integer id) {
        Repayment[] totalRepayments = repaymentMapper.selectRepaymentsByLoanId(id);
        // 有几个应还就对应几个已还数组。
        List<Repayment> shouldRepayments = calNormalRepayments(loanService.getLoan(id));
        ArrayList<ArrayList<Repayment>> repaymentsArray = new ArrayList< ArrayList<Repayment> >();
        for (Repayment shouldRepayment:shouldRepayments){
            ArrayList<Repayment> repayments = new ArrayList<>();
            //将每期的还款加到数组当中。

            repaymentsArray.add(repayments);
        }

        // 还款按时间重新排序
        return null ;
    }

    /**
     * 判断还款是逾期，提前还款，正常还款，转借。
     * @param loanId
     */
    public void repaymentDetail(Integer loanId){
        // 首先通过借款id查出所有还款记录。
//        Repayment[] repayments = this.getRepaymentsByLoanId(loanId);
//        List<Repayment> waitingRepayment = loanService.normalRepayments(loanService.getLoan(loanId));
//        for (Repayment repayment:repayments){
//
//        }
    }
    /***
     * 计算待还详情
     */
    @Override
    public List<Repayment> calNormalRepayments(Loan loan){
        if (loan == null){
            return null;
        }
        List<Repayment> repayments = new ArrayList<>();
        switch (loan.getMethod()){
            case 1:
                repayments = normalPrincipalAndInterest(loan);
                break;
            case 2:
                repayments = normalMonthlyRepayments(loan);
                break;
            case 3:
                repayments = normalStaging(loan);
                break;
            case 8:
                repayments = normalCurrent(loan);
                break;
                default:
                    repayments = null;
        }
        return repayments;
    }

    @Override
    public boolean createRepayment(Integer loanId, Integer borrowerId, Integer lenderId, String time, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) < 0){
            return false;
        }
        // 首先查看贷款是否有效
        Loan loan = loanService.getLoan(loanId);
        if (loan == null) {
            return  false;
        }
        // 检验借款人与还款人是否一致
        if (loan.getBorrower() != borrowerId && loan.getLender() != lenderId){
            return false;
        }
        // 未完待续。。。。。
        Repayment repayment = new Repayment();
        repayment.setLoanid(loanId);
        repayment.setMoney(money);
        repayment.setLenderId(lenderId);
        repayment.setBorrowerId(borrowerId);
        repayment.setTime(dateUtilService.dateFromString(time));
        if (repaymentMapper.insertSelective(repayment) < 1){
            // 受影响的条数为0所以插入失败
            return false;
        }
        return true;
    }
    /**
     * 计算一次性还本付息的应还款
     */
    public List<Repayment> normalPrincipalAndInterest(Loan loan){
        List<Repayment> repayments = new ArrayList<>();
        Repayment repayment = getByLoan(loan);
        repayment.setTime(dateUtilService.dateAddNumber('d',loan.getBorrwwingTime(),loan.getDeadline()));
        repayment.setMoney(loan.getMoney().add(loan.getDayRate().multiply(BigDecimal.valueOf(loan.getDeadline())).multiply(loan.getMoney())));
        repayments.add(repayment);
        return repayments;
    }
    /**
     * 计算按月付息到期还本应还计算，如果未到还款日也计算利息。
     */
    public List<Repayment> normalMonthlyRepayments(Loan loan){
        List<Repayment> repayments = new ArrayList<>();
        // 今天是几号
        long day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //计算借款，开始计息日期
        Date start;
        // 首先查看是否是第一次还款
        if (getRepaymentsByLoanId(loan.getId()).length == 0){
            //如果是第一次还款那么需要从借款日到还款日计算利息
            start = loan.getBorrwwingTime();
        } else {
            //今天是还款日，则需要计算一月的利息
            if (day == loan.getRepaymentDay()){
                start = dateUtilService.dateAddNumber('m',Calendar.getInstance().getTime(),-1);
            } else {
                //今天不是还款日，则需要计算当月的利息
                start = dateUtilService.setDateWithNumber('d',Calendar.getInstance().getTime(),(int)loan.getRepaymentDay());
            }
        }
        //开始日期到今天的间隔天数
        Integer gap = dateUtilService.intervalDay(start,Calendar.getInstance().getTime());
        Repayment repayment = getByLoan(loan);
        repayment.setMoney(loan.getDayRate().multiply(BigDecimal.valueOf(gap)).multiply(loan.getMoney()));
        repayments.add(repayment);
        return repayments;
    }
    /**
     * 计算分期贷款应还详情
     */
    public List<Repayment> normalStaging(Loan loan){
        // TODO 设置已知数
        // 借款日期
        String borrowDateStr = dateUtilService.fromDate(loan.getBorrwwingTime());
        Integer repaymentDay = (int)loan.getRepaymentDay();
        // 日利率
        BigDecimal dayRate = loan.getDayRate();
        //分期数
        long period = dateUtilService.calPeriodBy(loan.getBorrwwingTime(),loan.getDeadline());
        // 本金
        BigDecimal money = loan.getMoney();
        Date borrowDate = dateUtilService.dateFromString(borrowDateStr);
        // TODO 获得还款日期数组
        List<Date> repaymentDates = getRepaymentDates(borrowDate,period,repaymentDay);
        // TODO 获得日期间的间隔天数，首期为借款日->第一次还款。
        Integer[] gapDays = gapDayFromRepaymentDates(repaymentDates,borrowDate);
        // TODO 计算1+RD1，。。。1+RDn同类项。
        BigDecimal[] similarItems = similarItemsFromGapDays(gapDays,dayRate);
        // TODO 计算MB1B2。。。。Bn
        BigDecimal polynomial1 = money;
        for (int i=0;i<similarItems.length;i++){
            polynomial1 = polynomial1.multiply(similarItems[i]);
        }
        // TODO 计算B2B3…Bn + B3B4…Bn +… + Bn-1Bn+ Bn + 1
        BigDecimal polynomial2 = BigDecimal.valueOf(1);
        for (int i=0;i<similarItems.length-1;i++){
            BigDecimal mult =BigDecimal.valueOf(1);
            for (int j=i+1;j<similarItems.length;j++){
                mult = mult.multiply(similarItems[j]);
            }
            polynomial2 = polynomial2.add(mult);
        }
        // 除法保留小数点，四舍五入规则向上取整。
        BigDecimal result = polynomial1.divide(polynomial2,3, RoundingMode.HALF_UP);
        List<Repayment> repayments = new ArrayList<>();
        for (int i = 0;i<repaymentDates.size();i++) {
            Repayment repayment = getByLoan(loan);
            repayment.setTime(repaymentDates.get(i));
            repayment.setMoney(result);
            repayments.add(repayment);
        }
        return repayments;
    }
    /**
     * 计算活期应还利息
     */
    public List<Repayment> normalCurrent(Loan loan){
        List<Repayment> repayments = new ArrayList<>();
                // 今天是几号
        long day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        // 今天是还款日计算利息
        Integer gap = dateUtilService.intervalDay(loan.getBorrwwingTime(),Calendar.getInstance().getTime());
        Repayment repayment = getByLoan(loan);
        BigDecimal interest = loan.getDayRate().multiply(BigDecimal.valueOf(gap)).multiply(loan.getMoney());
        repayment.setMoney(interest.add(loan.getMoney()));
        repayments.add(repayment);
        return repayments;
    }
    /**
     * 通过借款loan获得还款基本信息
     */
    public Repayment getByLoan(Loan loan){
        Repayment repayment = new Repayment();
        repayment.setBorrowerId(loan.getBorrower());
        repayment.setLenderId(loan.getLender());
        repayment.setLoanid(loan.getId());
        repayment.setTime(Calendar.getInstance().getTime());
        return repayment;
    }
    /**
     * 通过借款日期，期数，还款日确定每期的还款日期
     * @param startDate 借款日期
     * @param period 借款期数
     * @param repaymentDay 还款日
     * @return 还款日期的列表。
     */
    public  List<Date> getRepaymentDates(Date startDate,long period,Integer repaymentDay){
        // 首先获取借款日期的借款日
        Integer day = dateUtilService.getDay(startDate);
        Date firstDate;
        // 确定第一期还款日期
        if (repaymentDay == day){
            /*还款日和借款日相同*/
            firstDate = dateUtilService.dateAddNumber('m',startDate,1);
        } else {
            //首先将还款日设置为repayment
            firstDate = dateUtilService.setDateWithNumber('d', startDate, repaymentDay);
            if (repaymentDay > day) {
                /*第一期还款有可能在借款日期当月*/
                if (repaymentDay - day > 28) {
                } else {
                    firstDate = dateUtilService.dateAddNumber('m',firstDate,1);
                }
            }else {
                if ((30 - day + repaymentDay) > 28){
                    // 加一个月即可
                    firstDate = dateUtilService.dateAddNumber('m',firstDate,1);
                } else {
                    firstDate = dateUtilService.dateAddNumber('m',firstDate,2);
                }

            }
        }
        List<Date> dateList = new ArrayList<Date>();
        for (int i=0;i<period;i++){
            dateList.add(firstDate);
            firstDate = dateUtilService.dateAddNumber('m',firstDate,1);
        }
        return dateList;
    }
    /**
     * 用借款日期和还款日期集合获取每期的天数数组
     * @param list
     * @param borrowDate
     * @return
     */
    public  Integer[] gapDayFromRepaymentDates(List<Date> list,Date borrowDate){
        Integer[] gapDays = new Integer[list.size()];
        for (int i=0;i<list.size();i++){
            Integer gap = dateUtilService.intervalDateSpare(borrowDate,list.get(i));
            borrowDate = list.get(i);
            gapDays[i] = gap;
        }
        return gapDays;
    }

    /**
     * 计算1+RD1。。。。1+RDn的数组
     * @param gapDays 每期间隔天数
     * @param rateDay 日利率
     * @return
     */
    public  BigDecimal[] similarItemsFromGapDays(Integer[] gapDays,BigDecimal rateDay){
        BigDecimal[] si = new BigDecimal[gapDays.length];
        for (int i=0;i<gapDays.length;i++){
            si[i] = rateDay.multiply(BigDecimal.valueOf(gapDays[i])).add(BigDecimal.valueOf(1));
        }
        return si;
    }
}
