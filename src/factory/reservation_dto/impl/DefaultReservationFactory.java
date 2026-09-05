package factory.reservation_dto.impl;


import entity.Reservation;
import factory.ReservationFactory;
import factory.reservation_dto.ReservationRequest;

import java.util.Date;

public class DefaultReservationFactory implements ReservationFactory {

    @Override
    public Reservation createReservation(ReservationRequest request) {
        if (request == null || request.getPatron() == null || request.getBookId() == null) {
            throw new IllegalArgumentException("Patron and book are required to create a reservation");
        }

        return new Reservation.ReservationBuilder(null, false, request.getPatron(), null, 0)
                .setReservationId()
                .setReservationDate(request.getReservationDate() != null ? request.getReservationDate() : new Date())
                .build();
    }
}
