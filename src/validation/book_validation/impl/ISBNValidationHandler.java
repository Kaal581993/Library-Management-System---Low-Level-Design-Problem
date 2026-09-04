package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class ISBNValidationHandler implements BookValidationHandler {

    private BookValidationHandler nextHandler;
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public boolean handle(BookRequest request) {
        if (request.getIsbn() == null || request.getIsbn().isBlank() || request.getIsbn().isEmpty()) {
            throw new ValidationException("Error: ISBN number of the Book is required");
        }
        String isbn = request.getIsbn().replaceAll("[- ]", "");
        if (!isbn.matches("\\d{9}[\\dX]|\\d{13}")) {
            throw new ValidationException("Error: Invalid ISBN number format");
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return true;
    }
}
