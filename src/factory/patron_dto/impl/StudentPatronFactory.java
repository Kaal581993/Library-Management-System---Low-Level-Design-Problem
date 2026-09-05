package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import entity.util.IdGenerator;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class StudentPatronFactory implements PatronFactory {

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
                .maxBorrowingLimit(5)
                .loanPeriodDays(14)
                .fineRatePerDay(0.5)
                .fineAmount(0.0)
                .fineStatus(entity.FineStatus.CLEAR)
                .build();
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
