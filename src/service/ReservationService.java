package service;

import entity.Book;
import entity.Patron;
import entity.Reservation;
import entity.ReservationStatus;
import factory.ReservationFactory;
import factory.reservation_dto.ReservationRequest;
import factory.reservation_dto.impl.DefaultReservationFactory;
import observer.BookObserver;
import strategy.reservation_impl.FIFOPriorityStrategy;
import validation.reservation_validation.ReservationValidationHandler;
import validation.reservation_validation.impl.BookAvailabilityValidationHandler;
import validation.reservation_validation.impl.BorrowingLimitValidationHandler;
import validation.reservation_validation.impl.DuplicateReservationValidationHandler;
import validation.reservation_validation.impl.PatronEligibilityValidationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ReservationService implements BookObserver {

    private static volatile ReservationService instance;
    private final List<Reservation> reservations = new ArrayList<>();
    private final PriorityQueue<Reservation> reservationQueue = new PriorityQueue<>(new FIFOPriorityStrategy());
    private final ReservationFactory reservationFactory = new DefaultReservationFactory();
    private final NotificationService notificationService = NotificationService.getInstance();
    private ReservationValidationHandler validationChain;

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            synchronized (ReservationService.class) {
                if (instance == null) {
                    instance = new ReservationService();
                }
            }
        }
        return instance;
    }

    private BookService getBookService() {
        return BookService.getInstance();
    }

    private LoanService getLoanService() {
        return LoanService.getInstance();
    }

    private PatronService getPatronService() {
        return PatronService.getInstance();
    }

    private ReservationValidationHandler getValidationChain() {
        if (validationChain == null) {
            validationChain = new BookAvailabilityValidationHandler(getBookService())
                    .setNext(new PatronEligibilityValidationHandler(getPatronService())
                    .setNext(new BorrowingLimitValidationHandler(getLoanService())
                    .setNext(new DuplicateReservationValidationHandler(this))));
        }
        return validationChain;
    }

    public Reservation createReservation(ReservationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Reservation request is required");
        }

        getValidationChain().validate(request);

        Reservation reservation = reservationFactory.createReservation(request);
        Book book = getBookService().getBook(request.getBookId());
        reservation.setBook(book);
        reservation.setPatron(request.getPatron());

        if (book != null) {
            book.addObserver(this);
        }

        reservations.add(reservation);
        reservationQueue.add(reservation);

        return reservation;
    }

    public void cancelReservation(String reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID is required");
        }

        Reservation reservation = getReservation(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }

        if (reservation.getReservationStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel a reservation that is not pending");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationQueue.remove(reservation);
    }

    public Reservation getReservation(String reservationId) {
        if (reservationId == null) {
            return null;
        }
        return reservations.stream()
                .filter(reservation -> reservationId.equals(reservation.getReservationId()))
                .findFirst()
                .orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public List<Reservation> getReservationsByBook(String isbn) {
        if (isbn == null) {
            return new ArrayList<>();
        }
        return reservations.stream()
                .filter(reservation -> reservation.getBook() != null
                        && isbn.equals(reservation.getBook().getIsbn()))
                .toList();
    }

    public List<Reservation> getReservationsByPatron(String patronId) {
        if (patronId == null) {
            return new ArrayList<>();
        }
        return reservations.stream()
                .filter(reservation -> reservation.getPatron() != null
                        && patronId.equals(reservation.getPatron().getPatronId()))
                .toList();
    }

    public void fulfillReservation(String reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID is required");
        }

        Reservation reservation = getReservation(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }

        if (reservation.getReservationStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot fulfill a reservation that is not pending");
        }

        reservation.setFulfilled(true);
        reservation.setReservationStatus(ReservationStatus.FULFILLED);
        reservationQueue.remove(reservation);

        notifyPatron(reservation);
    }

    public void processNextReservation(String isbn) {
        if (isbn == null) {
            return;
        }

        List<Reservation> matching = reservations.stream()
                .filter(reservation -> reservation.getBook() != null
                        && isbn.equals(reservation.getBook().getIsbn())
                        && reservation.getReservationStatus() == ReservationStatus.PENDING)
                .sorted(new FIFOPriorityStrategy())
                .toList();

        if (!matching.isEmpty()) {
            Reservation next = matching.get(0);
            reservationQueue.remove(next);
            fulfillReservation(next.getReservationId());
        }
    }

    public void notifyPatron(Reservation reservation) {
        if (reservation == null) {
            return;
        }

        BookObserver observer = new observer.PatronObserver(reservation.getPatron());
        observer.update(reservation.getBook());

        notificationService.sendReservationNotification(reservation);
    }

    @Override
    public void update(Book book) {
        if (book == null) {
            return;
        }
        processNextReservation(book.getIsbn());
    }
}
