package validation.patron_validation.impl;

import factory.patron_dto.PatronRequest;
import validation.patron_validation.PatronValidationException;
import validation.patron_validation.PatronValidationHandler;

public class NameValidationHandler implements PatronValidationHandler {

    private PatronValidationHandler nextHandler;

    @Override
    public void setNextHandler(PatronValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(PatronRequest request) {
        if (request == null || request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new PatronValidationException("First name is required");
        }
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new PatronValidationException("Last name is required");
        }
        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
