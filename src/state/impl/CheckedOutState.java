package state.impl;

import entity.Loan;
import entity.LoanState;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CheckedOutState implements LoanState {

    @Override
    public void checkout(Loan loan) {
        if (loan != null) {
            loan.setCurrentState(this);
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
        if (loan == null || loan.getDueDate() == null) {
            return;
        }
        java.util.Date newDueDate = new java.util.Date(loan.getDueDate().toInstant().plus(14, ChronoUnit.DAYS).toEpochMilli());
        loan.setDueDate(newDueDate);
    }

    @Override
    public boolean isOverDue(Loan loan) {
        if (loan == null || loan.getDueDate() == null) {
            return false;
        }
        return new java.util.Date().after(loan.getDueDate());
    }
}
