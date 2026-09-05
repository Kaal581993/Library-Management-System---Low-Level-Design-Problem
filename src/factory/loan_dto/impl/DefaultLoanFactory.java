package factory.loan_dto.impl;

import entity.Loan;
import entity.LoanState;
import entity.LoanType;
import factory.LoanFactory;
import factory.loan_dto.LoanRequest;
import state.impl.CheckedOutState;

import java.util.Calendar;
import java.util.Date;

public class DefaultLoanFactory implements LoanFactory {

    private static final int REGULAR_LOAN_DAYS = 14;
    private static final int REFERENCE_LOAN_DAYS = 7;
    private static final int INTER_LIBRARY_LOAN_DAYS = 30;
    private static final int DIGITAL_LOAN_DAYS = 3;

    @Override
    public Loan createLoan(LoanRequest request) {
        Date checkoutDate = request.getCheckoutDate() != null ? request.getCheckoutDate() : new Date();
        Date dueDate = calculateDueDate(checkoutDate, request.getLoanType());
        Date returnDate = request.getReturnDate();
        LoanState currentState = request.getCurrentState() != null ? request.getCurrentState() : new state.impl.CheckedOutState();

        return new Loan(request.getPatronId(), request.getBookId(), checkoutDate, dueDate, returnDate, currentState);
    }

    @Override
    public Loan createRegularLoan(LoanRequest request) {
        request.setLoanType(LoanType.REGULAR);
        return createLoan(request);
    }

    @Override
    public Loan createReferenceLoan(LoanRequest request) {
        request.setLoanType(LoanType.REFERENCE);
        return createLoan(request);
    }

    @Override
    public Loan createInterLibraryLoan(LoanRequest request) {
        request.setLoanType(LoanType.INTER_LIBRARY);
        return createLoan(request);
    }

    private Date calculateDueDate(Date checkoutDate, LoanType loanType) {
        if (checkoutDate == null || loanType == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);

        int daysToAdd;
        switch (loanType) {
            case REGULAR -> daysToAdd = REGULAR_LOAN_DAYS;
            case REFERENCE -> daysToAdd = REFERENCE_LOAN_DAYS;
            case INTER_LIBRARY -> daysToAdd = INTER_LIBRARY_LOAN_DAYS;
            case DIGITAL -> daysToAdd = DIGITAL_LOAN_DAYS;
            default -> daysToAdd = REGULAR_LOAN_DAYS;
        }

        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return calendar.getTime();
    }
}
