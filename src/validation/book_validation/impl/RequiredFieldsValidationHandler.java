package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class RequiredFieldsValidationHandler implements BookValidationHandler {

    private BookValidationHandler nextHandler;

    @Override
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(BookRequest request) {
        if (request == null) {
            throw new ValidationException("Book request is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ValidationException("Title of the Book is required");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ValidationException("Author of the Book is required");
        }
        if (request.getIsbn() == null || request.getIsbn().isBlank()) {
            throw new ValidationException("ISBN of the Book is required");
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return true;
    }
}
