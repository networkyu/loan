package com.loan.model.loan;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
@Data
@ToString
public class LoanInfoModel {
    private Integer id;

    private Integer lender;

    private Integer borrower;

    private Date borrwwingTime;

    private Byte status;

    private BigDecimal money;

    private BigDecimal dayRate;

    private Byte method;

    private Integer deadline;

    private Short repaymentDay;
    //
    private String borrowerName;
    //
    private String lenderName;

    private String methodDesc;
}
