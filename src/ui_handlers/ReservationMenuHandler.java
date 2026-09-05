package ui_handlers;

import entity.Patron;
import factory.reservation_dto.ReservationRequest;
import service.BookService;
import service.PatronService;
import service.ReservationService;

import java.util.List;
import java.util.Scanner;

public class ReservationMenuHandler {
    private final ReservationService reservationService = ReservationService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private final PatronService patronService = PatronService.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Reservation Management ===");
            System.out.println("1. Create Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. View My Reservations");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createReservation();
                case "2" -> cancelReservation();
                case "3" -> viewReservations();
                case "4" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createReservation() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        System.out.print("Enter Book ISBN to reserve: ");
        String bookId = scanner.nextLine();

        Patron patron = patronService.getPatronById(patronId);
        if (patron == null) {
            System.out.println("Patron not found.");
            return;
        }

        ReservationRequest request = new ReservationRequest();
        request.setPatron(patron);
        request.setBookId(bookId);
        request.setReservationDate(new java.util.Date());

        try {
            var reservation = reservationService.createReservation(request);
            System.out.println("Reservation created successfully. Reservation ID: " + reservation.getReservationId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        String reservationId = scanner.nextLine();

        try {
            reservationService.cancelReservation(reservationId);
            System.out.println("Reservation cancelled successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewReservations() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        List<entity.Reservation> reservations = reservationService.getReservationsByPatron(patronId);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this patron.");
            return;
        }

        System.out.println("\nYour Reservations:");
        for (entity.Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId()
                    + " | Book: " + (reservation.getBook() != null ? reservation.getBook().getTitle() : "N/A")
                    + " | Status: " + reservation.getReservationStatus()
                    + " | Date: " + reservation.getReservationDate());
        }
    }
}
