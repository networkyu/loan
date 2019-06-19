package com.loan.service.loan;

import com.loan.dao.LoanMapper;
import com.loan.dataobject.Loan;
import com.loan.dataobject.Repayment;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import com.loan.service.client.ClientService;
import com.loan.service.dateutil.DateUtilService;
import com.loan.service.repayment.RepaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class LoanServiceImp implements LoanService {

    @Autowired
    LoanMapper loanMapper;
    @Autowired
    ClientService clientService;

    @Autowired
    RepaymentService repaymentService;
    @Autowired
    DateUtilService dateUtilService;

    @Override
    public Loan getLoan(Integer loanId) {
        return loanMapper.selectByPrimaryKey(loanId);
    }

    @Override
    public boolean createLoan(LoanModel loanModel) {
        Loan loan = convertFromModel(loanModel);
        if(loanMapper.insertSelective(loan) < 1){
            return false;
        }
        return true;
    }
    private Loan convertFromModel(LoanModel loanModel){
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanModel,loan);
        return loan;
    }

    @Override
    public List<Loan> getLoansByClientId(Integer clientId) {
        List<Loan> loans = loanMapper.selectLoansByClientId(clientId);
        return loans;
    }

    @Override
    public List<Loan> getEffectiveLoansByClientId(Integer clientId) {
        return loanMapper.selectEffectiveLoansByClientId(clientId);
    }
//    @Override
//    public BigDecimal getEarningsByClientId(Integer clientId) {
//        return null;
//    }
//
//    @Override
//    public BigDecimal getRateOfReturnsByClientId(Integer clientId) {
//        return null;
//    }
//
//    @Override
//    /**
//     * 客户余额为当前借出剩余未还的值包含利息-客户借入剩余未还的值包包含未还利息。
//     */
//    public BigDecimal getBalanceByClientId(Integer clientId) {
//        BigDecimal borrowValue = BigDecimal.valueOf(0);
//        BigDecimal lendValue = BigDecimal.valueOf(0);
//        // 首先拿出该用户的所有借款
//        List<Loan> loans = this.getEffectiveLoansByClientId(clientId);
//        for (int i = 0; i < loans.size() ; i++) {
//            Loan loan = loans.get(i);
//            Repayment[] repayments = repaymentService.getRepaymentsByLoanId(loan.getId());
//
//
//        }
//        return lendValue.subtract(borrowValue);
//    }
    @Override
    public LoanInfoModel[] getLoansByStartRowAndLimit(Integer start, Integer limit) {
        List<Loan> loans = loanMapper.selectLoansByStartRowAndLimit(start,limit);
        LoanInfoModel[] loanInfoModels = new LoanInfoModel[loans.size()];
        for (int i = 0; i < loans.size(); i++) {
            LoanInfoModel loanInfoModel = new LoanInfoModel();
            Loan loan = loans.get(i);
            BeanUtils.copyProperties(loan,loanInfoModel);
            loanInfoModel.setBorrowerName(clientService.getNameByClientId(loan.getBorrower()));
            loanInfoModel.setLenderName(clientService.getNameByClientId(loan.getLender()));
            switch (loan.getMethod()){
                case 1:
                    loanInfoModel.setMethodDesc("一次性还本付息");
                    break;
                case 2:
                    loanInfoModel.setMethodDesc("按月付息到期还本");
                    break;
                case 3:
                    loanInfoModel.setMethodDesc("等额本息按月分期");
                    break;
                case 8:
                    loanInfoModel.setMethodDesc("不定期借贷");
                    break;
                default:
                    loanInfoModel.setMethodDesc("其他");
                    break;
            }
            loanInfoModels[i] = loanInfoModel;
        }
        return loanInfoModels;
    }

    @Override
    public Integer loanSum() {
        return loanMapper.selectCountId();
    }
    /***
     * 计算待还详情
     */
    @Override
    public List<Repayment> normalRepayments(Loan loan){
        List<Repayment> repayments = new ArrayList<>();
        if (loan.getMethod() == 1){
            Repayment repayment = new Repayment();
            repayment.setBorrowerId(loan.getBorrower());
            repayment.setLenderId(loan.getLender());
            repayment.setLoanid(loan.getId());
            repayment.setTime(dateUtilService.dateAddNumber('d',loan.getBorrwwingTime(),loan.getDeadline()));
            repayment.setMoney(loan.getMoney().add(loan.getDayRate().multiply(BigDecimal.valueOf(loan.getDeadline())).multiply(loan.getMoney())));
            repayments.add(repayment);
        }
        if (loan.getMethod() == 2){
            // 今天是几号
            long day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            // 今天是还款日则计算利息
            if (day == loan.getRepaymentDay()){
                Date start;
                // 首先查看是否是第一次还款
                if (repaymentService.getRepaymentsByLoanId(loan.getId()).length == 0){
                    //如果是第一次还款那么需要从借款日到还款日计算利息
                    start = loan.getBorrwwingTime();
                } else {
                    start = dateUtilService.dateAddNumber('m',Calendar.getInstance().getTime(),-1);
                }

                Integer gap = dateUtilService.intervalDate(start,Calendar.getInstance().getTime());
                Repayment repayment = new Repayment();
                repayment.setBorrowerId(loan.getBorrower());
                repayment.setLenderId(loan.getLender());
                repayment.setLoanid(loan.getId());
                repayment.setTime(Calendar.getInstance().getTime());
                repayment.setMoney(loan.getDayRate().multiply(BigDecimal.valueOf(gap)).multiply(loan.getMoney()));
                repayments.add(repayment);
            }
        }
        if (loan.getMethod() == 8){
            // 今天是几号
            long day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            // 今天是还款日计算利息
            Integer gap = dateUtilService.intervalDate(loan.getBorrwwingTime(),Calendar.getInstance().getTime());
            Repayment repayment = new Repayment();
            repayment.setBorrowerId(loan.getBorrower());
            repayment.setLenderId(loan.getLender());
            repayment.setLoanid(loan.getId());
            repayment.setTime(Calendar.getInstance().getTime());
            BigDecimal interest = loan.getDayRate().multiply(BigDecimal.valueOf(gap)).multiply(loan.getMoney());
            repayment.setMoney(interest.add(loan.getMoney()));
            repayments.add(repayment);
        }
        if (loan.getMethod() == 3){
            // 首先计算出最后还款日期。
            Date date = dateUtilService.dateAddNumber('d',loan.getBorrwwingTime(),loan.getDeadline());
            //月利率数组
            List<BigDecimal> rates = new ArrayList<>();
            // 月利率乘积
            BigDecimal monthRateMut = BigDecimal.valueOf(0);
            while ((loan.getBorrwwingTime()).before(date)){
                Repayment repayment = new Repayment();
                repayment.setTime(date);
                if(dateUtilService.intervalDate(loan.getBorrwwingTime(),date) > 28){
                    repayments.add(repayment);
                    /*直接月利率+1构造B1。。。Bn*/
                    BigDecimal monthRates = BigDecimal.valueOf(1).add(loan.getDayRate().multiply(BigDecimal.valueOf((dateUtilService.intervalDate(dateUtilService.dateAddNumber('m',date,-1),repayment.getTime())))));
                    rates.add(monthRates);
                }
                date = dateUtilService.dateAddNumber('m',date,-1);
                // 月利率
            }
            // 修改月利率的最后一条数据。
            if (rates.size()>0&&repayments.size()>0){
                rates.set(rates.size() - 1,BigDecimal.valueOf(dateUtilService.intervalDate(loan.getBorrwwingTime(),repayments.get(repayments.size()-1).getTime())).multiply(loan.getDayRate()).add(BigDecimal.valueOf(1)));
            }
            Collections.reverse(rates);
            BigDecimal rateMut = BigDecimal.valueOf(1);
            for (BigDecimal rate:rates){
                rateMut = rateMut.multiply(rate);
            }
            BigDecimal rateAdd = BigDecimal.valueOf(1);
            // 例如有5期，0，1，2，3，4分别为
            for (int i=0;i<rates.size()-1;i++){
                BigDecimal rateMutTwo =BigDecimal.valueOf(1);
                for (int j=i+1;j<rates.size();j++){
                    rateMutTwo = rateMutTwo.multiply(rates.get(j));
                }
                rateAdd = rateAdd.add(rateMutTwo);
            }
            System.out.println("乘积为："+rateMut.toString()+"乘积递减和为："+rateAdd);
            rateMut = loan.getMoney().multiply(rateMut);
            BigDecimal money = rateMut.divide(rateAdd,3,RoundingMode.HALF_UP);
            for (Repayment repayment:repayments){
                repayment.setMoney(money);
            }
        }
        return repayments;
    }

    @Override
    public Map<String, Object> getLoanRepayment(Integer loanId) {
        Map<String,Object> loanRepaymentDetail = new HashMap<>();
//        首先查询出该笔借款
        Loan loan = loanMapper.selectByPrimaryKey(loanId);
        loanRepaymentDetail.put("loan",loan);
        List<Repayment> waitingRepayments = this.normalRepayments(loan);
        loanRepaymentDetail.put("waitingRepayments",waitingRepayments);
        Repayment[] repayments = repaymentService.getRepaymentsByLoanId(loanId);
        loanRepaymentDetail.put("repayments",repayments);
        //计算收益，收益率，余额。
        // 先计算余额。
        // 应收总额
        BigDecimal balance = this.loanBalance(loan,repayments,waitingRepayments);
        BigDecimal recome = BigDecimal.ZERO;

        loanRepaymentDetail.put("balance",balance);
        return  loanRepaymentDetail;
    }
    /**
     * 计算一笔借款的余额,包含未还和未还利息。
     */
    public BigDecimal loanBalance(Loan loan,Repayment[] repayments,List<Repayment> waitingRepay){
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        // 如果借款已经完成，那么它的余额为0。
        if(loan.getStatus() == 1){
            return bigDecimal;
        }
        if (loan.getMethod()==1){
            Date promiseDate = dateUtilService.dateAddNumber('d',loan.getBorrwwingTime(),loan.getDeadline());
            Date today = new Date();
            // 一次性还本付息
            if (repayments.length==0){
                // 查看是否逾期
                if (today.after(promiseDate)){
                    // 逾期--原来应还+本金*日利率*逾期天数，计算到当天。
                    return waitingRepay.get(0).getMoney().add(loan.getMoney().multiply(loan.getDayRate()).multiply(BigDecimal.valueOf(dateUtilService.intervalDate(waitingRepay.get(0).getTime(),new Date()))));
                } else {
                    //没有还款，没有逾期
                    return waitingRepay.get(0).getMoney();
                }
            } else {
                // 有还款-肯定未还完。
                //起始利息
                BigDecimal interest = BigDecimal.ZERO;
                // 起始本金
                BigDecimal principal = loan.getMoney();
                Date startTime = loan.getBorrwwingTime();
                // 理论上讲有无数笔
                for (int i=0;i<repayments.length;i++){
                    // 当前的一笔还款
                    Repayment repayment = repayments[i];
                    // 首先计算一下还款日与开始日期的利息
                    Integer gap = dateUtilService.intervalDate(startTime,repayment.getTime());
                    // 算一下期间利息(相当于应还利息)
                    BigDecimal gapInterest = BigDecimal.valueOf(gap).multiply(principal).multiply(loan.getDayRate());
                    // 实际还款>应还 本金减，利息不管。实际还款=应还 无操作，实际还款<应还，利息加，本金不变，
                    if (gapInterest.compareTo(repayment.getMoney())==1){
                        //应还大于还款
                        interest = interest.add(gapInterest.subtract(repayment.getMoney()));
                    }
                    if (gapInterest.compareTo(repayment.getMoney())==-1){
                        //  应还小于实际还的
                        principal = principal.subtract(repayment.getMoney().subtract(gapInterest));
                    }
                    startTime = repayment.getTime();
                }
                if (!startTime.after(promiseDate)){
                    // 未逾期
                    interest = interest.add(principal.multiply(loan.getDayRate()).multiply(BigDecimal.valueOf(dateUtilService.intervalDate(startTime,promiseDate))));
                } else {
                    interest = interest.add(principal.multiply(loan.getDayRate()).multiply(BigDecimal.valueOf(dateUtilService.intervalDate(startTime,new Date()))));
                }
                return interest.add(principal);
            }
        }
        if (loan.getMethod() == 2){
            // 按月付息，到期还本。
        }
        return BigDecimal.ZERO;
    }

}
