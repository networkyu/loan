package com.loan.error;

public enum EmBussinessError implements CommonError {
    //通用错误类型20001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),
    //2000开头为用户信息香港错误定义。
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAILURE(20002,"用户登录失败，用户名或密码错误"),
    MODIFY_PASSWORD_FAILURE(20003,"修改失败，用户名或原密码错误"),
    CAL_REPAYMENT_FAILURE(30001,"计算应还款失败"),
    ;

    private EmBussinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    private int errCode;
    private String errMsg;
    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
