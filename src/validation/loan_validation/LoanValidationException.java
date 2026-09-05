package validation.loan_validation;

public class LoanValidationException extends RuntimeException {
    public LoanValidationException(String message) {
        super(message);
    }
}
