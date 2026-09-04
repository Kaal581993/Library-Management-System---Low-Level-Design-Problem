package validation.book_validation.impl;

import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class ISBNValidationHandler implements BookValidationHandler {

    private BookValidationHandler nextHandler;
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public boolean handle(BookSearchRequest bookSearchRequest) throws ValidationException {
        if (bookSearchRequest.getIsbn() == null || bookSearchRequest.getIsbn().isBlank() || bookSearchRequest.getIsbn().isEmpty()) {
            System.out.println("Error: ISBN number of the Book  is required");
            return false;
        }
        String isbn = bookSearchRequest.getIsbn().replaceAll("[- ]", "");
        if (!isbn.matches("\\d{9}[\\dX]|\\d{13}")) {
            System.out.println("Error: Invalid ISBN number format");
            throw new ValidationException("Error: Invalid ISBN number format");
        }
        System.out.println("ISBN validated: " + isbn);
        System.out.println("Book available: " + isbn +","+bookSearchRequest.getTitle()+ " by " + bookSearchRequest.getAuthor());
        return nextHandler != null && nextHandler.handle(bookSearchRequest);
    }
}
