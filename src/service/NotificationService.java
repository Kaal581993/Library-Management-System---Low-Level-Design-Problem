package service;

import entity.Reservation;

public class NotificationService {

    private static volatile NotificationService instance;

    private NotificationService() {
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            synchronized (NotificationService.class) {
                if (instance == null) {
                    instance = new NotificationService();
                }
            }
        }
        return instance;
    }

    public void sendReservationNotification(Reservation reservation) {
        if (reservation == null || reservation.getPatron() == null) {
            return;
        }
        System.out.println("Notification sent to " + reservation.getPatron().getUserName()
                + " for reservation " + reservation.getReservationId());
    }
}
