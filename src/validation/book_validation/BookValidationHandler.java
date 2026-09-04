package validation.book_validation;

import java.beans.ExceptionListener;

public interface BookValidationHandler {
    void setNextHandler(BookValidationHandler nextHandler);
    boolean handle(BookSearchRequest request) throws ValidationException;


}
