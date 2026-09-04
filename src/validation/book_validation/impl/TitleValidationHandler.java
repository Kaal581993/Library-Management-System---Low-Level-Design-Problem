package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class TitleValidationHandler implements BookValidationHandler {
    private BookValidationHandler nextHandler;

    @Override
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(BookRequest request) {
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new ValidationException("Error: Title of the Book is required");
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return true;
    }
}
