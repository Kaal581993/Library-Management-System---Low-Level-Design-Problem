package validation.reservation_validation.impl;

import entity.Patron;
import factory.reservation_dto.ReservationRequest;
import service.LoanService;
import validation.reservation_validation.ReservationValidationException;
import validation.reservation_validation.ReservationValidationHandler;

public class BorrowingLimitValidationHandler implements ReservationValidationHandler {

    private static final int MAX_ACTIVE_LOANS = 5;
    private final LoanService loanService;
    private ReservationValidationHandler nextHandler;

    public BorrowingLimitValidationHandler(LoanService loanService) {
        this.loanService = loanService;
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

        int activeLoans = loanService.getActiveLoans(request.getPatron().getPatronId()).size();
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new ReservationValidationException(
                    "Patron has reached the maximum number of active loans (" + MAX_ACTIVE_LOANS + ")"
            );
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
