package com.loan.controller.loan;

import com.loan.controller.BaseController;
import com.loan.controller.loan.viewobjct.LoanView;
import com.loan.dataobject.Loan;
import com.loan.dataobject.LoanCertificate;
import com.loan.dataobject.Repayment;
import com.loan.dataobject.RepaymentCertificate;
import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import com.loan.response.CommonReturnType;
import com.loan.service.client.ClientService;
import com.loan.service.dateutil.DateUtilService;
import com.loan.service.loan.LCertificateService;
import com.loan.service.loan.LoanService;
import com.loan.service.repayment.RCertificateService;
import com.loan.service.repayment.RepaymentService;
import com.loan.validator.ValidationResult;
import com.loan.validator.ValidatorImpl;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 借款功能
 */
@Controller
@RequestMapping("/loan")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class LoanController {
    // 创建一笔贷款

    @Autowired
    private ValidatorImpl validator ;

    @Autowired
    private  ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private RCertificateService rCertificateService;
    @Autowired
    private LCertificateService lCertificateService;
    @Autowired
    private DateUtilService dateUtilService;
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType addLoan(@RequestParam(name ="lenderId") Integer lenderId,
                                      @RequestParam(name="borrowerId") Integer borrowerId,
                                      @RequestParam(name="time") String time,
                                      @RequestParam(name="status") byte status,
                                      @RequestParam(name="money") BigDecimal money,
                                      @RequestParam(name="rate") BigDecimal rate,
                                      @RequestParam(name="method") byte method,
                                      @RequestParam(name="deadline")  Integer deadline,
                                      @RequestParam(name="repaymentDay") Short repaymentDay
                                        ) throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException {
        // 查看客户是否有效
        if (!clientService.clientValid(lenderId) || !clientService.clientValid(borrowerId)){
            throw  new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        LoanModel loanModel = new LoanModel();
        loanModel.setBorrower(borrowerId);
        loanModel.setLender(lenderId);
        loanModel.setBorrwwingTime(strToDate(time));
        loanModel.setStatus(status);
        loanModel.setMoney(money);
        loanModel.setDayRate(rate);
        loanModel.setMethod(method);
        loanModel.setDeadline(deadline);
        loanModel.setRepaymentDay(repaymentDay);

        ValidationResult result = validator.validate(loanModel);
        if (result.isHasErrors() || loanModel==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(!loanService.createLoan(loanModel)){
            throw new BussinessException(EmBussinessError.UNKNOWN_ERROR,result.getErrMsg());
        }
        return CommonReturnType.create(null);
    }
    public Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        formatter.setTimeZone(TimeZone.getTimeZone("CST"));
        Date date = formatter.parse(strDate, pos);
        return date ;
    }
    /**
     * 查看所有借款。
     */
    @RequestMapping(value = "/loans",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getLoans(@RequestParam(name = "limit") Integer limit,@RequestParam(name = "page",required = false) Integer page){
        // 首先获取总条数。
        Integer total = loanService.loanSum();
        // 设置第几页
        if (page==null||page==0) {
            page = 1;
        }
        if (page > (total / limit)+1){
            page = (total/limit)+1;
        }
        if(limit == null||limit == 0){
            limit = 50;
        }
        Map<String,Object> result = new HashMap<>() ;
        result.put("sum",total);
        result.put("page",page);
        if (limit >500){
            result.put("notice","一次至多查询500条数据");
        }
        LoanInfoModel[] loanInfoModels = loanService.getLoansByStartRowAndLimit((page-1)*limit,limit);
        result.put("loanInfoModelArr",loanInfoModels);
        return CommonReturnType.create(result);
    }
    /**
     * 通过借款id查询借款待还详情。
     * 测试方法。
     */
    @RequestMapping(value = "/billdetails",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getLoanRepayment(@RequestParam(name = "id") Integer id){
        Map<String,Object> result = new HashMap<>();
        // 借款
        LoanInfoModel loanInfoModel = loanService.convertFromLoan(loanService.getLoan(id));
        result.put("loanInfoModel",loanInfoModel);
        //应还款
        List<Repayment> shouldRepayments = repaymentService.calNormalRepayments(loanService.convertFromLoanInfoModel(loanInfoModel));
        result.put("shouldRepayment",shouldRepayments);
        //已还款
        Repayment[] repayments = repaymentService.getRepaymentsByLoanId(loanInfoModel.getId());
        result.put("repayments",repayments);
        // 有几个还款就有几个凭证（理论）
        RepaymentCertificate[] rCers = new RepaymentCertificate[repayments.length];
        for (int i = 0; i < rCers.length ; i++) {
            rCers[i] = rCertificateService.getRCertificatesByRepaymentId(repayments[i].getId());
        }
        // 还款凭证
        result.put("rCertification",rCers);
        //借款凭证
        LoanCertificate lCer = lCertificateService.getLCertificatesByLoanId(loanInfoModel.getId());
        result.put("lCertification",lCer);
        return CommonReturnType.create(result);
    }
    /**
     * 查看和俞连鹏（name）有关的今天的收款和还款
     */
    @RequestMapping(value = "/today",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getLoanByName(@RequestParam(name = "name",required = false) String name,
                                          @RequestParam(name = "day",required = false) String dayOfMonth) throws BussinessException {
        // 请设置默认的客户号码
        Integer clientId = 1;
        if (name != null){
            List<Integer> ids = clientService.getIdByName(name);
            if (ids.size() > 0){
                clientId = ids.get(0);
            }
        }
        // 1.首先查询出所有还款日为今天的有效贷款。
        Integer day = dateUtilService.getDay(new Date());
        if (dayOfMonth != null){
            day = Integer.valueOf(dayOfMonth);
        }
        List<Loan> loans = loanService.getLoanByRepaymentDayAndId(day,clientId);
        // 2.计算出今天的应还款金额。
        List<Repayment> shouldRs = new ArrayList<>();
        for (Loan loan:loans){
            List<Repayment> repayments = repaymentService.calNormalRepayments(loan);
            for (Repayment repayment : repayments){
                String time1 = dateUtilService.fromDate(repayment.getTime());
                String now = dateUtilService.fromDate(new Date());
                if (time1.equals(now)){
                    // 如果是今天的还款那么就确定。
                    shouldRs.add(repayment);
                    break;//结束循环
                }
            }



        }
        if(shouldRs.size() != loans.size()){
            throw new BussinessException(EmBussinessError.CAL_REPAYMENT_FAILURE);
        }
        // 收入
        ArrayList<Object> receipt = new ArrayList<>();
        // 支出
        ArrayList<Object> repay = new ArrayList<>();
        for (int i = 0;i<loans.size();i++){
            Loan loan = loans.get(i);
            LoanInfoModel loanInfoModel = loanService.convertFromLoan(loan);
            Repayment repayment = shouldRs.get(i);
            HashMap<String,String> today = new HashMap<>();
            today.put("time",dateUtilService.fromDate(loan.getBorrwwingTime()));
            today.put("money",loan.getMoney().toString());
            today.put("pay",repayment.getMoney().toString());
            today.put("type",loanInfoModel.getMethodDesc());
            today.put("loanId",loanInfoModel.getId().toString());
            // 如果出借人为默认客户那么就是收款，否则为还款。
            if(loan.getLender() == clientId){
                today.put("clientId",loan.getBorrower().toString());
                today.put("name",loanInfoModel.getBorrowerName());
                receipt.add(today);
            } else {
                today.put("clientId",loan.getLender().toString());
                today.put("name",loanInfoModel.getLenderName());
                repay.add(today);
            }
        }
        // 3.计算出实际还款。
        HashMap<String,Object> result = new HashMap<>();
        result.put("receipt",receipt);
        result.put("pay",repay);
        // 4.封装数据返回到前台。
        return CommonReturnType.create(result);
    }
    /**
     * 通过借款id查询借款
     */

}
