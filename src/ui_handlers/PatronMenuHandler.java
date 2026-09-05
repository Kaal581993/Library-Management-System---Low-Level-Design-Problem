package ui_handlers;

import entity.Patron;
import factory.patron_dto.PatronRequest;
import service.LoanService;
import service.PatronService;
import strategy.PatronSearchType;

import java.util.List;
import java.util.Scanner;

public class PatronMenuHandler {
    private final PatronService patronService = PatronService.getInstance();
    private final LoanService loanService = LoanService.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Patron Management ===");
            System.out.println("1. Register New Patron");
            System.out.println("2. Search Patrons");
            System.out.println("3. View Patron Details");
            System.out.println("4. Update Patron Status");
            System.out.println("5. Pay Fine");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> registerPatron();
                case "2" -> searchPatrons();
                case "3" -> viewPatronDetails();
                case "4" -> updatePatronStatus();
                case "5" -> payFine();
                case "6" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerPatron() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("Select Patron Type:");
        System.out.println("1. STUDENT");
        System.out.println("2. FACULTY");
        System.out.println("3. STAFF");
        System.out.println("4. EXTERNAL");
        System.out.print("Enter choice: ");

        String typeChoice = scanner.nextLine();
        entity.PatronType patronType;
        switch (typeChoice) {
            case "1" -> patronType = entity.PatronType.STUDENT;
            case "2" -> patronType = entity.PatronType.FACULTY;
            case "3" -> patronType = entity.PatronType.STAFF;
            case "4" -> patronType = entity.PatronType.GUEST;
            default -> {
                System.out.println("Invalid patron type.");
                return;
            }
        }

        PatronRequest request = new PatronRequest();
        request.setPatronId(patronId);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPatronType(patronType);

        try {
            Patron patron = patronService.addPatron(request);
            System.out.println("Patron registered successfully. Patron ID: " + patron.getPatronId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchPatrons() {
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. ID");
        System.out.println("4. Fine Status");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();
        PatronSearchType searchType;
        String query;

        switch (choice) {
            case "1" -> {
                searchType = PatronSearchType.NAME;
                System.out.print("Enter name: ");
                query = scanner.nextLine();
            }
            case "2" -> {
                searchType = PatronSearchType.EMAIL;
                System.out.print("Enter email: ");
                query = scanner.nextLine();
            }
            case "3" -> {
                searchType = PatronSearchType.ID;
                System.out.print("Enter patron ID: ");
                query = scanner.nextLine();
            }
            case "4" -> {
                searchType = PatronSearchType.FINE_STATUS;
                System.out.print("Enter fine status (CLEAR, PENDING, OVERDUE): ");
                query = scanner.nextLine();
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        try {
            List<Patron> results = patronService.searchPatrons(searchType, query);
            if (results.isEmpty()) {
                System.out.println("No patrons found.");
            } else {
                for (Patron patron : results) {
                    System.out.println(patron.getPatronId() + " | " + patron.getFirstName() + " " + patron.getLastName()
                            + " | " + patron.getEmail() + " | " + patron.getPatronStatus());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewPatronDetails() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        Patron patron = patronService.getPatronById(patronId);
        if (patron == null) {
            System.out.println("Patron not found.");
            return;
        }

        System.out.println("\n=== Patron Details ===");
        System.out.println("ID: " + patron.getPatronId());
        System.out.println("Name: " + patron.getFirstName() + " " + patron.getLastName());
        System.out.println("Email: " + patron.getEmail());
        System.out.println("Type: " + patron.getPatronType());
        System.out.println("Status: " + patron.getPatronStatus());
        System.out.println("Fine Amount: " + patron.getFineAmount());
        System.out.println("Fine Status: " + patron.getFineStatus());
        System.out.println("Borrowing Limit: " + patron.getMaxBorrowingLimit());
        System.out.println("Loan Period: " + patron.getLoanPeriodDays() + " days");
        System.out.println("Fine Rate: " + patron.getFineRatePerDay() + " per day");

        System.out.println("\n--- Current Loans ---");
        List<entity.Loan> currentLoans = patronService.getCurrentLoans(patronId);
        if (currentLoans.isEmpty()) {
            System.out.println("No active loans.");
        } else {
            for (entity.Loan loan : currentLoans) {
                System.out.println("Loan ID: " + loan.getLoanId() + " | Due: " + loan.getDueDate());
            }
        }

        System.out.println("\n--- Reservations ---");
        List<entity.Reservation> reservations = patronService.getReservations(patronId);
        if (reservations.isEmpty()) {
            System.out.println("No reservations.");
        } else {
            for (entity.Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId()
                        + " | Status: " + reservation.getReservationStatus()
                        + " | Date: " + reservation.getReservationDate());
            }
        }
    }

    private void updatePatronStatus() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        Patron patron = patronService.getPatronById(patronId);
        if (patron == null) {
            System.out.println("Patron not found.");
            return;
        }

        System.out.println("Current Status: " + patron.getPatronStatus());
        System.out.println("Select New Status:");
        System.out.println("1. ACTIVE");
        System.out.println("2. SUSPENDED");
        System.out.println("3. INACTIVE");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                patronService.activatePatron(patronId);
                System.out.println("Patron activated.");
            }
            case "2" -> {
                System.out.print("Enter suspension reason: ");
                String reason = scanner.nextLine();
                patronService.suspendPatron(patronId, reason);
                System.out.println("Patron suspended.");
            }
            case "3" -> {
                patronService.deactivatePatron(patronId);
                System.out.println("Patron deactivated.");
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void payFine() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        Patron patron = patronService.getPatronById(patronId);
        if (patron == null) {
            System.out.println("Patron not found.");
            return;
        }

        System.out.println("Current Fine: " + patron.getFineAmount());
        System.out.print("Enter payment amount: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            patronService.payFine(patronId, amount);
            System.out.println("Payment processed. Remaining fine: " + patronService.calculateFine(patronId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
