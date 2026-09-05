package factory.patron_dto.impl;

import entity.Patron;
import entity.PatronStatus;
import entity.util.IdGenerator;
import factory.PatronFactory;
import factory.patron_dto.PatronRequest;

public class StaffPatronFactory implements PatronFactory {

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
                .maxBorrowingLimit(8)
                .loanPeriodDays(21)
                .fineRatePerDay(0.3)
                .fineAmount(0.0)
                .fineStatus(entity.FineStatus.CLEAR)
                .build();
    }

    @Override
    public int getBorrowingLimit() {
        return 8;
    }

    @Override
    public int getLoanPeriod() {
        return 21;
    }

    @Override
    public double getFineRate() {
        return 0.3;
    }
}
