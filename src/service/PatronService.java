package service;

import entity.Patron;
import entity.PatronType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PatronService {

    private static volatile PatronService instance;
    private final List<Patron> patrons = new ArrayList<>();
    private final AtomicReference<Patron> patronCache = new AtomicReference<>();

    private PatronService() {
    }

    public static PatronService getInstance() {
        if (instance == null) {
            synchronized (PatronService.class) {
                if (instance == null) {
                    instance = new PatronService();
                }
            }
        }
        return instance;
    }

    public Patron getPatronById(String patronId) {
        if (patronId == null) {
            return null;
        }
        return patrons.stream()
                .filter(patron -> patronId.equals(patron.getPatronId()))
                .findFirst()
                .orElse(null);
    }

    public void addPatron(Patron patron) {
        if (patron != null) {
            patrons.add(patron);
        }
    }

    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons);
    }

    public boolean removePatron(String patronId) {
        return patrons.removeIf(patron -> patronId.equals(patron.getPatronId()));
    }
}
