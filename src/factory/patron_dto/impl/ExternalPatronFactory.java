package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import entity.util.IdGenerator;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class ExternalPatronFactory implements PatronFactory {

    @Override
    public Patron createPatron(PatronRequest request) {
        return new Patron.PatronBuilder()
                .patronId(request.getPatronId() != null ? request.getPatronId() : IdGenerator.generatePatronId())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .patronType(request.getPatronType())
                .patronStatus(PatronStatus.ACTIVE)
                .maxBorrowingLimit(3)
                .loanPeriodDays(7)
                .fineRatePerDay(1.0)
                .fineAmount(0.0)
                .fineStatus(entity.FineStatus.CLEAR)
                .build();
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
