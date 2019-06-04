package com.loan.service.admin;

import com.loan.dao.AdminMapper;
import com.loan.dao.AdminPasswordMapper;
import com.loan.dataobject.Admin;
import com.loan.dataobject.AdminPassword;
import com.loan.model.admin.AdminModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImp implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    AdminPasswordMapper adminPasswordMapper;
    @Override
    public AdminModel verification(String account, String encryptPassword) {
        Admin admin = adminMapper.selectByAccount(account);
        if (admin == null){
            // 用户不存在
            return null;
        }
        AdminPassword adminPassword = adminPasswordMapper.selectByAdminId(admin.getId());
        if (adminPassword.getCiphertext()==null||adminPassword.getCiphertext().isEmpty()){
            // 未设置密码，不能登录，让其修改密码后登录。
            return null;
        }
        if (!encryptPassword.equals(adminPassword.getCiphertext())){
            // 密码错误，登录失败
            return null;
        }
        AdminModel adminModel = new AdminModel();
        adminModel.setAccount(admin.getAccount());
        return adminModel;
    }
    @Override
    public boolean changePassword(String originalPsd, String newPsd, String account) {
        Admin admin = adminMapper.selectByAccount(account);
        if (admin == null){
            // 用户不存在
            return false;
        }
        AdminPassword adminPassword = adminPasswordMapper.selectByAdminId(admin.getId());
        if (adminPassword.getCiphertext()==null||adminPassword.getCiphertext().isEmpty()){
            // 未设置密码，不能修改。
            return false;
        }
        int resultValue = adminPasswordMapper.updateCiphertextByAdminId(newPsd,admin.getId());
        if (resultValue<1){
            return false;
        }
        // 将新密码插入到数据库中。
        return true;
    }
}
