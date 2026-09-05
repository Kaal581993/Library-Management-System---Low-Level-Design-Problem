package ui_handlers;

import entity.Loan;
import factory.loan_dto.LoanRequest;
import service.LoanService;
import service.PatronService;
import state.LoanState;

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

        LoanRequest request = new LoanRequest();
        request.setPatronId(patronId);
        request.setBookId(bookId);
        request.setLoanType(loanType);
        request.setCheckoutDate(new java.util.Date());

        try {
            Loan loan = loanService.checkoutBook(request);
            System.out.println("Loan created successfully. Loan ID: " + loan.getLoanId());
            System.out.println("Due Date: " + loan.getDueDate());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.print("Enter Loan ID to return: ");
        String loanId = scanner.nextLine();

        Loan loan = loanService.getLoanById(loanId);
        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        if (loan.getReturnDate() != null) {
            System.out.println("This book has already been returned.");
            return;
        }

        loan.setReturnDate(new java.util.Date());
        LoanState currentState = loan.getCurrentState();
        if (currentState != null) {
            currentState.returnBook(loan);
        }

        System.out.println("Book returned successfully. Return Date: " + loan.getReturnDate());

        if (loan.getCurrentState() != null && loan.getCurrentState().getClass().getSimpleName().equals("OverdueState")) {
            double fine = loan.getCurrentState().calculateFine(loan);
            System.out.println("Fine due: " + fine);
        }
    }

    private void renewLoan() {
        System.out.print("Enter Loan ID to renew: ");
        String loanId = scanner.nextLine();

        Loan loan = loanService.getLoanById(loanId);
        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        LoanState currentState = loan.getCurrentState();
        if (currentState != null) {
            currentState.renew(loan);
            System.out.println("Loan renewed successfully. New Due Date: " + loan.getDueDate());
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
}
