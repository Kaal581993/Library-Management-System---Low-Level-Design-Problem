package observer;

import entity.Book;
import entity.Patron;

public class PatronObserver implements BookObserver {

    private final Patron patron;

    public PatronObserver(Patron patron) {
        this.patron = patron;
    }

    @Override
    public void update(Book book) {
        if (patron != null && book != null) {
            System.out.println("Notification: Dear " + patron.getFirstName()
                    + ", the book \"" + book.getTitle()
                    + "\" is now available for checkout.");
        }
    }
}
