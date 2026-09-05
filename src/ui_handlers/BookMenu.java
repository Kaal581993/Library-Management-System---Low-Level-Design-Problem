package ui_handlers;

import entity.Book;
import entity.Loan;
import factory.book_dto.BookRequest;
import factory.loan_dto.LoanRequest;
import service.BookService;
import service.LoanService;
import strategy.SearchType;
import validation.book_validation.ValidationException;

import java.util.List;
import java.util.Scanner;

public class BookMenu {
    private final BookService bookService = BookService.getInstance();
    private final LoanService loanService = LoanService.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. Remove Book");
            System.out.println("4. List All Books");
            System.out.println("5. Checkout Book");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addBook();
                case "2" -> searchBook();
                case "3" -> removeBook();
                case "4" -> listAllBooks();
                case "5" -> checkoutBook();
                case "6" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void addBook() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter Publication Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        BookRequest request = new BookRequest();
        request.setIsbn(isbn);
        request.setTitle(title);
        request.setAuthor(author);
        request.setYear(year);
        request.setQuantity(quantity);

        try {
            bookService.addBook(request);
            System.out.println("Book added successfully.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchBook() {
        System.out.println("Search by:");
        System.out.println("1. ISBN");
        System.out.println("2. Title");
        System.out.println("3. Author");
        System.out.println("4. Combined");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();
        SearchType searchType;
        switch (choice) {
            case "1" -> searchType = SearchType.ISBN;
            case "2" -> searchType = SearchType.TITLE;
            case "3" -> searchType = SearchType.AUTHOR;
            case "4" -> searchType = SearchType.COMBINED;
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<Book> results = bookService.searchBooks(searchType, query);
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : results) {
                System.out.println(book.getIsbn() + " | " + book.getTitle() + " | " + book.getAuthor());
            }
        }
    }

    public void removeBook() {
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();
        bookService.removeBook(isbn);
        System.out.println("Book removed (if it existed).");
    }

    public void listAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book.getIsbn() + " | " + book.getTitle() + " | " + book.getAuthor() + " | Qty: " + book.getQuantity());
        }
    }

    public void checkoutBook() {
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
}
