package observer;

import entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookSubject {
    private final List<BookObserver> observers = new ArrayList<>();

    public void addObserver(BookObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(BookObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Book book) {
        for (BookObserver observer : observers) {
            observer.update(book);
        }
    }
}
