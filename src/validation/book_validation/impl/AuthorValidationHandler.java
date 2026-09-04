package validation.book_validation.impl;

import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;


public class AuthorValidationHandler implements BookValidationHandler {
        private BookValidationHandler nextHandler;
        public void setNextHandler(BookValidationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
        public boolean handle(BookSearchRequest bookSearchRequest) throws ValidationException {
            if (bookSearchRequest.getAuthor() == null) {
                System.out.println("Error: Author of the Book  is required");
                throw new ValidationException("Error: Author of the Book  is required");
            }
            System.out.println("Author validated: " + bookSearchRequest.getAuthor());
            System.out.println("Book available: " + bookSearchRequest.getIsbn() +","+bookSearchRequest.getTitle()+ " by " + bookSearchRequest.getAuthor());
            return nextHandler != null && nextHandler.handle(bookSearchRequest);
        }
    }

