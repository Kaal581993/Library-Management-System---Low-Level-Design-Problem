package strategy.reservation_impl;

import entity.Patron;
import entity.Reservation;

import java.util.Comparator;

public class CategoryPriorityStrategy implements Comparator<Reservation> {

    @Override
    public int compare(Reservation r1, Reservation r2) {
        int priority1 = getCategoryWeight(r1.getPatron());
        int priority2 = getCategoryWeight(r2.getPatron());
        return Integer.compare(priority2, priority1);
    }

    private int getCategoryWeight(Patron patron) {
        if (patron == null || patron.getPatronType() == null) {
            return 0;
        }
        return switch (patron.getPatronType()) {
            case FACULTY, HOD, HEADMASTER -> 3;
            case STUDENT, PROFESSOR -> 2;
            case REGULAR -> 1;
            case GUEST, ADMIN -> 0;
            default -> 0;
        };
    }
}
