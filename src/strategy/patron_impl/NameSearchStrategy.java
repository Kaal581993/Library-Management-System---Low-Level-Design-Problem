package strategy.patron_impl;

import entity.Patron;
import strategy.PatronSearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class NameSearchStrategy implements PatronSearchStrategy {
    @Override
    public List<Patron> searchPatron(List<Patron> patrons, String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String lower = query.toLowerCase();
        return patrons.stream()
                .filter(p -> (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
