package com.loan.controller.client.viewobject;

import com.loan.controller.loan.viewobjct.LoanView;
import com.loan.dataobject.Loan;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
@Data
public class ClientPreviewVO {
    // 一个客户模型
    private ClientVO clientVO;
    // 余额
    private BigDecimal balance;
    // 收益
    private BigDecimal earnings;
    //收益率
    private BigDecimal rateOfReturns;
    // 最近一笔贷款
    private LoanView simpleLoanView;


}
