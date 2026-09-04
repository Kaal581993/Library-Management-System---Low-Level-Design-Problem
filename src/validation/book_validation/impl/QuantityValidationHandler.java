package validation.book_validation.impl;

import entity.BookStatus;
import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;

public class QuantityValidationHandler implements BookValidationHandler {
    private BookValidationHandler nextHandler;
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public boolean handle(BookSearchRequest bookSearchRequest) {
        if (
                bookSearchRequest.getQuantity() < 0
                || bookSearchRequest.getBookStatus() == BookStatus.LOST
                ||bookSearchRequest.getBookStatus() == BookStatus.DAMAGED
                ||bookSearchRequest.getBookStatus() == BookStatus.UNAVAILABLE
        ) {
            System.out.println("Error: Book quantity cannot be negative or the book is not available");
            return false;
        }
        System.out.println("Book available: " + bookSearchRequest.getIsbn() +","+bookSearchRequest.getTitle()+ " by " + bookSearchRequest.getAuthor());
        return nextHandler != null && nextHandler.handle(bookSearchRequest);
    }
}
