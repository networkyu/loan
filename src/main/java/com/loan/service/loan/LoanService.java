package com.loan.service.loan;

import com.loan.dataobject.Loan;
import com.loan.model.loan.LoanModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanService {
    public List<Loan> getLoansByClientId(Integer clientId);
    public boolean createLoan(LoanModel loanModel);
}
