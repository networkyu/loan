package com.loan.dataobject;

public class LoanCertificate {
    private Integer id;

    private Integer loanId;

    private String photoUrl;

    private String transferVoucherUrl;

    private String recordingCertificate;

    private String videoCertificate;

    private String transferMethod;

    private String lenderAccount;

    private String borrwerAccount;

    private String addressBookUrl;

    private String borrowing;

    private String borrowingId;

    private String use;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }

    public String getTransferVoucherUrl() {
        return transferVoucherUrl;
    }

    public void setTransferVoucherUrl(String transferVoucherUrl) {
        this.transferVoucherUrl = transferVoucherUrl == null ? null : transferVoucherUrl.trim();
    }

    public String getRecordingCertificate() {
        return recordingCertificate;
    }

    public void setRecordingCertificate(String recordingCertificate) {
        this.recordingCertificate = recordingCertificate == null ? null : recordingCertificate.trim();
    }

    public String getVideoCertificate() {
        return videoCertificate;
    }

    public void setVideoCertificate(String videoCertificate) {
        this.videoCertificate = videoCertificate == null ? null : videoCertificate.trim();
    }

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod == null ? null : transferMethod.trim();
    }

    public String getLenderAccount() {
        return lenderAccount;
    }

    public void setLenderAccount(String lenderAccount) {
        this.lenderAccount = lenderAccount == null ? null : lenderAccount.trim();
    }

    public String getBorrwerAccount() {
        return borrwerAccount;
    }

    public void setBorrwerAccount(String borrwerAccount) {
        this.borrwerAccount = borrwerAccount == null ? null : borrwerAccount.trim();
    }

    public String getAddressBookUrl() {
        return addressBookUrl;
    }

    public void setAddressBookUrl(String addressBookUrl) {
        this.addressBookUrl = addressBookUrl == null ? null : addressBookUrl.trim();
    }

    public String getBorrowing() {
        return borrowing;
    }

    public void setBorrowing(String borrowing) {
        this.borrowing = borrowing == null ? null : borrowing.trim();
    }

    public String getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(String borrowingId) {
        this.borrowingId = borrowingId == null ? null : borrowingId.trim();
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use == null ? null : use.trim();
    }
}