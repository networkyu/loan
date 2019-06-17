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
    public BigDecimal getEarningsByClientId(Integer clientId) {
        return null;
    }

    @Override
    public BigDecimal getRateOfReturnsByClientId(Integer clientId) {
        return null;
    }

    @Override
    /**
     * 客户余额为当前借出剩余未还的值包含利息-客户借入剩余未还的值包包含未还利息。
     */
    public BigDecimal getBalanceByClientId(Integer clientId) {
        BigDecimal borrowValue = BigDecimal.valueOf(0);
        BigDecimal lendValue = BigDecimal.valueOf(0);
        // 首先拿出该用户的所有借款
        List<Loan> loans = this.getEffectiveLoansByClientId(clientId);
        for (int i = 0; i < loans.size() ; i++) {
            Loan loan = loans.get(i);
            Repayment[] repayments = repaymentService.getRepaymentsByLoanId(loan.getId());


        }
        return lendValue.subtract(borrowValue);
    }
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
    /**
     * 计算分期贷款的利息和还款详情。
     */
    public Map<String,Object> calculatorLoanAndRepayment(Loan loan,Repayment[] repayments){
        // 余额=本金+利息-已还
        BigDecimal balance = BigDecimal.valueOf(0);
        // 总收利息
        BigDecimal allInterest = BigDecimal.valueOf(0);
        // 总额
        BigDecimal totalMoney = BigDecimal.valueOf(0);
        // 收益率
        BigDecimal rateOfReturns = BigDecimal.valueOf(0);
        // 一次性还本付息
        if (loan.getMethod() == '1'){
                // 没有还款
            if (repayments.length == 0){

                allInterest = loan.getDayRate().multiply(BigDecimal.valueOf(loan.getDeadline())).multiply(loan.getMoney());
                totalMoney =
                /// 余额为本息和。
                balance = loan.getMoney().add(loan.getDayRate().multiply(BigDecimal.valueOf(loan.getDeadline())).multiply(loan.getMoney()));
            }
        }
        return new HashMap<>();
    }
    /***
     * 计算待还详情
     */
    public List<Repayment> loanDetail(Loan loan){
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
                Date start = dateUtilService.dateAddNumber('m',Calendar.getInstance().getTime(),-1);
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
            // 今天是还款日则计算利息
            Integer gap = dateUtilService.intervalDate(loan.getBorrwwingTime(),Calendar.getInstance().getTime());
            Repayment repayment = new Repayment();
            repayment.setBorrowerId(loan.getBorrower());
            repayment.setLenderId(loan.getLender());
            repayment.setLoanid(loan.getId());
            repayment.setTime(Calendar.getInstance().getTime());
            repayment.setMoney(loan.getDayRate().multiply(BigDecimal.valueOf(gap)).multiply(loan.getMoney()));
            repayments.add(repayment);
        }
        if (loan.getMethod() == 3){

            // 首先计算出最后还款日期。
            Date date = dateUtilService.dateAddNumber('d',loan.getBorrwwingTime(),loan.getDeadline());
            // 期数
            int period = 0;
            while (date.before(loan.getBorrwwingTime())){
                Repayment repayment = new Repayment();
                repayment.setTime(date);
                date = dateUtilService.dateAddNumber('m',date,-1);
                if(dateUtilService.intervalDate(loan.getBorrwwingTime(),date) > 28){
                    period = period+1;
                }
                repayments.add(repayment);
            }
//            每月还款额=贷款本金×[月利率×(1+月利率) ^ 还款月数]÷{[(1+月利率) ^ 还款月数]-1}
            Date firstDate = repayments.get(repayments.size() - 1).getTime();
            // 判断借款日期与第一期日期上月的天数,如果大于30天需要加利息，如果小于30天那么就业减本金。
            int gapDay = dateUtilService.intervalDate(loan.getBorrwwingTime(),dateUtilService.dateAddNumber('m',firstDate,-1));



        }
        return repayments;
    }
}
