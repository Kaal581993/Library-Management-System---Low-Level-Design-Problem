package ui_handlers;

import entity.Loan;
import service.LoanService;
import service.PatronService;
import validation.loan_validation.LoanValidationException;

import java.util.List;
import java.util.Scanner;

public class LoanMenuHandler {
    private final LoanService loanService = LoanService.getInstance();
    private final PatronService patronService = PatronService.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Loan Management ===");
            System.out.println("1. Checkout Book");
            System.out.println("2. Return Book");
            System.out.println("3. Renew Loan");
            System.out.println("4. View Active Loans");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> checkoutBook();
                case "2" -> returnBook();
                case "3" -> renewLoan();
                case "4" -> viewActiveLoans();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void checkoutBook() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        System.out.print("Enter Book ISBN: ");
        String bookId = scanner.nextLine();

        System.out.println("Select Loan Type:");
        System.out.println("1. REGULAR");
        System.out.println("2. REFERENCE");
        System.out.println("3. INTER_LIBRARY");
        System.out.println("4. DIGITAL");
        System.out.print("Enter choice: ");

        String loanTypeChoice = scanner.nextLine();
        entity.LoanType loanType;
        switch (loanTypeChoice) {
            case "1" -> loanType = entity.LoanType.REGULAR;
            case "2" -> loanType = entity.LoanType.REFERENCE;
            case "3" -> loanType = entity.LoanType.INTER_LIBRARY;
            case "4" -> loanType = entity.LoanType.DIGITAL;
            default -> {
                System.out.println("Invalid loan type.");
                return;
            }
        }

        factory.loan_dto.LoanRequest request = new factory.loan_dto.LoanRequest();
        request.setPatronId(patronId);
        request.setBookId(bookId);
        request.setLoanType(loanType);
        java.util.Date checkoutDate = new java.util.Date();
        request.setCheckoutDate(checkoutDate);

        // Calculate due date before validation
        request.setDueDate(calculateDueDate(checkoutDate, loanType));

        try {
            Loan loan = loanService.checkoutBook(request);
            System.out.println("Loan created successfully. Loan ID: " + loan.getLoanId());
            System.out.println("Due Date: " + loan.getDueDate());
        } catch (IllegalArgumentException | LoanValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.print("Enter Loan ID to return: ");
        String loanId = scanner.nextLine();

        try {
            loanService.returnBook(loanId);
            System.out.println("Book returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void renewLoan() {
        System.out.print("Enter Loan ID to renew: ");
        String loanId = scanner.nextLine();

        try {
            loanService.renewLoan(loanId);
            Loan loan = loanService.getLoanById(loanId);
            System.out.println("Loan renewed successfully. New Due Date: " + loan.getDueDate());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewActiveLoans() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine();

        List<Loan> activeLoans = loanService.getActiveLoans(patronId);
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans found for this patron.");
            return;
        }

        System.out.println("\nActive Loans:");
        for (Loan loan : activeLoans) {
            System.out.println("Loan ID: " + loan.getLoanId()
                    + " | Book ID: " + loan.getBookId()
                    + " | Due Date: " + loan.getDueDate()
                    + " | Status: " + loan.getCurrentState().getClass().getSimpleName());
        }
    }

    private java.util.Date calculateDueDate(java.util.Date checkoutDate, entity.LoanType loanType) {
        if (checkoutDate == null || loanType == null) {
            return null;
        }

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(checkoutDate);

        int daysToAdd;
        switch (loanType) {
            case REGULAR -> daysToAdd = 14;
            case REFERENCE -> daysToAdd = 7;
            case INTER_LIBRARY -> daysToAdd = 30;
            case DIGITAL -> daysToAdd = 3;
            default -> daysToAdd = 14;
        }

        calendar.add(java.util.Calendar.DAY_OF_MONTH, daysToAdd);
        return calendar.getTime();
    }
}
