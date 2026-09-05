package validation.loan_validation.impl;

import factory.loan_dto.LoanRequest;
import service.LoanService;
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;

public class LoanLimitHandler implements LoanValidationHandler {

    private static final int MAX_ACTIVE_LOANS = 5;
    private final LoanService loanService;
    private LoanValidationHandler nextHandler;

    public LoanLimitHandler(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public void setNextHandler(LoanValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(LoanRequest request) {
        if (request == null || request.getPatronId() == null) {
            throw new LoanValidationException("Patron ID is required for loan limit check");
        }

        long activeLoans = loanService.getActiveLoans(request.getPatronId()).size();
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new LoanValidationException(
                    "Patron has reached the maximum number of active loans (" + MAX_ACTIVE_LOANS + ")"
            );
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
