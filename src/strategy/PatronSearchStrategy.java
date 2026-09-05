package strategy;

import entity.Patron;

import java.util.List;

public interface PatronSearchStrategy {
    List<Patron> searchPatron(List<Patron> patrons, String query);
}
