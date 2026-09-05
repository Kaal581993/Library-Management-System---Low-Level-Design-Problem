package service;

import entity.Book;
import entity.Loan;
import entity.LoanState;
import entity.Patron;
import factory.LoanFactory;
import factory.book_dto.BookRequest;
import factory.loan_dto.LoanRequest;
import factory.loan_dto.impl.DefaultLoanFactory;
import state.impl.CheckedOutState;

import java.util.ArrayList;
import java.util.List;

public class LoanService {

    private static volatile LoanService instance;
    private final PatronService patronService = PatronService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private final LoanFactory loanFactory = new DefaultLoanFactory();
    private final List<Loan> loans = new ArrayList<>();

    private LoanService() {
    }

    public static LoanService getInstance() {
        if (instance == null) {
            synchronized (LoanService.class) {
                if (instance == null) {
                    instance = new LoanService();
                }
            }
        }
        return instance;
    }

    public Loan checkoutBook(LoanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Loan request is required");
        }

        validatePatronExists(request.getPatronId());
        Book book = validateBookAvailable(request.getBookId());

        Loan loan = loanFactory.createLoan(request);
        loan.setCurrentState(new CheckedOutState());

        if (book != null) {
            book.setQuantity(book.getQuantity() - 1);
            bookService.updateBook(book);
        }

        Patron patron = patronService.getPatronById(request.getPatronId());
        if (patron != null && patron.getBorrowingHistory() != null) {
            patron.getBorrowingHistory().add(loan);
        }

        loans.add(loan);
        return loan;
    }

    private void validatePatronExists(String patronId) {
        Patron patron = patronService.getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
    }

    private Book validateBookAvailable(String bookId) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
        if (book.getQuantity() <= 0) {
            throw new IllegalArgumentException("Book is not available: " + bookId);
        }
        return book;
    }

    public Loan getLoanById(String loanId) {
        return loans.stream()
                .filter(loan -> loanId.equals(loan.getLoanId()))
                .findFirst()
                .orElse(null);
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }
}
