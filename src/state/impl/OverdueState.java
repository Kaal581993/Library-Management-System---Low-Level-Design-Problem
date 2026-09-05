package state.impl;

import entity.Loan;
import entity.LoanState;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class OverdueState implements LoanState {

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
        throw new IllegalStateException("Cannot renew an overdue loan");
    }

    @Override
    public boolean isOverDue(Loan loan) {
        return true;
    }
}
