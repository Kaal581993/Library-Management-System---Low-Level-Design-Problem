package strategy.patron_impl;

import entity.FineStatus;
import entity.Patron;
import strategy.PatronSearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class FineStatusSearchStrategy implements PatronSearchStrategy {
    @Override
    public List<Patron> searchPatron(List<Patron> patrons, String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        FineStatus status = FineStatus.valueOf(query.toUpperCase());
        return patrons.stream()
                .filter(p -> p.getFineStatus() == status)
                .collect(Collectors.toList());
    }
}
