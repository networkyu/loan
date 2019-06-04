package com.loan.model.loan;

import com.loan.model.client.ClientModel;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;
@Data
@ToString
public class LoanModel {
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
}
