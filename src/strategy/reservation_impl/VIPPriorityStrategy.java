package strategy.reservation_impl;

import entity.Patron;
import entity.Reservation;

import java.util.Comparator;

public class VIPPriorityStrategy implements Comparator<Reservation> {

    @Override
    public int compare(Reservation r1, Reservation r2) {
        int priority1 = getPriorityWeight(r1.getPatron());
        int priority2 = getPriorityWeight(r2.getPatron());
        return Integer.compare(priority2, priority1);
    }

    private int getPriorityWeight(Patron patron) {
        if (patron == null || patron.getPatronType() == null) {
            return 0;
        }
        return switch (patron.getPatronType()) {
            case VIP, HEADMASTER, ADMIN -> 3;
            case PROFESSOR, FACULTY, HOD -> 2;
            case STUDENT -> 1;
            case REGULAR, GUEST -> 0;
            default -> 0;
        };
    }
}
