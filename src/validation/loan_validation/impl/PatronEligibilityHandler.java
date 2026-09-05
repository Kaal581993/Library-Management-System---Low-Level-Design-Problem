package validation.loan_validation.impl;

import factory.loan_dto.LoanRequest;
import service.PatronService;
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;

public class PatronEligibilityHandler implements LoanValidationHandler {

    private final PatronService patronService;
    private LoanValidationHandler nextHandler;

    public PatronEligibilityHandler(PatronService patronService) {
        this.patronService = patronService;
    }

    @Override
    public void setNextHandler(LoanValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(LoanRequest request) {
        if (request == null || request.getPatronId() == null || request.getPatronId().isBlank()) {
            throw new LoanValidationException("Patron ID is required");
        }

        if (patronService.getPatronById(request.getPatronId()) == null) {
            throw new LoanValidationException("Patron not found: " + request.getPatronId());
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
