package validation.book_validation.impl;

import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class RequiredFieldsValidationHandler implements BookValidationHandler {

    private BookValidationHandler nextHandler;

    @Override
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(BookSearchRequest bookSearchRequest) throws ValidationException {
        if (bookSearchRequest == null) {
            throw new ValidationException("Book request is required");
        }
        if (bookSearchRequest.getTitle() == null || bookSearchRequest.getTitle().isBlank()) {
            throw new ValidationException("Title of the Book is required");
        }
        if (bookSearchRequest.getAuthor() == null || bookSearchRequest.getAuthor().isBlank()) {
            throw new ValidationException("Author of the Book is required");
        }
        if (bookSearchRequest.getIsbn() == null || bookSearchRequest.getIsbn().isBlank()) {
            throw new ValidationException("ISBN of the Book is required");
        }
        if (nextHandler != null) {
            return nextHandler.handle(bookSearchRequest);
        }
        return true;
    }
}
