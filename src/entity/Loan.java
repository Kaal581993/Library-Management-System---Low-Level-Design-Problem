package entity;

import java.util.Date;

public class Loan {
    private String loanId;
    private String patronId;
    private String bookId;
    private Date checkoutDate;
    private Date dueDate;
    private Date returnDate;
    private LoanState currentState;

    public Loan(String patronId, String bookId, Date checkoutDate, Date dueDate, Date returnDate, LoanState currentState) {
        this.patronId = patronId;
        this.bookId = bookId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.currentState = currentState;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getBookId() {
        return bookId;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public state.LoanState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(LoanState currentState) {
        this.currentState = currentState;
    }
}
