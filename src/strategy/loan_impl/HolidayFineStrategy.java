package strategy.loan_impl;

import entity.Loan;
import strategy.FineCalculationStrategy;

import java.time.temporal.ChronoUnit;

public class HolidayFineStrategy implements FineCalculationStrategy {
    private static final double DAILY_FINE_RATE = 1.0; // standard rate
private static final double HOLIDAY_DISCOUNT = 1.0;

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

        return daysLate * DAILY_FINE_RATE * (1 - HOLIDAY_DISCOUNT);
    }
}
