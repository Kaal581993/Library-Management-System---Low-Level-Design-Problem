package entity;

import entity.util.IdGenerator;

import java.util.Date;

public class Reservation {
    private String reservationId;
    private Date reservationDate;
    private boolean isFulfilled;
    private Patron patron;
    private Book book;
    private int priority;
    private ReservationStatus reservationStatus;

    private Reservation(ReservationBuilder builder) {
        this.reservationId = builder.reservationId;
        this.reservationDate = builder.reservationDate;
        this.isFulfilled = builder.isFulfilled;
        this.patron = builder.patron;
        this.book = builder.book;
        this.priority = builder.priority;
        this.reservationStatus = builder.reservationStatus;
    }


    public static class ReservationBuilder{
        private String reservationId;
        private Date reservationDate;
        private boolean isFulfilled;
        private Patron patron;
        private Book book;
        private int priority;
        private ReservationStatus reservationStatus;

        public ReservationBuilder(Date reservationDate, boolean isFulfilled, Patron patron, Book book, int priority){
            this.reservationDate = reservationDate;
            this.isFulfilled = isFulfilled;
            this.patron = patron;
            this.book = book;
            this.priority = priority;
            this.reservationStatus = ReservationStatus.PENDING;
        }

        public ReservationBuilder setIsFulfilled(boolean isFulfilled){
            this.isFulfilled = isFulfilled;
            return this;
        }

        public ReservationBuilder setReservationDate(Date reservationDate){
            this.reservationDate = reservationDate;
            return this;
        }

        public ReservationBuilder setPatron(Patron patron){
            this.patron = patron;
            return this;
        }

        public ReservationBuilder setBook(Book book){
            this.book = book;
            return this;
        }

        public ReservationBuilder setPriority(int priority){
            this.priority = priority;
            return this;
        }

        public ReservationBuilder setReservationStatus(ReservationStatus reservationStatus){
            this.reservationStatus = reservationStatus;
            return this;
        }
        public ReservationBuilder setReservationId() {
            this.reservationId = IdGenerator.generateReservationId();
            return this;
        }
        public Reservation build(){
            return new Reservation(this);
        }


    }

    public String getReservationId() {
        return reservationId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public void setFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public void setBook(entity.Book book) {
        this.book = book;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    public Patron getPatron() {
        return patron;
    }

    public Book getBook() {
        return book;
    }

    public int getPriority() {
        return priority;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }
}
