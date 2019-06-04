package com.loan;

import com.loan.dao.ClientMapper;
import com.loan.dataobject.Client;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication(scanBasePackages = {"com.loan"})
@RestController
@MapperScan({"com.loan.dao"})
public class App {
    @Autowired
    private ClientMapper clientMapper;

    // 读取所有用户
    @RequestMapping("/client")
    public String client(){
        Client client = clientMapper.selectByPrimaryKey(1);
        if (client == null){
            return "用户对象不存在";
        } else {
            return client.getName();
        }
    }
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        SpringApplication.run(App.class,args);

    }


}
