package validation.book_validation.impl;

import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;


    public class AuthorValidationHandler implements BookValidationHandler {
        private BookValidationHandler nextHandler;
        public void setNextHandler(BookValidationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
        public boolean handle(BookSearchRequest bookSearchRequest) {
            if (bookSearchRequest.getAuthor() == null) {
                System.out.println("Error: Author of the Book  is required");
                return false;
            }
            System.out.println("Author validated: " + bookSearchRequest.getAuthor());
            System.out.println("Book available: " + bookSearchRequest.getIsbn() +","+bookSearchRequest.getTitle()+ " by " + bookSearchRequest.getAuthor());
            return nextHandler != null && nextHandler.handle(bookSearchRequest);
        }
    }

