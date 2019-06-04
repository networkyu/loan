package com.loan.dataobject;

import java.util.Date;

public class RepaymentIntegral {
    private Integer id;

    private Date time;

    private Integer outValue;

    private Integer client;

    private Integer enterValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getOutValue() {
        return outValue;
    }

    public void setOutValue(Integer outValue) {
        this.outValue = outValue;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getEnterValue() {
        return enterValue;
    }

    public void setEnterValue(Integer enterValue) {
        this.enterValue = enterValue;
    }
}