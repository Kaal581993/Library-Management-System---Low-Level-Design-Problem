package validation.reservation_validation.impl;

import entity.Patron;
import factory.reservation_dto.ReservationRequest;
import service.PatronService;
import validation.reservation_validation.ReservationValidationException;
import validation.reservation_validation.ReservationValidationHandler;

public class PatronEligibilityValidationHandler implements ReservationValidationHandler {

    private final PatronService patronService;
    private ReservationValidationHandler nextHandler;

    public PatronEligibilityValidationHandler(PatronService patronService) {
        this.patronService = patronService;
    }

    @Override
    public void setNextHandler(ReservationValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(ReservationRequest request) {
        if (request == null || request.getPatron() == null) {
            throw new ReservationValidationException("Patron is required");
        }

        Patron patron = patronService.getPatronById(request.getPatron().getPatronId());
        if (patron == null) {
            throw new ReservationValidationException("Patron not found");
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
