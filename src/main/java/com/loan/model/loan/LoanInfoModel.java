package com.loan.model.loan;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
//@Data
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLender() {
        return lender;
    }

    public void setLender(Integer lender) {
        this.lender = lender;
    }

    public Integer getBorrower() {
        return borrower;
    }

    public void setBorrower(Integer borrower) {
        this.borrower = borrower;
    }

    public Date getBorrwwingTime() {
        return borrwwingTime;
    }

    public void setBorrwwingTime(Date borrwwingTime) {
        this.borrwwingTime = borrwwingTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getDayRate() {
        return dayRate;
    }

    public void setDayRate(BigDecimal dayRate) {
        this.dayRate = dayRate;
    }

    public Byte getMethod() {
        return method;
    }

    public void setMethod(Byte method) {
        this.method = method;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Short getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(Short repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    private String methodDesc;
}
