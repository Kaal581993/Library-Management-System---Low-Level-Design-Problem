package validation.reservation_validation.impl;

import entity.Book;
import factory.reservation_dto.ReservationRequest;
import service.BookService;
import validation.reservation_validation.ReservationValidationException;
import validation.reservation_validation.ReservationValidationHandler;

public class BookAvailabilityValidationHandler implements ReservationValidationHandler {

    private final BookService bookService;
    private ReservationValidationHandler nextHandler;

    public BookAvailabilityValidationHandler(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void setNextHandler(ReservationValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(ReservationRequest request) {
        if (request == null || request.getBookId() == null || request.getBookId().isBlank()) {
            throw new ReservationValidationException("Book ID is required");
        }

        Book book = bookService.getBook(request.getBookId());
        if (book == null) {
            throw new ReservationValidationException("Book not found: " + request.getBookId());
        }

        if (book.getQuantity() > 0) {
            throw new ReservationValidationException("Book is available for checkout, no need to reserve");
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
