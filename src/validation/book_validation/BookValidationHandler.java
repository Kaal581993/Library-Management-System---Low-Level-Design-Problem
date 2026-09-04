package validation.book_validation;

public interface BookValidationHandler {
    void setNextHandler(BookValidationHandler nextHandler);

    default BookValidationHandler setNext(BookValidationHandler nextHandler) {
        setNextHandler(nextHandler);
        return nextHandler;
    }

    boolean handle(BookSearchRequest request);
}
