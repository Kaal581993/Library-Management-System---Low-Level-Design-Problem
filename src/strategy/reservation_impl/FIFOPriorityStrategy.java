package strategy.reservation_impl;

import entity.Patron;
import entity.Reservation;

import java.util.Comparator;

public class FIFOPriorityStrategy implements Comparator<Reservation> {

    @Override
    public int compare(Reservation r1, Reservation r2) {
        return r1.getReservationDate().compareTo(r2.getReservationDate());
    }
}
