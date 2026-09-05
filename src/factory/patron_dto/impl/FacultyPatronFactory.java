package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class FacultyPatronFactory implements PatronFactory {

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
        patron.setMaxBorrowingLimit(10);
        patron.setLoanPeriodDays(30);
        patron.setFineRatePerDay(0.25);
        patron.setFineAmount(0.0);
        patron.setFineStatus(entity.FineStatus.CLEAR);
        return patron;
    }

    @Override
    public int getBorrowingLimit() {
        return 10;
    }

    @Override
    public int getLoanPeriod() {
        return 30;
    }

    @Override
    public double getFineRate() {
        return 0.25;
    }
}
