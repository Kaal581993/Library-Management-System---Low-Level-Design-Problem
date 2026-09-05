package validation.loan_validation.impl;

import entity.Patron;
import entity.PatronStatus;
import factory.loan_dto.LoanRequest;
import service.LoanService;
import service.PatronService;
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;

public class LoanLimitHandler implements LoanValidationHandler {

    private static final int DEFAULT_MAX_ACTIVE_LOANS = 5;
    private final LoanService loanService;
    private final PatronService patronService;
    private LoanValidationHandler nextHandler;

    public LoanLimitHandler(LoanService loanService, PatronService patronService) {
        this.loanService = loanService;
        this.patronService = patronService;
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

        Patron patron = patronService.getPatronById(request.getPatronId());
        int maxLoans = (patron != null && patron.getMaxBorrowingLimit() > 0)
                ? patron.getMaxBorrowingLimit()
                : DEFAULT_MAX_ACTIVE_LOANS;

        long activeLoans = loanService.getActiveLoans(request.getPatronId()).size();
        if (activeLoans >= maxLoans) {
            throw new LoanValidationException(
                    "Patron has reached the maximum number of active loans (" + maxLoans + ")"
            );
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
