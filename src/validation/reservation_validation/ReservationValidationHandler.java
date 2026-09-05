package validation.reservation_validation;

import factory.reservation_dto.ReservationRequest;

public interface ReservationValidationHandler {
    void setNextHandler(ReservationValidationHandler nextHandler);

    default ReservationValidationHandler setNext(ReservationValidationHandler nextHandler) {
        setNextHandler(nextHandler);
        return nextHandler;
    }

    boolean validate(ReservationRequest request);
}
