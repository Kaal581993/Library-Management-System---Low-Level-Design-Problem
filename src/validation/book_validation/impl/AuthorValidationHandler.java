package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

public class AuthorValidationHandler implements BookValidationHandler {
        private BookValidationHandler nextHandler;
        public void setNextHandler(BookValidationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
        public boolean handle(BookRequest request) {
            if (request.getAuthor() == null || request.getAuthor().isBlank()) {
                throw new ValidationException("Error: Author of the Book is required");
            }
            if (nextHandler != null) {
                return nextHandler.handle(request);
            }
            return true;
        }
    }
