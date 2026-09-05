package factory;

import entity.Patron;
import entity.Reservation;
import factory.reservation_dto.ReservationRequest;

import java.util.Date;

public interface ReservationFactory {
    Reservation createReservation(ReservationRequest request);
}
