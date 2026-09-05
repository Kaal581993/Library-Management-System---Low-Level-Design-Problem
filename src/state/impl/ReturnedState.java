package state.impl;

import entity.Loan;
import entity.LoanState;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ReturnedState implements LoanState {

    @Override
    public void checkout(Loan loan) {
        if (loan != null) {
            loan.setCurrentState(new CheckedOutState());
        }
    }

    @Override
    public void returnBook(Loan loan) {
        if (loan != null) {
            loan.setCurrentState(this);
            loan.setReturnDate(new java.util.Date());
        }
    }

    @Override
    public void renew(Loan loan) {
    }

    @Override
    public double calculateFine(Loan loan) {
        if (loan == null || loan.getDueDate() == null || loan.getReturnDate() == null) {
            return 0.0;
        }
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate().toInstant(), loan.getReturnDate().toInstant());
        return daysLate > 0 ? daysLate * 2.0 : 0.0;
    }

    @Override
    public boolean isOverDue(Loan loan) {
        return false;
    }
}
