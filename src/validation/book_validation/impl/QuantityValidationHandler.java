package validation.book_validation.impl;

import entity.BookStatus;
import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class QuantityValidationHandler implements BookValidationHandler {
    private BookValidationHandler nextHandler;
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public boolean handle(BookSearchRequest bookSearchRequest) throws ValidationException {
        if (
                bookSearchRequest.getQuantity() < 0
                || bookSearchRequest.getBookStatus() == BookStatus.LOST
                ||bookSearchRequest.getBookStatus() == BookStatus.DAMAGED
                ||bookSearchRequest.getBookStatus() == BookStatus.UNAVAILABLE
        ) {
            throw new ValidationException("Error: Book quantity cannot be negative or the book is not available");
        }
        System.out.println("Book available: " + bookSearchRequest.getIsbn() +","+bookSearchRequest.getTitle()+ " by " + bookSearchRequest.getAuthor());
        return nextHandler != null && nextHandler.handle(bookSearchRequest);
    }
}
