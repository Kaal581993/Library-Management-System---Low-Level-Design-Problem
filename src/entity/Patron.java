package entity;

import java.util.List;

public class Patron {
    private String patronId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String email;
    private PatronType patronType;
    private PatronStatus patronStatus;
    private double fineAmount;
    private FineStatus fineStatus;
    private String suspensionReason;
    private int maxBorrowingLimit;
    private int loanPeriodDays;
    private double fineRatePerDay;
    private List<Loan> borrowingHistory;

    private Patron(PatronBuilder builder) {
        this.patronId = builder.patronId;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.email = builder.email;
        this.patronType = builder.patronType;
        this.patronStatus = builder.patronStatus;
        this.fineAmount = builder.fineAmount;
        this.fineStatus = builder.fineStatus;
        this.suspensionReason = builder.suspensionReason;
        this.maxBorrowingLimit = builder.maxBorrowingLimit;
        this.loanPeriodDays = builder.loanPeriodDays;
        this.fineRatePerDay = builder.fineRatePerDay;
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

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<Loan> getBorrowingHistory() {
        return borrowingHistory;
    }

    public PatronType getPatronType() {
        return patronType;
    }

    public PatronStatus getPatronStatus() {
        return patronStatus;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public FineStatus getFineStatus() {
        return fineStatus;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public int getMaxBorrowingLimit() {
        return maxBorrowingLimit;
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }

    public double getFineRatePerDay() {
        return fineRatePerDay;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBorrowingHistory(List<Loan> borrowingHistory) {
        this.borrowingHistory = borrowingHistory;
    }

    public void setPatronType(PatronType patronType) {
        this.patronType = patronType;
    }

    public void setPatronStatus(PatronStatus patronStatus) {
        this.patronStatus = patronStatus;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setFineStatus(FineStatus fineStatus) {
        this.fineStatus = fineStatus;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }

    public void setMaxBorrowingLimit(int maxBorrowingLimit) {
        this.maxBorrowingLimit = maxBorrowingLimit;
    }

    public void setLoanPeriodDays(int loanPeriodDays) {
        this.loanPeriodDays = loanPeriodDays;
    }

    public void setFineRatePerDay(double fineRatePerDay) {
        this.fineRatePerDay = fineRatePerDay;
    }

    public static class PatronBuilder {
        private String patronId;
        private String firstName;
        private String middleName;
        private String lastName;
        private String userName;
        private String email;
        private List<Loan> borrowingHistory;
        private PatronType patronType;
        private PatronStatus patronStatus;
        private double fineAmount;
        private FineStatus fineStatus;
        private String suspensionReason;
        private int maxBorrowingLimit;
        private int loanPeriodDays;
        private double fineRatePerDay;

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

        public PatronBuilder patronType(PatronType patronType) {
            this.patronType = patronType;
            return this;
        }

        public PatronBuilder patronStatus(PatronStatus patronStatus) {
            this.patronStatus = patronStatus;
            return this;
        }

        public PatronBuilder fineAmount(double fineAmount) {
            this.fineAmount = fineAmount;
            return this;
        }

        public PatronBuilder fineStatus(FineStatus fineStatus) {
            this.fineStatus = fineStatus;
            return this;
        }

        public PatronBuilder suspensionReason(String suspensionReason) {
            this.suspensionReason = suspensionReason;
            return this;
        }

        public PatronBuilder maxBorrowingLimit(int maxBorrowingLimit) {
            this.maxBorrowingLimit = maxBorrowingLimit;
            return this;
        }

        public PatronBuilder loanPeriodDays(int loanPeriodDays) {
            this.loanPeriodDays = loanPeriodDays;
            return this;
        }

        public PatronBuilder fineRatePerDay(double fineRatePerDay) {
            this.fineRatePerDay = fineRatePerDay;
            return this;
        }

        public Patron build() {
            return new Patron(this);
        }
    }
}
