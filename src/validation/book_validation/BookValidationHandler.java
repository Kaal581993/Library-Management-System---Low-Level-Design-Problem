package validation.book_validation;

import factory.book_dto.BookRequest;

public interface BookValidationHandler {
    void setNextHandler(BookValidationHandler nextHandler);

    default BookValidationHandler setNext(BookValidationHandler nextHandler) {
        setNextHandler(nextHandler);
        return nextHandler;
    }

    boolean handle(BookRequest request);
}
