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
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;
import validation.loan_validation.impl.BookAvailabilityHandler;
import validation.loan_validation.impl.DueDateValidationHandler;
import validation.loan_validation.impl.LoanLimitHandler;
import validation.loan_validation.impl.PatronEligibilityHandler;

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

        LoanValidationHandler chain = new PatronEligibilityHandler(patronService)
                .setNext(new BookAvailabilityHandler(bookService)
                .setNext(new LoanLimitHandler(this)
                .setNext(new DueDateValidationHandler())));

        chain.validate(request);

        Loan loan = loanFactory.createLoan(request);
        loan.setCurrentState(new state.impl.CheckedOutState());

        Book book = bookService.getBook(request.getBookId());
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

    public List<Loan> getActiveLoans(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        return loans.stream()
                .filter(loan -> patronId.equals(loan.getPatronId())
                        && loan.getReturnDate() == null)
                .toList();
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
