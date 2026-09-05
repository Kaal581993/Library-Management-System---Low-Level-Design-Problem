package factory;

import entity.Loan;
import factory.loan_dto.LoanRequest;

public interface LoanFactory {
   public Loan createLoan(LoanRequest request);
    public Loan createRegularLoan(LoanRequest request);
    public Loan createReferenceLoan(LoanRequest request);
    public Loan createInterLibraryLoan(LoanRequest request);
}
