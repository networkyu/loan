package com.loan.service.loan;

import com.loan.dao.LoanMapper;
import com.loan.dataobject.Client;
import com.loan.dataobject.Loan;
import com.loan.model.client.ClientModel;
import com.loan.model.loan.LoanModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImp implements LoanService {

    @Autowired
    LoanMapper loanMapper;
    @Override
    public boolean createLoan(LoanModel loanModel) {
        Loan loan = convertFromModel(loanModel);
        if(loanMapper.insertSelective(loan) < 1){
            return false;
        }
        return true;
    }
    private Loan convertFromModel(LoanModel loanModel){
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanModel,loan);
        return loan;
    }

    @Override
    public List<Loan> getLoansByClientId(Integer clientId) {
        List<Loan> loans = loanMapper.selectLoansByClientId(clientId);
        return loans;
    }
}
