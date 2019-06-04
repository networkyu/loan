package com.loan.controller;

import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {


    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request,Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if (ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException)ex;
            responseData.put("errCode",bussinessException.getErrCode());
            responseData.put("errMsg",bussinessException.getErrMsg());
        } else {
            System.out.println(ex);
            responseData.put("errCode",EmBussinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBussinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
