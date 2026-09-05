package validation.loan_validation.impl;

import entity.Loan;
import entity.LoanState;
import factory.loan_dto.LoanRequest;
import validation.loan_validation.LoanValidationException;
import validation.loan_validation.LoanValidationHandler;

import java.util.Date;

public class DueDateValidationHandler implements LoanValidationHandler {

    private LoanValidationHandler nextHandler;

    @Override
    public void setNextHandler(LoanValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(LoanRequest request) {
        if (request == null) {
            throw new LoanValidationException("Loan request is required");
        }

        Date checkoutDate = request.getCheckoutDate();
        Date dueDate = request.getDueDate();

        if (checkoutDate == null) {
            throw new LoanValidationException("Checkout date is required");
        }

        if (dueDate == null) {
            throw new LoanValidationException("Due date is required");
        }

        if (dueDate.before(checkoutDate)) {
            throw new LoanValidationException("Due date must be after checkout date");
        }

        if (nextHandler != null) {
            return nextHandler.validate(request);
        }
        return true;
    }
}
