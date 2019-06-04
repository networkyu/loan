package com.loan.dataobject;

import java.util.Date;

public class CreditScore {
    private Integer id;

    private Integer score;

    private Date time;

    private Integer repayment;

    private String desc;

    private Integer referenceClient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRepayment() {
        return repayment;
    }

    public void setRepayment(Integer repayment) {
        this.repayment = repayment;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Integer getReferenceClient() {
        return referenceClient;
    }

    public void setReferenceClient(Integer referenceClient) {
        this.referenceClient = referenceClient;
    }
}