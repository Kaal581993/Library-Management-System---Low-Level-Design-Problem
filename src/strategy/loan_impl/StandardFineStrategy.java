package strategy.loan_impl;

import entity.Loan;
import strategy.FineCalculationStrategy;

import java.time.temporal.ChronoUnit;

public class StandardFineStrategy implements FineCalculationStrategy {
    private static final double DAILY_FINE_RATE = 5.0; // standard rate


    @Override
    public double calculateFine(Loan loan) {
        if (loan.getReturnDate() == null) {
            return 0.0;
        }

        long daysLate = ChronoUnit.DAYS.between(
                loan.getDueDate().toInstant(),
                loan.getReturnDate().toInstant()
        );

        if (daysLate <= 0) {
            return 0.0;
        }

        return daysLate * DAILY_FINE_RATE;
    }
}
