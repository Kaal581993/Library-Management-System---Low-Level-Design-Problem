package factory.loan_dto;

import entity.LoanState;
import entity.LoanType;

import java.util.Date;

public class LoanRequest {

    private String loanId;
    private String patronId;
    private String bookId;
    private LoanType loanType;
    private Date checkoutDate;
    private Date dueDate;
    private Date returnDate;
    private LoanState currentState;



    public String getLoanId() {
        return loanId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getBookId() {
        return bookId;
    }

    public LoanType getLoanType() {
        return loanType;
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

    public LoanState getCurrentState() {
        return currentState;
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

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
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

    public void setCurrentState(LoanState currentState) {
        this.currentState = currentState;
    }
}
