package com.loan.controller.client;

import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.client.ClientModel;
import com.loan.response.CommonReturnType;
import com.loan.service.client.ClientService;
import com.loan.validator.ValidationResult;
import com.loan.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 客户管理功能
 */
@Controller
@RequestMapping("/client")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ClientController {

    @Autowired
    private ValidatorImpl validator ;

    @Autowired
    private ClientService clientService;
    // 添加用户
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType addClient(@RequestParam(name = "name") String name, @RequestParam(name="tel") String tel, @RequestParam(name="idCardNumber") String idCardNumber,@RequestParam(name="eMail") String eMail) throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException {
        // 验证参数
        ClientModel clientModel = new ClientModel();
        clientModel.setName(name);
        clientModel.setTel(tel);
        clientModel.seteMail(eMail);
        clientModel.setIdentityNumber(idCardNumber);
        ValidationResult result = validator.validate(clientModel);
        if (result.isHasErrors() || clientModel==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(!clientService.addClient(clientModel)){
            throw new BussinessException(EmBussinessError.UNKNOWN_ERROR,result.getErrMsg());
        }
        return CommonReturnType.create(null);
    }
    //获取用户借出积分和借入积分排序{id:2,name:王小，data:{}}
    @RequestMapping(value = "/listintegra",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listIntegra(){

    }

}
