package com.loan.controller.repayment;

import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.response.CommonReturnType;
import com.loan.service.repayment.RepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/repayment")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class RepaymentController {
    @Autowired
    RepaymentService repaymentService;
    // 创建一笔还款
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createRepayment(@RequestParam(name ="lenderId") String lenderId,
                                            @RequestParam(name="borrowerId") String borrowerId,
                                            @RequestParam(name="time") String time,
                                            @RequestParam(name="money") BigDecimal money,
                                            @RequestParam(name ="loanId") String loanId)throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException{

        if (!repaymentService.createRepayment(Integer.parseInt(loanId),Integer.parseInt(borrowerId),Integer.parseInt(lenderId),time,money)){
            // 参数不合法，无法添加。
           throw  new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        return CommonReturnType.create(null);
    }

}
