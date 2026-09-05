package validation.loan_validation.impl;

import entity.Book;
import factory.loan_dto.LoanRequest;
import service.BookService;
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;

public class BookAvailabilityHandler implements LoanValidationHandler {

    private final BookService bookService;
    private LoanValidationHandler nextHandler;

    public BookAvailabilityHandler(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void setNextHandler(LoanValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(LoanRequest request) {
        if (request == null || request.getBookId() == null || request.getBookId().isBlank()) {
            throw new LoanValidationException("Book ID is required");
        }

        Book book = bookService.getBook(request.getBookId());
        if (book == null) {
            throw new LoanValidationException("Book not found: " + request.getBookId());
        }

        if (book.getQuantity() <= 0) {
            throw new LoanValidationException("Book is not available: " + request.getBookId());
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
