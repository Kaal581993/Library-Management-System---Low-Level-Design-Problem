package factory.reservation_dto.impl;

import entity.Patron;
import entity.Reservation;
import entity.ReservationStatus;
import factory.ReservationFactory;
import factory.reservation_dto.ReservationRequest;

import java.util.Date;
import java.util.UUID;

public class DefaultReservationFactory implements ReservationFactory {

    @Override
    public Reservation createReservation(ReservationRequest request) {
        if (request == null || request.getPatron() == null || request.getBookId() == null) {
            throw new IllegalArgumentException("Patron and book are required to create a reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setReservationId(UUID.randomUUID().toString());
        reservation.setReservationDate(request.getReservationDate() != null ? request.getReservationDate() : new Date());
        reservation.setPatron(request.getPatron());
        reservation.setFulfilled(false);
        reservation.setReservationStatus(ReservationStatus.PENDING);

        return reservation;
    }
}
