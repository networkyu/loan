package com.loan.service.loan;

import com.loan.dao.LoanMapper;
import com.loan.dataobject.Loan;
import com.loan.dataobject.Repayment;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import com.loan.service.client.ClientService;
import com.loan.service.dateutil.DateUtilService;
import com.loan.service.dateutil.DateUtilServiceImp;
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

    @Override
    public LoanModel[] getLoanModelsByBorrowerName(String borrowerName) {
        if (borrowerName == null){
            return null;
        }

        return new LoanModel[0];
    }
    @Override
    public LoanInfoModel[] getLoansByStartRowAndLimit(Integer start, Integer limit) {
        List<Loan> loans = loanMapper.selectLoansByStartRowAndLimit(start,limit);
        LoanInfoModel[] loanInfoModels = new LoanInfoModel[loans.size()];
        for (int i = 0; i < loans.size(); i++) {
            LoanInfoModel loanInfoModel = new LoanInfoModel();
            Loan loan = loans.get(i);
            loanInfoModels[i] = convertFromLoan(loan);
        }
        return loanInfoModels;
    }

    /**
     * 将loan转换为LoanInfoModel
     * @return
     */
    @Override
    public LoanInfoModel convertFromLoan(Loan loan){
        LoanInfoModel loanInfoModel = new LoanInfoModel();
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
        return loanInfoModel;
    }

    @Override
    public Loan convertFromLoanInfoModel(LoanInfoModel loanInfoModel){
        if (loanInfoModel == null){
            return null;
        }
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanInfoModel,loan);
        return loan;
    }
    @Override
    public Integer loanSum() {
        return loanMapper.selectCountId();
    }
    @Override
    public Map<String, Object> getLoanRepayment(Integer loanId) {
//        Map<String,Object> loanRepaymentDetail = new HashMap<>();
////        首先查询出该笔借款
//        Loan loan = loanMapper.selectByPrimaryKey(loanId);
//        loanRepaymentDetail.put("loan",loan);
//        List<Repayment> waitingRepayments = this.normalRepayments(loan);
//        loanRepaymentDetail.put("waitingRepayments",waitingRepayments);
//        Repayment[] repayments = repaymentService.getRepaymentsByLoanId(loanId);
//        loanRepaymentDetail.put("repayments",repayments);
//        //计算收益，收益率，余额。
//        // 先计算余额。
//        // 应收总额
//        BigDecimal balance = this.loanBalance(loan,repayments,waitingRepayments);
//        BigDecimal recome = BigDecimal.ZERO;
//
//        loanRepaymentDetail.put("balance",balance);
//        return  loanRepaymentDetail;
        return null;
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
            // 结束日期肯定是当月还款日。
            Date endDate = dateUtilService.setDateWithNumber('d',new Date(),Integer.valueOf(loan.getRepaymentDay()));
            BigDecimal repaymentBalance = balanceFromMonthly(loan.getBorrwwingTime(),endDate,loan.getMoney(),loan.getDayRate(),repayments);
            Date startDate = loan.getBorrwwingTime();
            if (repayments.length>0){
                startDate = repayments[repayments.length-1].getTime();
            }
            return repaymentBalance.add(monthInterestSumFrom(startDate,endDate,repaymentBalance,loan.getDayRate()));
        }
        if (loan.getMethod() == 8){
            BigDecimal repaymentBalance = balanceFromMonthly(loan.getBorrwwingTime(),new Date(),loan.getMoney(),loan.getDayRate(),repayments);
            Date startDate = loan.getBorrwwingTime();
            if (repayments.length>0){
                startDate = repayments[repayments.length-1].getTime();
            }
            // 从开始日期到今天的本息和。
            return repaymentBalance.add(repaymentBalance.multiply(loan.getDayRate()).multiply(BigDecimal.valueOf(dateUtilService.intervalDay(startDate,new Date()))));
        }
        if (loan.getMethod() == 3){
            // 等额本息。

        }
        return BigDecimal.ZERO;
    }

    /**
     * 根据多条还款计算累计利息和剩余,可以衍生未长期借款累计
     * @param
     * @param repayments
     * @return
     */
    public BigDecimal balanceFromMonthly(Date startDate,Date endDate,BigDecimal money,BigDecimal dayRate,Repayment[] repayments){
        //起始利息
        BigDecimal interest = BigDecimal.ZERO;
        // 起始本金
        BigDecimal principal = money;
//        Date startTime = loan.getBorrwwingTime();
        // 理论上讲有无数笔
        for (int i=0;i<repayments.length;i++){
            // 当前的一笔还款
            Repayment repayment = repayments[i];
            // 首先计算一下还款日与开始日期的利息
            Integer gap = dateUtilService.intervalDay(startDate,repayment.getTime());
            // 算一下期间利息(相当于应还利息)
            BigDecimal gapInterest = BigDecimal.valueOf(gap).multiply(principal).multiply(dayRate);
            // 实际还款>应还 本金减，利息不管。实际还款=应还 无操作，实际还款<应还，利息加，本金不变，
            if (gapInterest.compareTo(repayment.getMoney())==1){
                //应还大于还款
                interest = interest.add(gapInterest.subtract(repayment.getMoney()));
            }
            if (gapInterest.compareTo(repayment.getMoney())==-1){
                //  应还小于实际还的
                principal = principal.subtract(repayment.getMoney().subtract(gapInterest));
            }
            // 往后推。
            startDate = repayment.getTime();
        }
        return interest.add(principal);
    }
    /**
     * 开始日期到结束日期的月利息总额,为月付利息到期还本，如果付息日和第一次借款日未达到10天则不计算利息。并且往前
     * 推断是否预计累计每个月未还的利息。
     * @return
     */
    public BigDecimal monthInterestSumFrom(Date startDate,Date endDate,BigDecimal money,BigDecimal dayRate){
        Date lastDate = endDate;
//                累加利息
        BigDecimal interest = BigDecimal.ZERO;
        //只有开始时间在结束时间之前就要计算利息。
        while (startDate.before(endDate)){
            Date startTime=dateUtilService.dateAddNumber('m',endDate,-1);
            if (startTime.before(startDate)){
                startTime = startDate;
            }
            Integer gapDay = dateUtilService.intervalDay(startTime,endDate);
            //第一笔应还需要超过10天。
            if (dateUtilService.intervalDay(startTime,lastDate) > 10){
                interest = interest.add(money.multiply(dayRate).multiply(BigDecimal.valueOf(gapDay)));
            }
            /*往前推*/
            endDate = startTime;
        }
        return interest;
    }

    /**
     * 计算分期贷款的利息。
     * @param loan
     * @param repayments
     * @param waitingRepay
     * @return
     */
    public BigDecimal balanceFromStaging(Loan loan,Repayment[] repayments,List<Repayment> waitingRepay){
        // 开始日期为
        /**未逾期的情况下*/
//        Date startDate = loan.getBorrwwingTime();
//        for (int i=0;i<waitingRepay.size();i++){
//            Repayment waitRepayment = waitingRepay.get(i);
//            Date endDate = waitRepayment.getTime();
//            // 从期间内开始到结束日期的已还款。
//            List<Repayment> alreadyRepayment = new ArrayList<>();
//            // 每期还款，将应还日期与开始日期的所有还款都查出来比较一下。
//            for (int j = 0;j < repayments.length;j++){
//                Repayment tmpAlreadlyRepayment = repayments[i];
//                if (!tmpAlreadlyRepayment.getTime().before(startDate) && !tmpAlreadlyRepayment.getTime().after(endDate)){
//                    alreadyRepayment.add(tmpAlreadlyRepayment);
//                }
//            }
//            // TODO 计算当期剩余本息和，如果未还计入下一个周期。
//            while (alreadyRepayment.size()>0){}
//        }
//        Date endDate = waitingRepay
        for (int i=0;i<repayments.length;i++){
            Repayment repayment = repayments[i];
            Date repaymentDate = repayment.getTime();
            // 期间本息计算
            for (int j=0;j<waitingRepay.size();j++){
                Repayment waitRepayment = waitingRepay.get(i);
                if (repaymentDate.after(waitRepayment.getTime())){

                } else {
                    // 是这一期的还款。

                }
            }

        }
        return BigDecimal.ZERO;
    }
}
