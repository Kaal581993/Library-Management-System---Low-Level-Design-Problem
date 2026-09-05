package validation.patron_validation.impl;

import factory.patron_dto.PatronRequest;
import validation.patron_validation.PatronValidationException;
import validation.patron_validation.PatronValidationHandler;

public class IdValidationHandler implements PatronValidationHandler {

    private PatronValidationHandler nextHandler;

    @Override
    public void setNextHandler(PatronValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(PatronRequest request) {
        if (request == null || request.getPatronId() == null || request.getPatronId().isBlank()) {
            throw new PatronValidationException("Patron ID is required");
        }
        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
