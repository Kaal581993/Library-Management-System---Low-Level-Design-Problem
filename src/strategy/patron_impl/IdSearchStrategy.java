package strategy.patron_impl;

import entity.Patron;
import strategy.PatronSearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class IdSearchStrategy implements PatronSearchStrategy {
    @Override
    public List<Patron> searchPatron(List<Patron> patrons, String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return patrons.stream()
                .filter(p -> p.getPatronId() != null && p.getPatronId().equals(query))
                .collect(Collectors.toList());
    }
}
