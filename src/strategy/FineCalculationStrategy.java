package strategy;

import entity.Loan;

public interface FineCalculationStrategy {
    public double calculateFine(Loan loan);
}
