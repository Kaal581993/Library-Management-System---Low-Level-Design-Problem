package service;

import entity.FineStatus;
import entity.Loan;
import entity.Patron;
import entity.PatronStatus;
import entity.Reservation;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;
import factory.patron_dto.impl.ExternalPatronFactory;
import factory.patron_dto.impl.FacultyPatronFactory;
import factory.patron_dto.impl.StaffPatronFactory;
import factory.patron_dto.impl.StudentPatronFactory;
import strategy.PatronSearchStrategy;
import strategy.PatronSearchType;
import strategy.patron_impl.BorrowingHistorySearchStrategy;
import strategy.patron_impl.EmailSearchStrategy;
import strategy.patron_impl.FineStatusSearchStrategy;
import strategy.patron_impl.IdSearchStrategy;
import strategy.patron_impl.NameSearchStrategy;
import validation.patron_validation.PatronValidationHandler;
import validation.patron_validation.impl.BorrowingLimitValidationHandler;
import validation.patron_validation.impl.EmailValidationHandler;
import validation.patron_validation.impl.IdValidationHandler;
import validation.patron_validation.impl.NameValidationHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PatronService {

    private static volatile PatronService instance;
    private final List<Patron> patrons = new ArrayList<>();
    private final Map<PatronSearchType, PatronSearchStrategy> strategies = new EnumMap<>(PatronSearchType.class);
    private PatronValidationHandler validationChain;

    private PatronService() {
        strategies.put(PatronSearchType.NAME, new NameSearchStrategy());
        strategies.put(PatronSearchType.EMAIL, new EmailSearchStrategy());
        strategies.put(PatronSearchType.ID, new IdSearchStrategy());
        strategies.put(PatronSearchType.BORROWING_HISTORY, new BorrowingHistorySearchStrategy());
        strategies.put(PatronSearchType.FINE_STATUS, new FineStatusSearchStrategy());
    }

    public static PatronService getInstance() {
        if (instance == null) {
            synchronized (PatronService.class) {
                if (instance == null) {
                    instance = new PatronService();
                }
            }
        }
        return instance;
    }

    private PatronFactory getFactory(PatronRequest request) {
        if (request == null || request.getPatronType() == null) {
            return new ExternalPatronFactory();
        }
        return switch (request.getPatronType()) {
            case STUDENT -> new StudentPatronFactory();
            case FACULTY, PROFESSOR, HOD, HEADMASTER -> new FacultyPatronFactory();
            case STAFF -> new StaffPatronFactory();
            default -> new ExternalPatronFactory();
        };
    }

    private PatronValidationHandler getValidationChain() {
        if (validationChain == null) {
            validationChain = new EmailValidationHandler()
                    .setNext(new NameValidationHandler()
                    .setNext(new IdValidationHandler()
                    .setNext(new BorrowingLimitValidationHandler())));
        }
        return validationChain;
    }

    public Patron addPatron(PatronRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Patron request is required");
        }

        getValidationChain().validate(request);

        PatronFactory factory = getFactory(request);
        Patron patron = factory.createPatron(request);

        patrons.add(patron);
        return patron;
    }

    public Patron updatePatron(String patronId, PatronRequest request) {
        if (patronId == null || request == null) {
            throw new IllegalArgumentException("Patron ID and request are required");
        }

        Patron existing = getPatronById(patronId);
        if (existing == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }

        if (request.getFirstName() != null) {
            existing.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existing.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            existing.setEmail(request.getEmail());
        }
        if (request.getPatronType() != null) {
            existing.setPatronType(request.getPatronType());
        }

        return existing;
    }

    public void removePatron(String patronId) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patrons.remove(patron);
    }

    public Patron getPatronById(String patronId) {
        if (patronId == null) {
            return null;
        }
        return patrons.stream()
                .filter(patron -> patronId.equals(patron.getPatronId()))
                .findFirst()
                .orElse(null);
    }

    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons);
    }

    public List<Patron> searchPatrons(PatronSearchType type, String query) {
        if (type == null || query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        PatronSearchStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy registered for search type: " + type);
        }
        return strategy.searchPatron(patrons, query);
    }

    public void suspendPatron(String patronId, String reason) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patron.setPatronStatus(PatronStatus.SUSPENDED);
        patron.setSuspensionReason(reason);
    }

    public void activatePatron(String patronId) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patron.setPatronStatus(PatronStatus.ACTIVE);
        patron.setSuspensionReason(null);
    }

    public void deactivatePatron(String patronId) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patron.setPatronStatus(PatronStatus.INACTIVE);
    }

    public List<Loan> getBorrowingHistory(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        Patron patron = getPatronById(patronId);
        if (patron == null || patron.getBorrowingHistory() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(patron.getBorrowingHistory());
    }

    public List<Loan> getCurrentLoans(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        Patron patron = getPatronById(patronId);
        if (patron == null || patron.getBorrowingHistory() == null) {
            return new ArrayList<>();
        }
        return patron.getBorrowingHistory().stream()
                .filter(loan -> loan.getReturnDate() == null)
                .toList();
    }

    public List<Loan> getOverdueLoans(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        Patron patron = getPatronById(patronId);
        if (patron == null || patron.getBorrowingHistory() == null) {
            return new ArrayList<>();
        }
        return patron.getBorrowingHistory().stream()
                .filter(loan -> loan.getReturnDate() == null
                        && loan.getCurrentState() != null
                        && loan.getCurrentState().isOverDue(loan))
                .toList();
    }

    public double calculateFine(String patronId) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        return patron.getFineAmount();
    }

    public void payFine(String patronId, double amount) {
        if (patronId == null) {
            throw new IllegalArgumentException("Patron ID is required");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        double newFine = Math.max(0, patron.getFineAmount() - amount);
        patron.setFineAmount(newFine);
        patron.setFineStatus(newFine == 0 ? FineStatus.CLEAR : FineStatus.PENDING);
    }

    public FineStatus getFineStatus(String patronId) {
        if (patronId == null) {
            return FineStatus.CLEAR;
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            return FineStatus.CLEAR;
        }
        return patron.getFineStatus();
    }

    public List<entity.Reservation> getReservations(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        Patron patron = getPatronById(patronId);
        if (patron == null) {
            return new ArrayList<>();
        }
        return ReservationService.getInstance().getReservationsByPatron(patronId);
    }
}
