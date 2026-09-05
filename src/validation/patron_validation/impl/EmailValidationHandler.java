package validation.patron_validation.impl;

import factory.patron_dto.PatronRequest;
import validation.patron_validation.PatronValidationException;
import validation.patron_validation.PatronValidationHandler;

public class EmailValidationHandler implements PatronValidationHandler {

    private PatronValidationHandler nextHandler;

    @Override
    public void setNextHandler(PatronValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(PatronRequest request) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            throw new PatronValidationException("Email is required");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new PatronValidationException("Invalid email format");
        }
        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
