package com.loan.controller.loan;

import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.client.ClientModel;
import com.loan.model.loan.LoanModel;
import com.loan.response.CommonReturnType;
import com.loan.service.client.ClientService;
import com.loan.service.loan.LoanService;
import com.loan.validator.ValidationResult;
import com.loan.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType addClient(@RequestParam(name ="lenderId") Integer lenderId,
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

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


}
