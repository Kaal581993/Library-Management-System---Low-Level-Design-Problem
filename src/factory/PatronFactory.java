package factory;

import entity.Patron;
import factory.patron_dto.PatronRequest;

public interface PatronFactory {
    Patron createPatron(PatronRequest request);
    int getBorrowingLimit();
    int getLoanPeriod();
    double getFineRate();
}
