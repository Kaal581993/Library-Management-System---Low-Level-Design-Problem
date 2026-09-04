package validation.book_validation.impl;

import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class TitleValidationHandler implements BookValidationHandler {
    private BookValidationHandler nextHandler;

    @Override
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(validation.book_validation.BookSearchRequest bookSearchRequest) throws ValidationException {
        if (bookSearchRequest.getTitle() == null || bookSearchRequest.getTitle().isEmpty()) {
            throw new ValidationException("Error: Title of the Book is required");
        }
        System.out.println("Title validated: " + bookSearchRequest.getTitle());
        System.out.println("Book available: " + bookSearchRequest.getIsbn() + "," + bookSearchRequest.getTitle() + " by " + bookSearchRequest.getAuthor());
        return nextHandler != null && nextHandler.handle(bookSearchRequest);
    }
}
