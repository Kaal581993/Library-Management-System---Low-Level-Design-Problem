package validation.loan_validation;

import factory.loan_dto.LoanRequest;

public interface LoanValidationHandler {
    void setNextHandler(LoanValidationHandler nextHandler);

    default LoanValidationHandler setNext(LoanValidationHandler nextHandler) {
        setNextHandler(nextHandler);
        return nextHandler;
    }

    boolean validate(LoanRequest request);
}
