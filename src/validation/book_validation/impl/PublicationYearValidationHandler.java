package validation.book_validation.impl;

import factory.book_dto.BookRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;

import java.time.Year;

public class PublicationYearValidationHandler implements BookValidationHandler {

    private static final int MIN_YEAR = 1000;
    private static final int MAX_YEAR = Year.now().getValue() + 1;

    private BookValidationHandler nextHandler;

    @Override
    public void setNextHandler(BookValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(BookRequest request) {
        int year = request.getYear();
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new ValidationException(
                    "Publication year must be between " + MIN_YEAR + " and " + MAX_YEAR);
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return true;
    }
}
