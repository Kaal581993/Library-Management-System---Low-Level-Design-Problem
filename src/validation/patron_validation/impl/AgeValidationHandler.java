package validation.patron_validation.impl;

import entity.PatronType;
import factory.patron_dto.PatronRequest;
import validation.patron_validation.PatronValidationException;
import validation.patron_validation.PatronValidationHandler;

public class AgeValidationHandler implements PatronValidationHandler {

    private static final int MIN_AGE_FOR_EXTERNAL = 18;
    private PatronValidationHandler nextHandler;

    @Override
    public void setNextHandler(PatronValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(PatronRequest request) {
        if (request == null || request.getPatronType() == null) {
            if (nextHandler != null) {
                return nextHandler.validate(request);
            }
            return true;
        }

        if (request.getPatronType() == PatronType.EXTERNAL) {
            if (request.getPatronId() == null || request.getPatronId().length() < 5) {
                throw new PatronValidationException("External patrons must meet minimum age/ID requirements");
            }
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
