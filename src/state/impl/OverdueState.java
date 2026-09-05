package state.impl;

import entity.Loan;
import entity.LoanState;

public class OverdueState implements LoanState {

    @Override
    public void checkout(Loan loan) {
        throw new IllegalStateException("Cannot checkout an overdue loan. Return or renew it first.");
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
        throw new IllegalStateException("Cannot renew an overdue loan");
    }

    @Override
    public boolean isOverDue(Loan loan) {
        return true;
    }
}
