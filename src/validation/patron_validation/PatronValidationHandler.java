package validation.patron_validation;

import factory.patron_dto.PatronRequest;

public interface PatronValidationHandler {
    void setNextHandler(PatronValidationHandler nextHandler);

    default PatronValidationHandler setNext(PatronValidationHandler nextHandler) {
        setNextHandler(nextHandler);
        return nextHandler;
    }

    boolean validate(PatronRequest request);
}
