package service;

import entity.Book;
import entity.BookStatus;
import entity.Loan;
import entity.LoanState;
import entity.Patron;
import entity.PatronType;
import factory.LoanFactory;
import factory.loan_dto.LoanRequest;
import factory.loan_dto.impl.DefaultLoanFactory;
import service.ReservationService;
import state.impl.CheckedOutState;
import strategy.FineCalculationStrategy;
import strategy.loan_impl.HolidayFineStrategy;
import strategy.loan_impl.NoFineStrategy;
import strategy.loan_impl.StandardFineStrategy;
import strategy.loan_impl.StudentFineStrategy;
import strategy.loan_impl.VIPFineStrategy;
import validation.loan_validation.LoanValidationHandler;
import validation.loan_validation.impl.BookAvailabilityHandler;
import validation.loan_validation.impl.DueDateValidationHandler;
import validation.loan_validation.impl.LoanLimitHandler;
import validation.loan_validation.impl.PatronEligibilityHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoanService {

    private static volatile LoanService instance;
    private final PatronService patronService = PatronService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    private final LoanFactory loanFactory = new DefaultLoanFactory();
    private final List<Loan> loans = new ArrayList<>();
    private final Map<PatronType, FineCalculationStrategy> fineStrategies = new EnumMap<>(PatronType.class);

    private LoanService() {
        fineStrategies.put(PatronType.REGULAR, new StandardFineStrategy());
        fineStrategies.put(PatronType.VIP, new VIPFineStrategy());
        fineStrategies.put(PatronType.STUDENT, new StudentFineStrategy());
        fineStrategies.put(PatronType.GUEST, new NoFineStrategy());
        fineStrategies.put(PatronType.PROFESSOR, new StandardFineStrategy());
        fineStrategies.put(PatronType.FACULTY, new StandardFineStrategy());
        fineStrategies.put(PatronType.HEADMASTER, new VIPFineStrategy());
        fineStrategies.put(PatronType.ADMIN, new NoFineStrategy());
        fineStrategies.put(PatronType.HOD, new StandardFineStrategy());
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
                .setNext(new LoanLimitHandler(this, patronService)
                .setNext(new DueDateValidationHandler())));

        chain.validate(request);

        Loan loan = loanFactory.createLoan(request);
        loan.setCurrentState(new CheckedOutState());

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
                .filter(loan -> Objects.equals(patronId, loan.getPatronId())
                        && loan.getReturnDate() == null)
                .toList();
    }

    public Loan getLoanById(String loanId) {
        return loans.stream()
                .filter(loan -> Objects.equals(loanId, loan.getLoanId()))
                .findFirst()
                .orElse(null);
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public void returnBook(String loanId) {
        if (loanId == null) {
            throw new IllegalArgumentException("Loan ID is required");
        }

        Loan loan = getLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned");
        }

        loan.getCurrentState().returnBook(loan);

        Book book = bookService.getBook(loan.getBookId());
        if (book != null) {
            book.setQuantity(book.getQuantity() + 1);
            if (book.getQuantity() > 0) {
                book.setBookStatus(BookStatus.AVAILABLE);
            }
            bookService.updateBook(book);
        }
    }

    public void renewLoan(String loanId) {
        if (loanId == null) {
            throw new IllegalArgumentException("Loan ID is required");
        }

        Loan loan = getLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Cannot renew a returned loan");
        }

        loan.getCurrentState().renew(loan);
    }

    public double calculateFine(String loanId) {
        if (loanId == null) {
            throw new IllegalArgumentException("Loan ID is required");
        }

        Loan loan = getLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }

        Patron patron = patronService.getPatronById(loan.getPatronId());
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found for loan: " + loanId);
        }

        FineCalculationStrategy strategy = fineStrategies.get(patron.getPatronType());
        if (strategy == null) {
            strategy = new StandardFineStrategy();
        }

        return strategy.calculateFine(loan);
    }

    public void updateOverdueLoans() {
        for (Loan loan : loans) {
            if (loan.getReturnDate() == null
                    && loan.getCurrentState() != null
                    && loan.getCurrentState().isOverDue(loan)
                    && !(loan.getCurrentState() instanceof state.impl.OverdueState)) {
                loan.setCurrentState(new state.impl.OverdueState());
            }
        }
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(loan -> loan.getCurrentState() != null
                        && loan.getCurrentState().isOverDue(loan)
                        && loan.getReturnDate() == null)
                .toList();
    }

    public List<Loan> getLoansByPatron(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        return loans.stream()
                .filter(loan -> Objects.equals(patronId, loan.getPatronId()))
                .toList();
    }

    public void updateLoanStatus(String loanId, LoanState newState) {
        if (loanId == null || newState == null) {
            throw new IllegalArgumentException("Loan ID and new state are required");
        }

        Loan loan = getLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }

        loan.setCurrentState(newState);
    }
}
