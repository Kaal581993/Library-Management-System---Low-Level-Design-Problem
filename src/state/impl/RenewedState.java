package state.impl;

import entity.Loan;
import entity.LoanState;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RenewedState implements LoanState {

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
        if (loan == null || loan.getDueDate() == null) {
            return;
        }
        java.util.Date newDueDate = new java.util.Date(loan.getDueDate().toInstant().plus(14, ChronoUnit.DAYS).toMillis());
        loan.setDueDate(newDueDate);
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
        if (loan == null || loan.getDueDate() == null) {
            return false;
        }
        return new java.util.Date().after(loan.getDueDate());
    }
}
