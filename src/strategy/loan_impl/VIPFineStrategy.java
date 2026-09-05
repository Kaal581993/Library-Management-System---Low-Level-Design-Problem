package strategy.loan_impl;

import entity.Loan;
import strategy.FineCalculationStrategy;

import java.time.temporal.ChronoUnit;


public class VIPFineStrategy implements FineCalculationStrategy {
    private static final double DAILY_FINE_RATE = 2.0; // standard rate
    private static final double VIP_DISCOUNT = 0.5;     // 50% off for VIPs

    @Override
    public double calculateFine(Loan loan) {
        if (loan.getReturnDate() == null) {
            return 0.0;
        }

        long daysLate = ChronoUnit.DAYS.between(
                loan.getDueDate().toInstant(),
                loan.getReturnDate().toInstant()
        );

        if(daysLate<=15){
            return 0.0;
            // 15 days waver for VIP
        }



        return daysLate * DAILY_FINE_RATE * (1 - VIP_DISCOUNT);
    }
}