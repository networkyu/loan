package com.loan.controller.loan.viewobjct;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class LoanView {
    // 出借人姓名。
    private String lenderName;
    // 借款人姓名
    private String borrwoerName;
    // 借款id
    private Integer loanId;
    // 借款金额
    private BigDecimal money;
    // 借款时间
    private Date loanTimeStr;

    @Override
    public String toString() {
        // 2018-3-5日张三借出500元给刘某。
        return loanTimeStr+"日:"+lenderName+"借出"+money+"元给"+borrwoerName;
    }
}
