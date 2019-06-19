package com.loan.model.repayment;

import java.math.BigDecimal;
import java.util.Date;

public class RepaymentModel {
    private Integer id;

    private Integer borrowerId;

    private Integer lenderId;

    private Date time;

    private Integer loanid;

    private BigDecimal money;

    // 还款状态
    private byte status;

}
