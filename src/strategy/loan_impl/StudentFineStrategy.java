package strategy.loan_impl;

import entity.Loan;
import strategy.FineCalculationStrategy;

import java.time.temporal.ChronoUnit;

public class StudentFineStrategy implements FineCalculationStrategy {
    private static final double DAILY_FINE_RATE = 1.0; // standard rate
    private static final double STUDENT_DISCOUNT = 0.5;     // 50% off for STUDENT

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

        return daysLate * DAILY_FINE_RATE * (1 - STUDENT_DISCOUNT);
    }
}
