package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class StudentPatronFactory implements PatronFactory {

    @Override
    public Patron createPatron(PatronRequest request) {
        Patron patron = new Patron();
        patron.setPatronId(request.getPatronId());
        patron.setFirstName(request.getFirstName());
        patron.setMiddleName(request.getMiddleName());
        patron.setLastName(request.getLastName());
        patron.setUserName(request.getUserName());
        patron.setEmail(request.getEmail());
        patron.setPatronType(request.getPatronType());
        patron.setPatronStatus(PatronStatus.ACTIVE);
        patron.setMaxBorrowingLimit(5);
        patron.setLoanPeriodDays(14);
        patron.setFineRatePerDay(0.5);
        patron.setFineAmount(0.0);
        patron.setFineStatus(entity.FineStatus.CLEAR);
        return patron;
    }

    @Override
    public int getBorrowingLimit() {
        return 5;
    }

    @Override
    public int getLoanPeriod() {
        return 14;
    }

    @Override
    public double getFineRate() {
        return 0.5;
    }
}
