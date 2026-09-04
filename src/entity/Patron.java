package entity;

import java.util.List;

public class Patron {
    private String patronId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String email;
    private List<Loan> borrowingHistory;

    private Patron(PatronBuilder builder) {
        this.patronId = builder.patronId;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.email = builder.email;
        this.borrowingHistory = builder.borrowingHistory;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUnserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<Loan> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUnserName(String unserName) {
        this.userName = unserName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBorrowingHistory(List<Loan> borrowingHistory) {
        this.borrowingHistory = borrowingHistory;
    }

    public static class PatronBuilder {
        private String patronId;
        private String firstName;
        private String middleName;
        private String lastName;
        private String userName;
        private String email;
        private List<Loan> borrowingHistory;

        public PatronBuilder patronId(String patronId) {
            this.patronId = patronId;
            return this;
        }

        public PatronBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PatronBuilder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public PatronBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PatronBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public PatronBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PatronBuilder borrowingHistory(List<Loan> borrowingHistory) {
            this.borrowingHistory = borrowingHistory;
            return this;
        }

        public Patron build() {
            return new Patron(this);
        }
    }
}
