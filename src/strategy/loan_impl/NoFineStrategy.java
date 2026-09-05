package strategy.loan_impl;

import entity.Loan;
import strategy.FineCalculationStrategy;

public class NoFineStrategy implements FineCalculationStrategy {


    private double fineAmount;
    @Override
    public double calculateFine(Loan loan) {
        fineAmount = loan.getDueDate().before(loan.getReturnDate()) ? 0.0 : 0.0;
        return fineAmount;
    }
}
