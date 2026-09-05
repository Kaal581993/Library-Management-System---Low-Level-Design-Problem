package validation.reservation_validation.impl;

import entity.Patron;
import factory.reservation_dto.ReservationRequest;
import service.ReservationService;
import validation.reservation_validation.ReservationValidationException;
import validation.reservation_validation.ReservationValidationHandler;

public class DuplicateReservationValidationHandler implements ReservationValidationHandler {

    private final ReservationService reservationService;
    private ReservationValidationHandler nextHandler;

    public DuplicateReservationValidationHandler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void setNextHandler(ReservationValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(ReservationRequest request) {
        if (request == null || request.getPatron() == null || request.getBookId() == null) {
            throw new ReservationValidationException("Patron and book are required");
        }

        boolean hasExisting = reservationService.getReservationsByPatron(request.getPatron().getPatronId()).stream()
                .anyMatch(reservation -> reservation.getBook().getIsbn().equals(request.getBookId())
                        && reservation.getReservationStatus() == entity.ReservationStatus.PENDING);

        if (hasExisting) {
            throw new ReservationValidationException("Patron already has an active reservation for this book");
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
