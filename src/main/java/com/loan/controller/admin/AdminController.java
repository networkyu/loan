package com.loan.controller.admin;

import com.loan.controller.BaseController;
import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.admin.AdminModel;
import com.loan.response.CommonReturnType;
import com.loan.service.admin.AdminService;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 管理员操作
 */
@Controller
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class AdminController extends BaseController {

    @Autowired
    AdminService adminService;

    @Autowired
    HttpServletRequest httpServletRequest;

    //登录
    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "account") String account, @RequestParam(name="password") String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (account==null||account.isEmpty()||password.isEmpty()||account==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        AdminModel adminModel = adminService.verification(account,EncodeByMd5(password));
        if (adminModel==null){
            throw  new BussinessException(EmBussinessError.USER_LOGIN_FAILURE);
        }
        // 将登录凭证加入到用户登录成功的session内。
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("ADMIN",adminModel);
        return CommonReturnType.create(null);
    }
    // 修改密码
    @RequestMapping(value = "/modify",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType modifyPassword(@RequestParam(name = "originalPassword") String originalPassword,@RequestParam(name="newPassword") String newPassword,@RequestParam(name="account") String account) throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException {
        if (originalPassword.equals(newPassword)||newPassword==null||newPassword.isEmpty()){
            throw  new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (!originalPassword.equals("cmccTTuijj090")){
            throw  new BussinessException(EmBussinessError.MODIFY_PASSWORD_FAILURE);
        }
        if (!adminService.changePassword(EncodeByMd5(originalPassword),EncodeByMd5(newPassword),account)){
            throw  new BussinessException(EmBussinessError.MODIFY_PASSWORD_FAILURE);
        }
        return CommonReturnType.create(null);
    }
    // 查看登录ip和登录时间
    @RequestMapping(value = "/history",method = {RequestMethod.GET})
    @ResponseBody
    public String loginHistory(){
        return this.httpServletRequest.getRemoteAddr();
    }
    // md5混淆
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException,UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密字符串
        String newString = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newString;
    }
}
