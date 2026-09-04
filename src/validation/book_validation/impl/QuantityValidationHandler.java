package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class QuantityValidationHandler implements BookValidationHandler {
    private BookValidationHandler nextHandler;
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public boolean handle(BookRequest request) {
        if (request.getQuantity() < 0) {
            throw new ValidationException("Error: Book quantity cannot be negative");
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return true;
    }
}
