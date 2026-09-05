package validation.patron_validation.impl;

import factory.patron_dto.PatronRequest;
import validation.patron_validation.PatronValidationException;
import validation.patron_validation.PatronValidationHandler;

public class BorrowingLimitValidationHandler implements PatronValidationHandler {

    private PatronValidationHandler nextHandler;

    @Override
    public void setNextHandler(PatronValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(PatronRequest request) {
        if (request == null || request.getPatronType() == null) {
            throw new PatronValidationException("Patron type is required for borrowing limit validation");
        }
        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
