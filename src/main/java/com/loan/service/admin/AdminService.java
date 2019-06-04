package com.loan.service.admin;

import com.loan.model.admin.AdminModel;

public interface AdminService {
    AdminModel verification(String account,String encryptPassword);
    boolean changePassword(String originalPsd,String newPsd,String account);
}
