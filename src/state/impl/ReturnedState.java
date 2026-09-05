package state.impl;

import entity.Loan;
import entity.LoanState;

public class ReturnedState implements LoanState {

    @Override
    public void checkout(Loan loan) {
        throw new IllegalStateException("Cannot checkout a returned loan. Create a new loan instead.");
    }

    @Override
    public void returnBook(Loan loan) {
        throw new IllegalStateException("Book has already been returned");
    }

    @Override
    public void renew(Loan loan) {
    }

    @Override
    public boolean isOverDue(Loan loan) {
        return false;
    }
}
