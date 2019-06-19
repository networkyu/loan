package com.loan.controller.loan;

import com.loan.controller.BaseController;
import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.loan.LoanInfoModel;
import com.loan.model.loan.LoanModel;
import com.loan.response.CommonReturnType;
import com.loan.service.client.ClientService;
import com.loan.service.loan.LoanService;
import com.loan.validator.ValidationResult;
import com.loan.validator.ValidatorImpl;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 借款功能
 */
@Controller
@RequestMapping("/loan")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class LoanController extends BaseController {
    // 创建一笔贷款

    @Autowired
    private ValidatorImpl validator ;

    @Autowired
    private  ClientService clientService;

    @Autowired
    private LoanService loanService;
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
    @RequestMapping(value = "/loanrepayment",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getLoanRepayment(@RequestParam(name = "id") Integer id){

        return CommonReturnType.create(loanService.getLoanRepayment(id));
    }


}
