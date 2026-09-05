package state.impl;

import entity.Loan;
import entity.LoanState;

public class ReservedState implements LoanState {

    @Override
    public void checkout(Loan loan) {
        if (loan != null) {
            loan.setCurrentState(new CheckedOutState());
        }
    }

    @Override
    public void returnBook(Loan loan) {
        if (loan != null) {
            loan.setCurrentState(new ReturnedState());
            loan.setReturnDate(new java.util.Date());
        }
    }

    @Override
    public void renew(Loan loan) {
        throw new IllegalStateException("Cannot renew a reserved loan");
    }

    @Override
    public boolean isOverDue(Loan loan) {
        return false;
    }
}
