package state;

import entity.Book;

public interface LoanState {
    public void checkout(Book book);
    public void returnBook(Book book);
    public void renew(Book book);
    public void calculateFine(Book book);
    public boolean isOverDue(Book book);
}
