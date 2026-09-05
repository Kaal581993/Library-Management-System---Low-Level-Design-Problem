package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class ExternalPatronFactory implements PatronFactory {

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
        patron.setMaxBorrowingLimit(3);
        patron.setLoanPeriodDays(7);
        patron.setFineRatePerDay(1.0);
        patron.setFineAmount(0.0);
        patron.setFineStatus(entity.FineStatus.CLEAR);
        return patron;
    }

    @Override
    public int getBorrowingLimit() {
        return 3;
    }

    @Override
    public int getLoanPeriod() {
        return 7;
    }

    @Override
    public double getFineRate() {
        return 1.0;
    }
}
