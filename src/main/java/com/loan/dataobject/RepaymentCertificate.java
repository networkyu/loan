package com.loan.dataobject;

public class RepaymentCertificate {
    private Integer id;

    private Integer repaymentId;

    private String photoUrl;

    private String method;

    private String lenderAccount;

    private String borrowerAccount;

    private String transferUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getLenderAccount() {
        return lenderAccount;
    }

    public void setLenderAccount(String lenderAccount) {
        this.lenderAccount = lenderAccount == null ? null : lenderAccount.trim();
    }

    public String getBorrowerAccount() {
        return borrowerAccount;
    }

    public void setBorrowerAccount(String borrowerAccount) {
        this.borrowerAccount = borrowerAccount == null ? null : borrowerAccount.trim();
    }

    public String getTransferUrl() {
        return transferUrl;
    }

    public void setTransferUrl(String transferUrl) {
        this.transferUrl = transferUrl == null ? null : transferUrl.trim();
    }
}